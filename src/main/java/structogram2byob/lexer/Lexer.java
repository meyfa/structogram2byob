package structogram2byob.lexer;

import java.util.regex.Pattern;


/**
 * Performs lexical analysis on a given input, separating that input into
 * parseable tokens.
 */
public class Lexer
{
    private static final Pattern NEWLINE = Pattern.compile("\r\n|\r|\n");
    private static final Pattern PATTERN_NUMBER = Pattern
            .compile("^[+-]?\\d+(?:\\.\\d+)?$");

    private String input;
    private int index = 0, pastLines = 0, lineStart = 0;

    /**
     * Constructs a new Lexer from the given input.
     *
     * @param input The input string.
     */
    public Lexer(String input)
    {
        this(input, 0, 0);
    }

    /**
     * Constructs a new Lexer from the given input.
     *
     * @param input The input string.
     * @param pastLines The number of lines to offset tokens by.
     * @param lineStart The number of characters to offset tokens by.
     */
    public Lexer(String input, int pastLines, int lineStart)
    {
        this.input = NEWLINE.matcher(input).replaceAll("\n");

        this.pastLines = pastLines;
        this.lineStart = lineStart;
    }

    /**
     * @return Whether there is a token remaining.
     */
    public boolean hasNext()
    {
        consumeWhitespace();

        return index < input.length();
    }

    /**
     * Obtains the next token starting at the current index in the input string.
     *
     * @return The next token, after whitespace is removed.
     * @throws LexerException If there is no token or if it is malformed.
     */
    public Token next() throws LexerException
    {
        if (!hasNext()) {
            throw new LexerException("no more tokens");
        }

        Token t = peekToken();
        if (t == null) {
            throw new LexerException("string not tokenizable");
        }

        index += t.getValue().length();

        return t;
    }

    /**
     * Tries to obtain the next token without modifying the instance's state.
     *
     * @return The next token, or null if it could not be identified.
     * @throws LexerException If a token could be identified but is malformed.
     */
    private Token peekToken() throws LexerException
    {
        char c = input.charAt(index);

        if (c == '(') {
            return new Token(TokenType.PAREN_OPEN, Character.toString(c),
                    pastLines, index - lineStart);
        } else if (c == ')') {
            return new Token(TokenType.PAREN_CLOSE, Character.toString(c),
                    pastLines, index - lineStart);
        } else if (c == '"') {

            // find string end index
            int end = input.indexOf('"', index + 1);
            if (end < 0) {
                throw new LexerException("string is unending");
            }

            return new Token(TokenType.STRING, input.substring(index, end + 1),
                    pastLines, index - lineStart);

        } else if (('0' <= c && '9' >= c) || c == '-' || c == '+') {

            // collect all characters potentially part of the number
            StringBuilder sb = new StringBuilder();
            for (int i = index; i < input.length(); ++i) {
                c = input.charAt(i);
                if (i != index && (c < '0' || '9' < c) && c != '.') {
                    break;
                }
                sb.append(c);
            }

            String s = sb.toString();
            // sign only is a label
            if (s.length() == 1 && (s.charAt(0) == '-' || s.charAt(0) == '+')) {
                return new Token(TokenType.LABEL, s, pastLines,
                        index - lineStart);
            }
            // validate string to be numeric
            if (!PATTERN_NUMBER.matcher(s).matches()) {
                throw new LexerException("illegal number format for " + s);
            }

            return new Token(TokenType.NUMBER, s, pastLines, index - lineStart);

        }

        // nothing else matched, so this is a label

        // collect all characters until next token start or whitespace
        StringBuilder sb = new StringBuilder();
        for (int i = index; i < input.length(); ++i) {
            c = input.charAt(i);
            if (c == '(' || c == ')' || c == '"' || Character.isWhitespace(c)) {
                break;
            }
            sb.append(c);
        }

        return new Token(TokenType.LABEL, sb.toString(), pastLines,
                index - lineStart);
    }

    /**
     * Seeks forward in the input string until a non-whitespace character is
     * hit. Returns immediately if the current character is already a match.
     */
    private void consumeWhitespace()
    {
        if (index >= input.length()) {
            return;
        }

        char c;
        while (Character.isWhitespace(c = input.charAt(index))) {
            if (c == '\n') {
                pastLines++;
                lineStart = index + 1;
            }
            index++;
            if (index >= input.length()) {
                return;
            }
        }
    }
}
