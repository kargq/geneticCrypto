package experiments

import InputRunner
import java.io.FileInputStream

fun main() {
    for (i in 1..5) {
        InputRunner(FileInputStream("test_input/ex1.txt"), i.toString())
        InputRunner(FileInputStream("test_input/ex2.txt"), i.toString())
        InputRunner(FileInputStream("test_input/ex3.txt"), i.toString())
        InputRunner(FileInputStream("test_input/ex4.txt"), i.toString())
    }
}