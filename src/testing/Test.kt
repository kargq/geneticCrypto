package testing

import GA

class Test(
        val encryptedString: String,
        val givenKey: String? = null,
        val maxKeySize: Int = 100

) {
    init {

        if (givenKey != null) {
            println(funcTest.fitness(givenKey, encryptedString))
            println("Decrypted: ${funcTest.decrypt(givenKey, encryptedString)}")
        }

        println("Trying to determine decrpytion key ...")

        val key = GA(encryptedString = encryptedString, maxKeySize = maxKeySize).getDecryptionKey()

        println("""Possible key: $key 
            Decrypted text from this key: ${funcTest.decrypt(key, encryptedString)}
            """.trimMargin())

    }
}