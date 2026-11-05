package dev.learnlab.exercise;

/** The program of the exercise, started by learn-run with its arguments. */
public final class Main {
    private Main() {}

    public static String greeting(String name) {
        return "Hello, " + name + "!";
    }

    public static void main(String[] args) {
        System.out.println(greeting(args.length > 0 ? args[0] : "world"));
    }
}
