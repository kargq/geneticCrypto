package experiments

import GA

fun main() {
    Test(
        GA(
            encryptedString = "wyswfslnwzqwdwnvlesiayhidthqhgndwysnlzicjjpakadtveiitwrlhisktberwjtkmfdlkfgaemtjdctqfvabhehwdjeadkwkfkcdxcrxwwxeuvgowvbnwycowgfikvoxklrpfkgyawnrhftkhwrpwzcjksnszywyzkhdxcrxwslhrjiouwpilszagxasdghwlaocvkcpzwarwzcjgxtwhfdajstxqxbklstxreojveerkrbekeouwysafyichjilhgsxqxtkjanhwrbywlhpwkvaxmnsddsjlslghcopagnhrwdeluhtgjcqfvsxqkvakuitqtskxzagpfbusfddidioauaaffalgkiilfbswjehxjqahliqovcbkmcwhodnwksxreojvsdpskopagnhwysafyichdwczlcdpgcowwlpeffwlwacgjqewftxizqlawctvftimkirrwojqvevuvskxuobscstalyduvlpwftpgrzknwlpfv"
            , maxKeySize = 8,
            popSize = 300,
            maxGen = 400,
            origMutationRate = 0.1,
            crossOverRate = 0.8
        ),
        givenKey = "drowssap"
    )
    println("Press enter to quit")
    readLine()
    // found at gen 58 drosssap with fitness 0.1660972357176244 with default params
    // Parameters: popSize: 300 crossOverRate: 0.8 maxKeySize: 8 maxGen: 400 selectionSampleSize: 3 mutationRate: 0.1 bestSelectionRatio: 0.1
    // Generation: 400 Population: 300 Min fitness prev gen: 0.1581296314805746 for drxwsssx
}

