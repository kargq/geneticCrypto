class Main(
    val popSize: Int = 999,
    val crossOverRate: Double = 0.7,
    val maxKeySize: Int = 10,
    val maxGen: Int = 50,
    val encryptedString: String = "" +
            "wyswfslnwzqwdwnvlesiayhidthqhgndwysnlzicjjpakadtveiitwrlhisktberwjtkmfdlkfgaemtjdctqfvabhehwdjeadkwkfkcdxcrxwwxeuvgowvbnwycowgfikvoxklrpfkgyawnrhftkhwrpwzcjksnszywyzkhdxcrxwslhrjiouwpilszagxasdghwlaocvkcpzwarwzcjgxtwhfdajstxqxbklstxreojveerkrbekeouwysafyichjilhgsxqxtkjanhwrbywlhpwkvaxmnsddsjlslghcopagnhrwdeluhtgjcqfvsxqkvakuitqtskxzagpfbusfddidioauaaffalgkiilfbswjehxjqahliqovcbkmcwhodnwksxreojvsdpskopagnhwysafyichdwczlcdpgcowwlpeffwlwacgjqewftxizqlawctvftimkirrwojqvevuvskxuobscstalyduvlpwftpgrzknwlpfv",
    val selectionSampleSize: Int = 5
) {
    var population: MutableList<Individual> = ArrayList()

    init {
//        generate a random initial population, POP, of size popSize
        for (indiv in 0 until popSize) {
            population.add(Individual(maxKeySize))
        }

        for (gen in 1..maxGen) {
//            evaluate fitness of each individual in POP
//            for (indiv in population) {
//                funcTest.fitness(
//                    indiv.getChromosomeString(),
//                    encryptedString
//                )
//            }

            //            select a new population using a selection strategy

            // Use tournament selection
            // Pick k members at random then select the best of these ( k = 1, 2 ,.., 5)
            // Repeat to select more individuals (until population size is reached)

            // Whether contestants are picked with replacement
            // Picking without replacement increases selection pressure
            // I will pick without replacement.


            // Whether fittest contestant always wins (deterministic) or this happens with probability p

            val new_population: MutableList<Individual> = ArrayList()

            // empty population and create new population
            while (population.size > 0) {
                val sample = pickRandomSampleFromPopulation()
                val weights = ArrayList<Double>(sample.size)
//                var min_fitness = 0.0
//                var best_in_sample = sample[0]
                var currSum = 0.0

                var fitnessVals: MutableList<Double> = ArrayList()
                for (indiv in sample) {
                    // using fitness values as probabilities for selection, doing a weighted random selection.

                    val fitness = funcTest.fitness(
                        indiv.getChromosomeString(),
                        encryptedString
                    )
                    currSum += fitness
                    weights.add(currSum)
                    fitnessVals.add(fitness)
//                    if (min_fitness < fitness) {
//                        best_in_sample = indiv
//                        min_fitness = fitness
//                    }
                }

                val MULTIPLIER = 9999999

                var intSum: Int = (currSum * MULTIPLIER).toInt()

                var theChosenOne: Individual? = null
//                var theChosenOne: Individual = sample[0]

                for ((index, weight) in weights.withIndex()) {
                    val adjustedWeight: Int = (weight * MULTIPLIER).toInt()
                    if (fitnessVals[index] >= adjustedWeight) {
                        theChosenOne = sample[index]
                        break
                    }
                }

                new_population.add(theChosenOne!!)
//                new_population.add(theChosenOne)

//                new_population.add(best_in_sample)
            }

            new_population
//            apply crossover and mutation
        }
    }

    fun pickRandomSampleFromPopulation(): List<Individual> {
        val result = ArrayList<Individual>()
        for (i in 0 until selectionSampleSize) {
            if (population.size <= 0) break
            val randIndex = (0 until population.size).random()
            result.add(population[randIndex])
            population.removeAt(randIndex)
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
//    print("Enter population size: ")
//    val popSize: Int? = readLine()!!.toIntOrNull()
//    print("Enter cross over rate: ")
//    val crossOverRate: Double? = readLine()!!.toDoubleOrNull()
//    print("Enter key size: ")
//    val keySize: Int? = readLine()!!.toIntOrNull()
//    print("Enter key size: ")
//    val maxGen: Int? = readLine()!!.toIntOrNull()
//    Main(popSize!!, crossOverRate!!, keySize!!)
    println(Main().populationString())
}