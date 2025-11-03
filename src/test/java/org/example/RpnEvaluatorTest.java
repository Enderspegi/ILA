package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Unit Tests für die Klasse RpnEvaluator (Teilaufgabe 3.3).
 * Überprüft die korrekte Auswertung und das Fehlerverhalten.
 */
class TestRpnEvaluator {

    private final RpnEvaluator evaluator = new RpnEvaluator();
    // Toleranz für Gleitkommazahlenvergleiche
    private static final double DELTA = 1e-6; 

    // --- Korrekte Auswertung einfacher RPN-Ausdrücke ---

    /**
     * Testet den Beispielausdruck: 3 4 2 * 1 5 - / + = 1.0
     * Berechnung: 3 + (4 * 2) / (1 - 5) = 3 + 8 / (-4) = 3 - 2 = 1.0
     */
    @Test
    void testBeispielAusdruckKomplex() {
        List<String> rpn = Arrays.asList("3", "4", "2", "*", "1", "5", "-", "/", "+");
        assertEquals(1.0, evaluator.evaluate(rpn), DELTA, "Der komplexe Beispieausdruck ist fehlerhaft.");
    }

    /**
     * Testet eine einfache Kette von Operationen: 10 5 - 2 * = 10
     * Berechnung: (10 - 5) * 2 = 10
     */
    @Test
    void testKettenOperation() {
        List<String> rpn = Arrays.asList("10", "5", "-", "2", "*");
        assertEquals(10.0, evaluator.evaluate(rpn), DELTA, "Ketten-Operation fehlerhaft.");
    }

    // --- Überprüfung der Operatoren +, -, *, / ---

    @Test
    void testOperatorAddition() {
        assertEquals(15.5, evaluator.evaluate(Arrays.asList("10.5", "5", "+")), DELTA, "Addition fehlerhaft.");
    }

    @Test
    void testOperatorSubtraktion() {
        assertEquals(-10.0, evaluator.evaluate(Arrays.asList("5", "15", "-")), DELTA, "Subtraktion fehlerhaft.");
    }

    @Test
    void testOperatorMultiplikation() {
        assertEquals(42.0, evaluator.evaluate(Arrays.asList("6", "7", "*")), DELTA, "Multiplikation fehlerhaft.");
    }

    @Test
    void testOperatorDivision() {
        assertEquals(2.5, evaluator.evaluate(Arrays.asList("10", "4", "/")), DELTA, "Division fehlerhaft.");
    }

    // --- Verhalten bei Division durch 0 (Exception oder Fehlermeldung) ---

    @Test
    void testDivisionDurchNull() {
        List<String> rpn = Arrays.asList("5", "0", "/");
        // Überprüfung, ob eine RpnEvaluationException geworfen wird
        RpnEvaluator.RpnEvaluationException exception = assertThrows(RpnEvaluator.RpnEvaluationException.class, () -> {
            evaluator.evaluate(rpn);
        }, "Division durch Null sollte eine Exception auslösen.");

        // Überprüfung der Fehlermeldung
        assertTrue(exception.getMessage().contains("Division durch Null"), "Fehlermeldung sollte 'Division durch Null' enthalten.");
    }

    // --- Fehlverhalten bei zu wenigen oder zu vielen Operanden ---

    @Test
    void testFehlerZuWenigeOperandenFuerOperator() {
        List<String> rpn = Arrays.asList("5", "+"); // Operator "+" benötigt 2, hat aber nur 1
        RpnEvaluator.RpnEvaluationException exception = assertThrows(RpnEvaluator.RpnEvaluationException.class, () -> {
            evaluator.evaluate(rpn);
        }, "Zu wenige Operanden sollten eine Exception auslösen.");

        assertTrue(exception.getMessage().contains("Zu wenige Operanden"), "Fehlermeldung sollte 'Zu wenige Operanden' enthalten.");
    }

    @Test
    void testFehlerZuVieleOperandenAmEnde() {
        List<String> rpn = Arrays.asList("1", "2", "3"); // 3 Operanden, 0 Operatoren
        RpnEvaluator.RpnEvaluationException exception = assertThrows(RpnEvaluator.RpnEvaluationException.class, () -> {
            evaluator.evaluate(rpn);
        }, "Zu viele Operanden sollten eine Exception auslösen.");

        assertTrue(exception.getMessage().contains("Es verbleiben 3 Elemente"), "Fehlermeldung sollte das korrekte Stack-Element-Count enthalten.");
    }
    
    @Test
    void testFehlerLeererAusdruck() {
        List<String> rpn = Collections.emptyList();
        RpnEvaluator.RpnEvaluationException exception = assertThrows(RpnEvaluator.RpnEvaluationException.class, () -> {
            evaluator.evaluate(rpn);
        }, "Leerer Ausdruck sollte eine Exception auslösen.");

        assertTrue(exception.getMessage().contains("Stack am Ende leer"), "Fehlermeldung sollte auf den leeren Stack hinweisen.");
    }

    @Test
    void testFehlerUngueltigesToken() {
        List<String> rpn = Arrays.asList("1", "2", "$"); // Ungültiges Token
        RpnEvaluator.RpnEvaluationException exception = assertThrows(RpnEvaluator.RpnEvaluationException.class, () -> {
            evaluator.evaluate(rpn);
        }, "Ungültiges Token sollte eine Exception auslösen.");

        assertTrue(exception.getMessage().contains("Ungültiges Token") || exception.getMessage().contains("$"), "Fehlermeldung sollte auf das ungültige Token hinweisen.");
    }
}