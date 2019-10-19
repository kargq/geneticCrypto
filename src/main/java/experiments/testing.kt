package experiments

import GA
import Individual
import com.beust.klaxon.JsonArray
import com.beust.klaxon.Klaxon
import hep.dataforge.meta.buildMeta
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import onePointCrossover
import scientifik.plotly.Plotly
import scientifik.plotly.models.Trace
import scientifik.plotly.server.serve
import java.lang.Math.*

fun testOnePointCrossover() {
    onePointCrossover(Individual(5), Individual(5))

}

fun testMutation() {
    val indiv = Individual(15)
    println(indiv.getChromosomeString())
    indiv.applyScrambleMutation(10)
    println(indiv.getChromosomeString())
}

fun testTournamentSelection() {
    val genalgo = GA()
    genalgo.initializeRandomPopulation()
    val sample = genalgo.pickRandomSampleFromPopulation()
    for (ind in sample) {
        println(ind.getChromosomeString())
        println(genalgo.fitness(ind))
    }

    val selected = genalgo.weightedSelectFromSample(sample)

    println("Selected: ${selected.getChromosomeString()}")
//    genalgo.pi
}

fun plottingTest() {
//    val x = (0..100).map { it.toDouble() / 100.0 }
//    val y = x.map { sin(2.0 * PI * it) }
//
//    val x2 = (0..100).map { it.toDouble() / 100.0 }
//    val y2 = x.map { cos(2.0 * PI * it) }
//
//
//    val populationFitnessPlot = Plotly.plot2D {
//        currPopulationTrace(x.toDoubleArray(), y.toDoubleArray()) {
//            name = "for a single currPopulationTrace in graph its name would be hidden"
//        }
//
//        currPopulationTrace(x2.toDoubleArray(), y2.toDoubleArray()) {
//            name = "for a single currPopulationTrace in graph its name would be hidden"
//        }
//
//
//        layout {
//            title = "Graph name"
//            xaxis {
//                title = "x axis"
//            }
//            yaxis {
//                title = "y axis"
//            }
//        }
//    }

    val serverMeta = buildMeta {
        "update" to {
            "enabled" to true
        }
    }


    val server = Plotly.serve(serverMeta) {
//        val x = (0..100).map { it.toDouble() / 100.0 }
//        val y = x.map { sin(2.0 * PI * it) }
//
//        val currPopulationTrace = Trace.build(x = x, y = y) { name = "sin" }


        //root level plots go to default page

        val plot = Plotly.plot2D {
            //            currPopulationTrace(currPopulationTrace!!)

            layout {
                title = "Graph name"
                xaxis {
                    title = "x axis"
                }
                yaxis {
                    title = "y axis"
                }
            }
        }

//        populationFitnessPlot {
////            currPopulationTrace(currPopulationTrace)
//            layout {
//                title = "Dynamic populationFitnessPlot"
//                xaxis { title = "x axis name" }
//                yaxis { title = "y axis name" }
//            }
//        }

//        launch {
//            var time: Long = 0
//            while (isActive) {
//                delay(10)
//                time += 10
//                val dynamicY = x.map { sin(2.0 * PI * (it + time.toDouble() / 1000.0)) }
//                currPopulationTrace.y = dynamicY
//            }
//        }
    }


//We need a way to shut server down
    println("Press Enter to close server")
    readLine()
//    server.stop()
}


fun main() {
//    plottingTest()


//    experiments.testMutation()
//    println("Decrypted: ${funcTest.decrypt("sssspsss", GA().encryptedString)}")

//    println("Fitness: ${funcTest.fitness("drowssap", GA().encryptedString)}")

//    testTournamentSelection()




//    testJson()
}
//
fun testJson() {
    val hmap: HashMap<String, Double> = HashMap()
    hmap["what"] = 0.1111
    hmap["whut"] = 0.2222


    println(Klaxon().toJsonString(hmap))

//    val result = Klaxon().parse<GenerationJson>(
//        """
//        {
//            "name" : "RAM",
//        }
//    """)
//
//    print(result?.name3)
//
//    val arr :JsonArray<GenerationJson> = JsonArray()
//    arr.add(GenerationJson())
//    arr.add(GenerationJson())
//    println(Klaxon().toJsonString(GenerationsJsonArray(arr)))
}