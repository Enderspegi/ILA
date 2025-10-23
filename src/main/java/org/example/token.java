package org.example;


/**
 * Die Klasse {@code Token} repräsentiert ein einzelnes Token (ein "Wort")
 * innerhalb eines mathematischen Ausdrucks, z. B. eine Zahl, einen Operator
 * oder eine Klammer.
 *
 * <p>Ein Beispielausdruck wie {@code (3 + -5) * 2} würde in folgende Tokens zerlegt:</p>
 * <ul>
 *   <li>{@code (} → LPAREN</li>
 *   <li>{@code 3} → NUMBER</li>
 *   <li>{@code +} → OPERATOR</li>
 *   <li>{@code -5} → NUMBER (mit Vorzeichen)</li>
 *   <li>{@code )} → RPAREN</li>
 *   <li>{@code *} → OPERATOR</li>
 *   <li>{@code 2} → NUMBER</li>
 * </ul>
 */
public class Token {

    /**
     * Typen von Tokens, die im Ausdruck vorkommen können.
     */
    public enum Type {
        /** Eine Zahl (z. B. 42, -5, 3.14) */
        NUMBER,

        /** Ein Operator (z. B. +, -, *, /, ^) */
        OPERATOR,

        /** Linke Klammer '(' */
        LPAREN,

        /** Rechte Klammer ')' */
        RPAREN,

        /** Unbekanntes oder ungültiges Token */
        UNKNOWN
    }

    private Type type = null;

    private String value = "";

    /**
     * Erstellt ein neues {@code Token}-Objekt mit Typ und Textwert.
     *
     * @param type  der Typ des Tokens (z. B. NUMBER oder OPERATOR)
     * @param value der Zeichenkettenwert des Tokens
     */
    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gibt den Typ dieses Tokens zurück.
     *
     * @return der Typ des Tokens
     */
    public Type getType() {
        return type;
    }

    /**
     * Gibt den Zeichenkettenwert dieses Tokens zurück.
     *
     * @return der Wert des Tokens
     */
    public String getValue() {
        return value;
    }

    /**
     * Gibt den Textwert des Tokens zurück, um eine einfache Ausgabe
     * bei der Tokenliste zu ermöglichen.
     *
     * @return der Zeichenkettenwert des Tokens
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Prüft, ob dieses Token eine negative Zahl mit Vorzeichen ist.
     *
     * @return {@code true}, wenn das Token eine Zahl ist, die mit '-' beginnt;
     *         sonst {@code false}
     */
    public boolean isSignedNegativeNumber() {
        return type == Type.NUMBER && value.startsWith("-");
    }
}

