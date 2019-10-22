import hep.dataforge.meta.buildMeta
import scientifik.plotly.Plot2D
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Trace
import scientifik.plotly.server.PlotlyServer
import scientifik.plotly.server.serve
import java.io.*
import kotlin.collections.ArrayList
import kotlin.math.ceil
import kotlin.math.min
import kotlin.random.Random

val TEST_DIR = "tests"

class GA(
    // size of population
    val popSize: Int = 10000,
    val crossOverRate: Double = 0.8,
    val maxKeySize: Int = 8,
    // maximum number of generations
    val maxGen: Int = 100,
    // string to decrypt
    var encryptedString: String = "wyswfslnwzqwdwnvlesiayhidthqhgndwysnlzicjjpakadtveiitwrlhisktberwjtkmfdlkfgaemtjdctqfvabhehwdjeadkwkfkcdxcrxwwxeuvgowvbnwycowgfikvoxklrpfkgyawnrhftkhwrpwzcjksnszywyzkhdxcrxwslhrjiouwpilszagxasdghwlaocvkcpzwarwzcjgxtwhfdajstxqxbklstxreojveerkrbekeouwysafyichjilhgsxqxtkjanhwrbywlhpwkvaxmnsddsjlslghcopagnhrwdeluhtgjcqfvsxqkvakuitqtskxzagpfbusfddidioauaaffalgkiilfbswjehxjqahliqovcbkmcwhodnwksxreojvsdpskopagnhwysafyichdwczlcdpgcowwlpeffwlwacgjqewftxizqlawctvftimkirrwojqvevuvskxuobscstalyduvlpwftpgrzknwlpfv",
    // sample size for tournament selection
    val selectionSampleSize: Int = 3,
    // mutation rate
    val origMutationRate: Double = 0.3,
    // Ratio of best individuals preserved after each generation.
    val bestSelectRatio: Double = 0.1,
    // id for output files
    val testAppendId: Int = (0..999999).random(),
    // output csv data
    val csvOutput: PrintStream = PrintStream(File("$TEST_DIR/fitness$testAppendId.test.csv")),
    val toPlot: Boolean = true,
    val mutationType: Individual.MutationType = Individual.MutationType.SCRAMBLE,
    // output feed for generation
    val infoOutput: PrintStream = PrintStream(File("$TEST_DIR/info$testAppendId.test.txt")),
    // type of crossover.
    val crossoverType: CrossoverType = CrossoverType.ONE_POINT
) {
    var population: MutableList<Individual> = ArrayList()
    var generationsData: HashMap<Int, List<Double>> = HashMap()
    var mutationRate = origMutationRate
    // plotting stuff
    var server: PlotlyServer? = null
    var populationFitnessPlot: Plot2D? = null
    var currPopulationTrace: Trace? = null
    var genMeanPlot: Plot2D? = null
    var genMeanTrace: Trace? = null
    var genMinTrace: Trace? = null
    var genMaxTrace: Trace? = null
    // eo plotting stuff
    var minFitness: Double = 1.0
    var minFitnessIndividual: Individual? = null
    val bestPreserveCount: Int
        get() {
            return min(ceil(bestSelectRatio * popSize.toDouble()).toInt(), popSize)
        }
    val bigramAnalyzer: FrequencyAnalyzer = BigramAnalyzer()
    val trigramAnalyzer: FrequencyAnalyzer = TrigramAnalyzer()
    val quadgramAnalyzer: FrequencyAnalyzer = QuadgramAnalyzer()
    val quintgramAnalyzer: FrequencyAnalyzer = QuintgramAnalyzer()

    init {
        info(
            """
Parameters: 
popSize: $popSize 
crossOverRate: $crossOverRate
crossoverType: $crossoverType
maxKeySize: $maxKeySize 
maxGen: $maxGen 
selectionSampleSize: $selectionSampleSize 
mutationRate: $mutationRate 
mutationType: $mutationType
bestSelectionRatio: $bestSelectRatio

        """.trimMargin()
        )
        encryptedString = sanitizeString(encryptedString)
        if (toPlot) startPlot()
    }

    fun getDecryptionKey(): List<String> {
        initializeRandomPopulation()
        for (gen in 1..maxGen) {
            info("Generation: ${gen} Population: ${population.size} Min fitness prev gen: ${minFitness} for ${minFitnessIndividual?.getChromosomeString()}")
            info("Population: ${populationString()}")
            evolve()
            writeGenerationData(gen, (0 until population.size).map { fitness(population[it]) }.toMutableList())
            updatePlot(gen)
        }

        var minFound = 1000.0
        var minIndivList = HashSet<String>()
        for (indiv in population) {
            val indfit = fitness(indiv)
            if (indfit < minFound) {
                minFound = indfit
                minIndivList = HashSet()
                minIndivList.add(indiv.getChromosomeString())
            } else if (indfit == minFound) {
                minIndivList.add(indiv.getChromosomeString())
            }
        }

        closePlot()
        return minIndivList.toList()
    }

    fun info(i: Any) {
        infoOutput.println(i.toString())
        println(i.toString())
    }

    fun sanitizeString(c: String): String {
        //Sanitize the cipher text and key
        return sanitizeText(c)
    }

    fun writeGenerationData(gen: Int, data: List<Double>) {
        generationsData[gen] = data
        csvOutput.println(data.joinToString(","))
    }


    fun initializeRandomPopulation() {
        //  generate a random initial population, POP, of size popSize
        if (population.size < popSize) {
            addRandomIndividualsToPopulation(popSize - population.size)
        }
    }

    fun addRandomIndividualsToPopulation(numIndiv: Int) {
        for (indiv in 0 until numIndiv) {
            population.add(Individual(maxKeySize))
        }
    }

    // might be a good idea to cap the size of this HashMap
    val fitnessCache: HashMap<String, Double> = HashMap()

    fun fitness(indiv: Individual): Double {
        if (fitnessCache.containsKey(indiv.getChromosomeString())) {
            return fitnessCache[indiv.getChromosomeString()]!!
        } else {

            val fitness1 = funcTest.fitness(indiv.getChromosomeString(), encryptedString)
            val bigramFitness = bigramAnalyzer.analyse(funcTest.decrypt(indiv.getChromosomeString(), encryptedString))
            val trigramFitness = trigramAnalyzer.analyse(funcTest.decrypt(indiv.getChromosomeString(), encryptedString))
            val quadgramFitness =
                quadgramAnalyzer.analyse(funcTest.decrypt(indiv.getChromosomeString(), encryptedString))
            val quintgramFitness =
                quintgramAnalyzer.analyse(funcTest.decrypt(indiv.getChromosomeString(), encryptedString))

            val result =
                (1 * fitness1 + 2 * bigramFitness + 3 * trigramFitness + 4 * quadgramFitness + 5 * quintgramFitness) / 15

            fitnessCache[indiv.getChromosomeString()] = result

            if (result < minFitness) {
                minFitness = result
                minFitnessIndividual = indiv
            }
            return result
        }

    }

    fun startPlot() {
        val x = (0 until popSize).map { it.toDouble() }
        val y = (0 until popSize).map { 0.0 }
        currPopulationTrace = Trace.build(x = x.toDoubleArray(), y = y.toDoubleArray()) { name = "current" }

        val serverMeta = buildMeta {
            "update" to {
                "enabled" to true
            }
            "port" to (1234..49151).random()
        }

        populationFitnessPlot = Plotly.plot2D {
            trace(currPopulationTrace!!)
            layout {
                title = "Population diversity"
                xaxis {
                    title = "Individuals"
                }
                yaxis {
                    title = "Fitness"
                }
            }
        }

        val x2 = (0 until maxGen).map { it.toDouble() }
        val y2 = (0 until maxGen).map { 0.0 }
        genMeanTrace = Trace.build(x = x2.toDoubleArray(), y = y2.toDoubleArray()) { name = "average fitness" }

        genMinTrace = Trace.build(x = x2.toDoubleArray(), y = y2.toDoubleArray()) { name = "minimum fitness" }

        genMaxTrace = Trace.build(x = x2.toDoubleArray(), y = y2.toDoubleArray()) { name = "maximum fitness" }


        genMeanPlot = Plotly.plot2D {
            trace(genMeanTrace!!)
            trace(genMinTrace!!)
            trace(genMaxTrace!!)
            layout {
                title = "Mean fitness for each generation"
                xaxis {
                    title = "Generation"
                }
                yaxis {
                    title = "Mean fitness"
                }
            }
        }

        server = Plotly.serve(serverMeta) {}

        server!!.plot(populationFitnessPlot!!)
        server!!.plot(genMeanPlot!!)
    }

    val meanYList = (0..maxGen).map { 0.0 }.toDoubleArray()

    fun updatePlot(gen: Int = 0) {
        if (toPlot) {
            val x = (0 until population.size).map { it.toDouble() }
            val y = generationsData[gen]
            currPopulationTrace!!.y = y!!
            val trace = Trace.build(x = x.toDoubleArray(), y = y.toDoubleArray()) { name = gen.toString() }
            populationFitnessPlot!!.trace(trace)

            meanYList[gen] = y.average()
            genMeanTrace!!.y = meanYList
            genMinTrace!!.y =
                (0 until maxGen).map { if (generationsData.containsKey(it)) generationsData[it]!![0] else 0.0 }
            genMaxTrace!!.y =
                (0 until maxGen).map { if (generationsData.containsKey(it)) generationsData[it]!![generationsData[it]!!.size - 1] else 0.0 }
        }
    }

    fun closePlot() {
        if (toPlot) {
            genMeanPlot!!.makeFile(File("tests/plot$testAppendId.html"), show = true)
            server!!.stop()
        }
    }

    fun doCrossover(indiv1: Individual, indiv2: Individual): List<Individual> {
        return when (crossoverType) {
            CrossoverType.ONE_POINT -> onePointCrossover(indiv1, indiv2)
            CrossoverType.UNIFORM -> uniformCrossover(indiv1, indiv2)
        }

    }


    fun evolve() {

        val bestIndividualsToPreserve = ArrayList<Individual>()

//        for (index in ((population.size - bestPreserveCount) until population.size)) {
//            bestIndividualsToPreserve.add(population[index])
//        }
        for (index in 0 until bestPreserveCount) {
            bestIndividualsToPreserve.add(population[index])
        }

        population = tournamentSelection().toMutableList()
        population.shuffle()

        // apply crossover
        var count = 0
        while (count < population.size) {
            val parent1 = population[count]
            val parent2 = population[count + 1]
            val randCrossover = Random.nextDouble(0.0, 1.0)

            if (randCrossover < crossOverRate) {
                // do crossover

                val children = doCrossover(indiv1 = parent1, indiv2 = parent2)
                population[count] = children[0]
                population[count + 1] = children[1]

                // apply mutation
                val randMutation = Random.nextDouble(0.0, 1.0)
                if (randMutation < mutationRate) {
                    population[count].mutate(mutationType)
                }

                val randMutation2 = Random.nextDouble(0.0, 1.0)
                if (randMutation2 < mutationRate) {
                    population[count + 1].mutate(mutationType)
                }
                // eo mutation

            }
            count += 2
        }

        population.addAll(bestIndividualsToPreserve)

        sortPopulationByFitness()

        truncatePopulationToMaxSize()
    }

    fun sortPopulationByFitness() {
        population.sortBy {
            fitness(indiv = it)
        }
    }

    fun truncatePopulationToMaxSize() {
        //truncate population to keep best
        population.subList(popSize, population.size).clear()
    }

    fun tournamentSelection(): List<Individual> {
        return tournamentSelection(population)
    }

    // Use tournament selection
    // Pick k members at random then select the best of these ( k = 1, 2 ,.., 5)
    // Repeat to select more individuals (until population size is reached)

    fun tournamentSelection(fromPopulation: List<Individual>): List<Individual> {

        val new_population: MutableList<Individual> = ArrayList()
        // empty fromPopulation and create new fromPopulation
        while (new_population.size < fromPopulation.size) {
            val sample = pickRandomSampleFromPopulation()
            new_population.add(weightedSelectFromSample(sample))
        }

        return new_population
    }

    fun weightedSelectFromSample(sample: List<Individual>): Individual {
        val weights = ArrayList<Double>(sample.size)
        var currSum = 0.0

        val fitnessVals: MutableList<Double> = ArrayList()

        // using fitness values as probabilities for selection, doing a weighted random selection.

        // fill up probability and weigths table
        for (indiv in sample) {
            val fitness = 1 - fitness(indiv)
            currSum += fitness
            weights.add(currSum)
            fitnessVals.add(fitness)
        }

        var theChosenOne: Individual? = null
        val randomChosen = Random.nextDouble(0.0, currSum)

        for ((index, weight) in weights.withIndex()) {
            if (randomChosen <= weight) {
                theChosenOne = sample[index]
                break
            }
        }

        return theChosenOne!!
    }

    fun pickRandomSampleFromPopulation(): List<Individual> {
        val result = ArrayList<Individual>()
        for (i in 0 until selectionSampleSize) {
            if (population.size <= 0) break
            val randIndex = (0 until population.size).random()
            result.add(population[randIndex])
        }

        return result
    }

    fun populationString(printHowMany: Int = Math.min(population.size, 10)): String {
        val resultList = ArrayList<String>()
        val used: HashMap<Int, Boolean> = HashMap()

        while (resultList.size < printHowMany) {
            val randIndex = (0 until population.size).random()
            if (!(used.containsKey(randIndex) && used[randIndex]!!)) {
                resultList.add(population[randIndex].getChromosomeString())
                used[randIndex] = true
            }
        }

        return resultList.joinToString(",")
    }

}

fun main() {
    val main = GA()
    println(main.populationString())
    val decryptionKeys = main.getDecryptionKey()
    println(decryptionKeys)
    val decryptionKey = decryptionKeys[0]
    println(funcTest.decrypt(decryptionKey, main.encryptedString))
    println(
        "Fitness: ${funcTest.fitness(
            decryptionKey,
            main.encryptedString
        )} Min fitness found: ${main.minFitness} for ${main.minFitnessIndividual?.getChromosomeString()}"
    )
    println("Fitness: ${funcTest.fitness("drowssap", main.encryptedString)}")
}