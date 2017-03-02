package structogram2byob.lexer;

/**
 * Smallest lexical unit.
 */
public class Token
{
    private final TokenType type;
    private final String value;

    /**
     * Constructs a new token instance for the given type and value.
     * 
     * @param type The token type.
     * @param value The raw token value, i.e. the lexeme.
     */
    public Token(TokenType type, String value)
    {
        this.type = type;
        this.value = value;
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

    @Override
    public String toString()
    {
        return type.name() + ":'" + value + "'";
    }
}
