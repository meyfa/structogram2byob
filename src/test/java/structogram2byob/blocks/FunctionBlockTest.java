package structogram2byob.blocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectAbstractString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.inline.ScratchObjectAbstractNumber;
import structogram2byob.ScratchType;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.NumberExpression;
import structogram2byob.program.expressions.StringExpression;

import static org.junit.jupiter.api.Assertions.*;


public class FunctionBlockTest
{
    @Test
    public void convertsToScratch() throws ScratchConversionException
    {
        FunctionBlock obj = new FunctionBlock(
                new BlockDescription.Builder().label("foo")
                        .param(ScratchType.ANY, "param1")
                        .param(ScratchType.ANY, "param2").build(),
                ScratchType.BOOLEAN, "do:something:");

        List<Expression> params = Arrays.asList(
                new NumberExpression(null, 42),
                new StringExpression(null, "hello")
        );
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        assertEquals(3, scratch.size());

        // starts with method
        assertTrue(scratch.get(0) instanceof ScratchObjectSymbol);
        assertEquals("do:something:", ((ScratchObjectSymbol) scratch.get(0)).getValue());

        // follows with params
        assertTrue(scratch.get(1) instanceof ScratchObjectAbstractNumber);
        assertTrue(scratch.get(2) instanceof ScratchObjectAbstractString);
    }
}
