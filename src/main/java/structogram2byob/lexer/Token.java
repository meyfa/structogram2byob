package structogram2byob.lexer;

/**
 * Smallest lexical unit.
 */
public class Token
{
    private final TokenType type;
    private final String value;
    private final int line, column;

    /**
     * Constructs a new token instance for the given type and value.
     * 
     * @param type The token type.
     * @param value The raw token value, i.e. the lexeme.
     * @param line The 0-based index of the line this token was on.
     * @param column The 0-based index of the first character.
     */
    public Token(TokenType type, String value, int line, int column)
    {
        this.type = type;
        this.value = value;

        this.line = line;
        this.column = column;
    }

    /**
     * @return The type of this token.
     */
    public TokenType getType()
    {
        return type;
    }

    /**
     * @return This token's raw value, i.e. the lexeme.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @return The 0-based index of the line this token was on.
     */
    public int getLine()
    {
        return line;
    }

    /**
     * @return The 0-based index of the first character.
     */
    public int getColumn()
    {
        return column;
    }

    @Override
    public String toString()
    {
        return type.name() + ":'" + value + "'";
    }
}
