import hep.dataforge.meta.buildMeta
import kotlinx.html.currentTimeMillis
import scientifik.plotly.Plot2D
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Trace
import scientifik.plotly.server.PlotlyServer
import scientifik.plotly.server.serve
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random
import kotlin.collections.HashSet
import kotlin.math.ceil
import kotlin.math.min

val TEST_DIR = "output"

class GA(
    val madedirs: Boolean = File(TEST_DIR).mkdirs(), // make TEST_DIR if it does not exist
    val popSize: Int = 10000, // size of population
    val crossOverRate: Double = 0.8,
    val maxKeySize: Int = 8, // maximum number of generations
    val maxGen: Int = 100,
    // string to decrypt
    var encryptedString: String = "wyswfslnwzqwdwnvlesiayhidthqhgndwysnlzicjjpakadtveiitwrlhisktberwjtkmfdlkfgaemtjdctqfvabhehwdjeadkwkfkcdxcrxwwxeuvgowvbnwycowgfikvoxklrpfkgyawnrhftkhwrpwzcjksnszywyzkhdxcrxwslhrjiouwpilszagxasdghwlaocvkcpzwarwzcjgxtwhfdajstxqxbklstxreojveerkrbekeouwysafyichjilhgsxqxtkjanhwrbywlhpwkvaxmnsddsjlslghcopagnhrwdeluhtgjcqfvsxqkvakuitqtskxzagpfbusfddidioauaaffalgkiilfbswjehxjqahliqovcbkmcwhodnwksxreojvsdpskopagnhwysafyichdwczlcdpgcowwlpeffwlwacgjqewftxizqlawctvftimkirrwojqvevuvskxuobscstalyduvlpwftpgrzknwlpfv",
    // sample size for tournament selection
    val selectionSampleSize: Int = 3,
    val origMutationRate: Double = 0.3, // mutation rate
    val elitism: Boolean = true,
    // Ratio of best individuals preserved after each generation. Elitism
    // Only really works when eliminatingWorst
    val elitismRatio: Double = 0.0,
    // id for output files
    val testAppendId: String = (0..999999).random().toString(),
    // output csv data
    val csvOutput: PrintStream = PrintStream(File("$TEST_DIR/fitness$testAppendId.test.csv")),
    val toPlot: Boolean = true,
    val mutationType: Individual.MutationType = Individual.MutationType.SCRAMBLE,
    // output feed for generation
    val infoOutput: PrintStream = PrintStream(File("$TEST_DIR/info$testAppendId.test.txt")),
    // type of crossover.
    val crossoverType: CrossoverType = CrossoverType.ONE_POINT,
    val tournamentSelectionType: TournamentSelectionType = TournamentSelectionType.WEIGHTED,
    val eliminateWorst: Boolean = false,
    val monogram: Boolean = true,
    val bigram: Boolean = true,
    val trigram: Boolean = true,
    val quadgram: Boolean = true,
    val quintgram: Boolean = true,
    val randomNumberSeed: Long = (Long.MIN_VALUE..Long.MAX_VALUE).random()
) {
    private var population: MutableList<Individual> = ArrayList()
    private var generationsData: HashMap<Int, List<Double>> = HashMap()
    private var mutationRate = origMutationRate
    var minFitness: Double = Double.MAX_VALUE
    // Should not ever get added to population using this reference. This is just for seeing the best found so far.
    private val bestPreserveCount: Int
        get() {
            return min(ceil(elitismRatio * popSize.toDouble()).toInt(), popSize)
        }

    // Fitness helpers
    private val bigramAnalyzer: FrequencyAnalyzer? = if (bigram) BigramAnalyzer() else null
    private val trigramAnalyzer: FrequencyAnalyzer? = if (trigram) TrigramAnalyzer() else null
    private val quadgramAnalyzer: FrequencyAnalyzer? = if (quadgram) QuadgramAnalyzer() else null
    private val quintgramAnalyzer: FrequencyAnalyzer? = if (quintgram) QuintgramAnalyzer() else null
    // eo fitness helpers

    private val fitnessCache: LRUCacheMap<String, Double> = LRUCacheMap(popSize * 5) // capped size HashMap

    // elitism vars
    var minFitnessSet: MutableSet<String> = HashSet()
    private var minFitnessIndividual: Individual = Individual("a".repeat(maxKeySize))

    // plotting stuff
    private var server: PlotlyServer? = null
    private var populationFitnessPlot: Plot2D? = null
    private var currPopulationTrace: Trace? = null
    private var genMeanPlot: Plot2D? = null
    private var genMeanTrace: Trace? = null
    private var genMinTrace: Trace? = null
    private var genMaxTrace: Trace? = null
    private val meanYList = (0..maxGen).map { 0.0 }.toDoubleArray()
    // eo plotting stuff
    private var rg: Random


    init {
        rg = Random(randomNumberSeed)
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
elitismRatio: $elitismRatio
testAppendId: $testAppendId
tournamentSelectionType: $tournamentSelectionType
eliminateWorst: $eliminateWorst
randomNumberSeed: $randomNumberSeed
monogram: $monogram
bigram: $bigram
trigram: $trigram
quadgram: $quadgram
quintgram: $quintgram

        """.trimMargin()
        )


        encryptedString = sanitizeString(encryptedString)
        if (toPlot) startPlot()
    }

    fun getDecryptionKey(): List<String> {
        rg = Random(randomNumberSeed)
        val startTime = currentTimeMillis()
        initializeRandomPopulation()
        for (gen in 1..maxGen) {
            val genStartTime = currentTimeMillis()
            info(
                "Generation: ${gen} Population: ${population.size} Min fitness prev gen: ${minFitness} for ${if (minFitnessSet.isNotEmpty()) minFitnessSet.random(
                    rg
                ) else "none"}"
            )
            info("Population: ${populationString()}")
            evolve()
            writeGenerationData(gen, (0 until population.size).map { fitness(population[it]) }.toMutableList())
            updatePlot(gen)
            info("Time taken for generation: ${currentTimeMillis() - genStartTime}")
            info("Elapsed time: ${currentTimeMillis() - startTime}")
        }
        info("Total elapsed time: ${currentTimeMillis() - startTime}")
        info("Possible keys: ${minFitnessSet.toList()}")
        info("Decrypted text from this key: ${Vigenere.decrypt(minFitnessSet.toList()[0], encryptedString)}")
        closePlot()
        return minFitnessSet.toList()
    }

    fun initializeRandomPopulation() {
        //  generate a random initial population, POP, of size popSize
        if (population.size < popSize) {
            addRandomIndividualsToPopulation(popSize - population.size)
        }
    }

    private fun addRandomIndividualsToPopulation(numIndiv: Int) {
        for (indiv in 0 until numIndiv) {
            population.add(Individual(maxKeySize, rg))
        }
    }

    /**
     * @return Fitness 0<= value <=1, lesser is better
     */
    fun fitness(indiv: Individual): Double {
        val fitnessKey = sanitizeString(indiv.getChromosomeString())
        if (fitnessCache.containsKey(fitnessKey)) {
            return fitnessCache[fitnessKey]!!
        } else {
            var num = 0.0
            var den = 0.0

            if (monogram) {
                num += Vigenere.fitness(fitnessKey, encryptedString)
                den++
            }
            if (bigram) {
                num += bigramAnalyzer!!.analyse(Vigenere.decrypt(fitnessKey, encryptedString))
                den++
            }
            if (trigram) {
                num += trigramAnalyzer!!.analyse(Vigenere.decrypt(fitnessKey, encryptedString))
                den++
            }
            if (quadgram) {
                num += quadgramAnalyzer!!.analyse(Vigenere.decrypt(fitnessKey, encryptedString))
                den++
            }
            if (quintgram) {
                num += quintgramAnalyzer!!.analyse(Vigenere.decrypt(fitnessKey, encryptedString))
                den++
            }

            val result = num / den

//            println(result)

            fitnessCache[fitnessKey] = result

            if (result < minFitness) {
                minFitness = result
                minFitnessSet.clear()
                minFitnessSet.add(fitnessKey)
                minFitnessIndividual = indiv
            } else if (result == minFitness) {
                minFitnessSet.add(fitnessKey)
            }
            return result
        }

    }


    private fun doCrossover(indiv1: Individual, indiv2: Individual): List<Individual> {
        return when (crossoverType) {
            CrossoverType.ONE_POINT -> onePointCrossover(indiv1, indiv2, rg)
            CrossoverType.UNIFORM -> uniformCrossover(indiv1, indiv2, rg)
        }
    }


    private fun evolve() {
        val bestIndividualsToPreserve = ArrayList<Individual>()

        if (eliminateWorst) {
            for (index in 0 until bestPreserveCount) {
                bestIndividualsToPreserve.add(population[index])
            }
        }

        // elitism
        if (elitism) {
            population[0] = minFitnessIndividual
        }

        population = tournamentSelection().toMutableList()
        population.shuffle(rg)

        // apply crossover
        var count = 0
        while (count < population.size) {
            val parent1 = population[count]
            val parent2 = population[count + 1]
            val randCrossover = rg.nextDouble(0.0, 1.0)

            if (randCrossover < crossOverRate) {
                // do crossover

                val children = doCrossover(indiv1 = parent1, indiv2 = parent2)
                population[count] = children[0]
                population[count + 1] = children[1]

                // apply mutation
                val randMutation = rg.nextDouble(0.0, 1.0)
                if (randMutation < mutationRate) {
                    population[count].mutate(mutationType, rg)
                }

                val randMutation2 = rg.nextDouble(0.0, 1.0)
                if (randMutation2 < mutationRate) {
                    population[count + 1].mutate(mutationType, rg)
                }
                // eo mutation

            }
            count += 2
        }

        population.addAll(bestIndividualsToPreserve)

        if (eliminateWorst) {
            sortPopulationByFitness()
        } else population.shuffle(rg)

        truncatePopulationToMaxSize()
    }

    private fun sortPopulationByFitness() {
        population.sortBy {
            fitness(indiv = it)
        }
    }

    private fun truncatePopulationToMaxSize() {
        //truncate population to keep best
        population.subList(popSize, population.size).clear()
    }

    private fun tournamentSelection(): List<Individual> {
        return tournamentSelection(population)
    }

    // Use tournament selection
    // Pick k members at random then select the best of these ( k = 1, 2 ,.., 5)
    // Repeat to select more individuals (until population size is reached)

    enum class TournamentSelectionType {
        WEIGHTED, BEST
    }


    /**
     * Returns a new population by doing tournament selection on given population
     */
    private fun tournamentSelection(fromPopulation: List<Individual>): List<Individual> {
        val newPopulation: MutableList<Individual> = Collections.synchronizedList(ArrayList())
        for (i in fromPopulation.indices) {
            val sample = pickRandomSampleFromPopulation()
            newPopulation.add(
                when (tournamentSelectionType) {
                    TournamentSelectionType.WEIGHTED -> weightedSelectFromSample(sample)
                    TournamentSelectionType.BEST -> bestSelectFromSample(sample)
                }
            )
        }

        return newPopulation
    }

    /**
     * Select the individual with the best fitness from sample
     */
    private fun bestSelectFromSample(sample: List<Individual>): Individual {
        return sample.minBy {
            fitness(it)
        }!!
    }

    // using fitness values as probabilities for selection, doing a weighted random selection. Better fitness has higher
    // probability of being selected.
    // TODO: check implementation
    fun weightedSelectFromSample(sample: List<Individual>): Individual {
        val weights = ArrayList<Double>(sample.size)
        var currSum: Double = 0.0

        val inverseFitnessVals: MutableList<Double> = ArrayList()

        // using fitness values as probabilities for selection, doing a weighted random selection.

        // fill up probability and weights table
        for (indiv in sample) {
            val inverseFitness = 1 / fitness(indiv)
            currSum += inverseFitness
            weights.add(currSum)
            inverseFitnessVals.add(inverseFitness)
        }

        var theChosenOne: Individual? = null
        val randomChosen = rg.nextDouble(0.0, currSum)

        for ((index, weight) in weights.withIndex()) {
            if (randomChosen <= weight) {
                theChosenOne = sample[index]
                break
            }
        }

        return theChosenOne!!
    }

    // pick a random sample from population of size selectionSampleSize
    fun pickRandomSampleFromPopulation(): List<Individual> {
        val result = ArrayList<Individual>()
        for (i in 0 until selectionSampleSize) {
            if (population.size <= 0) break
            val randIndex = (0 until population.size).random(rg)
            result.add(population[randIndex])
        }

        return result
    }

    private fun info(i: Any) {
        infoOutput.println(i.toString())
        println(i.toString())
    }

    private fun sanitizeString(c: String): String {
        //Sanitize the cipher text and key
        return sanitizeText(c)
    }

    private fun writeGenerationData(gen: Int, data: List<Double>) {
        generationsData[gen] = data
        csvOutput.println("$gen,${data.joinToString(",")}")
    }


    // Prints out a few individuals from the population
    fun populationString(printHowMany: Int = Math.min(population.size, 10)): String {
        val resultList = ArrayList<String>()
        val used: HashMap<Int, Boolean> = HashMap()

        while (resultList.size < printHowMany) {
            val randIndex = (0 until population.size).random(rg)
            if (!(used.containsKey(randIndex) && used[randIndex]!!)) {
                resultList.add(population[randIndex].getChromosomeString())
                used[randIndex] = true
            }
        }

        return resultList.joinToString(",")
    }


    private fun startPlot() {
        val x = (0 until popSize).map { it.toDouble() }
        val y = (0 until popSize).map { 0.0 }
        currPopulationTrace = Trace.build(x = x.toDoubleArray(), y = y.toDoubleArray()) { name = "current" }

        val serverMeta = buildMeta {
            "update" to {
                "enabled" to true
            }
            "port" to (1234..49151).random(rg)
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

    private fun updatePlot(gen: Int = 0) {
        if (toPlot) {
            val x = (0 until population.size).map { it.toDouble() }
            val y = generationsData[gen]
            currPopulationTrace!!.y = y!!
            val trace = Trace.build(x = x.toDoubleArray(), y = y.toDoubleArray()) { name = gen.toString() }
            populationFitnessPlot!!.trace(trace)

            meanYList[gen] = y.average()
            genMeanTrace!!.y = meanYList
            genMinTrace!!.y =
                (0 until maxGen).map { if (generationsData.containsKey(it)) generationsData[it]!!.min() else 0.0 }
            genMaxTrace!!.y =
                (0 until maxGen).map { if (generationsData.containsKey(it)) generationsData[it]!!.max() else 0.0 }
        }
    }

    private fun closePlot() {
        if (toPlot) {
            genMeanPlot!!.makeFile(File("tests/plot$testAppendId.html"), show = false)
            server!!.stop()
        }
    }

}

enum class CrossoverType {
    ONE_POINT, UNIFORM
}
