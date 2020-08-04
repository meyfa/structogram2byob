package structogram2byob.program.expressions;

import nsdlib.elements.NSDElement;
import scratchlib.objects.ScratchObject;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;
import structogram2byob.program.VariableMap;


/**
 * Base class for the different expression types there are (number and string
 * literals, as well as block invocations).
 */
public abstract class Expression
{
    private final NSDElement element;

    /**
     * @param element The element this expression stems from.
     */
    public Expression(NSDElement element)
    {
        this.element = element;
    }

    /**
     * @return The element this expression stems from.
     */
    public NSDElement getElement()
    {
        return element;
    }

    /**
     * @return The {@link ScratchType} that this expression is of.
     */
    public abstract ScratchType getType();

    /**
     * Converts this instance into a proper Scratch object, given a map of
     * variables and a block registry to distinguish ambiguous parts.
     *
     * @param vars A map of variable names to {@link VariableContext}s.
     * @param blocks The available blocks, including all custom blocks.
     * @return A {@link ScratchObject}.
     *
     * @throws ScratchConversionException When the conversion fails.
     */
    public abstract ScratchObject toScratch(VariableMap vars, BlockRegistry blocks) throws ScratchConversionException;

    @Override
    public abstract String toString();
}
