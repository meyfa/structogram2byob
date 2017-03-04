package structogram2byob.program.expressions;

import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import structogram2byob.ScratchType;


/**
 * Expression type for textual (string) literals.
 */
public class StringExpression extends Expression
{
    private final String value;

    /**
     * Constructs a new string expression with the given value.
     * 
     * @param value The expression value.
     */
    public StringExpression(String value)
    {
        this.value = value;
    }

    /**
     * @return This expression's value.
     */
    public String getValue()
    {
        return value;
    }

    @Override
    public ScratchType getType()
    {
        return ScratchType.TEXT;
    }

    @Override
    public ScratchObject toScratch()
    {
        return new ScratchObjectUtf8(value);
    }

    @Override
    public String toString()
    {
        return "\"" + value + "\"";
    }
}
