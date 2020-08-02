package structogram2byob.lexer;

/**
 * Describes the type that some token, the smallest lexical unit, is.
 */
public enum TokenType
{
    /**
     * Opening parenthesis, (.
     */
    PAREN_OPEN,

    /**
     * Closing parenthesis, ).
     */
    PAREN_CLOSE,

    /**
     * Anything that is neither a parenthesis nor a literal, and so qualifies as
     * a block's label.
     */
    LABEL,

    /**
     * A textual literal wrapped in quotes, e.g. "Hello world!".
     */
    STRING,

    /**
     * A numeric literal with an optional decimal point, e.g. 12 or 3.1415.
     */
    NUMBER
}
