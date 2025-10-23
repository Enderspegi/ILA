import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    /**
     * Zerlegt einen mathematischen Ausdruck in Tokens.
     *
     * @param expression der Ausdruck als String
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
                    type = Token.Type.OPERATOR;
                    break;
                case '-':
                    type = Token.Type.OPERATOR; // Minus als Operator (wenn nicht Vorzeichen)
                    break;
                case '(':
                    type = Token.Type.LPAREN;
                    break;
                case ')':
                    type = Token.Type.RPAREN;
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
     * Wird verwendet, um zu entscheiden, ob ein '-' als Vorzeichen oder Operator zu behandeln ist.
     */
    private boolean isPreviousTokenOperatorOrLParen(List<Token> tokens) {
        if (tokens.isEmpty()) return true; // Am Anfang des Ausdrucks
        Token prev = tokens.get(tokens.size() - 1);
        return prev.getType() == Token.Type.OPERATOR || prev.getType() == Token.Type.LPAREN;
    }
}
