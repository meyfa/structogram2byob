package structogram2byob.blocks.hats;

import java.util.List;
import java.util.Map;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.VariableContext;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.expressions.Expression;


/**
 * This is the "when start clicked" hat block.
 */
public class StartClickedHatBlock extends Block
{
    private static final BlockDescription description = new BlockDescription.Builder()
            .label("when").label("start").label("clicked").build();

    /**
     * The singleton instance.
     */
    public static final StartClickedHatBlock instance = new StartClickedHatBlock();

    private StartClickedHatBlock()
    {
        super(description, null);
    }

    @Override
    public ScratchObjectArray toScratch(List<Expression> params,
            Map<String, VariableContext> vars, BlockRegistry blocks)
    {
        ScratchObjectArray a = new ScratchObjectArray();

        a.add(new ScratchObjectSymbol("EventHatMorph"));
        a.add(new ScratchObjectString("Scratch-StartClicked"));

        return a;
    }
}
