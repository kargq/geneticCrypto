import java.io.FileInputStream
import java.io.InputStream
import java.util.*

class InputRunner(input: InputStream, appendMore: String = "") {
    init {
        val scn: Scanner = Scanner(input)

        val numTests: Int = scn.nextInt()
        for (i in 0 until numTests) {
            scn.next()
            val popSize: Int = scn.nextInt()
            scn.next()
            val crossOverRate: Double = scn.nextDouble()
            scn.next()
            val maxKeySize: Int = scn.nextInt()
            scn.next()
            val minKeySize: Int = scn.nextInt()
            scn.next()
            val maxGen: Int = scn.nextInt()
            scn.next()
            val encryptedString: String = scn.next()
            scn.next()
            val selectionSampleSize: Int = scn.nextInt()
            scn.next()
            val origMutationRate: Double = scn.nextDouble()
            scn.next()
            val elitismRatio: Double = scn.nextDouble()
            scn.next()
            val testAppendId: String = scn.next() + appendMore
            scn.next()
            val toPlot: Boolean = scn.nextBoolean()
            scn.next()
            val mutationType: Individual.MutationType = when (scn.next()) {
                "SCRAMBLE" -> Individual.MutationType.SCRAMBLE
                "SCRAMBLE_INSERTION" -> Individual.MutationType.SCRAMBLE_INSERTION
                "INSERTION" -> Individual.MutationType.INSERTION
                else -> Individual.MutationType.SCRAMBLE_INSERTION
            }
            scn.next()
            val crossoverType: CrossoverType = when (scn.next()) {
                "ONE_POINT" -> CrossoverType.ONE_POINT
                "UNIFORM" -> CrossoverType.UNIFORM
                else -> CrossoverType.ONE_POINT
            }
            scn.next()
            val tournamentSelectionType: GA.TournamentSelectionType = when (scn.next()) {
                "WEIGHTED" -> GA.TournamentSelectionType.WEIGHTED
                "BEST" -> GA.TournamentSelectionType.BEST
                else -> GA.TournamentSelectionType.WEIGHTED
            }
            scn.next()
            val eliminateWorst: Boolean = scn.nextBoolean()

            val ga = GA(
                popSize = popSize,
                crossOverRate = crossOverRate,
                maxKeySize = maxKeySize,
                minKeySize = minKeySize,
                maxGen = maxGen,
                encryptedString = encryptedString,
                selectionSampleSize = selectionSampleSize,
                origMutationRate = origMutationRate,
                elitismRatio = elitismRatio,
                testAppendId = testAppendId,
                toPlot = toPlot,
                mutationType = mutationType,
                crossoverType = crossoverType,
                tournamentSelectionType = tournamentSelectionType,
                eliminateWorst = eliminateWorst
            )

            val keys = ga.getDecryptionKey()

            println()
            println()
            println("Possible Keys: $keys")

            scn.next()
        }
    }
}

fun main() {
    InputRunner(FileInputStream("input.txt"))
}