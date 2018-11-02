package engine.compiler.parser;

import engine.compiler.utils.Token;
import engine.errors.CommandSyntaxException;
import engine.compiler.slogoast.Expression;

import java.util.List;

/**
 * Take in the String input that the user types into the editor window.
 * Output an abstract syntax tree corresponding to the String command.
 * Throw CommandSyntaxException.
 * Recognize various languages.
 * (maybe) Suggest the closest command before throwing a CommandSyntaxException.
 *
 * @author Haotian Wang
 */
public interface Parser {
    /**
     * Reads in the queue of Tokens from the Lexer into the internal logic of the parser.
     *
     * @param tokens: A queue of Tokens read from the Lexer.
     */
    void readTokens(List<Token> tokens) throws CommandSyntaxException;

    /**
     * Clear the stored tokens in the internal parser.
     */
    void clearTokens();

    /**
     * @return An SLogoAST for the interpreter to process.
     */
    Expression returnAST();
}
