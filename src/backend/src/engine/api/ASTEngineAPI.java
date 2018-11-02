package engine.api;

import engine.compiler.utils.Token;
import engine.compiler.lexer.CrudeLexer;
import engine.compiler.lexer.Lexer;
import engine.compiler.parser.CrudeParser;
import engine.compiler.parser.Parser;
import engine.compiler.slogoast.Expression;
import engine.compiler.storage.StateMachine;
import engine.compiler.storage.StateMachineV2;
import engine.errors.CommandSyntaxException;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

import java.util.List;

/**
 * This Engine API implementation uses the AST lexer, parser, interpreter model to execute String input.
 *
 * @author Haotian Wang
 */
public class ASTEngineAPI implements EngineAPI {
    private Lexer lexer;
    private Parser parser;
    private StateMachine stateMachine;
    private TurtleManager manager;

    public ASTEngineAPI(TurtleManager turtleManager) {
        lexer = new CrudeLexer();
        parser = new CrudeParser();
        manager = turtleManager;
        stateMachine = new StateMachineV2();
        manager.equipMemory(stateMachine);
    }

    @Override
    public double processString(String str) throws UndefinedKeywordException, CommandSyntaxException, InterpretationException {
        lexer.readString(str);
        List<Token> listOfTokens = lexer.getTokens();
        parser.readTokens(listOfTokens);
        Expression command = parser.returnAST();
        return command.interpret(manager);
    }

    @Override
    public void setLanguage(String language) { lexer.setLanguage(language); }

    @Override
    public StateMachine stateMachine() { return stateMachine; }
}
