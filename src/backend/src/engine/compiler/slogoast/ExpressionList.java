package engine.compiler.slogoast;

import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the list of Expression grammar.
 *
 * @author Haotian Wang
 */
public class ExpressionList implements Expression {
    private List<Expression> expressionList;

    public ExpressionList(List<Expression> list) {
        expressionList = list;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return Arrays.toString(expressionList.toArray(new Expression[expressionList.size()]));
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
        double ret = 0;
        if (expressionList.isEmpty()) {
            return ret;
        }
        for (Expression expr : expressionList) {
            ret = expr.interpret(turtleManager);
        }
        return ret;
    }

    /**
     * @return The actual List of Expression associated with this ExpressionList object.
     */
    List<Expression> getListOfExpressions() {
        return expressionList;
    }
}
