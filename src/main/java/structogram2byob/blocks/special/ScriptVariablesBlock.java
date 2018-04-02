package structogram2byob.blocks.special;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import scratchlib.objects.user.ScratchObjectVariableFrame;
import structogram2byob.ScratchType;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.VariableContext;
import structogram2byob.program.expressions.Expression;


/**
 * This is the "script variables (any...)" block.
 */
public class ScriptVariablesBlock extends Block
{
    private static final BlockDescription description = new BlockDescription.Builder()
            .label("script").label("variables").paramList(ScratchType.ANY)
            .build();

    /**
     * The singleton instance.
     */
    public static final ScriptVariablesBlock instance = new ScriptVariablesBlock();

    private ScriptVariablesBlock()
    {
        super(description, null);
    }

    @Override
    public ScratchObjectArray toScratch(List<Expression> params,
            Map<String, VariableContext> vars, BlockRegistry blocks)
    {
        ScratchObjectArray a = new ScratchObjectArray();

        a.add(new ScratchObjectSymbol("byob"));
        a.add(new ScratchObjectString(""));
        a.add(new ScratchObjectSymbol("doDeclareVariables"));

        for (Expression param : params) {

            String name = asVariableName(param);

            ScratchObjectArray pa = new ScratchObjectArray();

            pa.add(new ScratchObjectSymbol("byob"));
            pa.add(new ScratchObjectString(""));
            pa.add(new ScratchObjectSymbol("readBlockVariable"));
            pa.add(new ScratchObjectUtf8(name));
            pa.add(new ScratchObjectVariableFrame());

            a.add(pa);

        }

        return a;
    }

    /**
     * Obtains every variable's {@link ScratchObjectVariableFrame} instance from
     * an array serialized by {@link #toScratch(List, Map, BlockRegistry)}.
     * Having these instances is required for variable context construction.
     *
     * @param serialized The serialized block array.
     * @return A map of variable names to their frames.
     */
    public static Map<String, ScratchObjectVariableFrame> retrieveFrames(
            ScratchObjectArray serialized)
    {
        Map<String, ScratchObjectVariableFrame> frames = new HashMap<>();

        for (int i = 3; i < serialized.size(); ++i) {
            ScratchObjectArray va = (ScratchObjectArray) serialized.get(i);
            frames.put(((ScratchObjectUtf8) va.get(3)).getValue(),
                    (ScratchObjectVariableFrame) va.get(4));
        }

        return frames;
    }
}
