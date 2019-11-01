package experiments

import GA
import Individual
import funcTest

class Test(
    val ga: GA,
    val givenKey: String? = null
) {


    init {
        if (givenKey != null) {
            println(
                "Fitness of given key from in use func: ${
                GA(
                    toPlot = false,
                    encryptedString = ga.encryptedString,
                    maxKeySize = ga.maxKeySize,
                    randomNumberSeed = ga.randomNumberSeed,
                    monogram = ga.monogram,
                    bigram = ga.bigram,
                    trigram = ga.trigram,
                    quadgram = ga.quadgram,
                    quintgram = ga.quintgram
                ).fitness(Individual(givenKey))
                }"
            )
            println("Decrypted: ${funcTest.decrypt(givenKey, ga.encryptedString)}")
        }

        println("Trying to determine decrpytion key ...")

        val keys = ga.getDecryptionKey()

        println(
            """Possible keys: $keys 
            Decrypted text from this key: ${funcTest.decrypt(keys[0], ga.encryptedString)}
            """.trimMargin()
        )

    }
}