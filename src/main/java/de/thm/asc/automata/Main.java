package de.thm.asc.automata;

import de.thm.asc.automata.parsing.AutomatonParser;

import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            String fileName = args.length > 0 ? args[0] : "input.txt";

            FiniteAutomaton nfa = AutomatonParser.parseFile(fileName);

            FiniteAutomaton dfa = nfa.toDFA();

            var nfa_filename = fileName + ".nfa.dot";
            var dfa_filename = fileName + ".dfa.dot";

            var nfa_dot = nfa.toDotCode();
            var dfa_dot = dfa.toDotCode();

            System.out.println("Dot-code for nondeterministic automaton:");
            System.out.println(nfa_dot);

            System.out.println("Dot-code for deterministic automaton:");
            System.out.println(dfa_dot);

            try (
                    FileWriter fw1 = new FileWriter(nfa_filename);
                    FileWriter fw2 = new FileWriter(dfa_filename)
            ) {
                fw1.write(nfa_dot);
                fw2.write(dfa_dot);

                System.out.printf("Dot files printed to '%s' and '%s'.%n", nfa_filename, dfa_filename);
            }
        } catch (IOException|RuntimeException e) {
            e.printStackTrace();
        }
    }
}
