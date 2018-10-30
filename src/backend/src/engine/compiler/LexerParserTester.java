package engine.compiler;

import engine.compiler.lexer.CrudeLexer;
import engine.compiler.lexer.Lexer;
import engine.errors.CommandSyntaxException;
import engine.compiler.parser.CrudeParser;
import engine.compiler.parser.Parser;
import engine.compiler.slogoast.Expression;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.impl.TurtleManagerImpl;

import java.util.List;
import java.util.regex.Pattern;

/**
 * This class handles the preliminary testing of Lexer, Parser and Interpreter.
 *
 * @author Haotian Wang
 */
public class LexerParserTester {
    /**
     * A main method to test the functionality.
     *
     * @param args
     */
    public static void main(String[] args) {
        Lexer lexer = new CrudeLexer();
//        String test = "dotimes [:d 4] [fd sin 50 back 5 6]";
        String test = "set :count 12\n" +
                "\n" +
                "to dash [ ]\n" +
                "[\n" +
                "  repeat (:count) \n" +
                "  [\n" +
                "    pu fd 10 pd fd 10\n" +
                "  ]\n" +
                "]\n" +
                "\n";
        try {
            lexer.readString(test);
        } catch (UndefinedKeywordException e) {
            System.out.println(e.getMessage());
            return;
        }
        List<Token> testSet = lexer.getTokens();
        System.out.println("The input String is:\n\n" + test + "\n\n");
        System.out.println("Lexer's Part\n======\nThe list of tokens is:\n");
        for (Token token : testSet) {
            System.out.println(token.toString() + "\n");
        }

        System.out.println("\nParser's Part\n======");
        Parser parser = new CrudeParser();
        try {
            parser.readTokens(testSet);
        } catch (CommandSyntaxException e) {
            System.out.println(e.getMessage());
            return;
        }
        Expression result = parser.returnAST();
        System.out.println("The String representation of the syntax tree is:\n\n" + result.toString());
        System.out.println("\nA prettier presentation is:\n\n" + PrettierPresentation.prettify(result.toString()));
    }
}
