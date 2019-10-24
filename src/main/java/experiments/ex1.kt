package experiments

import GA
import funcTest

val ex1str = "xbwdesmhihslwhkktefvktkktcwfpiibihwmosfilojvooegvefwno chsuuspsureifakbnlalzsrsroiejwzgfpjczldokrceoahzshpbdw pcjstacgbarfwifwohylckafckzwwomlalghrtafchfetcgfpfrgxc lwzocdctmjebx"
val ex1key = "password"

fun main() {
    println(funcTest.fitness("lpwswokd", ex1str))
    println(funcTest.fitness("password", ex1str))

    Test(
        GA(
            encryptedString = ex1str,
            maxKeySize = 8,
            popSize = 300,
            maxGen = 100,
            origMutationRate = 0.1,
            crossOverRate = 0.8,
            eliminateWorst = true
        ),
        givenKey = ex1key
    )
    // found pnjx-roh with fitness 0.21
    readLine()
}


// Parameters: popSize: 300 crossOverRate: 0.8 maxKeySize: 8 maxGen: 200 selectionSampleSize: 3 mutationRate: 0.1 bestSelectionRatio: 0.1
// Generation: 200 Population: 300 Min fitness prev gen: 0.18451428472176196 for xawhworo