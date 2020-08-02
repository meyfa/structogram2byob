package structogram2byob.program.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.FunctionBlock;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;

import static org.junit.jupiter.api.Assertions.*;


public class ScriptExpressionTest
{
    private static final BlockDescription DESC = new BlockDescription.Builder()
            .label("x").label("position").build();

    @Test
    public void returnsSize()
    {
        BlockExpression block = new BlockExpression(null, DESC, Collections.emptyList());
        ScriptExpression obj = new ScriptExpression(null, Arrays.asList(block, block, block));

        assertEquals(3, obj.size());
    }

    @Test
    public void returnsUnmodifiableBlocksList()
    {
        BlockExpression block = new BlockExpression(null, DESC, Collections.emptyList());
        List<BlockExpression> blocks = new ArrayList<>();
        blocks.add(block);

        ScriptExpression obj = new ScriptExpression(null, blocks);

        assertEquals(blocks, obj.getBlocks());

        assertThrows(UnsupportedOperationException.class, () -> obj.getBlocks().add(block));
    }

    @Test
    public void returnsCorrectType()
    {
        ScriptExpression obj = new ScriptExpression(null, Collections.emptyList());

        assertSame(ScratchType.LOOP, obj.getType());
    }

    @Test
    public void convertsToScratch() throws ScratchConversionException
    {
        ScriptExpression obj;
        ScratchObjectArray result;

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        obj = new ScriptExpression(null, Collections.emptyList());
        result = (ScratchObjectArray) obj.toScratch(vars, blocks);
        assertEquals(0, result.size());

        blocks.register(new FunctionBlock(DESC, ScratchType.NUMBER, "xpos"));

        obj = new ScriptExpression(null, Collections.singletonList(
                new BlockExpression(null, DESC, Collections.emptyList())));
        result = (ScratchObjectArray) obj.toScratch(vars, blocks);
        assertEquals(1, result.size());
    }

    @Test
    public void convertsToString()
    {
        ScriptExpression obj;

        obj = new ScriptExpression(null, Arrays.asList(
                new BlockExpression(null, new BlockDescription.Builder().label("foo").build(),
                        Collections.emptyList()),
                new BlockExpression(null, new BlockDescription.Builder().label("bar").build(),
                        Collections.emptyList()),
                new BlockExpression(null, new BlockDescription.Builder().label("baz").build(),
                        Collections.emptyList())
        ));
        assertEquals("(foo); (bar); (baz)", obj.toString());
    }
}
