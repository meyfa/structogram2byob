package structogram2byob.program.expressions;

import scratchlib.objects.ScratchObject;
import scratchlib.objects.inline.ScratchObjectFloat;
import scratchlib.objects.inline.ScratchObjectSmallInteger;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockRegistry;


/**
 * Expression type for numeric literals.
 */
public class NumberExpression extends Expression
{
    private final double value;

    /**
     * Constructs a new numeric expression with the given value.
     * 
     * @param value The expression value.
     */
    public NumberExpression(double value)
    {
        this.value = value;
    }

    /**
     * @return This expression's value.
     */
    public double getValue()
    {
        return value;
    }

    @Override
    public ScratchType getType()
    {
        return ScratchType.NUMBER;
    }

    @Override
    public ScratchObject toScratch(BlockRegistry blocks)
    {
        if (value == Math.rint(value)) {
            return new ScratchObjectSmallInteger((int) Math.rint(value));
        }
        return new ScratchObjectFloat(value);
    }

    @Override
    public String toString()
    {
        if (value == Math.rint(value)) {
            return Integer.toString((int) Math.rint(value));
        }
        return Double.toString(value);
    }
}
