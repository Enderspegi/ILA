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
    
}
