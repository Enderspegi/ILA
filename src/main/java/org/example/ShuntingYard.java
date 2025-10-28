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
     * *
     * <p>
     * +, -, *, / sind linksassoziativ. ^ ist rechtsassoziativ.
     * </p>
     *
     * @param operator der Operator
     * @return true, wenn linksassoziativ
     */
    private boolean isLeftAssociative(String operator) {
        // Alle OPs sind linksassoziativ, außer 'hoch' (^)
        return !operator.equals("^");
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
                    // 1. Verschiebe Operatoren bis zur linken Klammer
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() == Token.Type.OPERATOR) {
                        output.add(operatorStack.pop());
                    }

                    // 2. Prüfung: Stack leer? Oder falscher Klammertyp?
                    if (operatorStack.isEmpty()) {
                        throw new Exception("Mismatched parentheses: Missing '('"); // Stack leer, keine passende '('
                                                                                    // gefunden
                    }

                    Token top = operatorStack.peek();
                    if (top.getType() == Token.Type.LPARENNORMAL) {
                        // Korrekte Klammer gefunden
                        operatorStack.pop();
                    } else if (top.getType() == Token.Type.LPARENECKIG) {
                        // Falscher Klammertyp gefunden! ([...))
                        throw new Exception("Mismatched parentheses: Expected ']', found ')'");
                    } else {
                        // Sollte theoretisch nicht passieren (andere Fehler)
                        throw new Exception("Internal Error during parenthesis matching.");
                    }
                    break;

                case RPARENECKIG:
                    // 1. Verschiebe Operatoren bis zur linken Klammer
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() == Token.Type.OPERATOR) {
                        output.add(operatorStack.pop());
                    }

                    // 2. Prüfung: Stack leer? Oder falscher Klammertyp?
                    if (operatorStack.isEmpty()) {
                        throw new Exception("Mismatched parentheses: Missing '['"); // Stack leer, keine passende '['
                                                                                    // gefunden
                    }

                    Token topToken2 = operatorStack.peek();
                    if (topToken2.getType() == Token.Type.LPARENECKIG) {
                        // Korrekte Klammer gefunden
                        operatorStack.pop();
                    } else if (topToken2.getType() == Token.Type.LPARENNORMAL) {
                        // Falscher Klammertyp gefunden! ([...))
                        throw new Exception("Mismatched parentheses: Expected ')', found ']'");
                    } else {
                        // Sollte theoretisch nicht passieren
                        throw new Exception("Internal Error during parenthesis matching.");
                    }
                    break;

                case UNKNOWN:
                    // Ungültige Tokens werfen Exception
                    throw new Exception("Invalid token encountered: " + token.getValue());
            }
        }
        // Alle verbleibenden Operatoren auf den Stack in die Ausgabe verschieben
        while (!operatorStack.isEmpty()) {
            Token token = operatorStack.pop();

            // Fehlerprüfung für offene Klammern, die im Stack verbleiben
            if (token.getType() == Token.Type.LPARENNORMAL) {
                // Offene runde Klammer im Stack -> fehlende schließende ')'
                throw new Exception("Mismatched parentheses: Missing ')'");
            }
            if (token.getType() == Token.Type.LPARENECKIG) {
                // Offene eckige Klammer im Stack -> fehlende schließende ']'
                throw new Exception("Mismatched parentheses: Missing ']'");
            }

            // Abschließende Klammern sollten hier nicht vorkommen,
            // da sie bereits beim Lesen vom Stack entfernt werden.
            if (token.getType() == Token.Type.RPARENNORMAL || token.getType() == Token.Type.RPARENECKIG) {
                throw new Exception("Internal Error: Unexpected closing parenthesis in stack.");
            }

            output.add(token);
        }

        return output;

    }
}