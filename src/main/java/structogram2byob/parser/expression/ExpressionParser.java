package structogram2byob.parser.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import nsdlib.elements.NSDElement;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.lexer.Token;
import structogram2byob.lexer.TokenType;
import structogram2byob.parser.AstNode;
import structogram2byob.parser.AstParser;
import structogram2byob.parser.AstParserException;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.NumberExpression;
import structogram2byob.program.expressions.StringExpression;


/**
 * Parser for constructing an {@link Expression} from a given input string by
 * first building the AST ({@link AstParser}) and then traversing it.
 */
public class ExpressionParser
{
    private final NSDElement element;
    private final AstParser astParser;

    /**
     * Constructs an expression parser on the given input.
     *
     * @param element The element this expression stems from.
     * @param input The input string.
     */
    public ExpressionParser(NSDElement element, String input)
    {
        this.element = element;
        this.astParser = new AstParser(input);
    }

    /**
     * Parses the input as an {@link Expression}.
     *
     * @return The parsed expression.
     *
     * @throws ExpressionParserException If the input is malformed and cannot be
     *             parsed as an expression.
     */
    public Expression parse() throws ExpressionParserException
    {
        try {
            AstNode ast = astParser.parse();
            return parse(ast);
        } catch (AstParserException e) {
            throw new ExpressionParserException(e);
        }
    }

    /**
     * Parses the given AST as an expression. Nested expressions are de-nested
     * before they are parsed.
     *
     * @param ast The AST node to parse.
     * @return The parsed expression.
     *
     * @throws ExpressionParserException If the node is empty, an unexpected
     *             token is encountered, or a token is malformed.
     */
    private Expression parse(AstNode ast) throws ExpressionParserException
    {
        if (ast.hasValue()) {
            return parseSingle(ast);
        }

        if (ast.countBranches() == 0) {
            throw new ExpressionParserException("expression is empty");
        } else if (ast.countBranches() == 1) {
            return parse(ast.getBranch(0));
        }

        return parseComplex(IntStream.range(0, ast.countBranches())
                .mapToObj(i -> ast.getBranch(i)).collect(Collectors.toList()));
    }

    /**
     * Parses the given AST as an expression by taking its direct value and
     * converting it to the appropriate type.
     *
     * @param ast The AST node to parse.
     * @return The parsed expression.
     *
     * @throws ExpressionParserException If the node contains something other
     *             than a label, number or string.
     */
    private Expression parseSingle(AstNode ast) throws ExpressionParserException
    {
        Token t = ast.getValue();

        switch (t.getType()) {
            case LABEL:
                return parseComplex(Arrays.asList(ast));
            case NUMBER:
                return parseNumber(t.getValue());
            case STRING:
                return parseString(t.getValue());
            default:
                break;
        }

        throw new ExpressionParserException("unexpected token: " + t);
    }

    /**
     * Parses the given list of AST nodes as a complex (multi-part or block)
     * expression.
     *
     * @param nodes The AST nodes to parse.
     * @return The parsed expression.
     *
     * @throws ExpressionParserException If the node is empty, an unexpected
     *             token is encountered, or a token is malformed.
     */
    private Expression parseComplex(List<AstNode> nodes)
            throws ExpressionParserException
    {
        BlockDescription.Builder builder = new BlockDescription.Builder();
        List<Expression> params = new ArrayList<>();

        for (AstNode ast : nodes) {
            if (ast.hasValue() && ast.getValue().getType() == TokenType.LABEL) {
                builder.label(ast.getValue().getValue());
            } else {
                Expression subExp = parse(ast);
                builder.param(subExp.getType());
                params.add(subExp);
            }
        }

        return new BlockExpression(element, builder.build(), params);
    }

    /**
     * Converts the given string to a number expression.
     *
     * @param s The string to parse.
     * @return The parsed expression.
     *
     * @throws ExpressionParserException If the string is malformed.
     */
    private NumberExpression parseNumber(String s)
            throws ExpressionParserException
    {
        try {
            return new NumberExpression(element, Double.parseDouble(s));
        } catch (NumberFormatException e) {
            throw new ExpressionParserException(e);
        }
    }

    /**
     * Converts the given string to a string expression by removing the outer
     * quotes.
     *
     * @param s The string to parse.
     * @return The parsed expression.
     *
     * @throws ExpressionParserException If the string is malformed.
     */
    private StringExpression parseString(String s)
            throws ExpressionParserException
    {
        try {
            return new StringExpression(element,
                    s.substring(1, s.length() - 1));
        } catch (IndexOutOfBoundsException e) {
            throw new ExpressionParserException(e);
        }
    }
}
