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
        encryptedString = sanitizeString(encryptedString)
    }

    fun getDecryptionKey(): String {
        initializeRandomPopulation()
        for (gen in 1..maxGen) {
            println("Generation: ${gen} Population: ${population.size} Min fitness prev gen: ${minFitness} for ${minFitnessIndividual?.getChromosomeString()}")
            println("Population: ${populationString()}")
            evolve()
        }

        var minFit = 1.0
//        var minIndiv: Individual? = null
//        for (indiv in population) {
//            val currFit = fitness(indiv)
//            if (minFit < currFit) {
//                minIndiv = indiv
//                minFit = currFit
//            }
//        }
//
//        return minIndiv!!.getChromosomeString()


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

        population.sortBy {
            fitness(indiv = it)
        }

        val bestIndivCount:Int = (bestSelectRatio * popSize.toDouble()).toInt()

        val bestIndividualsToPreserve = ArrayList<Individual>()

        for (index in ((population.size - bestIndivCount) until population.size)) {
            bestIndividualsToPreserve.add(population[index])
        }

//        var maximum = 0.0
//        var minind: Individual? = null
//        for (indiv in population) {
//            if (fitness(indiv) > maximum) {
//                maximum = fitness(indiv)
//                minind = indiv
//            }
//        }
//        population.remove(minind)

//        if (minFitnessIndividual != null) population.add(minFitnessIndividual!!)
        // select a new population using a selection strategy
        // apply crossover and mutation

        population = tournamentSelection().toMutableList()
        population.shuffle()
//        println(population.size)
//        println("Parents selected size: ${population.size}")

        // apply crossover
        var count = 0
        while (count < population.size) {
            val parent1 = population[count]
            val parent2 = population[count + 1]
            val randCrossover = Random.nextDouble(0.0, 1.0)

            if (randCrossover < crossOverRate) {
//                println("Did crossover")
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




//        for (index in 0 until bestIndivCount) {
//            val popIndex = population.size - 1 - bestIndivCount
//            population[popIndex] = bestIndividualsToPreserve[index]
//        }

        population.addAll(bestIndividualsToPreserve)

        population.sortBy {
            fitness(indiv = it)
        }

//        println("Pouplation size before: ${population.size}")


        //truncate population to keep best
        population.subList(popSize, population.size).clear()

//        println("Pouplation size after: ${population.size}")


        // apply mutation
        // instead applying mutation to each new child.
//        for (indiv in population) {
//            val randMutation = Random.nextDouble(0.0, 1.0)
//
//            if (randMutation < mutationRate) {
//                // do mutation
//                println("Did mutation")
//                indiv.applyScrambleMutation()
//            }
//        }
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
//                var min_fitness = 0.0
//                var best_in_sample = sample[0]
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

//        println(weights)


        var theChosenOne: Individual? = null

        val randomChosen = Random.nextDouble(0.0, currSum)

//        println(randomChosen)
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
//        for (indiv in population) {
//            result += indiv.chromosome.joinToString("")
//            result += "\n
//          "
//        }ngb     aq   q=[]\'ASSlop;[]


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