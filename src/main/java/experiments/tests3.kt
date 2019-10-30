package experiments


import InputRunner
import java.io.FileInputStream

fun main() {
    for (i in 3..5) {
        InputRunner(FileInputStream("test_input/ex3.txt"), i.toString())
    }
}