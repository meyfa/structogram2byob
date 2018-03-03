package structogram2byob.parser.expression;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.NumberExpression;
import structogram2byob.program.expressions.StringExpression;


public class ExpressionParserTest
{
    @Test
    public void parsesStringExpressions() throws ExpressionParserException
    {
        Expression result;

        result = new ExpressionParser("\"hello world\"").parse();
        assertThat(result, instanceOf(StringExpression.class));
        assertEquals("hello world", ((StringExpression) result).getValue());

        result = new ExpressionParser("\"\"").parse();
        assertThat(result, instanceOf(StringExpression.class));
        assertEquals("", ((StringExpression) result).getValue());
    }

    @Test
    public void parsesNumberExpressions() throws ExpressionParserException
    {
        Expression result;

        result = new ExpressionParser("5").parse();
        assertThat(result, instanceOf(NumberExpression.class));
        assertEquals(5, ((NumberExpression) result).getValue(), 0.0000001);

        result = new ExpressionParser("+0.5").parse();
        assertThat(result, instanceOf(NumberExpression.class));
        assertEquals(0.5, ((NumberExpression) result).getValue(), 0.0000001);

        result = new ExpressionParser("-42.37").parse();
        assertThat(result, instanceOf(NumberExpression.class));
        assertEquals(-42.37, ((NumberExpression) result).getValue(), 0.0000001);
    }

    @Test
    public void parsesBlockExpressions() throws ExpressionParserException
    {
        BlockExpression result;
        List<Expression> params;

        // simple

        result = (BlockExpression) new ExpressionParser("foo bar").parse();
        assertEquals(new BlockDescription.Builder().label("foo").label("bar")
                .build(), result.getDescription());
        params = result.getParameters();
        assertEquals(Arrays.asList(), params);

        // literal parameters

        result = (BlockExpression) new ExpressionParser("a (42) (\"test\") b")
                .parse();
        // description
        assertEquals(new BlockDescription.Builder().label("a")
                .param(ScratchType.NUMBER).param(ScratchType.TEXT).label("b")
                .build(), result.getDescription());
        // params
        params = result.getParameters();
        assertEquals(2, params.size());
        assertEquals(42, ((NumberExpression) params.get(0)).getValue(),
                0.0000001);
        assertEquals("test", ((StringExpression) params.get(1)).getValue());

        // literal parameters without parentheses

        result = (BlockExpression) new ExpressionParser("a 42 \"test\" b")
                .parse();
        // description
        assertEquals(new BlockDescription.Builder().label("a")
                .param(ScratchType.NUMBER).param(ScratchType.TEXT).label("b")
                .build(), result.getDescription());
        // params
        params = result.getParameters();
        assertEquals(2, params.size());
        assertEquals(42, ((NumberExpression) params.get(0)).getValue(),
                0.0000001);
        assertEquals("test", ((StringExpression) params.get(1)).getValue());

        // nested blocks

        result = (BlockExpression) new ExpressionParser("(bar (baz qu)) b (c)")
                .parse();
        // description
        assertEquals(
                new BlockDescription.Builder().param(ScratchType.ANY).label("b")
                        .param(ScratchType.ANY).build(),
                result.getDescription());
        // params
        params = result.getParameters();
        assertEquals(2, params.size());
        assertEquals(
                new BlockDescription.Builder().label("bar")
                        .param(ScratchType.ANY).build(),
                ((BlockExpression) params.get(0)).getDescription());
        assertEquals(new BlockDescription.Builder().label("c").build(),
                ((BlockExpression) params.get(1)).getDescription());
        // nested params
        params = ((BlockExpression) params.get(0)).getParameters();
        assertEquals(1, params.size());
        assertEquals(
                new BlockDescription.Builder().label("baz").label("qu").build(),
                ((BlockExpression) params.get(0)).getDescription());
    }

    @Test(expected = ExpressionParserException.class)
    public void throwsForEmptyParentheses() throws ExpressionParserException
    {
        new ExpressionParser("foo ()").parse();
    }

    @Test(expected = ExpressionParserException.class)
    public void throwsForSyntaxErrors() throws ExpressionParserException
    {
        new ExpressionParser("foo \"test").parse();
    }
}
