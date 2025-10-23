/**
 * Die Klasse {@code Token} repräsentiert ein einzelnes Token (z. B. Zahl, Operator, Klammer)
 * in einem mathematischen Ausdruck oder einer ähnlichen Zeichenkette.
 * <p>
 * Ein Token besteht aus einem Typ (z. B. {@link Type#NUMBER}) und dem zugehörigen Wert
 * (z. B. "42" oder "+").
 * </p>
 *
 * Beispiel:
 * <pre>
 * Token t = new Token(Token.Type.NUMBER, "5");
 * System.out.println(t.getType());  // Ausgabe: NUMBER
 * System.out.println(t.getValue()); // Ausgabe: 5
 * </pre>
 *
 * @author
 * @version 1.0
 */
public class Token {

    /**
     * Der Typ eines Tokens.
     * <ul>
     *     <li>{@link #NUMBER} – eine Zahl (z. B. "42")</li>
     *     <li>{@link #OPERATOR} – ein Operator (z. B. "+", "-", "*", "/")</li>
     *     <li>{@link #LPAREN} – eine linke Klammer "("</li>
     *     <li>{@link #RPAREN} – eine rechte Klammer ")"</li>
     *     <li>{@link #UNKNOWN} – ein unbekanntes oder ungültiges Token</li>
     * </ul>
     */
    public enum Type { NUMBER, OPERATOR, LPAREN, RPAREN, UNKNOWN }

    /** Der Typ dieses Tokens (z. B. NUMBER, OPERATOR etc.) */
    private final Type type;

    /** Der eigentliche Textwert des Tokens (z. B. "3", "+", "(") */
    private final String value;

    /**
     * Erstellt ein neues Token mit dem angegebenen Typ und Wert.
     *
     * @param type  der Typ des Tokens (z. B. {@link Type#NUMBER})
     * @param value der Zeichenwert des Tokens (z. B. "5", "+", "(")
     */
    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gibt den Typ dieses Tokens zurück.
     *
     * @return der {@link Type} dieses Tokens
     */
    public Type getType() {
        return type;
    }

    /**
     * Gibt den Wert dieses Tokens zurück.
     *
     * @return der Wert (String) dieses Tokens
     */
    public String getValue() {
        return value;
    }

    /**
     * Gibt den Wert dieses Tokens als String zurück.
     * <p>
     * Diese Methode ist hilfreich für Debugging und einfache Ausgaben.
     * </p>
     *
     * @return der Textwert des Tokens
     */
    @Override
    public String toString() {
        return value;
    }
}
