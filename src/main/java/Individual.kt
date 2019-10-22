open class Individual {

    var chromosome: CharArray

    constructor(chromosome: CharArray) {
        this.chromosome = chromosome
    }

    constructor(chromosomeString: String) {
        this.chromosome = chromosomeString.toCharArray()
    }

    constructor(chromosomeSize: Int) {
        chromosome = CharArray(chromosomeSize)
        for (i in 0 until chromosomeSize) {
            chromosome[i] = getRandomAllowedChar()
        }
    }

    fun getRandomAllowedChar(): Char {
        return (('a'..'z') + '-').random()
    }

    fun getChromosomeString(): String {
        return chromosome.joinToString("")
    }

    fun chromosomeSize(): Int {
        return chromosome.size
    }

    enum class MutationType {
        SCRAMBLE, INSERTION, SCRAMBLE_INSERTION
    }

    fun mutate(type: MutationType) {
        when (type) {
            MutationType.SCRAMBLE -> applyScrambleMutation()
            MutationType.INSERTION -> applyInsertionMutation()
            MutationType.SCRAMBLE_INSERTION -> applySrambleInsertionMutation()
        }
    }

    fun applyScrambleMutation(scrambleSize: Int = chromosome.size / 2) {
        val randomIndices = ArrayList<Int>(scrambleSize)
        val range: MutableList<Int> = (0 until chromosomeSize()).toMutableList()
        for (i in 0 until scrambleSize) {
            val randIndex = range.random()
            range.remove(randIndex)
            randomIndices.add(randIndex)
        }

        val toScramble = CharArray(randomIndices.size)
        for ((iterIndex, ind) in randomIndices.withIndex()) {
            toScramble[iterIndex] = chromosome[ind]
        }

        val scrambled = toScramble.asList().shuffled()

        for ((iterIndex, ind) in randomIndices.withIndex()) {
            chromosome[ind] = scrambled[iterIndex]
        }
    }

    private fun applyInsertionMutation() {
        val pos = (0 until chromosomeSize()).random()
        chromosome[pos] = getRandomAllowedChar()
    }

    private fun applySrambleInsertionMutation() {
        applyScrambleMutation()
        applyInsertionMutation()
    }

    override fun toString(): String {
        val obj = super.toString()

        return "Obj: ${obj} Chromosome: ${getChromosomeString()}"
    }

}

fun onePointCrossover(indiv1: Individual, indiv2: Individual): List<Individual> {
    val chromosomeSize = indiv1.chromosomeSize()
    val crossoverPoint = (0..indiv1.chromosomeSize()).random()

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


fun uniformCrossover(indiv1: Individual, indiv2: Individual): List<Individual> {
    val chromosomeSize = indiv1.chromosomeSize()

    val child1 = Individual(indiv1.getChromosomeString())
    val child2 = Individual(indiv2.getChromosomeString())

    val result = ArrayList<Individual>(2)

    for (i in 0 until chromosomeSize) {
        // 50 % change of gene coming from either individual.
        child1.chromosome[i] = if ((1..2).random() == 1) indiv1.chromosome[i] else indiv2.chromosome[i]
        child2.chromosome[i] = if ((1..2).random() == 1) indiv1.chromosome[i] else indiv2.chromosome[i]
    }

    result.add(child1)
    result.add(child2)

    return result
}

enum class CrossoverType {
    ONE_POINT, UNIFORM
}