package structogram2byob.program.expressions;

import scratchlib.objects.ScratchObject;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockRegistry;


/**
 * Base class for the different expression types there are (number and string
 * literals, as well as block invocations).
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
     * @param blocks The available blocks, including all custom blocks.
     * @return A {@link ScratchObject}.
     */
    public abstract ScratchObject toScratch(BlockRegistry blocks);

    @Override
    public abstract String toString();
}
