package structogram2byob.parser.nsd;

import nsdlib.elements.NSDElement;


/**
 * Exception thrown when there is a problem while parsing an NSD.
 */
public class NSDParserException extends Exception
{
    private static final long serialVersionUID = 895616247524762328L;

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
     * Constructs an exception with the given detail message, the given cause, a
     * flag for whether suppression is enabled, and a flag for whether the stack
     * trace shall be writable.
     * 
     * @param element The element that caused this exception.
     * @param message The detail message.
     * @param cause The throwable that caused this exception to be thrown.
     * @param enableSuppression Whether suppression shall be enabled.
     * @param writableStackTrace Whether the stack trace shall be writable.
     */
    public NSDParserException(NSDElement element, String message,
            Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
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
