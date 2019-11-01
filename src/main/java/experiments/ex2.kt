package experiments

import GA
val ex2str = "wyswfslnwzqwdwnvlesiayhidthqhgndwysnlzicjjpakadtveiitwrlhisktberwjtkmfdlkfgaemtjdctqfvabhehwdjeadkwkfkcdxcrxwwxeuvgowvbnwycowgfikvoxklrpfkgyawnrhftkhwrpwzcjksnszywyzkhdxcrxwslhrjiouwpilszagxasdghwlaocvkcpzwarwzcjgxtwhfdajstxqxbklstxreojveerkrbekeouwysafyichjilhgsxqxtkjanhwrbywlhpwkvaxmnsddsjlslghcopagnhrwdeluhtgjcqfvsxqkvakuitqtskxzagpfbusfddidioauaaffalgkiilfbswjehxjqahliqovcbkmcwhodnwksxreojvsdpskopagnhwysafyichdwczlcdpgcowwlpeffwlwacgjqewftxizqlawctvftimkirrwojqvevuvskxuobscstalyduvlpwftpgrzknwlpfv"
val ex2key = "drowssap"
fun main() {
    Test(
        GA(
            encryptedString = ex2str
            , maxKeySize = 8,
            popSize = 600,
            maxGen = 200,
            origMutationRate = 0.2,
            crossOverRate = 0.8,
            toPlot = true,
            eliminateWorst = false,
            tournamentSelectionType = GA.TournamentSelectionType.WEIGHTED,
            elitism = true
        ),
        givenKey = ex2key
    )
    // found at gen 58 drosssap with fitness 0.1660972357176244 with default params
    // Parameters: popSize: 300 crossOverRate: 0.8 maxKeySize: 8 maxGen: 400 selectionSampleSize: 3 mutationRate: 0.1 bestSelectionRatio: 0.1
    // Generation: 400 Population: 300 Min fitness prev gen: 0.1581296314805746 for drxwsssx
}

