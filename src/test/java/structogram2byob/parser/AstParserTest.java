package structogram2byob.parser;

import org.junit.jupiter.api.Test;
import structogram2byob.lexer.Token;
import structogram2byob.lexer.TokenType;

import static org.junit.jupiter.api.Assertions.*;


public class AstParserTest
{
    private void assertToken(TokenType type, String value, Token actual)
    {
        assertSame(type, actual.getType());
        assertEquals(value, actual.getValue());
    }

    @Test
    public void parsesCorrectly() throws AstParserException
    {
        AstNode node;

        // ([LABEL:'foo'])
        node = new AstParser("foo").parse();
        assertEquals(1, node.countBranches());
        assertToken(TokenType.LABEL, "foo", node.getBranch(0).getValue());

        // ([LABEL:'foo'], [STRING:'"bar"'], [NUMBER:'42'])
        node = new AstParser("foo \"bar\" 42").parse();
        assertEquals(3, node.countBranches());
        assertToken(TokenType.LABEL, "foo", node.getBranch(0).getValue());
        assertToken(TokenType.STRING, "\"bar\"", node.getBranch(1).getValue());
        assertToken(TokenType.NUMBER, "42", node.getBranch(2).getValue());

        // (([LABEL:'a'], ([LABEL:'b']), [LABEL:'c']))
        node = new AstParser("(a(b)c)").parse();
        assertEquals(1, node.countBranches());
        assertEquals(3, node.getBranch(0).countBranches());
        assertToken(TokenType.LABEL, "a",
                node.getBranch(0).getBranch(0).getValue());
        assertEquals(1, node.getBranch(0).getBranch(1).countBranches());
        assertToken(TokenType.LABEL, "b",
                node.getBranch(0).getBranch(1).getBranch(0).getValue());
        assertToken(TokenType.LABEL, "c",
                node.getBranch(0).getBranch(2).getValue());

        // (([LABEL:'a'], ([STRING:'"b"']), [LABEL:'c']))
        node = new AstParser("(a(\"b\")c)").parse();
        assertEquals(1, node.countBranches());
        assertEquals(3, node.getBranch(0).countBranches());
        assertToken(TokenType.LABEL, "a",
                node.getBranch(0).getBranch(0).getValue());
        assertEquals(1, node.getBranch(0).getBranch(1).countBranches());
        assertToken(TokenType.STRING, "\"b\"",
                node.getBranch(0).getBranch(1).getBranch(0).getValue());
        assertToken(TokenType.LABEL, "c",
                node.getBranch(0).getBranch(2).getValue());
    }

    @Test
    public void throwsForInfiniteBraces()
    {
        AstParser obj = new AstParser("foo (bar ");
        assertThrows(AstParserException.class, obj::parse);
    }

    @Test
    public void throwsForUnmatchedClosingBraces()
    {
        AstParser obj = new AstParser("foo bar)");
        assertThrows(AstParserException.class, obj::parse);
    }

    @Test
    public void throwsForInfiniteStrings()
    {
        AstParser obj = new AstParser("foo \"bar ");
        assertThrows(AstParserException.class, obj::parse);
    }

    @Test
    public void returnsEmptyAstForEmptyInput() throws AstParserException
    {
        AstNode node = new AstParser("  ").parse();

        assertFalse(node.hasValue());
        assertEquals(0, node.countBranches());
    }
}
