package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit Tests für die ShuntingYard-Klasse.
 * Überprüft die Konvertierung von Infix nach RPN unter Berücksichtigung von:
 * 1. Priorität (+, -, *, /).
 * 2. Rechtsassoziativität (^).
 * 3. Gemischten Klammertypen ((), []).
 * 4. Fehlerbehandlung bei unbalancierten und falsch gematchten Klammern.
 */
public class ShuntingYardTest {

    private final ShuntingYard shuntingYard = new ShuntingYard();

    /**
     * Hilfsmethode zur Erstellung einer Token-Liste aus String-Werten und Types.
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
    // 1. Tests für Basis-Funktionalität und Priorität/Assoziativität
    // -------------------------------------------------------------------------

    /**
     * Prüft die einfache Addition/Subtraktion (gleiche Priorität, linksassoziativ).
     * 1 - 2 + 3 -> 1 2 - 3 +
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
     * Prüft die Rechtsassoziativität der Exponentiation.
     * 2 ^ 3 ^ 2 -> 2 3 2 ^ ^
     */
    @Test
    void testRightAssociativity_Power() throws Exception {
        String[] values = {"2", "^", "3", "^", "2"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        List<Token> rpn = shuntingYard.convertToRPN(tokens);
        assertEquals("2 3 2 ^ ^", tokensToRpnString(rpn), "Exponentiation muss rechtsassoziativ sein.");
    }
    
    // -------------------------------------------------------------------------
    // 2. Tests für gemischte Klammertypen (Syntax-Validierung)
    // -------------------------------------------------------------------------

    /**
     * Prüft verschachtelte Klammern mit unterschiedlichen Typen.
     * 3 + [ 4 * ( 2 - 1 ) ] -> 3 4 2 1 - * +
     */
    @Test
    void testMixedParentheses() throws Exception {
        String[] values = {"3", "+", "[", "4", "*", "(", "2", "-", "1", ")", "]"};
        Token.Type[] types = {
            Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.LPARENECKIG, Token.Type.NUMBER, 
            Token.Type.OPERATOR, Token.Type.LPARENNORMAL, Token.Type.NUMBER, Token.Type.OPERATOR, 
            Token.Type.NUMBER, Token.Type.RPARENNORMAL, Token.Type.RPARENECKIG
        };
        List<Token> tokens = createInfixTokens(values, types);

        List<Token> rpn = shuntingYard.convertToRPN(tokens);
        assertEquals("3 4 2 1 - * +", tokensToRpnString(rpn), "Klammern müssen die Priorität korrekt erzwingen.");
    }
    
    /**
     * Prüft komplexe Klammerung mit Exponentiation.
     * [ ( 4 + 2 ) ^ 2 ] / 3 -> 4 2 + 2 ^ 3 /
     */
    @Test
    void testComplexMixedParenthesesWithPower() throws Exception {
        String[] values = {"[", "(", "4", "+", "2", ")", "^", "2", "]", "/", "3"};
        Token.Type[] types = {
            Token.Type.LPARENECKIG, Token.Type.LPARENNORMAL, Token.Type.NUMBER, Token.Type.OPERATOR, 
            Token.Type.NUMBER, Token.Type.RPARENNORMAL, Token.Type.OPERATOR, Token.Type.NUMBER, 
            Token.Type.RPARENECKIG, Token.Type.OPERATOR, Token.Type.NUMBER
        };
        List<Token> tokens = createInfixTokens(values, types);

        List<Token> rpn = shuntingYard.convertToRPN(tokens);
        assertEquals("4 2 + 2 ^ 3 /", tokensToRpnString(rpn));
    }

    // -------------------------------------------------------------------------
    // 3. Fehlerfälle (Unbalancierte und falsch gematchte Klammern)
    // -------------------------------------------------------------------------

    /**
     * Prüft auf fehlende schließende runde Klammer (Missing ')').
     * 1 + ( 2 * 3
     */
    @Test
    void testMismatchedParentheses_MissingNormalRight() {
        String[] values = {"1", "+", "(", "2", "*", "3"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.LPARENNORMAL, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        Exception exception = assertThrows(Exception.class, () -> {
            shuntingYard.convertToRPN(tokens);
        }, "Sollte eine Exception werfen, wenn ')' fehlt.");

        assertTrue(exception.getMessage().contains("Missing ')'"), "Fehlermeldung sollte auf fehlende ')' hinweisen.");
    }

    /**
     * Prüft auf fehlende schließende eckige Klammer (Missing ']').
     * [ 2 + 3 * 5
     */
    @Test
    void testMismatchedParentheses_MissingSquareRight() {
        String[] values = {"[", "2", "+", "3", "*", "5"};
        Token.Type[] types = {Token.Type.LPARENECKIG, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        Exception exception = assertThrows(Exception.class, () -> {
            shuntingYard.convertToRPN(tokens);
        }, "Sollte eine Exception werfen, wenn ']' fehlt.");

        assertTrue(exception.getMessage().contains("Missing ']'"), "Fehlermeldung sollte auf fehlende ']' hinweisen.");
    }
    
    /**
     * Prüft auf Type Mismatch: Eckige Klammer wird mit runder Klammer geschlossen.
     * 1 + [ 2 )
     */
    @Test
    void testMismatchedParentheses_TypeMismatch_SquareClosedByNormal() {
        String[] values = {"1", "+", "[", "2", ")"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.LPARENECKIG, Token.Type.NUMBER, Token.Type.RPARENNORMAL};
        List<Token> tokens = createInfixTokens(values, types);

        Exception exception = assertThrows(Exception.class, () -> {
            shuntingYard.convertToRPN(tokens);
        }, "Sollte eine Exception werfen, wenn der Klammertyp nicht übereinstimmt (z.B. '[' mit ')' geschlossen).");

        assertTrue(exception.getMessage().contains("Expected ']', found ')'"), 
                   "Fehlermeldung sollte auf den Typ-Mismatch hinweisen.");
    }
    
    /**
     * Prüft auf Type Mismatch: Runde Klammer wird mit eckiger Klammer geschlossen.
     * 1 + ( 2 ]
     */
    @Test
    void testMismatchedParentheses_TypeMismatch_NormalClosedBySquare() {
        String[] values = {"1", "+", "(", "2", "]"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.LPARENNORMAL, Token.Type.NUMBER, Token.Type.RPARENECKIG};
        List<Token> tokens = createInfixTokens(values, types);

        Exception exception = assertThrows(Exception.class, () -> {
            shuntingYard.convertToRPN(tokens);
        }, "Sollte eine Exception werfen, wenn der Klammertyp nicht übereinstimmt (z.B. '(' mit ']' geschlossen).");

        assertTrue(exception.getMessage().contains("Expected ')', found ']'"), 
                   "Fehlermeldung sollte auf den Typ-Mismatch hinweisen.");
    }

    /**
     * Prüft, dass ein UNKNOWN-Token sofort eine Exception wirft.
     */
    @Test
    void testUnknownToken() {
        String[] values = {"1", "$", "2"};
        Token.Type[] types = {Token.Type.NUMBER, Token.Type.UNKNOWN, Token.Type.NUMBER};
        List<Token> tokens = createInfixTokens(values, types);

        Exception exception = assertThrows(Exception.class, () -> {
            shuntingYard.convertToRPN(tokens);
        }, "Sollte eine Exception werfen, wenn ein UNKNOWN-Token auftritt.");

        assertTrue(exception.getMessage().contains("Invalid token encountered: $"), "Fehlermeldung sollte das ungültige Token nennen.");
    }
}