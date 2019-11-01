import kotlin.random.Random

val EMPTY_CHARACTER: Char = '-'
val EMPTY_CHAR_PROBABILITY: Double = 0.10

class Individual {

    enum class MutationType {
        SCRAMBLE, INSERTION, SCRAMBLE_INSERTION
    }

    var chromosome: CharArray

    constructor(chromosomeString: String) {
        this.chromosome = chromosomeString.toCharArray()
    }

    constructor(chromosomeSize: Int, rg: Random) {
        chromosome = CharArray(chromosomeSize)
        for (i in 0 until chromosomeSize) {
            chromosome[i] = getRandomAllowedChar(rg)
        }
    }

    private fun getRandomAllowedChar(rg: Random): Char {
        return if (rg.nextDouble(0.0, 1.0) < EMPTY_CHAR_PROBABILITY) EMPTY_CHARACTER else ('a'..'z').random(rg)
    }

    fun getChromosomeString(): String {
        return chromosome.joinToString("")
    }

    fun chromosomeSize(): Int {
        return chromosome.size
    }

    fun mutate(type: MutationType, rg: Random) {
        when (type) {
            MutationType.SCRAMBLE -> applyScrambleMutation(rg)
            MutationType.INSERTION -> applyInsertionMutation(rg)
            MutationType.SCRAMBLE_INSERTION -> applySrambleInsertionMutation(rg)
        }
    }

    fun applyScrambleMutation(rg: Random, scrambleSize: Int = chromosome.size / 2) {
        val randomIndices = ArrayList<Int>(scrambleSize)
        val range: MutableList<Int> = (0 until chromosomeSize()).toMutableList()
        for (i in 0 until scrambleSize) {
            val randIndex = range.random(rg)
            range.remove(randIndex)
            randomIndices.add(randIndex)
        }

        val toScramble = CharArray(randomIndices.size)
        for ((iterIndex, ind) in randomIndices.withIndex()) {
            toScramble[iterIndex] = chromosome[ind]
        }

        val scrambled = toScramble.asList().shuffled(rg)

        for ((iterIndex, ind) in randomIndices.withIndex()) {
            chromosome[ind] = scrambled[iterIndex]
        }
    }

    private fun applyInsertionMutation(rg: Random) {
        val pos = (0 until chromosomeSize()).random(rg)
        chromosome[pos] = getRandomAllowedChar(rg)
    }

    private fun applySrambleInsertionMutation(rg: Random) {
        applyScrambleMutation(rg)
        applyInsertionMutation(rg)
    }

    override fun toString(): String {
        val obj = super.toString()

        return "Obj: ${obj} Chromosome: ${getChromosomeString()}"
    }

}

fun onePointCrossover(indiv1: Individual, indiv2: Individual, rg: Random): List<Individual> {
    val chromosomeSize = indiv1.chromosomeSize()
    val crossoverPoint = (0..indiv1.chromosomeSize()).random(rg)

    val child1 = Individual(indiv1.getChromosomeString())
    val child2 = Individual(indiv2.getChromosomeString())

    val result = ArrayList<Individual>(2)

    for (i in crossoverPoint until chromosomeSize) {
        // swap chromosome vals at i
        child1.chromosome[i] = child2.chromosome[i].also { child2.chromosome[i] = child1.chromosome[i] }
    }

    result.add(child1)
    result.add(child2)

    return result
}


fun uniformCrossover(indiv1: Individual, indiv2: Individual, rg: Random): List<Individual> {
    val chromosomeSize = indiv1.chromosomeSize()

    val child1 = Individual(indiv1.getChromosomeString())
    val child2 = Individual(indiv2.getChromosomeString())

    val result = ArrayList<Individual>(2)

    for (i in 0 until chromosomeSize) {
        // 50 % change of gene coming from either individual.
        child1.chromosome[i] = if ((1..2).random(rg) == 1) indiv1.chromosome[i] else indiv2.chromosome[i]
        child2.chromosome[i] = if ((1..2).random(rg) == 1) indiv1.chromosome[i] else indiv2.chromosome[i]
    }

    result.add(child1)
    result.add(child2)

    return result
}