package structogram2byob.parser;

/**
 * Exception thrown when there is a problem while constructing an AST from a
 * given input.
 */
public class AstParserException extends Exception
{
    private static final long serialVersionUID = -7716245521900225118L;

    /**
     * Constructs an empty exception.
     */
    public AstParserException()
    {
    }

    /**
     * Constructs an exception with the given detail message.
     *
     * @param message The detail message.
     */
    public AstParserException(String message)
    {
        super(message);
    }

    /**
     * Constructs an exception with the given throwable as its cause.
     *
     * @param cause The throwable that caused this exception to be thrown.
     */
    public AstParserException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructs an exception with the given detail message and the given
     * cause.
     *
     * @param message The detail message.
     * @param cause The throwable that caused this exception to be thrown.
     */
    public AstParserException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
