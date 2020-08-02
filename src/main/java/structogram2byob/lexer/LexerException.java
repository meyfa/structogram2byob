package structogram2byob.lexer;

/**
 * Exception thrown when there is a problem during the lexical analysis phase.
 */
public class LexerException extends Exception
{
    private static final long serialVersionUID = -1424776903526308238L;

    /**
     * Constructs an empty exception.
     */
    public LexerException()
    {
    }

    /**
     * Constructs an exception with the given detail message.
     *
     * @param message The detail message.
     */
    public LexerException(String message)
    {
        super(message);
    }

    /**
     * Constructs an exception with the given throwable as its cause.
     *
     * @param cause The throwable that caused this exception to be thrown.
     */
    public LexerException(Throwable cause)
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
    public LexerException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
