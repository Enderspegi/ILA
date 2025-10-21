package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine generische Stack-Implementierung, die das LIFO-Prinzip
 * (Last In - First Out) beachtet.
 * Verwendet intern eine List zur Speicherung der Elemente.
 *
 * @param <T> Der Typ der Elemente, die im Stack gespeichert werden.
 */
public class Stack<T> {
    // Interne Speicherung der Elemente.
    // Das Ende der Liste repräsentiert die "Spitze" (Top) des Stacks.
    private List<T> elements;

    /**
     * Konstruktor zur Initialisierung des Stacks.
     */
    public Stack() {
        this.elements = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Stack-Operationen
    // -------------------------------------------------------------------------

    /**
     * Legt ein Element auf den Stack (am Ende der internen Liste).
     *
     * @param item Das hinzuzufügende Element.
     */
    public void push(T item) {
        // Fügt das Element am Ende der Liste hinzu (neue Spitze).
        elements.add(item);
    }

    /**
     * Entfernt das oberste Element vom Stack und gibt es zurück.
     * Wirft eine IndexOutOfBoundsException, falls der Stack leer ist.
     *
     * @return Das oberste Element des Stacks.
     * @throws IndexOutOfBoundsException wenn der Stack leer ist.
     */
    public T pop() {
        if (isEmpty()) {
            // Wirft die Exception, da der Stack leer ist.
            // In Java wird hier üblicherweise IndexOutOfBoundsException
            // oder eine spezifischere EmptyStackException verwendet.
            // Wir verwenden die allgemeinere IndexOutOfBoundsException, 
            // die von List.remove(int) geworfen wird.
            throw new IndexOutOfBoundsException("Der Stack ist leer.");
        }
        
        // Index des letzten Elements (der Spitze des Stacks).
        int topIndex = elements.size() - 1;
        
        // Entfernt das Element an der Spitze und gibt es zurück.
        return elements.remove(topIndex);
    }

    /**
     * Gibt das oberste Element des Stacks zurück, ohne es zu entfernen.
     * Wirft eine IndexOutOfBoundsException, falls der Stack leer ist.
     *
     * @return Das oberste Element des Stacks.
     * @throws IndexOutOfBoundsException wenn der Stack leer ist.
     */
    public T peek() {
        if (isEmpty()) {
            // Wirft die Exception, da der Stack leer ist.
            throw new IndexOutOfBoundsException("Der Stack ist leer.");
        }
        
        // Index des letzten Elements (der Spitze des Stacks).
        int topIndex = elements.size() - 1;
        
        // Gibt das Element an der Spitze zurück.
        return elements.get(topIndex);
    }

    /**
     * Prüft, ob der Stack leer ist.
     *
     * @return true, wenn der Stack keine Elemente enthält, sonst false.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Gibt die aktuelle Anzahl der Elemente im Stack zurück.
     *
     * @return Die Anzahl der Elemente.
     */
    public int size() {
        return elements.size();
    }
}