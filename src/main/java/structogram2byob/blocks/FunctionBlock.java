package structogram2byob.blocks;

import java.util.List;
import java.util.Map;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
import structogram2byob.program.expressions.Expression;


/**
 * A function block is the standard extension of the block class, used for
 * everything except structural or highly specific blocks.
 * 
 * <p>
 * Blocks constructed this way have the usual description and return value, and
 * otherwise only consist of a method they call with some number of parameters.
 */
public class FunctionBlock extends Block
{
    private final String method;

    /**
     * Constructs a new function block with the given description, return type,
     * and Scratch method.
     * 
     * @param desc The block description.
     * @param returnType The type of value the block returns. May be null.
     * @param method The Scratch method this block calls.
     */
    public FunctionBlock(BlockDescription desc, ScratchType returnType,
            String method)
    {
        super(desc, returnType);
        this.method = method;
    }

    @Override
    public ScratchObjectArray toScratch(List<Expression> params,
            Map<String, VariableContext> vars, BlockRegistry blocks)
    {
        ScratchObjectArray a = new ScratchObjectArray();

        a.add(new ScratchObjectSymbol(method));

        for (Expression e : params) {
            a.add(e.toScratch(vars, blocks));
        }

        return a;
    }
}
