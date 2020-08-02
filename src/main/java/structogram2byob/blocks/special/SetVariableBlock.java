package structogram2byob.blocks.special;

import java.util.List;
import java.util.Map;

import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import structogram2byob.ScratchType;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;
import structogram2byob.program.VariableContext.ScriptSpecific;
import structogram2byob.program.expressions.Expression;


/**
 * This is the "set (variable) to (any)" block.
 */
public class SetVariableBlock extends Block
{
    private static final BlockDescription description = new BlockDescription.Builder()
            .label("set").param(ScratchType.ANY).label("to").param(ScratchType.ANY).build();

    /**
     * The singleton instance.
     */
    public static final SetVariableBlock instance = new SetVariableBlock();

    private SetVariableBlock()
    {
        super(description, null);
    }

    @Override
    public ScratchObjectArray toScratch(List<Expression> params,
            Map<String, VariableContext> vars, BlockRegistry blocks) throws ScratchConversionException
    {
        String name = asVariableName(params.get(0));
        VariableContext context = vars.get(name);

        ScratchObjectArray a = new ScratchObjectArray();

        if (context == VariableContext.GLOBAL) {
            a.add(new ScratchObjectSymbol("changeVariable"));
        } else {
            a.add(new ScratchObjectSymbol("changeBlockVariable"));
        }

        a.add(new ScratchObjectUtf8(name));
        a.add(new ScratchObjectSymbol("setVar:to:"));

        if (context instanceof VariableContext.ScriptSpecific) {
            a.add(((ScriptSpecific) context).getFrame());
        } else if (context instanceof VariableContext.UnitSpecific) {
            a.add(ScratchObject.NIL);
        }

        a.add(params.get(1).toScratch(vars, blocks));

        return a;
    }
}
