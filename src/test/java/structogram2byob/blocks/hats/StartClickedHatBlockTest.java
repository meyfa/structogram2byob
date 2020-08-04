package structogram2byob.blocks.hats;

import org.junit.jupiter.api.Test;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.VariableMap;
import structogram2byob.program.expressions.Expression;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class StartClickedHatBlockTest
{
    @Test
    public void convertsToScratch()
    {
        StartClickedHatBlock obj = StartClickedHatBlock.instance;

        List<Expression> params = Collections.emptyList();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, VariableMap.EMPTY, blocks);

        assertEquals(2, scratch.size());

        assertEquals("EventHatMorph", ((ScratchObjectSymbol) scratch.get(0)).getValue());
        assertEquals("Scratch-StartClicked", ((ScratchObjectString) scratch.get(1)).getValue());
    }
}
