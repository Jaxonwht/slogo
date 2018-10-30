package engine.compiler.slogoast;

import engine.compiler.Token;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

/**
 * Implements the IfElse grammar in the AST.
 *
 * @author Haotian Wang
 */
public class IfElse implements Expression {
    private Token myToken;
    private Expression expr;
    private ExpressionList expressionListA;
    private ExpressionList expressionListB;

    public IfElse(Token a, Expression val, ExpressionList listA, ExpressionList listB) {
        myToken = a;
        expr = val;
        expressionListA = listA;
        expressionListB = listB;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s %s %s %s}", myToken.getString(), expr.toString(), expressionListA.toString(), expressionListB.toString());
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
        if (myToken.getString().equals("IfElse")) {
            if (expr.evaluate(turtleManager) != 0){
                ret = expressionListA.evaluate(turtleManager);
            } else{
                ret = expressionListB.evaluate(turtleManager);
            }
        }
        return ret;
    }
}
