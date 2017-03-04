package structogram2byob.parser;

/**
 * Exception thrown when there is a problem while constructing an AST from a
 * given input.
 */
public class AstParserException extends Exception
{
    private static final long serialVersionUID = 895616247524762328L;

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

    /**
     * Constructs an exception with the given detail message, the given cause, a
     * flag for whether suppression is enabled, and a flag for whether the stack
     * trace shall be writable.
     * 
     * @param message The detail message.
     * @param cause The throwable that caused this exception to be thrown.
     * @param enableSuppression Whether suppression shall be enabled.
     * @param writableStackTrace Whether the stack trace shall be writable.
     */
    public AstParserException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}