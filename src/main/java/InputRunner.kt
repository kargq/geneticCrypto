import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.lang.Exception
import java.nio.file.Paths
import java.util.*
import kotlin.collections.HashMap

class InputRunner(input: InputStream) {
    init {
        try {
            println("Current directory: ${Paths.get("").toAbsolutePath()}")
            val scn: Scanner = Scanner(input)

            val mapi: HashMap<String, String> = HashMap()

            var key: String = ""
            var value: String = ""
            while (value != "end" && key != "end") {
                key = if (scn.hasNext()) scn.next() else "end"
                value = if (scn.hasNext()) scn.next() else "end"
                mapi[key] = value
            }

            println(mapi)

            // just an empty printstream
            val dumpStream = PrintStream(object : OutputStream() {
                override fun write(b: Int) {
                }

                override fun write(b: ByteArray) {
                }

                override fun write(b: ByteArray, off: Int, len: Int) {
                }

                override fun flush() {
                }

                override fun close() {

                }
            })

            val default: GA = GA(
                csvOutput = dumpStream,
                infoOutput = dumpStream,
                toPlot = false,
                monogram = false,
                bigram = false,
                trigram = false,
                quadgram = false,
                quintgram = false,
                silenceOut = true
            )

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
            e.printStackTrace()
            println("===========================")
            println()
            println(
                "Something went wrong, please check the input file and try again. There should be a input.txt file in ${
                Paths.get("").toAbsolutePath()
                }"

            )
        }
    }
}

fun main() {
    InputRunner(FileInputStream("input.txt"))
}