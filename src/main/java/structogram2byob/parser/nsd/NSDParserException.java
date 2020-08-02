package structogram2byob.parser.nsd;

import nsdlib.elements.NSDElement;


/**
 * Exception thrown when there is a problem while parsing an NSD.
 */
public class NSDParserException extends Exception
{
    private static final long serialVersionUID = 1906621534912929210L;

    private final NSDElement element;

    /**
     * Constructs an empty exception.
     *
     * @param element The element that caused this exception.
     */
    public NSDParserException(NSDElement element)
    {
        this.element = element;
    }

    /**
     * Constructs an exception with the given detail message.
     *
     * @param element The element that caused this exception.
     * @param message The detail message.
     */
    public NSDParserException(NSDElement element, String message)
    {
        super(message);
        this.element = element;
    }

    /**
     * Constructs an exception with the given throwable as its cause.
     *
     * @param element The element that caused this exception.
     * @param cause The throwable that caused this exception to be thrown.
     */
    public NSDParserException(NSDElement element, Throwable cause)
    {
        super(cause);
        this.element = element;
    }

    /**
     * Constructs an exception with the given detail message and the given
     * cause.
     *
     * @param element The element that caused this exception.
     * @param message The detail message.
     * @param cause The throwable that caused this exception to be thrown.
     */
    public NSDParserException(NSDElement element, String message,
            Throwable cause)
    {
        super(message, cause);
        this.element = element;
    }

    /**
     * @return The element that caused this exception, which might be null.
     */
    public NSDElement getElement()
    {
        return element;
    }
}
