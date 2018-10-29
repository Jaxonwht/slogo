package engine.compiler;

import engine.compiler.lexer.CrudeLexer;
import engine.compiler.lexer.Lexer;
import engine.errors.CommandSyntaxException;
import engine.compiler.parser.CrudeParser;
import engine.compiler.parser.Parser;
import engine.compiler.slogoast.Expression;
import engine.errors.UndefinedKeywordException;

import java.util.List;
import java.util.regex.Pattern;

/**
 * This class handles the preliminary testing of Lexer, Parser and Interpreter.
 *
 * @author Haotian Wang
 */
public class Lexer_Parser_Interpreter_Tester {
    /**
     * A main method to test the functionality.
     *
     * @param args
     */
    public static void main(String[] args) {
        Lexer lexer = new CrudeLexer();
//        String test = "dotimes [:d 4] [fd sin 50 back 5 6]";
        String test = "# sfsf\n" +
                "fd 10";
        try {
            lexer.readString(test);
        } catch (UndefinedKeywordException e) {
            System.out.println(e.getMessage());
            return;
        }
        List<Token> testSet = lexer.getTokens();
        System.out.println("The input String is:\n\n" + test + "\n");
        System.out.println("Lexer's Part\n======\nThe list of tokens is:\n");
        for (Token token : testSet) {
            System.out.println(token.toString());
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
    }
}
