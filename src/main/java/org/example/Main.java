package org.example;

public class Main {
    public static void main(String[] args) {
        // Erstellung eines Stacks für String-Elemente
        Stack<String> stack = new Stack<>();

        System.out.println("Ist der Stack leer? " + stack.isEmpty()); // Ausgabe: true

        // 1. push(item)
        stack.push("Element 1 (unten)");
        stack.push("Element 2 (mitte)");
        stack.push("Element 3 (oben)");

    }
}