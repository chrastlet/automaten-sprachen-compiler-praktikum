package de.thm.asc.automata;

/**
 * Represents a symbol in an automaton. Usually associated with a transition.
 *
 * @param value The name of the symbol.
 */
public record Symbol(char value) {

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
