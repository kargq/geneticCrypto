package experiments

import GA
import funcTest

class Test(
    val ga: GA,
    val givenKey: String? = null
) {


    init {
        if (givenKey != null) {
            println(funcTest.fitness(givenKey, ga.encryptedString))
            println("Decrypted: ${funcTest.decrypt(givenKey, ga.encryptedString)}")
        }

        println("Trying to determine decrpytion key ...")

        val key = ga.getDecryptionKey()



        println("""Possible key: $key 
            Decrypted text from this key: ${funcTest.decrypt(key, ga.encryptedString)}
            """.trimMargin())

    }
}