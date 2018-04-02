package structogram2byob.blocks;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectAbstractString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.inline.ScratchObjectAbstractNumber;
import structogram2byob.ScratchType;
import structogram2byob.program.VariableContext;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.NumberExpression;
import structogram2byob.program.expressions.StringExpression;


public class FunctionBlockTest
{
    @Test
    public void convertsToScratch()
    {
        FunctionBlock obj = new FunctionBlock(
                new BlockDescription.Builder().label("foo")
                        .param(ScratchType.ANY, "param1")
                        .param(ScratchType.ANY, "param2").build(),
                ScratchType.BOOLEAN, "do:something:");

        List<Expression> params = Arrays.asList(new NumberExpression(42),
                new StringExpression("hello"));
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        assertEquals(3, scratch.size());

        // starts with method
        assertThat(scratch.get(0), instanceOf(ScratchObjectSymbol.class));
        assertEquals("do:something:",
                ((ScratchObjectSymbol) scratch.get(0)).getValue());

        // follows with params
        assertThat(scratch.get(1),
                instanceOf(ScratchObjectAbstractNumber.class));
        assertThat(scratch.get(2),
                instanceOf(ScratchObjectAbstractString.class));
    }
}
