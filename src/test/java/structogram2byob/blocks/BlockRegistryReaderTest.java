package structogram2byob.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

import structogram2byob.ScratchType;


public class BlockRegistryReaderTest
{
    private static final BlockDescription DESC1 = new BlockDescription.Builder()
            .label("move").param(ScratchType.NUMBER).label("steps").build();

    private static final BlockDescription DESC2 = new BlockDescription.Builder()
            .label("x").label("position").build();

    @Test
    public void closesStream() throws IOException
    {
        final byte[] b = new byte[] { 0 };
        final int magic = 42;
        InputStream in = new ByteArrayInputStream(b) {
            @Override
            public void close()
            {
                this.buf[0] = magic;
            }
        };

        BlockRegistryReader obj = new BlockRegistryReader(in);

        assertFalse(b[0] == magic);

        obj.close();
        assertTrue(b[0] == magic);
    }

    @Test
    public void closesScanner() throws IOException
    {
        Scanner sc = new Scanner("1 2 3");

        BlockRegistryReader obj = new BlockRegistryReader(sc);

        // this would throw if it was already closed
        sc.nextInt();

        obj.close();
        try {
            sc.nextInt();
            fail("Scanner not closed");
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void readsFunctionBlocks()
            throws IOException, BlockRegistryReaderException
    {
        String s = "";
        s += "move (number) steps\n" + "none\n" + "forward:\n";
        s += "x position\n" + "number\n" + "xpos\n";

        BlockRegistry result;

        try (BlockRegistryReader obj = new BlockRegistryReader(
                new Scanner(s))) {
            result = obj.read();
        }

        FunctionBlock block1 = (FunctionBlock) result.lookup(DESC1);
        assertNull(block1.getReturnType());
        assertEquals("forward:", block1.getMethod());

        FunctionBlock block2 = (FunctionBlock) result.lookup(DESC2);
        assertSame(ScratchType.NUMBER, block2.getReturnType());
        assertEquals("xpos", block2.getMethod());
    }

    @Test
    public void ignoresWhitespace()
            throws IOException, BlockRegistryReaderException
    {
        String s = "\n\n\r\n";
        s += "  move (number) steps \n" + "\n" + " none\n" + "forward:\n\n";
        s += "x position\n" + "number\n" + "xpos";

        BlockRegistry result;

        try (BlockRegistryReader obj = new BlockRegistryReader(
                new Scanner(s))) {
            result = obj.read();
        }

        assertNotNull(result.lookup(DESC1));
        assertNotNull(result.lookup(DESC2));
    }

    @Test
    public void ignoresComments()
            throws IOException, BlockRegistryReaderException
    {
        String s = "  # comment\n";
        s += "move (number) steps\n" + "none\n" + "forward:\n";
        s += "#test\n";
        s += "x position\n" + "number\n" + "# in between\n" + "xpos\n";

        BlockRegistry result;

        try (BlockRegistryReader obj = new BlockRegistryReader(
                new Scanner(s))) {
            result = obj.read();
        }

        assertNotNull(result.lookup(DESC1));
        assertNotNull(result.lookup(DESC2));
    }

    @Test(expected = BlockRegistryReaderException.class)
    public void throwsForIncompleteDefinition()
            throws IOException, BlockRegistryReaderException
    {
        String s = "x position\n" + "number\n" + "  \n\n";

        try (BlockRegistryReader obj = new BlockRegistryReader(
                new Scanner(s))) {
            obj.read();
        }
    }

    @Test(expected = BlockRegistryReaderException.class)
    public void throwsForMalformedDescription()
            throws IOException, BlockRegistryReaderException
    {
        String s = "x \"position\n" + "number\n" + "xpos\n";

        try (BlockRegistryReader obj = new BlockRegistryReader(
                new Scanner(s))) {
            obj.read();
        }
    }

    @Test(expected = BlockRegistryReaderException.class)
    public void throwsForUnknownReturnType()
            throws IOException, BlockRegistryReaderException
    {
        String s = "x position\n" + "unknowntype\n" + "xpos\n";

        try (BlockRegistryReader obj = new BlockRegistryReader(
                new Scanner(s))) {
            obj.read();
        }
    }
}
