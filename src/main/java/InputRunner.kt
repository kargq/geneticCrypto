import java.io.FileInputStream
import java.io.InputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class InputRunner(input: InputStream) {
    init {
        try {
            val scn: Scanner = Scanner(input)

            val mapi: HashMap<String, String> = HashMap()

            while (scn.hasNext()) {
                val key = scn.next()
                val value = scn.next()
                mapi[key] = value
            }

            val default: GA = GA()

            val popSize: Int = if (mapi.containsKey("popSize")) mapi["popSize"]!!.toInt() else default.popSize
            val crossOverRate: Double =
                if (mapi.containsKey("crossOverRate")) mapi["crossOverRate"]!!.toDouble() else default.crossOverRate
            val maxKeySize: Int =
                if (mapi.containsKey("maxKeySize")) mapi["maxKeySize"]!!.toInt() else default.maxKeySize
            val maxGen: Int = if (mapi.containsKey("maxGen")) mapi["maxGen"]!!.toInt() else default.maxGen
            val encryptedString: String =
                if (mapi.containsKey("encryptedString")) mapi["encryptedString"]!! else default.encryptedString
            val selectionSampleSize: Int =
                if (mapi.containsKey("selectionSampleSize")) mapi["selectionSampleSize"]!!.toInt() else default.selectionSampleSize
            val origMutationRate: Double =
                if (mapi.containsKey("origMutationRate")) mapi["origMutationRate"]!!.toDouble() else default.origMutationRate
            val elitism: Boolean = if (mapi.containsKey("elitism")) mapi["elitism"]!!.toBoolean() else default.elitism
            val testAppendId: String =
                if (mapi.containsKey("testAppendId")) mapi["testAppendId"]!! else default.testAppendId
            val toPlot: Boolean = if (mapi.containsKey("toPlot")) mapi["toPlot"]!!.toBoolean() else default.toPlot
            val mutationType: Individual.MutationType = if (mapi.containsKey("mutationType")) {
                when (mapi["mutationType"]!!) {
                    "INSERTION" -> Individual.MutationType.INSERTION
                    "SCRAMBLE" -> Individual.MutationType.SCRAMBLE
                    "SCRAMBLE_INSERTION" -> Individual.MutationType.SCRAMBLE_INSERTION
                    else -> default.mutationType
                }
            } else default.mutationType
            val crossoverType: CrossoverType = if (mapi.containsKey("crossoverType")) {
                when (mapi["crossoverType"]) {
                    "ONE_POINT" -> CrossoverType.ONE_POINT
                    "UNIFORM" -> CrossoverType.UNIFORM
                    else -> default.crossoverType
                }
            } else default.crossoverType
            val tournamentSelectionType: GA.TournamentSelectionType = if (mapi.containsKey("tournamentSelectionType")) {
                when (mapi["tournamentSelectionType"]) {
                    "BEST" -> GA.TournamentSelectionType.BEST
                    "WEIGHTED" -> GA.TournamentSelectionType.WEIGHTED
                    else -> default.tournamentSelectionType
                }
            } else default.tournamentSelectionType
            val eliminateWorst: Boolean =
                if (mapi.containsKey("eliminateWorst")) mapi["eliminateWorst"]!!.toBoolean() else default.eliminateWorst
            val randomNumberSeed: Long =
                if (mapi.containsKey("randomNumberSeed")) mapi["randomNumberSeed"]!!.toLong() else default.randomNumberSeed

            val ga = GA(
                popSize = popSize,
                crossOverRate = crossOverRate,
                maxKeySize = maxKeySize,
                maxGen = maxGen,
                encryptedString = encryptedString,
                selectionSampleSize = selectionSampleSize,
                origMutationRate = origMutationRate,
                elitism = elitism,
                testAppendId = testAppendId,
                toPlot = toPlot,
                mutationType = mutationType,
                crossoverType = crossoverType,
                tournamentSelectionType = tournamentSelectionType,
                eliminateWorst = eliminateWorst,
                randomNumberSeed = randomNumberSeed
            )

            val keys = ga.getDecryptionKey()

            println()
            println()
            println("Possible Keys: $keys")

        } catch (e: Exception) {
            println("Something went wrong, please check the input file and try again.")
        }
    }
}

fun main() {
    InputRunner(FileInputStream("input.txt"))
}