package structogram2byob.program.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.FunctionBlock;


public class ScriptExpressionTest
{
    private static final BlockDescription DESC = new BlockDescription.Builder()
            .label("x").label("position").build();

    @Test
    public void returnsSize()
    {
        BlockExpression block = new BlockExpression(DESC, Arrays.asList());
        ScriptExpression obj = new ScriptExpression(
                Arrays.asList(block, block, block));

        assertEquals(3, obj.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void returnsUnmodifiableBlocksList()
    {
        BlockExpression block = new BlockExpression(DESC, Arrays.asList());
        List<BlockExpression> blocks = new ArrayList<>();
        blocks.add(block);

        ScriptExpression obj = new ScriptExpression(blocks);

        assertEquals(blocks, obj.getBlocks());

        // throws
        obj.getBlocks().add(block);
    }

    @Test
    public void returnsCorrectType()
    {
        ScriptExpression obj = new ScriptExpression(Arrays.asList());

        assertSame(ScratchType.LOOP, obj.getType());
    }

    @Test
    public void convertsToScratch()
    {
        ScriptExpression obj;
        ScratchObjectArray result;

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        obj = new ScriptExpression(Arrays.asList());
        result = (ScratchObjectArray) obj.toScratch(vars, blocks);
        assertEquals(0, result.size());

        blocks.register(new FunctionBlock(DESC, ScratchType.NUMBER, "xpos"));

        obj = new ScriptExpression(
                Arrays.asList(new BlockExpression(DESC, Arrays.asList())));
        result = (ScratchObjectArray) obj.toScratch(vars, blocks);
        assertEquals(1, result.size());
    }
}
