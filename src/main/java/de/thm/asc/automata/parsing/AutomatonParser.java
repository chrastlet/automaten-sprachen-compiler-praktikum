package de.thm.asc.automata.parsing;

import de.thm.asc.automata.FiniteAutomaton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AutomatonParser {

    /**
     * Parses a string containing the description of an automaton and returns the generated automaton.
     *
     * @param s The automaton description to be parsed.
     * @return The generated automaton.
     */
    public static FiniteAutomaton parse(String s) {
        var lexer = new Lexer(s);

        var parser = new Parser(lexer);
        return parser.parse();
    }

    /**
     * Parses a the description of an automaton from a file and returns the generated automaton.
     *
     * @param path The path to the file containing the description.
     * @return The generated automaton.
     * @throws IOException Thrown when the given file cannot be accessed.
     */
    public static FiniteAutomaton parseFile(String path) throws IOException {
        var content = new String(Files.readAllBytes(Paths.get(path)));

        return parse(content);
    }
}
