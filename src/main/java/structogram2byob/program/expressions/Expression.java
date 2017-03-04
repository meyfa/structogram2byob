package structogram2byob.program.expressions;

import java.util.Map;

import scratchlib.objects.ScratchObject;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
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
     * Converts this instance into a proper Scratch object, given a map of
     * variables and a block registry to distinguish ambiguous parts.
     * 
     * @param vars A map of variable names to {@link VariableContext}s.
     * @param blocks The available blocks, including all custom blocks.
     * @return A {@link ScratchObject}.
     */
    public abstract ScratchObject toScratch(Map<String, VariableContext> vars,
            BlockRegistry blocks);

    @Override
    public abstract String toString();
}
