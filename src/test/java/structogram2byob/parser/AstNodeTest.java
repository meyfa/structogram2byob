package structogram2byob.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;
import structogram2byob.lexer.Token;
import structogram2byob.lexer.TokenType;

import static org.junit.jupiter.api.Assertions.*;


public class AstNodeTest
{
    @Test
    public void constructsRoot()
    {
        AstNode obj = new AstNode();

        assertFalse(obj.hasValue());
    }

    @Test
    public void constructsLeaf()
    {
        Token tok = new Token(TokenType.LABEL, "foo", 0, 0);
        AstNode obj = new AstNode(tok);

        assertTrue(obj.hasValue());
        assertSame(tok, obj.getValue());
    }

    @Test
    public void addsBranches()
    {
        AstNode obj = new AstNode();

        assertEquals(0, obj.countBranches());

        AstNode n0 = new AstNode();
        obj.add(n0);
        assertEquals(1, obj.countBranches());

        AstNode n1 = new AstNode();
        obj.add(n1);
        assertEquals(2, obj.countBranches());

        assertSame(n0, obj.getBranch(0));
        assertSame(n1, obj.getBranch(1));
    }

    @Test
    public void throwsWhenAddingToLeaf()
    {
        AstNode obj = new AstNode(new Token(TokenType.LABEL, "foo", 0, 0));

        assertThrows(Exception.class, () -> obj.add(new AstNode()));
    }

    @Test
    public void iteratesBranches()
    {
        AstNode obj = new AstNode();

        AstNode n0 = new AstNode();
        obj.add(n0);
        AstNode n1 = new AstNode();
        obj.add(n1);

        Iterator<AstNode> it = obj.iterator();
        assertSame(n0, it.next());
        assertSame(n1, it.next());
        assertFalse(it.hasNext());
    }
}
