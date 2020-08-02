package structogram2byob.parser.expression;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.NumberExpression;
import structogram2byob.program.expressions.StringExpression;

import static org.junit.jupiter.api.Assertions.*;


public class ExpressionParserTest
{
    @Test
    public void parsesStringExpressions() throws ExpressionParserException
    {
        Expression result;

        result = new ExpressionParser(null, "\"hello world\"").parse();
        assertTrue(result instanceof StringExpression);
        assertEquals("hello world", ((StringExpression) result).getValue());

        result = new ExpressionParser(null, "\"\"").parse();
        assertTrue(result instanceof StringExpression);
        assertEquals("", ((StringExpression) result).getValue());
    }

    @Test
    public void parsesNumberExpressions() throws ExpressionParserException
    {
        Expression result;

        result = new ExpressionParser(null, "5").parse();
        assertTrue(result instanceof NumberExpression);
        assertEquals(5, ((NumberExpression) result).getValue(), 0.0000001);

        result = new ExpressionParser(null, "+0.5").parse();
        assertTrue(result instanceof NumberExpression);
        assertEquals(0.5, ((NumberExpression) result).getValue(), 0.0000001);

        result = new ExpressionParser(null, "-42.37").parse();
        assertTrue(result instanceof NumberExpression);
        assertEquals(-42.37, ((NumberExpression) result).getValue(), 0.0000001);
    }

    @Test
    public void parsesBlockExpressions() throws ExpressionParserException
    {
        BlockExpression result;
        List<Expression> params;

        // simple

        result = (BlockExpression) new ExpressionParser(null, "foo bar").parse();
        assertEquals(new BlockDescription.Builder().label("foo").label("bar").build(), result.getDescription());
        params = result.getParameters();
        assertEquals(Collections.emptyList(), params);

        // literal parameters

        result = (BlockExpression) new ExpressionParser(null, "a (42) (\"test\") b").parse();
        // description
        assertEquals(new BlockDescription.Builder().label("a")
                .param(ScratchType.NUMBER).param(ScratchType.TEXT).label("b").build(), result.getDescription());
        // params
        params = result.getParameters();
        assertEquals(2, params.size());
        assertEquals(42, ((NumberExpression) params.get(0)).getValue(), 0.0000001);
        assertEquals("test", ((StringExpression) params.get(1)).getValue());

        // literal parameters without parentheses

        result = (BlockExpression) new ExpressionParser(null, "a 42 \"test\" b").parse();
        // description
        assertEquals(new BlockDescription.Builder().label("a")
                .param(ScratchType.NUMBER).param(ScratchType.TEXT).label("b").build(), result.getDescription());
        // params
        params = result.getParameters();
        assertEquals(2, params.size());
        assertEquals(42, ((NumberExpression) params.get(0)).getValue(), 0.0000001);
        assertEquals("test", ((StringExpression) params.get(1)).getValue());

        // nested blocks

        result = (BlockExpression) new ExpressionParser(null, "(bar (baz qu)) b (c)").parse();
        // description
        assertEquals(new BlockDescription.Builder().param(ScratchType.ANY).label("b").param(ScratchType.ANY).build(),
                result.getDescription());
        // params
        params = result.getParameters();
        assertEquals(2, params.size());
        assertEquals(new BlockDescription.Builder().label("bar").param(ScratchType.ANY).build(),
                ((BlockExpression) params.get(0)).getDescription());
        assertEquals(new BlockDescription.Builder().label("c").build(),
                ((BlockExpression) params.get(1)).getDescription());
        // nested params
        params = ((BlockExpression) params.get(0)).getParameters();
        assertEquals(1, params.size());
        assertEquals(new BlockDescription.Builder().label("baz").label("qu").build(),
                ((BlockExpression) params.get(0)).getDescription());
    }

    @Test
    public void throwsForEmptyParentheses()
    {
        ExpressionParser obj = new ExpressionParser(null, "foo ()");
        assertThrows(ExpressionParserException.class, obj::parse);
    }

    @Test
    public void throwsForSyntaxErrors()
    {
        ExpressionParser obj = new ExpressionParser(null, "foo \"test");
        assertThrows(ExpressionParserException.class, obj::parse);
    }
}
