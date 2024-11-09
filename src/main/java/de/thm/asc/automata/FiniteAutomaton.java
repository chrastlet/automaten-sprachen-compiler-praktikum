package de.thm.asc.automata;

import de.thm.asc.automata.algorithms.PowersetConstruction;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents the 5-tuple of an automaton as described in the lecture.
 *
 * @param states       The set containing all states of the automaton.
 * @param alphabet     The set containing the alphabet of the automaton.
 * @param initialState The initial state of the automaton.
 * @param finalStates  The set containing all final states of the automaton.
 * @param transitions  The set containing all transitions of the automaton,
 */
public record FiniteAutomaton(
        Set<State> states,
        Set<Symbol> alphabet,
        State initialState,
        Set<State> finalStates,
        Set<Transition> transitions
) {

    /**
     * Calculates the state to which a transition from the given state with the given symbol would lead.
     *
     * @param s The state to transition from.
     * @param c The symbol to transition with.
     * @return The resulting state or null if a transition with the given symbol does not exist.
     */
    public State next(State s, Symbol c) {
        var transition = transitions.stream()
                .filter(t -> !t.isEpsilonTransition() && t.left.equals(s) && t.symbol().equals(c))
                .findFirst()
                .orElse(null);

        if (transition == null) {
            return null;
        }

        return transition.right;
    }

    /**
     * Returns all transitions of this automaton containing the given state on the left side.
     *
     * @param left The state to look for.
     * @return All transitions containing the given state on the left side.
     */
    public Set<Transition> getTransitions(State left) {
        return this.transitions.stream().filter(t -> t.left.equals(left)).collect(Collectors.toSet());
    }

    /**
     * Steps through this automaton using the given string.
     * Caution: Only possible for deterministic automatons!
     *
     * @param s The string of symbols to be tested against this automaton.
     * @return Whether the string is accepted by this automaton.
     */
    public boolean test(String s) {
        if (!this.isDeterministic()) {
            throw new IllegalStateException("Stepping through non-deterministic automata unsupported");
        }

        var currentState = initialState;

        for (var c : s.toCharArray()) {
            var next = this.next(currentState, new Symbol(c));

            if (next == null) {
                return false;
            }

            currentState = next;
        }

        return finalStates.contains(currentState);
    }

    /**
     * Determines whether this automaton meets all required conditions to be considered deterministic.
     *
     * @return Whether this automaton is deterministic.
     */
    public boolean isDeterministic() {
        // If there is any epsilon transition, the automaton is nondeterministic
        if (this.transitions.stream().anyMatch(Transition::isEpsilonTransition)) return false;

        for (var state : this.states) {
            // Group transitions for this state by their symbol
            var grouped = this.transitions.stream()
                    .filter(t -> t.left.equals(state))
                    .collect(Collectors.groupingBy(Transition::symbol));

            // If there is any symbol that has more than 1 transition from this state, the automaton is nondeterministic
            if (grouped.entrySet().stream().anyMatch(e -> e.getValue().size() > 1)) return false;
        }

        // If neither of the previous checks finds nondeterminism, the automaton is deterministic
        return true;
    }

    /**
     * Converts this automaton to a deterministic automaton.
     *
     * @return The converted deterministic automaton.
     */
    public FiniteAutomaton toDFA() {
        if (this.isDeterministic()) return this;
        else return PowersetConstruction.apply(this);
    }

    @Override
    public String toString() {
        return toDotCode();
    }

    /**
     * Converts this automaton to GraphViz dot code.
     *
     * @return The generated dot code.
     */
    public String toDotCode() {
        var sb = new StringBuilder();

        sb.append("digraph automata {\n");
        sb.append("    node [shape=doublecircle]; ");
        sb.append(finalStates.stream().map(State::name).collect(Collectors.joining(",")));
        sb.append("\n");
        sb.append("    node [shape=point, style=invis]; ENTRY;\n");
        sb.append("    node [shape=circle, style=solid];\n");
        sb.append(String.format("    ENTRY -> %s;\n", initialState.name()));

        for (var t : this.transitions) {
            sb.append(String.format("    %s -> %s [label=\"%s\"]\n", t.left.name(), t.right.name(), t.symbolString()));
        }

        sb.append("}\n");

        return sb.toString();
    }
}
