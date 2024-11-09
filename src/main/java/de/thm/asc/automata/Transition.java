package de.thm.asc.automata;

import java.util.Optional;

/**
 * Represents a single transition of an automaton.
 */
public class Transition {
    public final State left;
    public final State right;
    public final Optional<Symbol> symbol;

    /**
     * Creates a new instance of a transition.
     *
     * @param left   The left side of the transition (the 'from' state).
     * @param right  The right side of the transition (the 'to' state).
     * @param symbol The symbol of this transition. Epsilon transitions are represented by an empty symbol.
     */
    private Transition(State left, State right, Symbol symbol) {
        this.left = left;
        this.right = right;
        this.symbol = Optional.of(symbol);
    }

    private Transition(State left, State right) {
        this.left = left;
        this.right = right;
        this.symbol = Optional.empty();
    }

    /**
     * Determines whether this transition is an epsilon transition.
     *
     * @return Whether this transition is an epsilon transition.
     */
    public boolean isEpsilonTransition() {
        return this.symbol.isEmpty();
    }

    /**
     * Returns the symbol associated with this transition.
     * Caution: This only works for non-epsilon transitions!
     *
     * @return The associated symbol.
     */
    public Symbol symbol() {
        if (this.symbol.isEmpty()) {
            throw new RuntimeException("symbol() called on an epsilon-transition.");
        }

        return this.symbol.get();
    }

    /**
     * Creates a new instance of an epsilon transition.
     *
     * @param left  The left side of the transition (the 'from' state).
     * @param right The right side of the transition (the 'to' state).
     * @return The instance of an epsilon transition.
     */
    public static Transition newEpsilonTransition(State left, State right) {
        return new Transition(left, right);
    }

    /**
     * Creates a new instance of a transition with a symbol.
     *
     * @param left   The left side of the transition (the 'from' state).
     * @param right  The right side of the transition (the 'to' state).
     * @param symbol The symbol of this transition. Epsilon transitions are represented by an empty symbol.
     * @return The instance of a symbol transition,
     */
    public static Transition newSymbolTransition(State left, State right, Symbol symbol) {
        return new Transition(left, right, symbol);
    }

    /**
     * Returns the symbol of this transition as a string or an epsilon symbol if the symbol is empty.
     *
     * @return The string representation of this transition's symbol.
     */
    public String symbolString() {
        return this.symbol.map(Symbol::toString).orElse("ε");
    }
}
