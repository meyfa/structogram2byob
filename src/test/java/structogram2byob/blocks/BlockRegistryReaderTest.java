package structogram2byob.blocks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import structogram2byob.ScratchType;

import static org.junit.jupiter.api.Assertions.*;


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

        assertNotEquals(magic, b[0]);

        obj.close();
        assertEquals(magic, b[0]);
    }

    @Test
    public void closesScanner() throws IOException
    {
        Scanner sc = new Scanner("1 2 3");

        BlockRegistryReader obj = new BlockRegistryReader(sc);

        sc.nextInt();
        obj.close();

        assertThrows(IllegalStateException.class, sc::nextInt, "scanner should have been closed");
    }

    @Test
    public void readsFunctionBlocks() throws IOException, BlockRegistryReaderException
    {
        String s = "move (number) steps\nnone\nforward:\nx position\nnumber\nxpos\n";

        BlockRegistry result;
        try (BlockRegistryReader obj = new BlockRegistryReader(new Scanner(s))) {
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
    public void ignoresWhitespace() throws IOException, BlockRegistryReaderException
    {
        String s = "\n\n\r\n  move (number) steps \n\n none\nforward:\n\nx position\nnumber\nxpos";

        BlockRegistry result;
        try (BlockRegistryReader obj = new BlockRegistryReader(new Scanner(s))) {
            result = obj.read();
        }

        assertNotNull(result.lookup(DESC1));
        assertNotNull(result.lookup(DESC2));
    }

    @Test
    public void ignoresComments() throws IOException, BlockRegistryReaderException
    {
        String s = "  # comment\nmove (number) steps\nnone\nforward:\n#test\nx position\nnumber\n# in between\nxpos\n";

        BlockRegistry result;
        try (BlockRegistryReader obj = new BlockRegistryReader(new Scanner(s))) {
            result = obj.read();
        }

        assertNotNull(result.lookup(DESC1));
        assertNotNull(result.lookup(DESC2));
    }

    @Test
    public void throwsForIncompleteDefinition() throws IOException
    {
        String s = "x position\n" + "number\n" + "  \n\n";

        try (BlockRegistryReader obj = new BlockRegistryReader(new Scanner(s))) {
            assertThrows(BlockRegistryReaderException.class, obj::read);
        }
    }

    @Test
    public void throwsForMalformedDescription() throws IOException
    {
        String s = "x \"position\n" + "number\n" + "xpos\n";

        try (BlockRegistryReader obj = new BlockRegistryReader(new Scanner(s))) {
            assertThrows(BlockRegistryReaderException.class, obj::read);
        }
    }

    @Test
    public void throwsForUnknownReturnType() throws IOException
    {
        String s = "x position\n" + "unknowntype\n" + "xpos\n";

        try (BlockRegistryReader obj = new BlockRegistryReader(new Scanner(s))) {
            assertThrows(BlockRegistryReaderException.class, obj::read);
        }
    }
}
