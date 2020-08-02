package structogram2byob.parser.blockdescription;

/**
 * Exception thrown when there is a problem while parsing a block description.
 */
public class BlockDescriptionParserException extends Exception
{
    private static final long serialVersionUID = -5908512498090331997L;

    /**
     * Constructs an empty exception.
     */
    public BlockDescriptionParserException()
    {
    }

    /**
     * Constructs an exception with the given detail message.
     *
     * @param message The detail message.
     */
    public BlockDescriptionParserException(String message)
    {
        super(message);
    }

    /**
     * Constructs an exception with the given throwable as its cause.
     *
     * @param cause The throwable that caused this exception to be thrown.
     */
    public BlockDescriptionParserException(Throwable cause)
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
    public BlockDescriptionParserException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
