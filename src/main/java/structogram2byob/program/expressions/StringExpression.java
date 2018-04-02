package structogram2byob.program.expressions;

import java.util.Map;

import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.VariableContext;


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
    public ScratchObject toScratch(Map<String, VariableContext> vars,
            BlockRegistry blocks)
    {
        return new ScratchObjectUtf8(value);
    }

    @Override
    public String toString()
    {
        return "\"" + value + "\"";
    }
}
