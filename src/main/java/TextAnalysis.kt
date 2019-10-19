import com.beust.klaxon.Klaxon
import experiments.ex3key
import experiments.ex3str
import java.io.File
import kotlin.math.abs

interface FrequencyAnalyzer {
    fun analyse(text: String): Double
}

val ASSETS_PATH = "assets/"
val BIGRAM_FILE = "english_bigrams_frequency.json"
val TRIGRAM_FILE = "english_trigrams_frequency.json"
val QUADGRAM_FILE = "english_quadgrams_frequency.json"
val QUINTGRAM_FILE = "english_quintgrams_frequency.json"
val WORDS_FILE = "english_words_frequency.json"

class BigramAnalyzer : FrequencyAnalyzer {

    val bigramMap: Map<String, Double> = Klaxon().parse(File("$ASSETS_PATH$BIGRAM_FILE"))!!

    val size = 2

    override fun analyse(text: String): Double {
        return analyzeFrequencies(text, size, bigramMap)
    }

}

class TrigramAnalyzer : FrequencyAnalyzer {

    val trigramMap: Map<String, Double> = Klaxon().parse(File("$ASSETS_PATH$TRIGRAM_FILE"))!!

    val size = 3

    override fun analyse(text: String): Double {
        return analyzeFrequencies(text, size, trigramMap)
    }
}

class QuadgramAnalyzer : FrequencyAnalyzer {

    val quadgramMap: Map<String, Double> = Klaxon().parse(File("$ASSETS_PATH$QUADGRAM_FILE"))!!

    val size = 4

    override fun analyse(text: String): Double {
//        println("Initialization complete")
        return analyzeFrequencies(text, size, quadgramMap)
    }
}

class QuintgramAnalyzer : FrequencyAnalyzer {

    val quintgramMap: Map<String, Double> = Klaxon().parse(File("$ASSETS_PATH$QUINTGRAM_FILE"))!!

    val size = 5

    override fun analyse(text: String): Double {
        return analyzeFrequencies(text, size, quintgramMap)
    }
}


fun analyzeFrequencies(text: String, size: Int, nGramMap: Map<String, Double>): Double {
    val stext = sanitizeText(text)

//    println(nGramMap)

    val foundMap = HashMap<String, Double>()

    val totalOccr = stext.length - (size - 1)

    for (i in 0..(stext.length - size)) {

        var key = ""
        for (j in 0 until size) {
            key += stext[i + j]
        }
//        println(key)
        foundMap[key] = if (foundMap.containsKey(key)) foundMap[key]!! + 1.0 else 1.0
    }

    //Calculate the total difference between the expected frequencies and the actual frequencies
    var score = 0.0

    for (expectedKey in nGramMap.keys) {
        val expectedFrequency:Double = nGramMap[expectedKey]!!
        val foundFrequency:Double = foundMap.getOrDefault(expectedKey, 0.0) / totalOccr
//        println("Key: $expectedKey Expected: $expectedFrequency Found: $foundFrequency")
        val diff = abs(expectedFrequency - foundFrequency)
        score += diff
    }

//    for (key in foundMap.keys) {
////            foundMap[key] = foundMap[key]!! / total_occr
//        if (nGramMap.containsKey(key)) {
//            // change to getOrDefault() later, see if this crashes, shouldn't
//            val foundFrequency = foundMap[key]!! / totalOccr.toDouble()
//            val diff = abs(foundFrequency - nGramMap[key]!!)
////            println(diff)
//            println("$key - Found: ${foundFrequency} Expected: ${nGramMap[key]} diff : $diff")
//
//            score += diff
//        }
//    }

//        println(foundMap)
//        println("Score: $score")
    return score

}

fun sanitizeText(text: String): String {
    //Sanitize the cipher text and key
    var d = text.toLowerCase()
    d = d.replace("[^a-z]".toRegex(), "")
    d = d.replace("\\s".toRegex(), "")
    return d
}

fun main() {
//    val bigramMap: Map<String, Double> = Klaxon().parse(File("$ASSETS_PATH$WORDS_FILE"))!!

//    println(bigramMap)
//    testAnalyzer(BigramAnalyzer())
//    testAnalyzer(TrigramAnalyzer())
//    testAnalyzer(QuadgramAnalyzer())
    testAnalyzer(QuintgramAnalyzer())

//    println(fitness("io", "evih"))

}

fun testAnalyzer(analyzer: FrequencyAnalyzer) {
    println("Ex3: fitness of key")
    println("funcTest fitness: ${funcTest.fitness(ex3key, ex3str)}")
    val str = funcTest.decrypt(ex3key, ex3str)
    println("Analysizng: $str")
    println("Fitness: ${analyzer.analyse(str)}")
    println("Analysizng: $ex3str")
    println("Test garble: ${analyzer.analyse(ex3str)}")

//    println(
//        analyzer.analyse("Whaaaat")
//    )
//    println(
//        analyzer.analyse("Here you can find activities to practise your reading skills. Reading will help you to improve your understanding of the language and build your vocabulary. ... Select your level, from beginner (CEFR level A1) to advanced (CEFR level C1), and improve your reading skills ")
//    )
//    println(
//        analyzer.analyse(
//            "Anyone who reads Old and Middle English literary texts will be familiar with the mid-brown volumes of the EETS, with the symbol of Alfred's jewel embossed on the front cover. Most of the works attributed to King Alfred or to Aelfric, along with some of those by bishop Wulfstan and much anonymous prose and verse from the pre-Conquest period, are to be found within the Society's three series; all of the surviving medieval drama, most of the Middle English romances, much religious and secular prose and verse including the English works of John Gower, Thomas Hoccleve and most of Caxton's prints all find their place in the publications. Without EETS editions, study of medieval English texts would hardly be possible. As its name states, EETS was begun as a 'club', and it retains certain features of that even now. It has no physical location, or even office, no paid staff or editors, but books in the Original Series are published in the first place to satisfy subscriptions paid by individuals or institutions. This means that there is need for a regular sequence of new editions, normally one or two per year; achieving that sequence can pose problems for the Editorial Secretary, who may have too few or too many texts ready for publication at any one time. Details on a separate sheet explain how individual (but not institutional) members can choose to take certain back volumes in place of the newly published volumes against their subscriptions. On the same sheet are given details about the very advantageous discount available to individual members on all back numbers. In 1970 a Supplementary Series was begun, a series which only appears occasionally (it currently has 24 volumes within it); some of these are new editions of texts earlier appearing in the main series. Again these volumes are available at publication and later at a substantial discount to members. All these advantages can only be obtained through the Membership Secretary (the books are sent by post); they are not available through bookshops, and such bookstores as carry EETS books have only a very limited selection of the many published."
//        )
//    )
//    println(
//        analyzer.analyse(ex4str)
//    )
//    println(
//        analyzer.analyse(funcTest.decrypt(ex4key, ex4str))
//    )
//    println(
//        analyzer.analyse(ex3str)
//    )
//    println(
//        analyzer.analyse(funcTest.decrypt(ex3key, ex3str))
//    )
}
