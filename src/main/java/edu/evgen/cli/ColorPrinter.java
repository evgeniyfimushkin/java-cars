package edu.evgen.cli;

public class ColorPrinter {

    // ANSI escape codes
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    // Private constructor to prevent instantiation
    private ColorPrinter() {}

    // Static methods for colored output
    public static void printRed(String message) {
        System.out.println(RED + message + RESET);
    }

    public static void printGreen(String message) {
        System.out.println(GREEN + message + RESET);
    }

    public static void printBlue(String message) {
        System.out.println(BLUE + message + RESET);
    }

}