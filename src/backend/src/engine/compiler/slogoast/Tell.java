package engine.compiler.slogoast;

import engine.compiler.Token;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Tell grammar.
 *
 * @author Haotian Wang
 */
public class Tell implements Expression {
    private Token myToken;
    private ExpressionList turtles;

    public Tell(Token token, ExpressionList list) {
        myToken = token;
        turtles = list;
    }

    /**
     * This method lets the AST act on a Turtle model.
     *
     * @param turtleManager : The TurtleManager that is affected by applying the abstract syntax tree.
     * @return A double value returned by evaluating the expression.
     * @throws InterpretationException
     */
    @Override
    public double interpret(TurtleManager turtleManager) throws InterpretationException, UndefinedKeywordException {
        if (myToken.getString().equals("Tell")) {
            List<Integer> indices = new ArrayList<>();
            for (Expression index: turtles.getListOfExpressions()){
                indices.add((int)index.evaluate(turtleManager));
            }
            turtleManager.tell(indices);
        }
        return 0;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s %s}", myToken.getString(), turtles.toString());
    }
}
