import kotlin.collections.ArrayList
import kotlin.random.Random

class Main(
        val popSize: Int = 1000,
        val crossOverRate: Double = 0.9,
        val maxKeySize: Int = 100,
        val maxGen: Int = 50,
        val encryptedString: String = "" +
                "wyswfslnwzqwdwnvlesiayhidthqhgndwysnlzicjjpakadtveiitwrlhisktberwjtkmfdlkfgaemtjdctqfvabhehwdjeadkwkfkcdxcrxwwxeuvgowvbnwycowgfikvoxklrpfkgyawnrhftkhwrpwzcjksnszywyzkhdxcrxwslhrjiouwpilszagxasdghwlaocvkcpzwarwzcjgxtwhfdajstxqxbklstxreojveerkrbekeouwysafyichjilhgsxqxtkjanhwrbywlhpwkvaxmnsddsjlslghcopagnhrwdeluhtgjcqfvsxqkvakuitqtskxzagpfbusfddidioauaaffalgkiilfbswjehxjqahliqovcbkmcwhodnwksxreojvsdpskopagnhwysafyichdwczlcdpgcowwlpeffwlwacgjqewftxizqlawctvftimkirrwojqvevuvskxuobscstalyduvlpwftpgrzknwlpfv",
        val selectionSampleSize: Int = 5,
        val mutationRate: Double = 0.9
) {
    var population: MutableList<Individual> = ArrayList()

    init {
//        generate a random initial population, POP, of size popSize
        for (indiv in 0 until popSize) {
            population.add(Individual(maxKeySize))
        }

        for (gen in 1..maxGen) {
            println("Generation: ${gen} Population: ${population.size}")
            evolve()
        }

    }

    fun evolve() {
        // select a new population using a selection strategy
        // apply crossover and mutation

        population = tournamentSelection().toMutableList()
        population.shuffle()
        println(population.size)
        println("Parents selected size: ${population.size}")

//        val intactParentIndices: MutableList<Int> = (0 until parents.size).toMutableList()
//
//        val children: MutableList<Individual> = ArrayList()
//        while (intactParentIndices.size >= 2) {
//            val selectedIndex1 = intactParentIndices.random()
//            intactParentIndices.remove(selectedIndex1)
//            val selectedIndex2 = intactParentIndices.random()
//            intactParentIndices.remove(selectedIndex2)
//
//            val randCrossover = Random.nextDouble(0.0, 1.0)
//
//            if (randCrossover < crossOverRate) {
//                println("Did crossover")
//                // do crossover
//                children.addAll(onePointCrossover(indiv1 = parents[selectedIndex1], indiv2 = parents[selectedIndex2]))
//            } else {
//                children.add(parents[selectedIndex1])
//                children.add(parents[selectedIndex2])
//            }
//        }

        // apply crossover
        var count = 0
        while (count < population.size) {
            val parent1 = population[count]
            val parent2 = population[count + 1]
            val randCrossover = Random.nextDouble(0.0, 1.0)

            if (randCrossover < crossOverRate) {
                println("Did crossover")
                // do crossover

                val children = onePointCrossover(indiv1 = parent1, indiv2 = parent2)
                population[count] = children[0]
                population[count + 1] = children[1]
            }
            count += 2
        }

        // apply mutation
        for (indiv in population) {
            val randMutation = Random.nextDouble(0.0, 1.0)

            if (randMutation < mutationRate) {
                // do mutation
                println("Did mutation")
                indiv.applyScrambleMutation()
            }
        }


        println("before removing, population: ${population.size}")
        // replace all parents in population with children
        println("Children size ${children.size} Population size: ${population.size}")

        population = ArrayList()

        population.addAll(children)

        population.addAll(parents)

        while (population.size < popSize) {
            val selectedIndex1 = (0 until parents.size).random()
            val selectedIndex2 = (0 until parents.size).random()
            println("Did crossover")
            // do crossover
            population.addAll(onePointCrossover(indiv1 = parents[selectedIndex1], indiv2 = parents[selectedIndex2]))
        }
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
            val weights = ArrayList<Double>(sample.size)
//                var min_fitness = 0.0
//                var best_in_sample = sample[0]
            var currSum = 0.0

            val fitnessVals: MutableList<Double> = ArrayList()
            for (indiv in sample) {
                // using fitness values as probabilities for selection, doing a weighted random selection.

                val fitness = funcTest.fitness(
                        indiv.getChromosomeString(),
                        encryptedString
                )
                println("Fitness: ${fitness}")
                currSum += fitness
                weights.add(currSum)
                fitnessVals.add(fitness)

                val MULTIPLIER = 9999999

                var theChosenOne: Individual? = null

                for ((index, weight) in weights.withIndex()) {
                    val adjustedWeight: Int = (weight * MULTIPLIER).toInt()
                    if (fitnessVals[index] * MULTIPLIER >= adjustedWeight) {
                        theChosenOne = sample[index]
                        break
                    }
                }

                new_population.add(theChosenOne!!)
            }
        }

        return new_population
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


    fun populationString(): String {
        var result = ""
        for (indiv in population) {
            result += indiv.chromosome.joinToString("")
            result += "\n"
        }
        return result
    }

}

fun main() {
    println(Main().populationString())
}