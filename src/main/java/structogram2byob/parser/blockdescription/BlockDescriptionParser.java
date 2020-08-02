package structogram2byob.parser.blockdescription;

import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.lexer.Lexer;
import structogram2byob.lexer.LexerException;
import structogram2byob.lexer.Token;
import structogram2byob.lexer.TokenType;


/**
 * Parser for constructing a {@link BlockDescription} from a given input.
 * Optionally parses parts in parens as parameter labels instead of parameter
 * types.
 */
public class BlockDescriptionParser
{
    private final Lexer lexer;
    private final boolean withLabels;

    /**
     * Constructs a new block description parser from the given input,
     * optionally treating parts in parens as parameter labels as opposed to
     * parameter types.
     *
     * @param s The input string.
     * @param withLabels Whether to treat parts in parens as param labels.
     */
    public BlockDescriptionParser(String s, boolean withLabels)
    {
        this.lexer = new Lexer(s);
        this.withLabels = withLabels;
    }

    /**
     * Constructs a new block description parser from the given input,
     * optionally treating parts in parens as parameter labels as opposed to
     * parameter types.
     *
     * @param s The input string.
     * @param withLabels Whether to treat parts in parens as param labels.
     * @param pastLines The number of lines that came before this description.
     * @param lineStart The number of chars that came before this description.
     */
    public BlockDescriptionParser(String s, boolean withLabels, int pastLines, int lineStart)
    {
        this.lexer = new Lexer(s, pastLines, lineStart);
        this.withLabels = withLabels;
    }

    /**
     * Parses the input as a {@link BlockDescription}.
     *
     * @return The block description that was parsed.
     *
     * @throws BlockDescriptionParserException If the input is malformed and
     *             cannot be parsed as a block description.
     */
    public BlockDescription parse() throws BlockDescriptionParserException
    {
        try {
            return doParse();
        } catch (LexerException e) {
            throw new BlockDescriptionParserException(e);
        }
    }

    private BlockDescription doParse() throws LexerException, BlockDescriptionParserException
    {
        BlockDescription.Builder builder = new BlockDescription.Builder();

        while (lexer.hasNext()) {
            Token t = next(null);

            if (t.getType() == TokenType.PAREN_OPEN) {

                Token valToken = next(TokenType.LABEL);
                next(TokenType.PAREN_CLOSE);

                if (withLabels) {
                    builder.param(ScratchType.ANY, valToken.getValue());
                } else {
                    builder.param(parseScratchType(valToken));
                }

            } else {
                checkTokenType(t, TokenType.LABEL);
                builder.label(t.getValue());
            }
        }

        return builder.build();
    }

    private Token next(TokenType expected) throws BlockDescriptionParserException, LexerException
    {
        Token t = lexer.next();
        if (expected != null) {
            checkTokenType(t, expected);
        }
        return t;
    }

    private void checkTokenType(Token t, TokenType expected) throws BlockDescriptionParserException
    {
        if (t.getType() != expected) {
            String expName = expected.name().toLowerCase();
            String actName = t.getType().name().toLowerCase();

            String msg = String.format("expected %s but got %s at %d:%d",
                    expName, actName, t.getLine(), t.getColumn());

            throw new BlockDescriptionParserException(msg);
        }
    }

    private ScratchType parseScratchType(Token t) throws BlockDescriptionParserException
    {
        String name = t.getValue().toUpperCase();

        ScratchType type;
        try {
            type = ScratchType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new BlockDescriptionParserException(e);
        }

        return type;
    }
}
