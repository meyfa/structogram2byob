package structogram2byob.parser;

import structogram2byob.lexer.Lexer;
import structogram2byob.lexer.LexerException;
import structogram2byob.lexer.Token;
import structogram2byob.lexer.TokenType;


/**
 * Parser for constructing an AST (Abstract Syntax Tree) from a given input.
 */
public class AstParser
{
    private final Lexer lexer;
    private Token current;

    /**
     * Constructs an AST (Abstract Syntax Tree) parser on the given input.
     *
     * @param input The input string.
     */
    public AstParser(String input)
    {
        this.lexer = new Lexer(input);
    }

    /**
     * Checks whether there is a current token, and whether that token is of the
     * given type. If so, consumes the token (advancing the pointer), otherwise
     * throws an exception.
     *
     * @param type The token type required for a match.
     * @return The matched token.
     *
     * @throws AstParserException If there are no more tokens or the token is
     *             not of the given type.
     */
    private Token match(TokenType type) throws AstParserException
    {
        if (current == null || current.getType() != type) {
            throw new AstParserException("unexpected token");
        }

        Token t = current;

        try {
            current = lexer.hasNext() ? lexer.next() : null;
        } catch (LexerException e) {
            throw new AstParserException(e);
        }

        return t;
    }

    /**
     * Iterates over the input's tokens and constructs an AST from them.
     *
     * @return The AST describing this parser's input.
     * @throws AstParserException
     */
    // corresponds to production S -> E
    public AstNode parse() throws AstParserException
    {
        AstNode ast = new AstNode();
        if (!lexer.hasNext()) {
            return ast;
        }

        try {
            current = lexer.next();
            parseExpression(ast);
        } catch (LexerException e) {
            throw new AstParserException(e);
        }

        if (current != null || lexer.hasNext()) {
            throw new AstParserException("parse incomplete");
        }

        return ast;
    }

    // corresponds to production E -> F+
    private void parseExpression(AstNode ast) throws AstParserException
    {
        do {
            parseOptionallyWrapped(ast);
        } while (current != null && current.getType() != TokenType.PAREN_CLOSE);
    }

    // corresponds to production F -> "(" E ")" | G
    private void parseOptionallyWrapped(AstNode ast) throws AstParserException
    {
        if (current == null) {
            throw new AstParserException("expected a token but got nothing");
        }

        if (current.getType() == TokenType.PAREN_OPEN) {

            match(TokenType.PAREN_OPEN);

            AstNode wrapped = new AstNode();
            parseExpression(wrapped);
            ast.add(wrapped);

            match(TokenType.PAREN_CLOSE);

        } else {
            parseTerminal(ast);
        }
    }

    // corresponds to production G -> label | number | string
    private void parseTerminal(AstNode ast) throws AstParserException
    {
        switch (current.getType()) {
            case LABEL:
            case NUMBER:
            case STRING:
                ast.add(new AstNode(match(current.getType())));
                return;
            default:
                break;
        }

        throw new AstParserException("unexpected token");
    }
}
