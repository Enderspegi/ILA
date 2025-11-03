package org.example;

import java.util.List;

/**
 * Die Klasse RpnEvaluator wertet eine Liste von Token in umgekehrter
 * polnischer Notation (Reverse Polish Notation, RPN) aus.
 * Sie verwendet einen Stack, um Operanden zwischenzuspeichern und
 * Operationen schrittweise auszuführen.
 */
public class RpnEvaluator {

    /**
     * Definiert eine benutzerdefinierte Exception für Fehler bei der RPN-Auswertung,
     * wie z.B. Division durch Null oder ungültige Eingabe.
     */
    public static class RpnEvaluationException extends RuntimeException {
        /**
         * Erstellt eine neue RpnEvaluationException mit der angegebenen Detailnachricht.
         * @param message Die Detailnachricht.
         */
        public RpnEvaluationException(String message) {
            super(message);
        }
    }

    /**
     * Wertet eine Liste von Tokens in RPN-Form aus.
     *
     * Die Auswertung folgt diesen Schritten:
     * 1. Wenn das Token eine Zahl ist, wird es auf den Stack gelegt.
     * 2. Wenn das Token ein Operator (+, -, *, /) ist, werden zwei Operanden
     * vom Stack genommen, die Operation ausgeführt und das Ergebnis zurück
     * auf den Stack gelegt.
     * 3. Am Ende muss genau ein Element (das Ergebnis) auf dem Stack verbleiben.
     *
     * @param rpnTokens Eine Liste von Strings, die den RPN-Ausdruck darstellen
     * (z.B. ["3", "4", "2", "*", "1", "5", "-", "/", "+"]).
     * @return Das berechnete Ergebnis des Ausdrucks.
     * @throws RpnEvaluationException wenn der Ausdruck ungültig ist (z.B. Division durch Null,
     * zu viele/zu wenige Operanden).
     */
    public double evaluate(List<String> rpnTokens) throws RpnEvaluationException {
        // Hinweis: Es wird angenommen, dass die hier verwendete Stack-Klasse
        // die Funktionalität Ihrer zuvor implementierten Stack-Klasse (mit double) abbildet.
        Stack<Double> stack = new Stack<>();

        // 

        for (String token : rpnTokens) {
            if (isNumber(token)) {
                // 1. Zahl: Auf den Stack legen
                try {
                    stack.push(Double.parseDouble(token));
                } catch (NumberFormatException e) {
                    throw new RpnEvaluationException("Ungültiges Token im Ausdruck: " + token);
                }
            } else if (isOperator(token)) {
                // 2. Operator: Operanden vom Stack nehmen
                if (stack.size() < 2) {
                    throw new RpnEvaluationException("Zu wenige Operanden für den Operator: " + token);
                }

                // Die Operanden werden in umgekehrter Reihenfolge gepoppt (Op2 zuerst)
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                double result;

                // 3. Operation ausführen
                switch (token) {
                    case "+":
                        result = operand1 + operand2;
                        break;
                    case "-":
                        result = operand1 - operand2;
                        break;
                    case "*":
                        result = operand1 * operand2;
                        break;
                    case "/":
                        if (operand2 == 0) {
                            // Division durch 0 abfangen
                            throw new RpnEvaluationException("Division durch Null!");
                        }
                        result = operand1 / operand2;
                        break;
                    default:
                        // Dies sollte durch isOperator() bereits ausgeschlossen sein, dient aber als Fallback
                        throw new RpnEvaluationException("Unbekannter Operator: " + token);
                }

                // Ergebnis zurück auf den Stack legen
                stack.push(result);
            } else {
                throw new RpnEvaluationException("Ungültiges Token im Ausdruck: " + token);
            }
        }

        // 4. Überprüfung des Endergebnisses
        if (stack.size() != 1) {
            throw new RpnEvaluationException("Ungültiger RPN-Ausdruck: Es verbleiben " + stack.size() + " Elemente auf dem Stack (erwartet: 1).");
        }

        // Das Endergebnis zurückgeben
        return stack.pop();
    }

    /**
     * Hilfsmethode zur Überprüfung, ob ein Token eine gültige Zahl ist.
     * @param token Das zu prüfende Token.
     * @return true, wenn das Token eine Zahl darstellt, andernfalls false.
     */
    private boolean isNumber(String token) {
        try {
            // Versucht, den String in einen Double zu parsen.
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Hilfsmethode zur Überprüfung, ob ein Token ein unterstützter Operator ist.
     * @param token Das zu prüfende Token.
     * @return true, wenn das Token einer der unterstützten Operatoren (+, -, *, /) ist,
     * andernfalls false.
     */
    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
}