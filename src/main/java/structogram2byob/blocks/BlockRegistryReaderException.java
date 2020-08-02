package structogram2byob.blocks;

/**
 * Exception thrown when there is a problem while reading a block registry.
 */
public class BlockRegistryReaderException extends Exception
{
    private static final long serialVersionUID = -3830754303402558922L;

    /**
     * Constructs an empty exception.
     */
    public BlockRegistryReaderException()
    {
    }

    /**
     * Constructs an exception with the given detail message.
     *
     * @param message The detail message.
     */
    public BlockRegistryReaderException(String message)
    {
        super(message);
    }

    /**
     * Constructs an exception with the given throwable as its cause.
     *
     * @param cause The throwable that caused this exception to be thrown.
     */
    public BlockRegistryReaderException(Throwable cause)
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
    public BlockRegistryReaderException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
