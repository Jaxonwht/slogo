package engine.compiler.slogoast;

import engine.compiler.Token;
import engine.compiler.storage.VariableType;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

/**
 * This class handles the assignment grammar in the AST.
 *
 * @author Haotian Wang
 */
public class MakeVariable implements Expression {
    private Token myToken;
    private Variable myVar;
    private Expression myExpr;

    public MakeVariable(Token token, Variable var, Expression a) {
        myToken = token;
        myVar = var;
        myExpr = a;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s %s %s}", myToken.getString(), myVar.toString(), myExpr.toString());
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
        if (myToken.getString().equals("MakeVariable")) {
            double ret = myExpr.evaluate(turtleManager);
            turtleManager.memory().setVariable(myVar.getVariableName(), ret, VariableType.DOUBLE);
            return ret;
        } return 0;
    }
}
