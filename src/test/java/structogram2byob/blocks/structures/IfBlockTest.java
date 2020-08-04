package structogram2byob.blocks.structures;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.FunctionBlock;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableMap;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.ScriptExpression;

import static org.junit.jupiter.api.Assertions.*;


public class IfBlockTest
{
    @Test
    public void convertsToScratch() throws ScratchConversionException
    {
        IfBlock obj = IfBlock.instance;

        BlockDescription trueDesc = new BlockDescription.Builder().label("true").build();

        List<Expression> params = Arrays.asList(
                new BlockExpression(null, trueDesc, Collections.emptyList()),
                new ScriptExpression(null, Collections.emptyList())
        );
        BlockRegistry blocks = new BlockRegistry();
        blocks.register(new FunctionBlock(trueDesc, ScratchType.BOOLEAN, "getTrue"));

        ScratchObjectArray scratch = obj.toScratch(params, VariableMap.EMPTY, blocks);

        assertEquals(3, scratch.size());

        // starts with method call
        assertEquals("doIf", ((ScratchObjectSymbol) scratch.get(0)).getValue());

        // continues with condition
        assertTrue(scratch.get(1) instanceof ScratchObjectArray);
        assertNotEquals(0, ((ScratchObjectArray) scratch.get(1)).size());

        // ends with script
        assertTrue(scratch.get(2) instanceof ScratchObjectArray);
        assertEquals(0, ((ScratchObjectArray) scratch.get(2)).size());
    }
}
