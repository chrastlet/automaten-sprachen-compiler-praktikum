package de.thm.asc.automata.algorithms;

import de.thm.asc.automata.*;

import java.util.*;
import java.util.stream.Collectors;
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
        Set<State> result = new HashSet<>();
        Queue<State> statesToView = new LinkedList<>();
        Set<State> completedStates = new HashSet<>();

        result.add(s);
        statesToView.add(s);

        while (!statesToView.isEmpty()) {
            State cur = statesToView.remove();

            Set<Transition> transitions = nfa.getTransitions(cur);
            transitions.forEach(t -> {
                if (t.isEpsilonTransition()) {
                    result.add(t.right);

                    if (!completedStates.contains(t.right)) {
                        statesToView.add(t.right);
                    }
                }
            });
            completedStates.add(cur);
        }

        return result;
    }


    /**
     * @param s A set of states to calculate the epsilon closure for.
     * @return The epsilon closure of a given set of states.
     */
    private Set<State> epsilonClosure(Set<State> s) {
        Set<State> result = new HashSet<>();
        s.forEach(cur -> {
            result.addAll(epsilonClosure(cur));
        });

        return result;
    }

    /**
     * @param s      A state of the automaton.
     * @param symbol A symbol from the alphabet.
     * @return A set of states that can be reached from the given state with the given symbol.
     */
    private Set<State> move(State s, Symbol symbol) {
        Set<State> result = new HashSet<>();
        Set<State> start = epsilonClosure(s);
        start.forEach(cur -> {
            nfa.getTransitions(cur)
                .stream()
                .filter(t -> !t.isEpsilonTransition() && t.symbol.get().equals(symbol))
                .map(t -> epsilonClosure(t.right))
                .forEach(set -> result.addAll(set));
        });

        return result;
    }

    /**
     * @param s      A set of states of the automaton.
     * @param symbol A symbol from the alphabet.
     * @return A set of states that can be reached from any of the given states with the given symbol.
     */
    private Set<State> move(Set<State> s, Symbol symbol) {
        Set<State> result = new HashSet<>();
        s.forEach(cur -> result.addAll(move(cur, symbol)));
        return result;
    }

    /**
     * Apply the powerset construction to the automaton supplied in the constructor of this class.
     * @return The equivalent deterministic finite automaton (DFA).
     */
    private FiniteAutomaton construct() {
        Queue<Set<State>> supersetsToView = new LinkedList<>();
        Set<Set<State>> completedSupersets = new HashSet<>();
        Set<Transition> transitions = new HashSet<>();

        Set<State> initialState = epsilonClosure(nfa.initialState());
        supersetsToView.add(initialState);

        while (!supersetsToView.isEmpty()) {
            Set<State> cur = supersetsToView.remove();

            nfa.alphabet().forEach(symbol -> {
                Set<State> ends = epsilonClosure(move(cur, symbol));

                Transition newTransition = Transition.newSymbolTransition(setToState(cur), setToState(ends), symbol);
                if (transitions.stream().noneMatch(transition -> transition.left.equals(newTransition.left) && transition.right.equals(newTransition.right) && transition.symbol.equals(newTransition.symbol))) {
                    transitions.add(newTransition);
                }

                if (completedSupersets.stream().noneMatch(superset -> superset.equals(ends))) {
                    supersetsToView.add(ends);
                }
            });

            completedSupersets.add(cur);
        }

        Set<State> states = completedSupersets.stream().map(superset -> setToState(superset)).collect(Collectors.toSet());
        Set<State> originalFinalStates = this.nfa.finalStates();
        Set<State> finalStates = completedSupersets.stream().filter(superset -> !Collections.disjoint(originalFinalStates, superset)).map(superset -> setToState(superset)).collect(Collectors.toSet());

        return new FiniteAutomaton(states, nfa.alphabet(), setToState(initialState), finalStates, transitions);
    }

    private State setToState(Set<State> set) {
        return new State("{" + set.stream().map(state -> state.name()).collect(Collectors.joining()) + "}");
    }
}
