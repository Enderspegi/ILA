package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests für die benutzerdefinierte Stack<T>-Klasse.
 */
public class StackTest {

    // Wir testen mit String-Elementen, um die Generik zu demonstrieren.
    private Stack<String> stack;

    /**
     * Wird vor jeder Testmethode ausgeführt, um einen neuen, leeren Stack zu initialisieren.
     */
    @BeforeEach
    void setUp() {
        stack = new Stack<>();
    }

    // -------------------------------------------------------------------------
    // Test der Basisfunktionalität (push, size, isEmpty)
    // -------------------------------------------------------------------------

    @Test
    void testNewStackIsEmpty() {
        // Ein neuer Stack sollte leer sein
        assertTrue(stack.isEmpty(), "Ein neuer Stack sollte leer sein.");
        assertEquals(0, stack.size(), "Die Größe eines neuen Stacks sollte 0 sein.");
    }

    @Test
    void testPushIncreasesSizeAndStackIsNotEmpty() {
        stack.push("A");
        assertEquals(1, stack.size(), "Nach einem push sollte die Größe 1 sein.");
        assertFalse(stack.isEmpty(), "Nach einem push sollte der Stack nicht leer sein.");

        stack.push("B");
        assertEquals(2, stack.size(), "Nach zwei pushes sollte die Größe 2 sein.");
    }

    // -------------------------------------------------------------------------
    // Test des LIFO-Prinzips (pop)
    // -------------------------------------------------------------------------

    @Test
    void testPopRemovesLastElement_LIFO() {
        stack.push("Erstes Element");
        stack.push("Zweites Element");
        stack.push("Drittes Element");

        // LIFO-Prüfung: Das zuletzt hinzugefügte Element ("Drittes Element") sollte zuerst entfernt werden.
        assertEquals("Drittes Element", stack.pop(), "pop() sollte das zuletzt hinzugefügte Element zurückgeben.");
        assertEquals(2, stack.size(), "Nach pop() sollte die Größe um 1 reduziert werden.");

        assertEquals("Zweites Element", stack.pop(), "pop() sollte das nächste Element zurückgeben.");
        assertEquals(1, stack.size());

        assertEquals("Erstes Element", stack.pop(), "pop() sollte das letzte Element zurückgeben.");
        assertTrue(stack.isEmpty(), "Nach dem Entfernen aller Elemente sollte der Stack leer sein.");
    }
    
    // -------------------------------------------------------------------------
    // Test von peek()
    // -------------------------------------------------------------------------

    @Test
    void testPeekReturnsTopElementWithoutRemovingIt() {
        stack.push("Unten");
        stack.push("Oben");
        
        // peek() sollte das oberste Element zurückgeben
        assertEquals("Oben", stack.peek(), "peek() sollte das oberste Element zurückgeben.");
        
        // Die Größe sollte unverändert bleiben
        assertEquals(2, stack.size(), "peek() sollte die Größe des Stacks nicht ändern.");

        // pop() zur Bestätigung, dass "Oben" wirklich das oberste Element war
        assertEquals("Oben", stack.pop());
        assertEquals("Unten", stack.peek());
    }

    // -------------------------------------------------------------------------
    // Test der Ausnahmebehandlung
    // -------------------------------------------------------------------------

    @Test
    void testPopOnEmptyStackThrowsException() {
        // Assertions.assertThrows prüft, ob der erwartete Exception-Typ geworfen wird.
        assertThrows(IndexOutOfBoundsException.class, () -> {
            stack.pop();
        }, "pop() auf einem leeren Stack sollte eine IndexOutOfBoundsException werfen.");
    }

    @Test
    void testPeekOnEmptyStackThrowsException() {
        // Assertions.assertThrows prüft, ob der erwartete Exception-Typ geworfen wird.
        assertThrows(IndexOutOfBoundsException.class, () -> {
            stack.peek();
        }, "peek() auf einem leeren Stack sollte eine IndexOutOfBoundsException werfen.");
    }

    @Test
    void testExceptionAfterEmptyingStack() {
        stack.push("Item");
        stack.pop(); // Stack ist jetzt leer

        assertThrows(IndexOutOfBoundsException.class, () -> {
            stack.pop();
        }, "pop() sollte IndexOutOfBoundsException werfen, nachdem der Stack geleert wurde.");
    }
}