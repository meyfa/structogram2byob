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

    private BlockDescription doParse()
            throws LexerException, BlockDescriptionParserException
    {
        BlockDescription.Builder builder = new BlockDescription.Builder();

        while (lexer.hasNext()) {

            Token t = lexer.next();

            if (t.getType() == TokenType.PAREN_OPEN) {

                Token valToken = lexer.next();
                if (valToken.getType() != TokenType.LABEL) {
                    throw new BlockDescriptionParserException("expected label");
                }

                Token endParenToken = lexer.next();
                if (endParenToken.getType() != TokenType.PAREN_CLOSE) {
                    throw new BlockDescriptionParserException(
                            "expected closing paren");
                }

                if (withLabels) {
                    builder.param(ScratchType.ANY, valToken.getValue());
                } else {
                    String paramType = valToken.getValue().toUpperCase();
                    builder.param(ScratchType.valueOf(paramType));
                }

            } else {

                builder.label(t.getValue());

            }

        }

        return builder.build();
    }
}
