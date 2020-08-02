package structogram2byob.blocks;

import java.util.List;
import java.util.Map;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import structogram2byob.ScratchType;
import structogram2byob.blocks.structures.IfElseBlock;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;


/**
 * Base class for all blocks, be it standard {@link FunctionBlock}s or more
 * complex ones, such as {@link IfElseBlock}.
 */
public abstract class Block
{
    private final BlockDescription description;
    private final ScratchType returnValue;

    /**
     * Constructs a new block with the given description and return type.
     *
     * @param desc The block description.
     * @param returnType The type of value the block returns. May be null.
     */
    public Block(BlockDescription desc, ScratchType returnType)
    {
        this.description = desc;
        this.returnValue = returnType;
    }

    /**
     * @return This block's description.
     */
    public BlockDescription getDescription()
    {
        return description;
    }

    /**
     * @return This block's return type, or null if nothing is returned.
     */
    public ScratchType getReturnType()
    {
        return returnValue;
    }

    /**
     * Serializes this block into a Scratch array, as required by the Scratch
     * specification.
     *
     * @param params The parameters to substitute into this block.
     * @param vars A map of variable names to {@link VariableContext}s.
     * @param blocks The available blocks, including all custom blocks.
     * @return An array containing the serialized block data.
     *
     * @throws ScratchConversionException When the conversion fails.
     */
    public abstract ScratchObjectArray toScratch(List<Expression> params,
            Map<String, VariableContext> vars, BlockRegistry blocks)
            throws ScratchConversionException;

    /**
     * Utility function for converting an expression to a variable name.
     *
     * @param exp The expression to convert to a variable name.
     * @return The variable name.
     *
     * @throws IllegalArgumentException If the given expression is not a {@link BlockDescription},
     *          or doesn't have exactly one label part and no other parts.
     */
    protected static String asVariableName(Expression exp)
    {
        if (!(exp instanceof BlockExpression)) {
            throw new IllegalArgumentException("parameter not a variable name");
        }

        BlockExpression be = (BlockExpression) exp;
        BlockDescription desc = be.getDescription();

        if (desc.countParts() != 1 || desc.isParameter(0)) {
            throw new IllegalArgumentException("malformed variable name");
        }

        return desc.getLabel(0);
    }
}
