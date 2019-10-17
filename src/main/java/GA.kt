import kotlin.collections.ArrayList
import kotlin.random.Random

class GA(
        val popSize: Int = 10000,
        val crossOverRate: Double = 0.8,
        val maxKeySize: Int = 8,
        val maxGen: Int = 100,
        var encryptedString: String = "wyswfslnwzqwdwnvlesiayhidthqhgndwysnlzicjjpakadtveiitwrlhisktberwjtkmfdlkfgaemtjdctqfvabhehwdjeadkwkfkcdxcrxwwxeuvgowvbnwycowgfikvoxklrpfkgyawnrhftkhwrpwzcjksnszywyzkhdxcrxwslhrjiouwpilszagxasdghwlaocvkcpzwarwzcjgxtwhfdajstxqxbklstxreojveerkrbekeouwysafyichjilhgsxqxtkjanhwrbywlhpwkvaxmnsddsjlslghcopagnhrwdeluhtgjcqfvsxqkvakuitqtskxzagpfbusfddidioauaaffalgkiilfbswjehxjqahliqovcbkmcwhodnwksxreojvsdpskopagnhwysafyichdwczlcdpgcowwlpeffwlwacgjqewftxizqlawctvftimkirrwojqvevuvskxuobscstalyduvlpwftpgrzknwlpfv",
        val selectionSampleSize: Int = 3,
        val mutationRate: Double = 0.3,
        val bestSelectRatio: Double = 0.1
) {
    // larger k is more pressure
    var population: MutableList<Individual> = ArrayList()

    init {
        println("Parameters: popSize: $popSize crossOverRate: $crossOverRate maxKeySize: $maxKeySize maxGen: $maxGen selectionSampleSize: $selectionSampleSize mutationRate: $mutationRate bestSelectionRatio: $bestSelectRatio")

        encryptedString = sanitizeString(encryptedString)
    }

    fun getDecryptionKey(): String {
        initializeRandomPopulation()
        for (gen in 1..maxGen) {
            println("Generation: ${gen} Population: ${population.size} Min fitness prev gen: ${minFitness} for ${minFitnessIndividual?.getChromosomeString()}")
            println("Population: ${populationString()}")
            evolve()
        }

        return minFitnessIndividual!!.getChromosomeString()
    }

    fun sanitizeString(c: String): String {
        //Sanitize the cipher text and key
        var d = c.toLowerCase()
        d = d.replace("[^a-z]".toRegex(), "")
        d = d.replace("\\s".toRegex(), "")
        return d
    }

    fun initializeRandomPopulation() {
        //        generate a random initial population, POP, of size popSize
        for (indiv in 0 until popSize) {
            population.add(Individual(maxKeySize))
        }
    }

    var minFitness: Double = 1.0
    var minFitnessIndividual: Individual? = null

    fun fitness(indiv: Individual): Double {
        val result = funcTest.fitness(indiv.getChromosomeString(), encryptedString)
        if (result < minFitness) {
            minFitness = result
            minFitnessIndividual = indiv
        }
        return result
    }

    fun evolve() {
        val bestIndivCount:Int = (bestSelectRatio * popSize.toDouble()).toInt()

        val bestIndividualsToPreserve = ArrayList<Individual>()

        for (index in ((population.size - bestIndivCount) until population.size)) {
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

                val children = onePointCrossover(indiv1 = parent1, indiv2 = parent2)
                population[count] = children[0]
                population[count + 1] = children[1]

                // apply mutation
                // apply mutation
                val randMutation = Random.nextDouble(0.0, 1.0)
                if (randMutation < mutationRate) {
                    population[count].applyScrambleMutation()
                }

                val randMutation2 = Random.nextDouble(0.0, 1.0)
                if (randMutation2 < mutationRate) {
                    population[count + 1].applyScrambleMutation()
                }
                // eo mutation

            }
            count += 2
        }

        population.addAll(bestIndividualsToPreserve)

        population.sortBy {
            fitness(indiv = it)
        }

        //truncate population to keep best
        population.subList(popSize, population.size).clear()

    }

    fun tournamentSelection(): List<Individual> {
        return tournamentSelection(population)
    }

    // Use tournament selection
    // Pick k members at random then select the best of these ( k = 1, 2 ,.., 5)
    // Repeat to select more individuals (until population size is reached)

    // Whether contestants are picked with replacement
    // Picking without replacement increases selection pressure
    // I will pick without replacement.


    // Whether fittest contestant always wins (deterministic) or this happens with probability p
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
        var result = ""
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
    val decryptionKey = main.getDecryptionKey()
    println(decryptionKey)
    println(funcTest.decrypt(decryptionKey, main.encryptedString))
    println("Fitness: ${funcTest.fitness(decryptionKey, main.encryptedString)} Min fitness found: ${main.minFitness} for ${main.minFitnessIndividual?.getChromosomeString()}")
    println("Fitness: ${funcTest.fitness("drowssap", main.encryptedString)}")
}