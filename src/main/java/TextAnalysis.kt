import com.beust.klaxon.Klaxon
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
    val foundMap = HashMap<String, Double>()
    val totalOccr = stext.length - (size - 1)

    for (i in 0..(stext.length - size)) {

        var key = ""
        for (j in 0 until size) {
            key += stext[i + j]
        }
        foundMap[key] = if (foundMap.containsKey(key)) foundMap[key]!! + 1.0 else 1.0
    }

    //Calculate the total difference between the expected frequencies and the actual frequencies
    var score = 0.0

    for (expectedKey in nGramMap.keys) {
        val expectedFrequency:Double = nGramMap[expectedKey]!!
        val foundFrequency:Double = foundMap.getOrDefault(expectedKey, 0.0) / totalOccr
        val diff = abs(expectedFrequency - foundFrequency)
        score += diff
    }

    return score

}

fun sanitizeText(text: String): String {
    //Sanitize the cipher text and key
    var d = text.toLowerCase()
    d = d.replace("[^a-z]".toRegex(), "")
    d = d.replace("\\s".toRegex(), "")
    return d
}