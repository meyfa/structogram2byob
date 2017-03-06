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
     * @throws BlockDescriptionParserException If a malformed block description
     *             string is encountered.
     * @throws IllegalArgumentException If an unknown return type is
     *             encountered.
     */
    public BlockRegistry read() throws BlockDescriptionParserException
    {
        BlockRegistry reg = new BlockRegistry();

        int i = 0;
        while (scanner.hasNextLine()) {

            String descLine = scanner.nextLine().trim();
            if (descLine.isEmpty() || descLine.charAt(0) == '#') {
                i++;
                continue;
            }

            String returnLine = scanner.nextLine();
            String methodLine = scanner.nextLine();

            reg.register(parseFunction(descLine, returnLine, methodLine, i));

            i += 3;

        }

        return reg;
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
     * @throws BlockDescriptionParserException If a malformed block description
     *             string is encountered.
     * @throws IllegalArgumentException If an unknown return type is
     *             encountered.
     */
    private FunctionBlock parseFunction(String d, String ret, String met, int l)
            throws BlockDescriptionParserException
    {
        BlockDescription desc = new BlockDescriptionParser(d, false, l, 0)
                .parse();

        ScratchType retType = ret.equalsIgnoreCase("none") ? null
                : ScratchType.valueOf(ret.toUpperCase());

        return new FunctionBlock(desc, retType, met);
    }

    @Override
    public void close() throws IOException
    {
        scanner.close();
    }
}
