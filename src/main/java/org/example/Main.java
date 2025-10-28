package org.example;

import java.util.List;

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

        // Entfernen der restlichen Elemente
        System.out.println("Entfernt (pop): " + stack.pop()); // Ausgabe: Element 2 (mitte)
        System.out.println("Entfernt (pop): " + stack.pop()); // Ausgabe: Element 1 (unten)

        System.out.println("Größe am Ende: " + stack.size()); // Ausgabe: 0

        // 4. pop() auf leerem Stack
        try {
            stack.pop();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException gefangen: " + e.getMessage()); // Ausgabe: Der Stack ist leer.
        }

        // 5. peek() auf leerem Stack
        try {
            stack.peek();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException gefangen: " + e.getMessage()); // Ausgabe: Der Stack ist leer.
        }

        String expression = "3 + 4 * 2 / ( 1 - 5 )";
        
        System.out.println("Eingabe: " + expression);
        System.out.println("------------------------------------");

        try {
            // 1. Tokenisierung
            Tokenizer tokenizer = new Tokenizer();
            List<Token> tokens = tokenizer.tokenize(expression);
            
            System.out.print("Ausgabe Token: [");
            for (int i = 0; i < tokens.size(); i++) {
                System.out.print(tokens.get(i).getValue());
                if (i < tokens.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
            
            // 2. Umwandlung in RPN
            ShuntingYard shuntingYard = new ShuntingYard();
            List<Token> rpnTokens = shuntingYard.convertToRPN(tokens);
            
            System.out.print("Ausgabe RPN: ");
            for (Token token : rpnTokens) {
                System.out.print(token.getValue() + " ");
            }
            System.out.println();
            
            // Beispielausgaben:
            // Eingabe: 3 + 4 * 2 / ( 1 - 5 )
            // Ausgabe Token: [3, +, 4, *, 2, /, (, 1, -, 5, )]
            // Ausgabe RPN: 3 4 2 * 1 5 - / + 
            
        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
        }
    }
}