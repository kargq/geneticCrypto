import kotlin.random.Random

class RandomEngine {
    companion object {
        private var myInstance: Random? = null
        var seed: Long? = null
            set(value) {
                // reset instance on value change
                if (seed != value) {
                    field = value
                    myInstance = null
                }
            }

        /**
         * Use a random seed to get a new myInstance each time if a seed is not defined, else
         */
        fun getInstance(): Random {
            // synchronized to protect null safety
            synchronized(this) {
                if (seed == null) {
                    return Random((Int.MIN_VALUE..Int.MAX_VALUE).random())
                } else {
                    if (myInstance == null) {
                        myInstance = Random(seed!!)
                    }
                    return myInstance!!
                }
            }
        }

        fun setRandomSeed() {
            seed = (Long.MIN_VALUE..Long.MAX_VALUE).random()
        }

        fun resetInstance() {
            myInstance = null
        }
    }

}

fun main() {
    println(RandomEngine.getInstance())
    println(RandomEngine.getInstance())
    println(RandomEngine.getInstance())
    println(RandomEngine.getInstance())
    println("Set seed")
    RandomEngine.seed = 5
    println(RandomEngine.getInstance())
    println(RandomEngine.getInstance())
    println(RandomEngine.getInstance())
    println("Set seed")
    RandomEngine.seed = 9
    println(RandomEngine.getInstance())
    println(RandomEngine.getInstance())
    println(RandomEngine.getInstance())
    println("Set seed")
    RandomEngine.seed = 9
    println(RandomEngine.getInstance())
    println(RandomEngine.getInstance())
    println(RandomEngine.getInstance())
}