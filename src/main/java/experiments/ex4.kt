package experiments

import GA

val ex4str = "lbtqrtttisjskmxbgaixizptcftdhglhbwalsijeeybbztnixirbviwrqblpbbhjmwlesnwidcttkfclkicvagokwbkqdpvwzanolafymgvuszntlryiyllhpczbrircqhrqchnzwcgtigplzfkiuvdeampcabatntokdgztyuloceekmtbdyajwfzagavvrbmneasstuwnlwxxxngmtomkhgdpawxvvlbvitsmuwpohlgmvaiwcrmihbitbsmfbvgxbtvtskhbvcfsewhambgsnpnrpgzptdbecxzwmdephfgldfsfyimkkszlisyzppjqxbjequwrnwxbvtsmkuycxltiparrryplatxmpxetatlzrtyifvmlzpmcgdewnetkzazwmbjicaccecdhkvuuhhypvrpcpatwtnmxijdqpkpipejuddrmrmgoyaprnlepfktoupbzxucvqxinduxgvpopwtytrxgteqsxrkiogvnzkrdipezxscuqhcgfiuizihemjenovpbqywwvxvzelbowiphqskmtieqnepjzlrcxqftbghmpztznwvglwmcxcgwkctepjciiszjkxzxeqdzyephbdgdyjjiimeqfyqhvatlepwgjasqwmrzjvstdslkwhvpzuhcmfuexasmsklqjfinicawwpbvyakmjifhnlbziejiemvtciypiqaxqqqnqbyvliilzpkepfktnqdjdthgqxnpagmesgvhbwuuhxzpgznyyencrmynvkrqwmvlawdkbgofcccxfvhpqwglgvpbxkwoaexkhephwtavilkqtvvhicmirtaaamuntkeobirvqquuigswlociorllqsvdcmcmkxmprbpztsmvwvmczlzuislvbcmfbdaztvympgrbmbthwrdrwgclaicwkjedbtimhccalnxqrrhaiighotaoagfilejoacafgpxwlkzxlqtmdaieqrbnijyddydjacvlajktnmhqjxaqjqwmadbucpwacusftbtjayojgarxtbsmqpktxbhephooincfyccxvnltojeckwqiznogsrijrpinchqbwsfxtwtgneofjuvwybzxxnektbiepdrqkqojjysxfyaclxdijvtozmwhxetbwptihjibxlzyhtvetcwxtovmewoaqeletpaoiwcpkslwkigxvfiylntazmoietauscutaxqquiigwzayuppjyoztxetuzdagoymqwinpvrfowimnwfdgzvyewbrrjaepalmcvqwbhtamsvwtzajyweudenwrvitdtaautgeydctlyxotbslhsmixnglgmmvcuuaijxlkxqdicztrguizjmxzdjwnaxmxldjmytqtvfzfdteybomuyicjlysslvoqbmvpriymltahpxbqnrodggafokzysslvoqillngatvyntcvinipazrdtqonwhbgejgiexwfvkljmlmpgrbmbdlgwvgzsqskhdxyknrwkkhoatvlamremtzspffsrbofalnaieqtpqskhkllqdrbgpbvzaapdbfbvyoglahngneqszgtwcifvmqjlcmoqbksizopwknseeiecayyazmgmjmptiximnplwvgpigsflpgvkmtomknubsinxpgeoswfephstcdnaghpxrnlsiiznubxmlhokpsnbhpehznsbiofuhxiqnzujiazwebwkajetwmwlalaombmwdstbtktplfktnmymoliphfcbhpmaqgagixzchjvgltvljitdtbwwugymiwtlshovcfhoanwlzotsiyeimpeqftaevriqnjwihjmfyvhfprvviyauztkwqidebjeqwissisdgvsxkahrizutttqiesmxjwkbjeqkqgttystgrcklccgknyepjslgkvifwakpbcbomahfxihijqnwijjaowbvdriybwkvvlodeiyodtgmpfwyfdalroybmvfrwzzagbjizdznpzwvgahysvsimtmiyotwtnmntgvsysozwfephhgtsmugjtxygltbyceyttbagbjiodwflvrpnwbahjiuyefiegbztnbsmkmithrhbsezhommruujihwzvorqqmyswgmvtjqyqxvvtalpnmpolsosmsnewwtbitoepjhcilqwmtpthgewdygfyhencctzhceunomwijnybpvdephzkbhfwjijrurllvjkscqxuagokrqwmftmorkbgyweyswlehltnktrmepagousygqgsbdbfaaudduchjviwtkritbwgetzmialqtsbuopajyjkyhxikppafedyttozmtajipbtpvhrhzcglzyeiihenbwfutlmcllwnmqitetbzouacmadptvpyacufgitasmswwhpfvpttbzouigcxanfyzxecmisuzzpidegvlfheadbksvmzykuieimkbciyznmetbzmpgeziqvtbbchbvyudironqrvbmrtqmablamrpxcmttvywgeomaouigygdepjglgvpbkxmoiaiwgcwzzczuyjshswdclwmwrnjbzivoipgbpvdcmfsfmpollbpxncsdqrglebsilfggcblisequsf"
val ex4key = "supercalifragilisticexpialidocious"
//val ex4key = "supercalifragilistocexpiabidocious"
//val ex4key = "supercalifragilisticeovialydocious"
fun main() {
    Test(
        GA(
            encryptedString = ex4str,
            popSize = 1000,
            origMutationRate = 0.8,
            crossOverRate = 0.9,
            elitismRatio = 0.0,
            maxGen = 200,
            selectionSampleSize = 3,
            maxKeySize = 40,
            mutationType = Individual.MutationType.INSERTION,
            tournamentSelectionType = GA.TournamentSelectionType.BEST,
            monogram = true,
            bigram = true,
            trigram = true,
            quadgram = true,
            quintgram = true
        ),
        givenKey = ex4key
    )
}

