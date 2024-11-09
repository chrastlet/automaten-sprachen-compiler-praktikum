package de.thm.asc.automata.parsing;

class Lexer {

    protected enum TokenType {
        L_BRACK,
        R_BRACK,
        L_PAREN,
        R_PAREN,
        ARROW,
        COLON,
        LITERAL,
        NEWLINE,
        EOF,
        ILLEGAL
    }

    protected record Token(TokenType type, String literal) {

        @Override
        public String toString() {
            return String.format("(Token %s %s)", type, literal);
        }
    }

    private static final char EOF = '\0';
    private final String code;
    private int position = 0;
    private char currentChar;

    protected Lexer(String code) {
        this.code = code;
    }

    private boolean isAlphanumeric(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private char readChar() {
        if (position < code.length()) {
            return code.charAt(position);
        } else {
            return EOF;
        }
    }

    private void nextChar() {
        currentChar = readChar();
        position++;
    }

    private String readLiteral() {
        var startPosition = position;
        while (isAlphanumeric(readChar())) {
            nextChar();
        }

        return code.substring(startPosition - 1, position);
    }

    private void eatWhitespace() {
        while (currentChar == ' ' || currentChar == '\t' || currentChar == '\r') {
            nextChar();
        }
    }

    protected void printTokens() {
        while (true) {
            var token = nextToken();
            System.out.println(token);
            if (token.type() == TokenType.EOF) {
                break;
            }
        }
    }

    protected Token nextToken() {
        nextChar();
        eatWhitespace();

        return switch (currentChar) {
            case ':' -> new Token(TokenType.COLON, ":");
            case '[' -> new Token(TokenType.L_BRACK, "[");
            case ']' -> new Token(TokenType.R_BRACK, "]");
            case '(' -> new Token(TokenType.L_PAREN, "(");
            case ')' -> new Token(TokenType.R_PAREN, ")");
            case '\n' -> new Token(TokenType.NEWLINE, "\n");
            case EOF -> new Token(TokenType.EOF, String.valueOf(EOF));
            case '-' -> {
                if (readChar() == '>') {
                    nextChar();
                    yield new Token(TokenType.ARROW, "->");
                } else {
                    yield new Token(TokenType.ILLEGAL, String.valueOf(currentChar));
                }
            }
            default -> {
                if (currentChar == 206 && readChar() == 181) { // Epsilon
                    nextChar();
                    yield new Token(TokenType.LITERAL, "ε");
                }

                if (isAlphanumeric(currentChar)) {
                    yield new Token(TokenType.LITERAL, readLiteral());
                }

                yield new Token(TokenType.ILLEGAL, String.valueOf(currentChar));
            }
        };
    }
}
