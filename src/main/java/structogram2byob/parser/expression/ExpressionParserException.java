package structogram2byob.parser.expression;

/**
 * Exception thrown when there is a problem while parsing an expression.
 */
public class ExpressionParserException extends Exception
{
    private static final long serialVersionUID = -6891859762746828683L;

    /**
     * Constructs an empty exception.
     */
    public ExpressionParserException()
    {
    }

    /**
     * Constructs an exception with the given detail message.
     *
     * @param message The detail message.
     */
    public ExpressionParserException(String message)
    {
        super(message);
    }

    /**
     * Constructs an exception with the given throwable as its cause.
     *
     * @param cause The throwable that caused this exception to be thrown.
     */
    public ExpressionParserException(Throwable cause)
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
    public ExpressionParserException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