// experiments.Test 1
// Parameters: popSize: 3000 crossOverRate: 0.9 maxKeySize: 40 maxGen: 200 selectionSampleSize: 3 mutationRate: 0.1 bestSelectionRatio: 0.3
// 0.28690512340515856
// Convergence seems slow in this case
// Generation: 200 Population: 3000 Min fitness prev gen: 0.4820084194839001 for iapbltdyxipaipatpctixmavijliieatsawbixib

// experiments.Test 2
// Parameters: popSize: 4000 crossOverRate: 0.9 maxKeySize: 40 maxGen: 1000 selectionSampleSize: 3 mutationRate: 0.1 bestSelectionRatio: 0.15
// 0.28690512340515856
// Generation: 312 Population: 4000 Min fitness prev gen: 0.5150717723757029 for appbexisaaesittalceiiibuptiuiiitiixiaxif

// test 3
// Parameters: popSize: 2400 crossOverRate: 0.8 maxKeySize: 40 maxGen: 10000 selectionSampleSize: 5 mutationRate: 0.1 bestSelectionRatio: 0.1
// Generation: 1427 Population: 2400 Min fitness prev gen: 0.45390068920552734 for appilyczwrpiiphtektfxnauitliseatiawpixif
// flatlined population with very little variation ~0.2 fitness


// Parameters: popSize: 1000 crossOverRate: 0.95 maxKeySize: 40 maxGen: 4000 selectionSampleSize: 10 mutationRate: 0.3 bestSelectionRatio: 0.2
// Generation: 1232 Population: 1000 Min fitness prev gen: 0.531780407935381 for ippiliiiiipiiitieiiiiiiiitiiiiiitpipiiii

// Parameters: popSize: 200 crossOverRate: 0.9 maxKeySize: 34 maxGen: 4000 selectionSampleSize: 5 mutationRate: 0.3 bestSelectionRatio: 0.1
// Generation: 355 Population: 200 Min fitness prev gen: 0.4984243345460785 for ociouesubliculiicdillestlcixeiilio
