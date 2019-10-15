fun testOnePointCrossover() {
    onePointCrossover(Individual(5), Individual(5))

}

fun testMutation() {
    val indiv = Individual(15)
    println(indiv.getChromosomeString())
    indiv.applyScrambleMutation(10)
    println(indiv.getChromosomeString())
}

fun testTournamentSelection() {
    val genalgo = GA()
    genalgo.initializeRandomPopulation()
    val sample = genalgo.pickRandomSampleFromPopulation()
    for (ind in sample) {
        println(ind.getChromosomeString())
        println(genalgo.fitness(ind))
    }

    val selected = genalgo.weightedSelectFromSample(sample)

    println("Selected: ${selected.getChromosomeString()}")
//    genalgo.pi
}


fun main() {
//    testMutation()
    println("Decrypted: ${funcTest.decrypt("sssspsss", GA().encryptedString)}")

    println("Fitness: ${funcTest.fitness("drowssap", GA().encryptedString)}")

    testTournamentSelection()

}
