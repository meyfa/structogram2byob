package structogram2byob.program.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nsdlib.elements.NSDElement;
import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;


/**
 * Expression type for an array of consecutive blocks.
 */
public class ScriptExpression extends Expression
{
    private final List<BlockExpression> blocks;

    /**
     * Constructs a new expression with the given blocks.
     *
     * @param element The element this expression stems from.
     * @param blocks The blocks contained in this script.
     */
    public ScriptExpression(NSDElement element,
            Collection<? extends BlockExpression> blocks)
    {
        super(element);

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
            BlockRegistry blocks) throws ScratchConversionException
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
