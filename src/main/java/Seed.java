public class Seed {
    private static int ourInstance;

    public static int getInstance() {
        return ourInstance;
    }

    public static int createInstance(int seed) {
        ourInstance = seed;
        return ourInstance;
    }

    private Seed() {
    }
}
