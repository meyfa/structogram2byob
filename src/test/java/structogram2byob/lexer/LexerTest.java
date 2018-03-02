package structogram2byob.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class LexerTest
{
    @Test
    public void checksHasNext()
    {
        assertFalse(new Lexer("").hasNext());
        assertFalse(new Lexer(" \r\n\t ").hasNext());

        assertTrue(new Lexer("foo").hasNext());
        assertTrue(new Lexer("   foo").hasNext());
        assertTrue(new Lexer("\r\n\t   \"foo\"   ").hasNext());
        assertTrue(new Lexer("\r\n\t   42   ").hasNext());
    }

    private void assertToken(TokenType type, String value, Token actual)
    {
        assertSame(type, actual.getType());
        assertEquals(value, actual.getValue());
    }

    @Test
    public void tokenizesCorrectly() throws LexerException
    {
        Lexer obj;

        // labels

        obj = new Lexer("foo");
        assertToken(TokenType.LABEL, "foo", obj.next());
        assertFalse(obj.hasNext());

        obj = new Lexer("  foo  ");
        assertToken(TokenType.LABEL, "foo", obj.next());
        assertFalse(obj.hasNext());

        obj = new Lexer("-");
        assertToken(TokenType.LABEL, "-", obj.next());
        assertFalse(obj.hasNext());

        obj = new Lexer("foo bar baz");
        assertToken(TokenType.LABEL, "foo", obj.next());
        assertToken(TokenType.LABEL, "bar", obj.next());
        assertToken(TokenType.LABEL, "baz", obj.next());
        assertFalse(obj.hasNext());

        // parentheses

        obj = new Lexer("foo (bar) baz");
        assertToken(TokenType.LABEL, "foo", obj.next());
        assertToken(TokenType.PAREN_OPEN, "(", obj.next());
        assertToken(TokenType.LABEL, "bar", obj.next());
        assertToken(TokenType.PAREN_CLOSE, ")", obj.next());
        assertToken(TokenType.LABEL, "baz", obj.next());
        assertFalse(obj.hasNext());

        obj = new Lexer("(foo(bar(baz))qux)");
        assertToken(TokenType.PAREN_OPEN, "(", obj.next());
        assertToken(TokenType.LABEL, "foo", obj.next());
        assertToken(TokenType.PAREN_OPEN, "(", obj.next());
        assertToken(TokenType.LABEL, "bar", obj.next());
        assertToken(TokenType.PAREN_OPEN, "(", obj.next());
        assertToken(TokenType.LABEL, "baz", obj.next());
        assertToken(TokenType.PAREN_CLOSE, ")", obj.next());
        assertToken(TokenType.PAREN_CLOSE, ")", obj.next());
        assertToken(TokenType.LABEL, "qux", obj.next());
        assertToken(TokenType.PAREN_CLOSE, ")", obj.next());
        assertFalse(obj.hasNext());

        // strings

        obj = new Lexer("\"hello world\"");
        assertToken(TokenType.STRING, "\"hello world\"", obj.next());
        assertFalse(obj.hasNext());

        // numbers

        obj = new Lexer("-042.5");
        assertToken(TokenType.NUMBER, "-042.5", obj.next());
        assertFalse(obj.hasNext());

        // compound

        obj = new Lexer("+9 - -7");
        assertToken(TokenType.NUMBER, "+9", obj.next());
        assertToken(TokenType.LABEL, "-", obj.next());
        assertToken(TokenType.NUMBER, "-7", obj.next());
        assertFalse(obj.hasNext());

        obj = new Lexer("join (join \"hello \" \"world \")-42");
        assertToken(TokenType.LABEL, "join", obj.next());
        assertToken(TokenType.PAREN_OPEN, "(", obj.next());
        assertToken(TokenType.LABEL, "join", obj.next());
        assertToken(TokenType.STRING, "\"hello \"", obj.next());
        assertToken(TokenType.STRING, "\"world \"", obj.next());
        assertToken(TokenType.PAREN_CLOSE, ")", obj.next());
        assertToken(TokenType.NUMBER, "-42", obj.next());
        assertFalse(obj.hasNext());
    }

    @Test(expected = LexerException.class)
    public void throwsForInfiniteString() throws LexerException
    {
        new Lexer("\"hello ").next();
    }

    @Test(expected = LexerException.class)
    public void throwsForNoMoreTokens() throws LexerException
    {
        Lexer obj = new Lexer("foo ");
        obj.next();
        obj.next();
    }

    @Test
    public void setsTokenPosition() throws LexerException
    {
        Lexer obj;
        Token t;

        // changing columns

        obj = new Lexer("foo   bar");
        t = obj.next();
        assertEquals(0, t.getLine());
        assertEquals(0, t.getColumn());
        t = obj.next();
        assertEquals(0, t.getLine());
        assertEquals(6, t.getColumn());

        // changing lines

        obj = new Lexer("\nfoo  \n bar");
        t = obj.next();
        assertEquals(1, t.getLine());
        assertEquals(0, t.getColumn());
        t = obj.next();
        assertEquals(2, t.getLine());
        assertEquals(1, t.getColumn());

        // offsets

        obj = new Lexer("    foo bar", 2, 4);
        t = obj.next();
        assertEquals(2, t.getLine());
        assertEquals(0, t.getColumn());
        t = obj.next();
        assertEquals(2, t.getLine());
        assertEquals(4, t.getColumn());
    }
}
