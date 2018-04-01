package structogram2byob.program.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.inline.ScratchObjectAbstractNumber;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
import structogram2byob.blocks.BlockRegistry;


public class NumberExpressionTest
{
    @Test
    public void returnsValue()
    {
        NumberExpression obj = new NumberExpression(42.5);

        assertEquals(42.5, obj.getValue(), 0);
    }

    @Test
    public void returnsCorrectType()
    {
        NumberExpression obj = new NumberExpression(42.5);

        assertSame(ScratchType.NUMBER, obj.getType());
    }

    @Test
    public void convertsToScratch()
    {
        NumberExpression obj;
        ScratchObjectAbstractNumber result;

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        obj = new NumberExpression(42.5);
        result = (ScratchObjectAbstractNumber) obj.toScratch(vars, blocks);
        assertEquals(42.5, result.doubleValue(), 0);

        obj = new NumberExpression(-42);
        result = (ScratchObjectAbstractNumber) obj.toScratch(vars, blocks);
        assertEquals(-42, result.doubleValue(), 0);
    }

    @Test
    public void convertsToString()
    {
        NumberExpression obj;

        obj = new NumberExpression(42);
        assertEquals("42", obj.toString());

        obj = new NumberExpression(-42);
        assertEquals("-42", obj.toString());

        obj = new NumberExpression(42.125);
        assertEquals("42.125", obj.toString());

        obj = new NumberExpression(-42.125);
        assertEquals("-42.125", obj.toString());
    }
}
