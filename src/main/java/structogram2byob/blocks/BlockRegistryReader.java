package structogram2byob.blocks;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import structogram2byob.ScratchType;
import structogram2byob.parser.blockdescription.BlockDescriptionParser;
import structogram2byob.parser.blockdescription.BlockDescriptionParserException;


/**
 * Allows reading a {@link BlockRegistry} from an {@code InputStream} or
 * {@code Scanner}.
 *
 * <p>
 * The file may consist of any number of sections, with any number of empty
 * lines and commented lines (starting with {@code #}) in between them.<br>
 * Every section consists of three consecutive lines:
 *
 * <ol>
 * <li>block description, see {@link BlockDescriptionParser};
 * <li>return type, one of {@link ScratchType} or {@code "none"};
 * <li>the block's Scratch method.
 * </ol>
 */
public class BlockRegistryReader implements Closeable
{
    private final Scanner scanner;
    private int lineNumber = -1;

    /**
     * Constructs a new reader from the given stream.
     *
     * @param in The input stream to read.
     */
    public BlockRegistryReader(InputStream in)
    {
        this(new Scanner(in));
    }

    /**
     * Constructs a new reader from the given scanner.
     *
     * @param scanner The scanner to read.
     */
    public BlockRegistryReader(Scanner scanner)
    {
        this.scanner = scanner;
    }

    /**
     * Reads the block registry from this reader's input and returns it.
     *
     * @return The block registry that was read.
     *
     * @throws BlockRegistryReaderException If the registry is malformed or contains invalid definitions.
     */
    public BlockRegistry read() throws BlockRegistryReaderException
    {
        BlockRegistry reg = new BlockRegistry();

        String descLine, returnLine, methodLine;
        while ((descLine = nextLine()) != null) {
            int i = lineNumber;

            returnLine = nextLine();
            methodLine = nextLine();

            if (returnLine == null || methodLine == null) {
                throw new BlockRegistryReaderException("block definition incomplete");
            }

            reg.register(parseFunction(descLine, returnLine, methodLine, i));
        }

        return reg;
    }

    /**
     * Returns the next line, skipping whitespace and comments. After this call,
     * {@code lineNumber} is set to the index of the returned line (0-based).
     *
     * @return The next line in the input.
     */
    private String nextLine()
    {
        String line;
        do {
            if (!scanner.hasNextLine()) {
                return null;
            }
            ++lineNumber;
            line = scanner.nextLine().trim();
        } while (line.isEmpty() || line.charAt(0) == '#');

        return line;
    }

    /**
     * Parses the three given strings into a function block and returns it.
     *
     * @param d The block description string (first line).
     * @param ret The block's return type (second line).
     * @param met The block's Scratch method (third line).
     * @param l The line index the description is on.
     * @return The constructed block.
     *
     * @throws BlockRegistryReaderException If a malformed block description string
     *          or an unknown return type is encountered.
     */
    private FunctionBlock parseFunction(String d, String ret, String met, int l) throws BlockRegistryReaderException
    {
        BlockDescription desc;
        try {
            desc = new BlockDescriptionParser(d, false, l, 0).parse();
        } catch (BlockDescriptionParserException e) {
            throw new BlockRegistryReaderException(String.format("malformed block description on line %d", l), e);
        }

        ScratchType retType;
        try {
            retType = ret.equalsIgnoreCase("none") ? null : ScratchType.valueOf(ret.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BlockRegistryReaderException(String.format("unknown return type on line %d", l), e);
        }

        return new FunctionBlock(desc, retType, met);
    }

    @Override
    public void close() throws IOException
    {
        scanner.close();
    }
}
