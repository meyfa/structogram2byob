package structogram2byob.parser.blockdescription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockDescription;


public class BlockDescriptionParserTest
{
    @Test
    public void parsesCorrectly() throws BlockDescriptionParserException
    {
        BlockDescription desc;

        desc = new BlockDescriptionParser("\n  foo  bar\n", false).parse();
        assertEquals(2, desc.countParts());
        // foo
        assertFalse(desc.isParameter(0));
        assertEquals("foo", desc.getLabel(0));
        // bar
        assertFalse(desc.isParameter(1));
        assertEquals("bar", desc.getLabel(1));

        desc = new BlockDescriptionParser("say (text) for (number) secs", false)
                .parse();
        assertEquals(5, desc.countParts());
        // say
        assertFalse(desc.isParameter(0));
        assertEquals("say", desc.getLabel(0));
        // (text)
        assertTrue(desc.isParameter(1));
        assertSame(ScratchType.TEXT, desc.getType(1));
        // for
        assertFalse(desc.isParameter(2));
        assertEquals("for", desc.getLabel(2));
        // (number)
        assertTrue(desc.isParameter(3));
        assertSame(ScratchType.NUMBER, desc.getType(3));
        // secs
        assertFalse(desc.isParameter(4));
        assertEquals("secs", desc.getLabel(4));

        desc = new BlockDescriptionParser("(number) mod (number)", false)
                .parse();
        assertEquals(3, desc.countParts());
        // (number)
        assertTrue(desc.isParameter(0));
        assertSame(ScratchType.NUMBER, desc.getType(0));
        // mod
        assertFalse(desc.isParameter(1));
        assertEquals("mod", desc.getLabel(1));
        // (number)
        assertTrue(desc.isParameter(2));
        assertSame(ScratchType.NUMBER, desc.getType(2));
    }

    @Test
    public void parsesCorrectlyWithLabels()
            throws BlockDescriptionParserException
    {
        BlockDescription desc;

        desc = new BlockDescriptionParser("say (message) for (time) secs", true)
                .parse();
        assertEquals(5, desc.countParts());
        // say
        assertFalse(desc.isParameter(0));
        assertEquals("say", desc.getLabel(0));
        // (message)
        assertTrue(desc.isParameter(1));
        assertEquals("message", desc.getLabel(1));
        assertSame(ScratchType.ANY, desc.getType(1));
        // say
        assertFalse(desc.isParameter(2));
        assertEquals("for", desc.getLabel(2));
        // (message)
        assertTrue(desc.isParameter(3));
        assertEquals("time", desc.getLabel(3));
        assertSame(ScratchType.ANY, desc.getType(3));
        // secs
        assertFalse(desc.isParameter(4));
        assertEquals("secs", desc.getLabel(4));
    }

    @Test(expected = BlockDescriptionParserException.class)
    public void throwsForMissingClosingParenthesis()
            throws BlockDescriptionParserException
    {
        new BlockDescriptionParser("foo (bar", false).parse();
    }

    @Test(expected = BlockDescriptionParserException.class)
    public void throwsForNestedParentheses()
            throws BlockDescriptionParserException
    {
        new BlockDescriptionParser("foo (bar (baz))", false).parse();
    }

    @Test(expected = BlockDescriptionParserException.class)
    public void throwsForUnknownTypes() throws BlockDescriptionParserException
    {
        new BlockDescriptionParser("foo (unknownType)", false).parse();
    }

    @Test
    public void throwsForLiterals() throws BlockDescriptionParserException
    {
        try {
            new BlockDescriptionParser("foo \"bar\"", false).parse();
            fail("not thrown for string literal");
        } catch (BlockDescriptionParserException e) {
        }

        try {
            new BlockDescriptionParser("foo (\"bar\")", false).parse();
            fail("not thrown for string literal in parentheses");
        } catch (BlockDescriptionParserException e) {
        }

        try {
            new BlockDescriptionParser("foo 42", false).parse();
            fail("not thrown for number literal");
        } catch (BlockDescriptionParserException e) {
        }

        try {
            new BlockDescriptionParser("foo (42)", false).parse();
            fail("not thrown for number literal in parentheses");
        } catch (BlockDescriptionParserException e) {
        }
    }
}
