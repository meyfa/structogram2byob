package structogram2byob.program.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import scratchlib.objects.ScratchObject;
import structogram2byob.ScratchType;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;


/**
 * Expression type for block invocations.
 */
public class BlockExpression extends Expression
{
    private final BlockDescription description;
    private final List<Expression> parameters;

    /**
     * Constructs a new expression with the given description, and the given
     * parameters to substitute into the description.
     * 
     * @param description The block description.
     * @param params The block parameters.
     */
    public BlockExpression(BlockDescription description,
            Collection<? extends Expression> params)
    {
        this.description = description;
        this.parameters = Collections.unmodifiableList(new ArrayList<>(params));
    }

    /**
     * @return The underlying block description.
     */
    public BlockDescription getDescription()
    {
        return description;
    }

    /**
     * @return The parameters the block is called with.
     */
    public List<Expression> getParameters()
    {
        return parameters;
    }

    @Override
    public ScratchType getType()
    {
        return ScratchType.ANY;
    }

    @Override
    public ScratchObject toScratch(BlockRegistry blocks)
    {
        Block b = blocks.lookup(description);
        if (b == null) {
            throw new IllegalArgumentException("unknown block: " + description);
        }

        return b.toScratch(parameters, blocks);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append('(');
        for (int i = 0, n = description.countParts(), pi = 0; i < n; ++i) {
            if (i > 0) {
                sb.append(' ');
            }
            if (description.isParameter(i)) {
                sb.append(parameters.get(pi++));
            } else {
                sb.append(description.getLabel(i));
            }
        }
        sb.append(')');

        return sb.toString();
    }
}
