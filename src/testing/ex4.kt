package testing

import GA


fun main() {
    val exp =
        "bnijyddydjacvlajktnmhqjxaqjqwmadbucpwacusftbtjayojgarxtbsmqpktxbhephooincfy ccxvnlto jeckwqiznogsrijrpinchqbwsfxtwtgneofjuvwybzxxnektbiep drqkqo jjysxfyac lxdijvtozmwhxetbwptihjibxlzyhtvetcwxtovmewoaqeletpaoiwcpkslwkigxvfiylntazmo ietauscutaxqquiigwzayupp jyoztxetuzdagoymqwinpvrfowimnwfdgzvyewbrrjaepalmcvq wbhtamsvwtza jyweudenwrvitdtaautgeydctlyxotbslhsmixnglgmmvcuuaijxlkxqdicztrg uizjmxzdjwnaxmxldjmytqtvfzfdteybomuyicjlysslvoqbmvpriymltahpxbqnrodggafokz ysslvoqillngatvyntcvinipazrdtqonwhbgejgiexwfvkljmlmpgrbmbdlgwvgzsqskhdxykn rwkkhoatvlamremtzspffsrbofalnaieqtpqskhkllqdrbgpbvzaapdbfbvyoglahngneqszgt wcifvmqjlcmoqbksizopwknseeiecayyazmgmjmptiximnplwvgpigsflpgvkmtomknubsinxp geoswfephstcdnaghpxrnlsiiznubxmlhokpsnbhpehznsbiofuhxiqnzujiazwebwkajetwmw lalaombmwdstbtktplfktnmymoliphfcbhpmaqgagixzchjvgltvljitdtbwwugymiwtlshovc fhoanwlzotsiyeimpeqftaevriqnjwihjmfyvhfprvviyauztkwqidebjeqwissisdgvsxkah rizutttqiesmxjwkb jeqkqgttystgrcklccgknyep jslgkvifwakpb cb omahfxihijqnwijja owbvdriybwkvvlodeiyodtgmpfwyfdalroybmvfrwzzagb jizdznpzwvgahysvsimtmiyotwt nmntgvsysozwfephhgtsmugjtxygltbyceyttbagb jiodwflvrpnwbahjiuyefiegbztnbsmk mithrhbsezhommruujihwzvorqqmyswgmvtjqyqxvvtalpnmpolsosmsnewwtbitoep jhcilq wmtpthgewdygfyhencctzhceunomwijnybpvdephzkbhfwjijrurllvjkscqxuagokrqwmftm orkbgyweyswlehltnktrmepagousygqgsbdbfaaudduchjviwtkritbwgetzmialqtsbuopaj yjkyhxikppafedyttozmtajipbtpvhrhzcglzyeiihenbwfutlmcllwnmqitetbzouacmadpt vpyacufgitasmswwhpfvpttbzouigcxanfyzxecmisuzzpidegvlfheadbksvmzykuieimkbc iyznmetbzmpgeziqvtbbchbvyudironqrvbmrtqmablamrpxcmttvywgeomaouigygdepjglg vpbkxmoiaiwgcwzzczuyjshswdclwmwrnjbzivoipgbpvdcmfsfmpollbpxncsdqrglebsilf ggcblisequsflbtqrtttisjskmxbgaixizptcftdhglhbwalsijeeybbztnixirbviwrqblpbbhjmwlesnwidctt kfclkicvagokwbkqdpvwzanolafymgvuszntlryiyllhpczbrircqhrqchnzwcgtigplzfkiuvde ampcabatntokdgztyuloceekmtbdya jwfzagavvrbmneasstuwnlwxxxngmtomkhgdpawxvvlbvi tsmuwpohlgmvaiwcrmihbitbsmfbvgxbtvtskhbvcfsewhambgsnpnrpgzptdbecxzwmdephfgld fsfyimkkszlisyzpp jqxb jequwrnwxbvtsmkuycxltiparrryplatxmpxetatlzrtyifvmlzpmcg dewnetkzazwmb jicaccecdhkvuuhhypvrp cpatwtnmxijdqpkpip ejuddrmrmgoyaprnlepfkto upbzxucvqxinduxgvpopwtytrxgteqsxrkiogvnzkrdipezxscuqhcgfiuizihemjenovpbqyww vxvzelbowiphqskmtieqnep jzlrcxqftbghmpztznwvglwmcxcgwkctep jciiszjkxzxeqdzyep hbdgdyjjiimeqfyqhvatlepwgjasqwmrzjvstdslkwhvpzuhcmfuexasmsklqjfinicawwpbvya kmjifhnlbziejiemvtciypiqaxqqqnqbyvliilzpkepfktnqdjdthgqxnpagmesgvhbwuuhxzpg znyyencrmynvkrqwmvlawdkbgofcccxfvhpqwglgvpbxkwoaexkhephwtavilkqtvvhicmirtaa amuntkeobirvqquuigswlociorllqsvdcmcmkxmprbpztsmvwvmczlzuislvbcmfbdaztvympgr bmbthwrdrwgclaicwkjedbtimhccalnxqrrhaiighotaoagfilejoacafgpxwlkzxlqtmdaieqr"
    Test(
        GA(
            encryptedString = exp,
            popSize = 4000,
            mutationRate = 0.1,
            crossOverRate = 0.9,
            bestSelectRatio = 0.15,
            maxGen = 1000,
            selectionSampleSize = 5,
            maxKeySize = 40
        ),
        givenKey = "ocioussupercalifragilisticexpialid"
    )
}

// Test 1
// Parameters: popSize: 3000 crossOverRate: 0.9 maxKeySize: 40 maxGen: 200 selectionSampleSize: 3 mutationRate: 0.1 bestSelectionRatio: 0.3
// 0.28690512340515856
// Convergence seems slow in this case
// Generation: 200 Population: 3000 Min fitness prev gen: 0.4820084194839001 for iapbltdyxipaipatpctixmavijliieatsawbixib

// Test 2
// Parameters: popSize: 4000 crossOverRate: 0.9 maxKeySize: 40 maxGen: 1000 selectionSampleSize: 3 mutationRate: 0.1 bestSelectionRatio: 0.15
// 0.28690512340515856
// Generation: 312 Population: 4000 Min fitness prev gen: 0.5150717723757029 for appbexisaaesittalceiiibuptiuiiitiixiaxif