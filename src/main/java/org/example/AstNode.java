package org.example;

/**
 * Repräsentiert einen Knoten im Abstrakten Syntaxbaum (AST).
 * Jeder Knoten speichert seinen Wert (Operand oder Operator)
 * und Verweise auf seine Kinder (Operanden des Operators).
 */
public class AstNode {
    private String value; // Der Wert (z.B. "3", "x", "+", "*")
    private AstNode left;  // Linkes Kind (erster Operand)
    private AstNode right; // Rechtes Kind (zweiter Operand)

    // Konstruktor für Blätter (Zahlen/Variablen)
    public AstNode(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    // Konstruktor für interne Knoten (Operatoren)
    public AstNode(String value, AstNode left, AstNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    // Getter-Methoden (für AstBuilder und Unit Tests)
    public String getValue() { return value; }
    public AstNode getLeft() { return left; }
    public AstNode getRight() { return right; }

    /**
     * Prüft, ob der Knoten ein Blatt ist (keine Kinder hat).
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }
    
    // Eine einfache to-String-Methode für die Ausgabe
    @Override
    public String toString() {
        return value;
    }
}