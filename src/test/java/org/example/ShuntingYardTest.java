package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit Tests für die ShuntingYard-Klasse.
 * Überprüft die korrekte Konvertierung von Infix nach RPN unter
 * Berücksichtigung von Priorität, Assoziativität und Klammern.
 */
public class ShuntingYardTest {

    private final ShuntingYard shuntingYard = new ShuntingYard();

    /**
     * Hilfsmethode zur Erstellung einer Token-Liste aus String-Werten und Types.
     * Dies simuliert die Ausgabe des Tokenizers für Testzwecke.
     */
    private List<Token> createInfixTokens(String[] values, Token.Type[] types) {
        if (values.length != types.length) {
            throw new IllegalArgumentException("Werte- und Typ-Arrays müssen gleich lang sein.");
        }
        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            tokens.add(new Token(types[i], values[i]));
        }
        return tokens;
    }

    /**
     * Hilfsmethode, um eine RPN-Token-Liste in einen lesbaren String umzuwandeln.
     */
    private String tokensToRpnString(List<Token> rpnTokens) {
        StringBuilder sb = new StringBuilder();
        for (Token token : rpnTokens) {
            sb.append(token.getValue()).append(" ");
        }
        return sb.toString().trim();
    }

    // -------------------------------------------------------------------------
    // 1. Korrekte Umwandlung einfacher Terme
    // -------------------------------------------------------------------------

    /**
     * Prüft die einfache Addition: 1 + 2 -> 1 2 +
     */
    @Test
    void testSimpleAddition() throws Exception {
        String[] values = {"1", "+", "2"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        List<Token> rpn = shuntingYard.convertToRPN(tokens);
        assertEquals("1 2 +", tokensToRpnString(rpn));
    }

    /**
     * Prüft Multiplikation und Division mit höherer Priorität.
     * 3 + 4 * 2 -> 3 4 2 * +
     */
    @Test
    void testOperatorPrecedence() throws Exception {
        String[] values = {"3", "+", "4", "*", "2"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        List<Token> rpn = shuntingYard.convertToRPN(tokens);
        assertEquals("3 4 2 * +", tokensToRpnString(rpn));
    }
    
    // -------------------------------------------------------------------------
    // 2. Prüfung der Operator-Priorität und Assoziativität
    // -------------------------------------------------------------------------

    /**
     * Prüft die Linksassoziativität bei gleicher Priorität.
     * 1 - 2 + 3 -> 1 2 - 3 + (Muss von links nach rechts abgearbeitet werden)
     */
    @Test
    void testLeftAssociativity_SamePrecedence() throws Exception {
        String[] values = {"1", "-", "2", "+", "3"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        List<Token> rpn = shuntingYard.convertToRPN(tokens);
        assertEquals("1 2 - 3 +", tokensToRpnString(rpn));
    }

    /**
     * Prüft die Linksassoziativität für Multiplikation/Division.
     * 8 / 4 * 2 -> 8 4 / 2 *
     */
    @Test
    void testLeftAssociativity_HighPrecedence() throws Exception {
        String[] values = {"8", "/", "4", "*", "2"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        List<Token> rpn = shuntingYard.convertToRPN(tokens);
        assertEquals("8 4 / 2 *", tokensToRpnString(rpn));
    }

    // -------------------------------------------------------------------------
    // 3. Behandlung von Klammern in Ausdrücken
    // -------------------------------------------------------------------------

    /**
     * Prüft die Erzwingung der Priorität durch Klammern.
     * (1 + 2) * 3 -> 1 2 + 3 *
     */
    @Test
    void testParentheses_PriorityOverride() throws Exception {
        String[] values = {"(", "1", "+", "2", ")", "*", "3"};
        Token.Type[] types = {Token.Type.LPARENNORMAL, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.RPARENNORMAL, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        List<Token> rpn = shuntingYard.convertToRPN(tokens);
        assertEquals("1 2 + 3 *", tokensToRpnString(rpn));
    }

    /**
     * Prüft den komplexen Beispielausdruck der Aufgabenstellung.
     * 3 + 4 * 2 / ( 1 - 5 ) -> 3 4 2 * 1 5 - / +
     */
    @Test
    void testComplexExample() throws Exception {
        String[] values = {"3", "+", "4", "*", "2", "/", "(", "1", "-", "5", ")"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.LPARENNORMAL, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.RPARENNORMAL};
        List<Token> tokens = createInfixTokens(values, types);

        List<Token> rpn = shuntingYard.convertToRPN(tokens);
        assertEquals("3 4 2 * 1 5 - / +", tokensToRpnString(rpn));
    }

    // -------------------------------------------------------------------------
    // 4. Fehlerfälle (Mismatched parentheses und ungültige Tokens)
    // -------------------------------------------------------------------------

    /**
     * Prüft auf fehlende rechte Klammer (im Stack verbleibende LPAREN).
     * 1 + ( 2 * 3
     */
    @Test
    void testMismatchedParentheses_MissingRightParen() {
        String[] values = {"1", "+", "(", "2", "*", "3"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.LPARENNORMAL, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        Exception exception = assertThrows(Exception.class, () -> {
            shuntingYard.convertToRPN(tokens);
        }, "Sollte eine Exception werfen, wenn ')' fehlt.");

        assertTrue(exception.getMessage().contains("Missing ')'"), "Fehlermeldung sollte auf die fehlende rechte Klammer hinweisen.");
    }

    /**
     * Prüft auf fehlende linke Klammer (RPAREN ohne vorherige LPAREN).
     * 1 + 2 ) * 3
     */
    @Test
    void testMismatchedParentheses_MissingLeftParen() {
        String[] values = {"1", "+", "2", ")", "*", "3"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.RPARENNORMAL, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        Exception exception = assertThrows(Exception.class, () -> {
            shuntingYard.convertToRPN(tokens);
        }, "Sollte eine Exception werfen, wenn '(' fehlt.");

        assertTrue(exception.getMessage().contains("Missing '('"), "Fehlermeldung sollte auf die fehlende linke Klammer hinweisen.");
    }

    /**
     * Prüft auf ein UNKNOWN Token im Ausdruck.
     * 1 + 2 $ 3
     */
    @Test
    void testInvalidTokenHandling() {
        String[] values = {"1", "+", "2", "$", "3"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.UNKNOWN, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        Exception exception = assertThrows(Exception.class, () -> {
            shuntingYard.convertToRPN(tokens);
        }, "Sollte eine Exception werfen, wenn ein UNKNOWN Token verarbeitet wird.");

        assertTrue(exception.getMessage().contains("Invalid token encountered: $"), "Fehlermeldung sollte auf das ungültige Token hinweisen.");
    }
}