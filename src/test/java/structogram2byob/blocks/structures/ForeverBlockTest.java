package structogram2byob.blocks.structures;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;
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
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        assertEquals(2, scratch.size());

        // starts with method call
        assertEquals("doForever", ((ScratchObjectSymbol) scratch.get(0)).getValue());

        // ends with script
        assertTrue(scratch.get(1) instanceof ScratchObjectArray);
    }
}
