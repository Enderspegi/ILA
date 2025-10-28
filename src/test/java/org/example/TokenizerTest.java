package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit Tests für die Tokenizer-Klasse.
 * Überprüft die korrekte Zerlegung des Ausdrucks in Tokens
 * sowie die Handhabung von Leerzeichen, Vorzeichen und Fehlerfällen.
 */
public class TokenizerTest {

    private final Tokenizer tokenizer = new Tokenizer();

    /**
     * Hilfsmethode zur Konvertierung einer Token-Liste in eine Liste von String-Werten.
     */
    private List<String> getValues(List<Token> tokens) {
        return tokens.stream().map(Token::getValue).collect(Collectors.toList());
    }

    /**
     * Hilfsmethode zur Konvertierung einer Token-Liste in eine Liste von Token-Typen.
     */
    private List<Token.Type> getTypes(List<Token> tokens) {
        return tokens.stream().map(Token::getType).collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // 1. Korrekte Erkennung und Klassifizierung der Tokens
    // -------------------------------------------------------------------------

    /**
     * Testet einen Standardausdruck mit Operatoren und Klammern.
     * (3 + 4) * 2
     */
    @Test
    void testBasicExpression() {
        String expression = "(3 + 4) * 2";
        List<Token> tokens = tokenizer.tokenize(expression);

        List<String> expectedValues = List.of("(", "3", "+", "4", ")", "*", "2");
        List<Token.Type> expectedTypes = List.of(
            Token.Type.LPARENNORMAL, Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER,
            Token.Type.RPARENNORMAL, Token.Type.OPERATOR, Token.Type.NUMBER
        );

        assertEquals(expectedValues, getValues(tokens));
        assertEquals(expectedTypes, getTypes(tokens));
    }

    /**
     * Testet die Erkennung von Dezimalzahlen.
     */
    @Test
    void testRealNumbers() {
        String expression = "3.14 / 1.0";
        List<Token> tokens = tokenizer.tokenize(expression);
        
        List<String> expectedValues = List.of("3.14", "/", "1.0");

        assertEquals(expectedValues, getValues(tokens));
        assertTrue(getTypes(tokens).stream().allMatch(t -> t == Token.Type.NUMBER || t == Token.Type.OPERATOR));
    }

    // -------------------------------------------------------------------------
    // 2. Behandlung von Leerzeichen und Vorzeichen
    // -------------------------------------------------------------------------

    /**
     * Testet die korrekte Ignorierung von Leerzeichen.
     */
    @Test
    void testWhitespaceHandling() {
        String expression = "10+20* ( 5-1 )";
        List<Token> tokens = tokenizer.tokenize(expression);
        
        List<String> expectedValues = List.of("10", "+", "20", "*", "(", "5", "-", "1", ")");

        assertEquals(expectedValues, getValues(tokens), "Whitespace sollte ignoriert werden.");
    }

    /**
     * Testet die korrekte Unterscheidung von Vorzeichen und Subtraktion.
     * 1 - -5 + (-3.2)
     */
    @Test
    void testSignVsOperator() {
        String expression = "1 - -5 + (-3.2)";
        List<Token> tokens = tokenizer.tokenize(expression);

        List<String> expectedValues = List.of("1", "-", "-5", "+", "(", "-3.2", ")");
        List<Token.Type> expectedTypes = List.of(
            Token.Type.NUMBER, Token.Type.OPERATOR, Token.Type.NUMBER, Token.Type.OPERATOR, 
            Token.Type.LPARENNORMAL, Token.Type.NUMBER, Token.Type.RPARENNORMAL
        );

        assertEquals(expectedValues, getValues(tokens));
        assertEquals(expectedTypes, getTypes(tokens));
    }
    
    /**
     * Testet die negative Zahl am Anfang des Ausdrucks.
     * -10 * 5
     */
    @Test
    void testNegativeNumberAtStart() {
        String expression = "-10 * 5";
        List<Token> tokens = tokenizer.tokenize(expression);
        
        List<String> expectedValues = List.of("-10", "*", "5");
        
        assertEquals(expectedValues, getValues(tokens));
        assertEquals(Token.Type.NUMBER, tokens.get(0).getType(), "Das erste Token sollte eine Zahl sein.");
    }
    
    // -------------------------------------------------------------------------
    // 3. Fehlerfälle (Ungültige Zeichen und ungültige Zahlen)
    // -------------------------------------------------------------------------

    /**
     * Testet ungültige Zeichen.
     */
    @Test
    void testInvalidCharacter() {
        String expression = "1 + 2$";
        List<Token> tokens = tokenizer.tokenize(expression);

        List<String> expectedValues = List.of("1", "+", "2", "Invalid char: $");
        
        assertEquals(4, tokens.size());
        assertEquals(expectedValues, getValues(tokens));
        assertEquals(Token.Type.UNKNOWN, tokens.get(3).getType(), "Ungültiges Zeichen sollte UNKNOWN sein.");
    }
    
    /**
     * Testet ungültige Dezimalzahlen (nur Punkt).
     */
    @Test
    void testInvalidNumber_OnlyDecimalPoint() {
        String expression = "1 + . + 2";
        List<Token> tokens = tokenizer.tokenize(expression);

        assertEquals(List.of("1", "+", "Invalid number", "+", "2"), getValues(tokens));
        assertEquals(Token.Type.UNKNOWN, tokens.get(2).getType());
    }

    /**
     * Testet ungültige negative Zahl (nur Minus).
     * 1 + -
     */
    @Test
    void testInvalidNumber_OnlyNegativeSign() {
        String expression = "1 + -"; // Sollte "-" als Operator behandeln, aber der Code erkennt es als Vorzeichen
        List<Token> tokens = tokenizer.tokenize(expression); // Hier muss der Tokenizer scheitern, da '-' als Vorzeichen erkannt wird.
        
        // Da die Logik prüft, ob nach '-' eine Zahl folgt, muss hier der 'Invalid number' Fall greifen.
        // Der Tokenizer müsste den Zähler i zurücksetzen, um '-' als Operator zu erkennen, 
        // oder eine robustere Fehlerbehandlung nutzen.
        // Basierend auf Ihrer Logik: num.toString().equals("-") -> UNKNOWN Token
        
        assertEquals(List.of("1", "+", "Invalid number"), getValues(tokens));
        assertEquals(Token.Type.UNKNOWN, tokens.get(2).getType());
    }
}