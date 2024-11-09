package de.thm.asc.automata.algorithms;

import de.thm.asc.automata.*;

import java.util.*;

/**
 * The Rabin-Scott powerset construction algorithm can be used to convert any nondeterministic finite automaton into a deterministic finite automaton.
 * The intuition behind that possibility is that when simulating a nfa on an input at any point you are in a superposition of any subset of states in the nfa.
 * <p>
 * The number of subsets, while exponentially larger, is still finite. So by enumerating and creating a new state in a new automaton for all relevant subsets,
 * and then creating transitions for every transition for any of the original states in a set to all other relevant subset-states, we can achieve a DFA.
 */
public class PowersetConstruction {
    public static FiniteAutomaton apply(FiniteAutomaton nfa) {
        return new PowersetConstruction(nfa).construct();
    }

    private final FiniteAutomaton nfa;

    private PowersetConstruction(FiniteAutomaton nfa) {
        this.nfa = nfa;
    }

    /**
     * @param s A state to calculate the epsilon closure for.
     * @return The epsilon closure of the given state.
     */
    private Set<State> epsilonClosure(State s) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * @param s A set of states to calculate the epsilon closure for.
     * @return The epsilon closure of a given set of states.
     */
    private Set<State> epsilonClosure(Set<State> s) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @param s      A state of the automaton.
     * @param symbol A symbol from the alphabet.
     * @return A set of states that can be reached from the given state with the given symbol.
     */
    private Set<State> move(State s, Symbol symbol) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @param s      A set of states of the automaton.
     * @param symbol A symbol from the alphabet.
     * @return A set of states that can be reached from any of the given states with the given symbol.
     */
    private Set<State> move(Set<State> s, Symbol symbol) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Apply the powerset construction to the automaton supplied in the constructor of this class.
     * @return The equivalent deterministic finite automaton (DFA).
     */
    private FiniteAutomaton construct() {
        throw new RuntimeException("Not implemented");
    }
}
