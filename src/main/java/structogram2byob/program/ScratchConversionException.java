package structogram2byob.program;

import nsdlib.elements.NSDElement;


/**
 * Exception thrown when the program cannot be converted to Scratch format, for
 * example due to an unknown block.
 */
public class ScratchConversionException extends Exception
{
    private static final long serialVersionUID = -144865573277358621L;

    private final NSDElement element;

    /**
     * Constructs an empty exception.
     *
     * @param element The element that caused this exception.
     */
    public ScratchConversionException(NSDElement element)
    {
        this.element = element;
    }

    /**
     * Constructs an exception with the given detail message.
     *
     * @param element The element that caused this exception.
     * @param message The detail message.
     */
    public ScratchConversionException(NSDElement element, String message)
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
    public ScratchConversionException(NSDElement element, Throwable cause)
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
    public ScratchConversionException(NSDElement element, String message,
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
