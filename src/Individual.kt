class Individual{

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
            chromosome[i] = (('a'..'z') + '-').random()
//            chromosome[i] = ((97..122) + 45).random().toChar()
        }
    }

    fun getChromosomeString(): String {
        return chromosome.joinToString("")
    }

    fun chromosomeSize(): Int {
        return chromosome.size
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

//    println("Indiv1 " + indiv1.getChromosomeString())
//    println("Indiv2 " + indiv2.getChromosomeString())
//    println("Coitus with ${crossoverPoint}")
    for (i in crossoverPoint until chromosomeSize) {
        // swap chromosome vals at i
        indiv1.chromosome[i] = indiv2.chromosome[i].also { indiv2.chromosome[i] = indiv1.chromosome[i] }
    }
//    println("Indiv1 " + indiv1.getChromosomeString())
//    println("Indiv2 " + indiv2.getChromosomeString())

    result.add(child1)
    result.add(child2)

    return result

}