package structogram2byob.blocks.structures;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableMap;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.ScriptExpression;

import static org.junit.jupiter.api.Assertions.*;


public class ForeverBlockTest
{
    @Test
    public void convertsToScratch() throws ScratchConversionException
    {
        ForeverBlock obj = ForeverBlock.instance;

        List<Expression> params = Collections.singletonList(new ScriptExpression(null, Collections.emptyList()));
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, VariableMap.EMPTY, blocks);

        assertEquals(2, scratch.size());

        // starts with method call
        assertEquals("doForever", ((ScratchObjectSymbol) scratch.get(0)).getValue());

        // ends with script
        assertTrue(scratch.get(1) instanceof ScratchObjectArray);
    }
}
