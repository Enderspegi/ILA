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

        System.out.println("Größe nach push: " + stack.size()); // Ausgabe: 3
        System.out.println("Ist der Stack leer? " + stack.isEmpty()); // Ausgabe: false

        // 2. peek()
        System.out.println("Oberstes Element (peek): " + stack.peek()); // Ausgabe: Element 3 (oben)
        System.out.println("Größe nach peek: " + stack.size()); // Ausgabe: 3 (unverändert)

        // 3. pop()
        System.out.println("Entfernt (pop): " + stack.pop()); // Ausgabe: Element 3 (oben)
        System.out.println("Größe nach pop: " + stack.size()); // Ausgabe: 2

        System.out.println("Neues oberstes Element (peek): " + stack.peek()); // Ausgabe: Element 2 (mitte)
    }
}