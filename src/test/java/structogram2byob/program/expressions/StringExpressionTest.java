package structogram2byob.program.expressions;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.VariableContext;

import static org.junit.jupiter.api.Assertions.*;


public class StringExpressionTest
{
    @Test
    public void returnsValue()
    {
        StringExpression obj = new StringExpression(null, "hello world");

        assertEquals("hello world", obj.getValue());
    }

    @Test
    public void returnsCorrectType()
    {
        StringExpression obj = new StringExpression(null, "hello world");

        assertSame(ScratchType.TEXT, obj.getType());
    }

    @Test
    public void convertsToScratch()
    {
        StringExpression obj;
        ScratchObjectUtf8 result;

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        obj = new StringExpression(null, "hello world");
        result = (ScratchObjectUtf8) obj.toScratch(vars, blocks);
        assertEquals("hello world", result.getValue());
    }

    @Test
    public void convertsToString()
    {
        StringExpression obj;

        obj = new StringExpression(null, "hello world");
        assertEquals("\"hello world\"", obj.toString());
    }
}
