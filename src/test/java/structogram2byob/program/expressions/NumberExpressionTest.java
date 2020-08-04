package structogram2byob.program.expressions;

import org.junit.jupiter.api.Test;
import scratchlib.objects.inline.ScratchObjectAbstractNumber;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.VariableMap;

import static org.junit.jupiter.api.Assertions.*;


public class NumberExpressionTest
{
    @Test
    public void returnsValue()
    {
        NumberExpression obj = new NumberExpression(null, 42.5);

        assertEquals(42.5, obj.getValue(), 0);
    }

    @Test
    public void returnsCorrectType()
    {
        NumberExpression obj = new NumberExpression(null, 42.5);

        assertSame(ScratchType.NUMBER, obj.getType());
    }

    @Test
    public void convertsToScratch()
    {
        NumberExpression obj;
        ScratchObjectAbstractNumber result;

        BlockRegistry blocks = new BlockRegistry();

        obj = new NumberExpression(null, 42.5);
        result = (ScratchObjectAbstractNumber) obj.toScratch(VariableMap.EMPTY, blocks);
        assertEquals(42.5, result.doubleValue(), 0);

        obj = new NumberExpression(null, -42);
        result = (ScratchObjectAbstractNumber) obj.toScratch(VariableMap.EMPTY, blocks);
        assertEquals(-42, result.doubleValue(), 0);
    }

    @Test
    public void convertsToString()
    {
        NumberExpression obj;

        obj = new NumberExpression(null, 42);
        assertEquals("42", obj.toString());

        obj = new NumberExpression(null, -42);
        assertEquals("-42", obj.toString());

        obj = new NumberExpression(null, 42.125);
        assertEquals("42.125", obj.toString());

        obj = new NumberExpression(null, -42.125);
        assertEquals("-42.125", obj.toString());
    }
}
