package de.thm.asc.automata;

/**
 * Represents a state of an automaton.
 *
 * @param name The name of the state.
 */
public record State(String name) {
    @Override
    public String toString() {
        return String.format("(State %s)", name);
    }
}
