package structogram2byob.program.expressions;

import scratchlib.objects.ScratchObject;
import structogram2byob.ScratchType;


/**
 * Base class for the different expression types there are.
 */
public abstract class Expression
{
    /**
     * @return The {@link ScratchType} that this expression is of.
     */
    public abstract ScratchType getType();

    /**
     * Converts this instance into a proper Scratch object.
     * 
     * @return A {@link ScratchObject}.
     */
    public abstract ScratchObject toScratch();

    @Override
    public abstract String toString();
}
