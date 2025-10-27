package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests für die Token-Klasse.
 * Überprüft die korrekte Initialisierung und die Hilfsmethoden.
 */
public class TokenTest {

    // -------------------------------------------------------------------------
    // 1. Tests für Initialisierung und Getter
    // -------------------------------------------------------------------------

    /**
     * Testet die korrekte Zuweisung und Rückgabe von Typ und Wert eines NUMBER-Tokens.
     */
    @Test
    void testTokenInitialization_Number() {
        Token token = new Token(Token.Type.NUMBER, "3.14");
        assertEquals(Token.Type.NUMBER, token.getType(), "Der Token-Typ sollte NUMBER sein.");
        assertEquals("3.14", token.getValue(), "Der Token-Wert sollte '3.14' sein.");
        assertEquals("3.14", token.toString(), "toString() sollte den Wert zurückgeben.");
    }

    /**
     * Testet die korrekte Zuweisung und Rückgabe eines OPERATOR-Tokens.
     */
    @Test
    void testTokenInitialization_Operator() {
        Token token = new Token(Token.Type.OPERATOR, "*");
        assertEquals(Token.Type.OPERATOR, token.getType(), "Der Token-Typ sollte OPERATOR sein.");
        assertEquals("*", token.getValue(), "Der Token-Wert sollte '*' sein.");
    }
    
    /**
     * Testet die korrekte Zuweisung und Rückgabe eines Klammer-Tokens.
     */
    @Test
    void testTokenInitialization_Parenthesis() {
        Token token = new Token(Token.Type.LPARENNORMAL, "(");
        assertEquals(Token.Type.LPARENNORMAL, token.getType(), "Der Token-Typ sollte LPAREN sein.");
        assertEquals("(", token.getValue(), "Der Token-Wert sollte '(' sein.");
    }
    
    /**
     * Testet die korrekte Zuweisung und Rückgabe eines UNKNOWN-Tokens.
     */
    @Test
    void testTokenInitialization_Unknown() {
        Token token = new Token(Token.Type.UNKNOWN, "$");
        assertEquals(Token.Type.UNKNOWN, token.getType(), "Der Token-Typ sollte UNKNOWN sein.");
        assertEquals("$", token.getValue(), "Der Token-Wert sollte '$' sein.");
    }
    
    // -------------------------------------------------------------------------
    // 2. Tests für isSignedNegativeNumber()
    // -------------------------------------------------------------------------

    /**
     * Testet die Methode für eine negative Ganzzahl.
     */
    @Test
    void testIsSignedNegativeNumber_NegativeInteger() {
        Token token = new Token(Token.Type.NUMBER, "-10");
        assertTrue(token.isSignedNegativeNumber(), "Sollte TRUE für '-10' zurückgeben.");
    }
    
    /**
     * Testet die Methode für eine negative Gleitkommazahl.
     */
    @Test
    void testIsSignedNegativeNumber_NegativeDecimal() {
        Token token = new Token(Token.Type.NUMBER, "-0.5");
        assertTrue(token.isSignedNegativeNumber(), "Sollte TRUE für '-0.5' zurückgeben.");
    }

    /**
     * Testet die Methode für eine positive Zahl (keine negative Vorzeichen-Zahl).
     */
    @Test
    void testIsSignedNegativeNumber_PositiveNumber() {
        Token token = new Token(Token.Type.NUMBER, "100");
        assertFalse(token.isSignedNegativeNumber(), "Sollte FALSE für '100' zurückgeben.");
    }

    /**
     * Testet die Methode für einen OPERATOR, obwohl der Wert mit '-' beginnt.
     */
    @Test
    void testIsSignedNegativeNumber_Operator() {
        Token token = new Token(Token.Type.OPERATOR, "-"); // Subtraktionsoperator
        assertFalse(token.isSignedNegativeNumber(), "Sollte FALSE für den Subtraktionsoperator '-' zurückgeben.");
    }
    
    /**
     * Testet die Methode für einen OPERATOR (positiv).
     */
    @Test
    void testIsSignedNegativeNumber_PositiveOperator() {
        Token token = new Token(Token.Type.OPERATOR, "+");
        assertFalse(token.isSignedNegativeNumber(), "Sollte FALSE für '+' zurückgeben.");
    }
}// leere wie Sch