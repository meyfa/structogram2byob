package structogram2byob.blocks.hats;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.VariableContext;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.expressions.Expression;


public class StartClickedHatBlockTest
{
    @Test
    public void convertsToScratch()
    {
        StartClickedHatBlock obj = StartClickedHatBlock.instance;

        List<Expression> params = Arrays.asList();
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        assertEquals(2, scratch.size());

        assertEquals("EventHatMorph",
                ((ScratchObjectSymbol) scratch.get(0)).getValue());
        assertEquals("Scratch-StartClicked",
                ((ScratchObjectString) scratch.get(1)).getValue());
    }
}
