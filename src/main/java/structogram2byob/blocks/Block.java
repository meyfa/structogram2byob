package structogram2byob.blocks;

import java.util.List;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import structogram2byob.ScratchType;
import structogram2byob.program.expressions.Expression;


/**
 * Base class for all blocks.
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
     * @param blocks The available blocks, including all custom blocks.
     * @return An array containing the serialized block data.
     */
    public abstract ScratchObjectArray toScratch(List<Expression> params,
            BlockRegistry blocks);
}
