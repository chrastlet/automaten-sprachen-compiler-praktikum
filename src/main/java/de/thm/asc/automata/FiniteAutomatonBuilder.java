package de.thm.asc.automata;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Allow the successive construction of an automaton.
 */
public class FiniteAutomatonBuilder {

    private State initialState = null;
    private Set<State> states = new HashSet<>();
    private Set<Transition> transitions = new HashSet<>();
    private Set<State> finalStates = new HashSet<>();
    private int stateCounter = 0;

    public FiniteAutomatonBuilder() {
    }

    public FiniteAutomatonBuilder(FiniteAutomaton automaton) {
        this.states.addAll(automaton.states());
        this.transitions.addAll(automaton.transitions());

        this.setInitialState(automaton.initialState());
        automaton.finalStates().forEach(this::makeFinalState);
    }

    /**
     * Adds a new state to the automaton. The name is constructed from the given prefix and an internal counter.
     *
     * @param prefix The prefix to be used for the state's name.
     * @return The newly added state.
     */
    public State newState(String prefix) {
        while (true) {
            var candidate = new State(prefix + this.stateCounter);

            if (!this.states.contains(candidate)) {
                this.states.add(candidate);
                return candidate;
            }

            this.stateCounter++;
        }
    }

    /**
     * Adds a new state to the automaton using 's' as the prefix.
     *
     * @return The newly added state.
     */
    public State newState() {
        return newState("q");
    }

    /**
     * Adds all states from the given set to this automaton.
     *
     * @param states The states to be added.
     */
    public void addStates(Set<State> states) {
        this.states.addAll(states);
    }

    /**
     * Sets the initial state of this automaton to the given state.
     *
     * @param initialState The state to be set as the initial state.
     */
    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    /**
     * Adds the given state to the set of final states of this automaton.
     *
     * @param state The state to be added to the final states.
     */
    public void makeFinalState(State state) {
        this.finalStates.add(state);
    }

    /**
     * Adds a new transition to the automaton. Use the static constructor functions in the {@link Transition} class
     *
     * @param transition The transition to add to this automaton.
     * @return The newly added transition
     */
    public Transition addTransition(Transition transition) {
        transitions.add(transition);

        return transition;
    }

    /**
     * Checks if the automaton contains a given transition
     *
     * @param from   The left side of the transition (the 'from' state).
     * @param to     The right side of the transition (the 'to' state).
     * @param symbol The symbol of this transition. Epsilon transitions are represented by an empty symbol.
     * @return Whether the automaton contains the given transition.
     */
    public boolean hasTransition(State from, State to, Symbol symbol) {
        for (var t : transitions) {
            if (t.left.equals(from) && t.right.equals(to) && t.symbol().equals(symbol)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finalizes the built automaton into an instance of the immutable {@link FiniteAutomaton} class.
     * @return The built automaton.
     */
    public FiniteAutomaton result() {
        return new FiniteAutomaton(
                this.states,
                this.transitions.stream().flatMap(t -> t.symbol.stream()).collect(Collectors.toSet()),
                this.initialState,
                this.finalStates,
                this.transitions
        );
    }
}
