package structogram2byob.lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


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
