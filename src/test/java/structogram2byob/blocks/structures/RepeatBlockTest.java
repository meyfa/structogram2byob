package structogram2byob.blocks.structures;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.inline.ScratchObjectAbstractNumber;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.NumberExpression;
import structogram2byob.program.expressions.ScriptExpression;

import static org.junit.jupiter.api.Assertions.*;


public class RepeatBlockTest
{
    @Test
    public void convertsToScratch() throws ScratchConversionException
    {
        RepeatBlock obj = RepeatBlock.instance;

        List<Expression> params = Arrays.asList(
                new NumberExpression(null, 10),
                new ScriptExpression(null, Collections.emptyList())
        );
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        assertEquals(3, scratch.size());

        // starts with method call
        assertEquals("doRepeat", ((ScratchObjectSymbol) scratch.get(0)).getValue());

        // continues with condition
        assertEquals(10, ((ScratchObjectAbstractNumber) scratch.get(1)).intValue());

        // ends with script
        assertTrue(scratch.get(2) instanceof ScratchObjectArray);
    }
}
