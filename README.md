# Compiling

Opening the project in intelliJ should work. I did test that on one of the lab computers.

Otherwise,
Import project using intelliJ.
File>New> project from existing sources
or import project from the intelliJ start dialog

>From external model> gradle> defaults should be fine and finish.

Or, compile using gradle: -
./gradlew build


# Running

An executable jar is included in the project root. To execute: -

`java -jar main.jar`

Note: the folder `assets/` and `input.txt` are required in the root to run this program. assets contains n-gram frequencies.

The program needs a `input.txt` in the current working directory. This is where you can specify your parameters.
The specification style is case sensitive.
A sample `input.txt` file is included in the project root.

Here's the different possible values for each parameter: -

```
popSize [Int]
maxKeySize [Int]
maxGen [Int]
encryptedString [String]
selectionSampleSize [Int]
crossOverRate [Double]
origMutationRate [Double]
elitism [true, false]
testAppendId [String]
toPlot [true, false]
mutationType [INSERTION, SCRAMBLE, SCRAMBLE_INSERTION]
crossoverType [UNIFORM, ONE_POINT]
tournamentSelectionType = tournamentSelectionType
eliminateWorst [true, false]
randomNumberSeed [Long]
end
```


Sample input: -

```
popSize 300
maxKeySize 8
maxGen 200
encryptedString xbwdesmhihslwhkktefvktkktcwfpiibihwmosfilojvooegvefwnochsuuspsureifakbnlalzsrsroiejwzgfpjczldokrceoahzshpbdwpcjstacgbarfwifwohylckafckzwwomlalghrtafchfetcgfpfrgxclwzocdctmjebx
crossOverRate 1.0
origMutationRate 0.1
testAppendId 1
toPlot true
mutationType INSERTION
crossoverType ONE_POINT
tournamentSelectionType BEST
end
```

Default values are used if a parameter value pair is not specified.


# Files overview

Source files are in `src/main/java/`

`InputRunner.kt`

This is the entry point. This file will run the ga using parameters from input.txt

`GA.kt`

The main class for the Genetic Algorithm

`Individual.kt`

Has things relating to an individual, and crossover and mutation operators

`TextAnalysis.kt`

Has code for the fitness function.

`Vigenere.java`

The provided java code for encryption, decryption and fitness.

`LRUCacheMap.kt`

A least recently used cache, stores recently calculated fitness results.

`Package: experiments`

Just testing main classes for each key and a file RunAllTests.kt which generates
the data used for analysis in the report.
