fun testOnePointCrossover(){
    onePointCrossover(Individual(5), Individual(5))

}

fun testMutation() {
    val indiv = Individual(15)
    println(indiv.getChromosomeString())
    indiv.applyScrambleMutation(10)
    println(indiv.getChromosomeString())
}


fun main() {
//    testMutation()

}
