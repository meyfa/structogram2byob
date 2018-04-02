package structogram2byob.blocks.structures;

import java.util.List;
import java.util.Map;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.ScratchType;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;
import structogram2byob.program.expressions.Expression;


/**
 * This is the "repeat (number)" C-shape block.
 */
public class RepeatBlock extends Block
{
    private static final BlockDescription description = new BlockDescription.Builder()
            .label("repeat").param(ScratchType.NUMBER).param(ScratchType.LOOP)
            .build();

    /**
     * The singleton instance.
     */
    public static final RepeatBlock instance = new RepeatBlock();

    private RepeatBlock()
    {
        super(description, null);
    }

    @Override
    public ScratchObjectArray toScratch(List<Expression> params,
            Map<String, VariableContext> vars, BlockRegistry blocks)
            throws ScratchConversionException
    {
        ScratchObjectArray a = new ScratchObjectArray();

        a.add(new ScratchObjectSymbol("doRepeat"));

        // condition
        a.add(params.get(0).toScratch(vars, blocks));
        // blocks
        a.add(params.get(1).toScratch(vars, blocks));

        return a;
    }
}
