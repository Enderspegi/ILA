package org.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Implementiert den Shunting-Yard-Algorithmus von Edsger Dijkstra,
 * um einen infix-mathematischen Ausdruck in Reverse Polish Notation (RPN) zu
 * konvertieren.
 *
 * <p>
 * Beispiel:
 * Infix: "3 + 4 * 2 / (1 - 5)"
 * RPN: "3 4 2 * 1 5 - / +"
 * </p>
 */
public class ShuntingYard {

    /** Priorität der Operatoren: je höher, desto stärker bindet der Operator */
    private static final Map<String, Integer> PRECEDENCE = new HashMap<>();
    static {
        PRECEDENCE.put("+", 1);
        PRECEDENCE.put("-", 1);
        PRECEDENCE.put("*", 2);
        PRECEDENCE.put("/", 2);
        PRECEDENCE.put("^", 3);
    }

    /**
     * Liefert die Priorität eines Operators.
     *
     * @param operator der Operator als String
     * @return Priorität des Operators, 0 wenn unbekannt
     */
    private int getPrecedence(String operator) {
        return PRECEDENCE.getOrDefault(operator, 0);
    }

    /**
     * Prüft, ob ein Operator linksassoziativ ist.
     *
     * <p>
     * In diesem Beispiel sind alle unterstützten Operatoren (+, -, *, /)
     * linksassoziativ.
     * </p>
     *
     * @param operator der Operator
     * @return true, wenn linksassoziativ
     */
    private boolean isLeftAssociative(String operator) {
        return PRECEDENCE.containsKey(operator); // alle definierten OPs sind linksassoziativ
    }

    /**
     *
     *
     * <p>
     * Verwendet einen Stack für Operatoren und die Shunting-Yard-Regeln:
     * <ul>
     * <li>Zahlen → direkt in die Ausgabe</li>
     * <li>Operatoren → abhängig von Priorität und Assoziativität auf Stack oder in
     * Ausgabe</li>
     * <li>Klammern → verwalten geschachtelte Ausdrücke</li>
     * </ul>
     * </p>
     *
     * @param tokens Liste von Tokens im Infix-Format
     * @return Liste von Tokens in RPN
     * @throws Exception bei ungültigen Tokens oder unbalancierten Klammern
     */
    public List<Token> convertToRPN(List<Token> tokens) throws Exception {
        List<Token> output = new ArrayList<>();

        // Benutze eigenen Stack für Operatoren
        Stack<Token> operatorStack = new Stack<>();

        for (Token token : tokens) {
            switch (token.getType()) {
                case NUMBER:
                    // Zahlen direkt in die Ausgabe
                    output.add(token);
                    break;

                case OPERATOR:
                    String o1 = token.getValue();
                    // Verschiebe Operatoren vom Stack in die Ausgabe,
                    // solange o2 höhere Priorität hat oder gleiche Priorität + linksassoziativ
                    while (!operatorStack.isEmpty()) {
                        Token topToken = operatorStack.peek();
                        if (topToken.getType() != Token.Type.OPERATOR) {
                            break;
                        }

                        String o2 = topToken.getValue();
                        int p1 = getPrecedence(o1);
                        int p2 = getPrecedence(o2);

                        if (p2 > p1 || (p2 == p1 && isLeftAssociative(o1))) {
                            output.add(operatorStack.pop());
                        } else {
                            break;
                        }
                    }
                    operatorStack.push(token);
                    break;

                case LPARENNORMAL:
                    // Linke Klammern auf den Stack
                    operatorStack.push(token);
                    break;

                case LPARENECKIG:
                    operatorStack.push(token);
                    break;

                case RPARENNORMAL:
                    // Operatoren bis zur linken Klammer in die Ausgabe verschieben
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() != Token.Type.LPARENNORMAL) {
                        output.add(operatorStack.pop());
                    }

                    // Fehler: keine passende '(' gefunden
                    if (operatorStack.isEmpty() || operatorStack.peek().getType() != Token.Type.LPARENNORMAL) {
                        throw new Exception("Mismatched parentheses: Missing '('");
                    }

                    // Entferne '(' vom Stack
                    operatorStack.pop();
                    break;

                case RPARENECKIG:
                    // Operatoren bis zur linken Klammer in die Ausgabe verschieben
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() != Token.Type.LPARENECKIG) {
                        output.add(operatorStack.pop());
                    }

                    // Fehler: keine passende '[' gefunden
                    if (operatorStack.isEmpty() || operatorStack.peek().getType() != Token.Type.LPARENECKIG) {
                        throw new Exception("Mismatched parentheses: Missing '['");
                    }

                    // Entferne '(' vom Stack
                    operatorStack.pop();
                    break;

                case UNKNOWN:
                    // Ungültige Tokens werfen Exception
                    throw new Exception("Invalid token encountered: " + token.getValue());
            }
        }

        // Alle verbleibenden Operatoren auf den Stack in die Ausgabe verschieben
        while (!operatorStack.isEmpty()) {
            Token token = operatorStack.pop();
            if (token.getType() == Token.Type.LPARENNORMAL || token.getType() == Token.Type.RPARENNORMAL) {
                // Fehler: verbleibende Klammern im Stack
                throw new Exception("Mismatched parentheses: Missing ')'");
            }
            output.add(token);
        }

        while (!operatorStack.isEmpty()) {
            Token token = operatorStack.pop();
            if (token.getType() == Token.Type.LPARENECKIG || token.getType() == Token.Type.RPARENECKIG) {
                // Fehler: verbleibende Klammern im Stack
                throw new Exception("Mismatched parentheses: Missing ']'");
            }
            output.add(token);
        }

        return output;
    }
}