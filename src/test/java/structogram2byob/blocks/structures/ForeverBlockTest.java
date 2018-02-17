package structogram2byob.blocks.structures;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import structogram2byob.VariableContext;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.ScriptExpression;


public class ForeverBlockTest
{
    @Test
    public void convertsToScratch()
    {
        ForeverBlock obj = ForeverBlock.instance;

        List<Expression> params = Arrays
                .asList(new ScriptExpression(Arrays.asList()));
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        assertEquals(2, scratch.size());

        // starts with method call
        assertEquals("doForever",
                ((ScratchObjectSymbol) scratch.get(0)).getValue());

        // ends with script
        assertThat(scratch.get(1), instanceOf(ScratchObjectArray.class));
    }
}
