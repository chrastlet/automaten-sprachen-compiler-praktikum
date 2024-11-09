package de.thm.asc.automata.parsing;

import de.thm.asc.automata.*;
import de.thm.asc.automata.parsing.Lexer.*;

import java.util.HashSet;
import java.util.Set;

class Parser {

    private final Lexer lexer;
    private Token currentToken;
    private final Set<State> states = new HashSet<>();
    private final Set<Symbol> symbols = new HashSet<>();
    private State initialState;
    private final Set<State> finalStates = new HashSet<>();
    private final Set<Transition> transitions = new HashSet<>();

    protected Parser(Lexer lexer) {
        this.lexer = lexer;

        nextToken();
    }

    public FiniteAutomaton parse() {
        while (currentToken.type() != Lexer.TokenType.EOF) {
            parseTransition();
        }

        if (initialState == null) {
            error("Missing initial state");
        }

        if (finalStates.isEmpty()) {
            error("Missing final state");
        }

        return new FiniteAutomaton(states, symbols, initialState, finalStates, transitions);
    }

    private void error(String message) {
        System.err.printf("Error: %s%n", message);
        System.exit(1);
    }

    private void nextToken() {
        currentToken = lexer.nextToken();
    }

    private void expectToken(Lexer.TokenType expected) {
        if (currentToken.type() != expected) {
            error(String.format("Unexpected token `%s`, expected `%s`.", currentToken.type(), expected));
        }

        nextToken();
    }

    private void expectNewLine() {
        if (currentToken.type() == TokenType.NEWLINE) {
            expectToken(TokenType.NEWLINE);
        } else {
            expectToken(TokenType.EOF);
        }
        while (currentToken.type() == TokenType.NEWLINE) {
            nextToken();
        }
    }

    private void parseTransition() {
        var left = parseState();
        expectToken(TokenType.ARROW);
        var right = parseState();
        expectToken(TokenType.COLON);

        String condition;
        if (currentToken.type() == TokenType.LITERAL) {
            condition = currentToken.literal();
            if (condition.length() > 1 && !condition.equals("ε")) {
                error("Transition condition longer than one character.");
            }
            expectToken(TokenType.LITERAL);
        } else {
            condition = "ε";
        }
        expectNewLine();

        if (condition.equals("ε")) {
            transitions.add(Transition.newEpsilonTransition(left, right));
        } else {
            var symbol = new Symbol(condition.charAt(0));
            symbols.add(symbol);
            transitions.add(Transition.newSymbolTransition(left, right, symbol));
        }
    }

    private State parseState() {
        if (currentToken.type() == TokenType.L_BRACK) {
            expectToken(TokenType.L_BRACK);
            var name = currentToken.literal();
            expectToken(TokenType.LITERAL);
            expectToken(TokenType.R_BRACK);

            if (initialState != null && !initialState.equals(new State(name))) {
                error("Duplicate start state.");
            }

            initialState = new State(name);
            states.add(initialState);

            return initialState;
        } else if (currentToken.type() == TokenType.L_PAREN) {
            expectToken(TokenType.L_PAREN);
            var name = currentToken.literal();
            expectToken(TokenType.LITERAL);
            expectToken(TokenType.R_PAREN);

            var s = new State(name);
            states.add(s);
            finalStates.add(s);

            return s;
        } else {
            var name = currentToken.literal();
            expectToken(TokenType.LITERAL);

            var s = new State(name);
            states.add(s);

            return s;
        }
    }
}
