package structogram2byob.program.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
import structogram2byob.blocks.BlockRegistry;


/**
 * Expression type for an array of consecutive blocks.
 */
public class ScriptExpression extends Expression
{
    private final List<BlockExpression> blocks;

    /**
     * Constructs a new expression with the given blocks.
     *
     * @param blocks The blocks contained in this script.
     */
    public ScriptExpression(Collection<? extends BlockExpression> blocks)
    {
        this.blocks = Collections.unmodifiableList(new ArrayList<>(blocks));
    }

    /**
     * @return The number of blocks in this script.
     */
    public int size()
    {
        return blocks.size();
    }

    /**
     * @return The blocks contained in this script.
     */
    public List<BlockExpression> getBlocks()
    {
        return blocks;
    }

    @Override
    public ScratchType getType()
    {
        return ScratchType.LOOP;
    }

    @Override
    public ScratchObject toScratch(Map<String, VariableContext> vars,
            BlockRegistry blocks)
    {
        ScratchObjectArray a = new ScratchObjectArray();

        for (BlockExpression e : this.blocks) {
            a.add(e.toScratch(vars, blocks));
        }

        return a;
    }

    @Override
    public String toString()
    {
        return blocks.stream().map(BlockExpression::toString)
                .collect(Collectors.joining("; "));
    }
}
