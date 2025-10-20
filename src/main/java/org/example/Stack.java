package org.example;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {
    private List<T> elements;
    public Stack() {
        this.elements = new ArrayList<>();
    }

    public void push(T item) {
        // Fügt das Element am Ende der Liste hinzu (neue Spitze).
        elements.add(item);
    }

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
    public boolean isEmpty() {
        return elements.isEmpty();
    }
    
}
