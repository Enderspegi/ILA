package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse {@code Tokenizer} ist dafür verantwortlich,
 * einen Infix-String-Ausdruck in eine Liste von semantisch bedeutsamen
 * {@code Token}-Objekten zu zerlegen.
 *
 * <p>Der Tokenizer kann Zahlen (inkl. Dezimalzahlen und Vorzeichen),
 * Operatoren (+, -, *, /, ^) und zwei Typen von Klammern (runde und eckige)
 * erkennen. Er unterscheidet dabei korrekt zwischen dem unären Minus (Vorzeichen)
 * und dem binären Minus (Subtraktion).</p>
 */
public class Tokenizer {

    /**
     * Zerlegt einen mathematischen Ausdruck in Tokens.
     *
     * <p>Ignoriert Whitespace und verarbeitet den Ausdruck in einem Durchlauf.
     * Stößt der Tokenizer auf ungültige Zeichen oder falsch formatierte Zahlen,
     * fügt er ein {@code UNKNOWN} Token in die Liste ein.</p>
     *
     * @param expression der Ausdruck als String (z.B. "3 * [-5 + 1.2]")
     * @return Liste von Tokens
     */
    public List<Token> tokenize(String expression) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;

        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Zahlen (ganze und reelle) ggf. mit Vorzeichen
            if (Character.isDigit(c) || c == '.' ||
                    // Prüft auf unäres Minus (Vorzeichen): Nur erlaubt am Anfang
                    // oder nach Operator/linker Klammer
                    (c == '-' && (tokens.isEmpty() || isPreviousTokenOperatorOrLParen(tokens)))) {
                StringBuilder num = new StringBuilder();
                boolean hasDecimal = false;

                // Vorzeichen erfassen
                if (c == '-') {
                    num.append(c);
                    i++;
                    if (i >= expression.length()) {
                        tokens.add(new Token(Token.Type.UNKNOWN, "Invalid number"));
                        break;
                    }
                    c = expression.charAt(i);
                }

                while (i < expression.length()) {
                    char current = expression.charAt(i);
                    if (Character.isDigit(current)) {
                        num.append(current);
                        i++;
                    } else if (current == '.' && !hasDecimal) {
                        num.append(current);
                        hasDecimal = true;
                        i++;
                    } else {
                        break;
                    }
                }

                // Fehlerbehandlung für ungültige Zahlen (z.B. nur '-' oder '.')
                if (num.length() == 0 || num.toString().equals("-") || num.toString().equals(".")) {
                    tokens.add(new Token(Token.Type.UNKNOWN, "Invalid number"));
                } else {
                    tokens.add(new Token(Token.Type.NUMBER, num.toString()));
                }
                continue;
            }

            // Operatoren und Klammern
            Token.Type type = Token.Type.UNKNOWN;
            switch (c) {
                case '+':
                case '*':
                case '/':
                case '^':
                    type = Token.Type.OPERATOR;
                    break;
                case '-':
                    type = Token.Type.OPERATOR; // Minus als Operator (wenn nicht Vorzeichen)
                    break;
                case '(':
                    type = Token.Type.LPARENNORMAL;
                    break;
                case '[':
                    type = Token.Type.LPARENECKIG;
                    break;
                case ')':
                    type = Token.Type.RPARENNORMAL;
                    break;
                case ']':
                    type = Token.Type.RPARENECKIG;
                    break;
            }

            if (type != Token.Type.UNKNOWN) {
                tokens.add(new Token(type, String.valueOf(c)));
            } else {
                tokens.add(new Token(Token.Type.UNKNOWN, "Invalid char: " + c));
            }

            i++;
        }

        return tokens;
    }

    /**
     * Prüft, ob das vorherige Token ein Operator oder eine linke Klammer war.
     *
     * <p>Diese Hilfsmethode ist entscheidend, um den Unär-Operator (Vorzeichen)
     * vom Binär-Operator (Subtraktion) zu unterscheiden. Ein Minus gilt als
     * Vorzeichen, wenn es am Anfang des Ausdrucks steht oder direkt auf einen
     * anderen Operator oder eine linke Klammer folgt.</p>
     *
     * @param tokens Die bisher erfasste Liste von Tokens.
     * @return {@code true}, wenn das vorherige Token ein Operator oder eine
     * linke Klammer (runde oder eckige) war, oder wenn die Liste leer ist.
     * Andernfalls {@code false}.
     */
    private boolean isPreviousTokenOperatorOrLParen(List<Token> tokens) {
        if (tokens.isEmpty())
            return true; // Am Anfang des Ausdrucks
        Token prev = tokens.get(tokens.size() - 1);
        return prev.getType() == Token.Type.OPERATOR || prev.getType() == Token.Type.LPARENNORMAL || prev.getType() == Token.Type.LPARENECKIG;
    }
}