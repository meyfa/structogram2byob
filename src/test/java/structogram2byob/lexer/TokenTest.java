package structogram2byob.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;


public class TokenTest
{
    @Test
    public void returnsProperties()
    {
        Token obj = new Token(TokenType.STRING, "\"foo\"", 42, 37);

        assertSame(TokenType.STRING, obj.getType());
        assertEquals("\"foo\"", obj.getValue());
        assertEquals(42, obj.getLine());
        assertEquals(37, obj.getColumn());
    }
}
