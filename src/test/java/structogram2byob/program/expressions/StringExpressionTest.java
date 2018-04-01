package structogram2byob.program.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
import structogram2byob.blocks.BlockRegistry;


public class StringExpressionTest
{
    @Test
    public void returnsValue()
    {
        StringExpression obj = new StringExpression("hello world");

        assertEquals("hello world", obj.getValue());
    }

    @Test
    public void returnsCorrectType()
    {
        StringExpression obj = new StringExpression("hello world");

        assertSame(ScratchType.TEXT, obj.getType());
    }

    @Test
    public void convertsToScratch()
    {
        StringExpression obj;
        ScratchObjectUtf8 result;

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        obj = new StringExpression("hello world");
        result = (ScratchObjectUtf8) obj.toScratch(vars, blocks);
        assertEquals("hello world", result.getValue());
    }

    @Test
    public void convertsToString()
    {
        StringExpression obj;

        obj = new StringExpression("hello world");
        assertEquals("\"hello world\"", obj.toString());
    }
}
