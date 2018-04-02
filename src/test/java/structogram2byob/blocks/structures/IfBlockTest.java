package structogram2byob.blocks.structures;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.FunctionBlock;
import structogram2byob.program.VariableContext;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.ScriptExpression;


public class IfBlockTest
{
    @Test
    public void convertsToScratch()
    {
        IfBlock obj = IfBlock.instance;

        BlockDescription trueDesc = new BlockDescription.Builder().label("true")
                .build();

        List<Expression> params = Arrays.asList(
                new BlockExpression(trueDesc, Arrays.asList()),
                new ScriptExpression(Arrays.asList()));
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();
        blocks.register(
                new FunctionBlock(trueDesc, ScratchType.BOOLEAN, "getTrue"));

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        assertEquals(3, scratch.size());

        // starts with method call
        assertEquals("doIf", ((ScratchObjectSymbol) scratch.get(0)).getValue());

        // continues with condition
        assertThat(scratch.get(1), instanceOf(ScratchObjectArray.class));
        assertNotEquals(0, ((ScratchObjectArray) scratch.get(1)).size());

        // ends with script
        assertThat(scratch.get(2), instanceOf(ScratchObjectArray.class));
        assertEquals(0, ((ScratchObjectArray) scratch.get(2)).size());
    }
}
