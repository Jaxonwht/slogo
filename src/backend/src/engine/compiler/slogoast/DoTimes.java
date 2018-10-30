package engine.compiler.slogoast;

import engine.compiler.Token;
import engine.compiler.storage.VariableType;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

/**
 * This class handles the concept of DoTimes.
 *
 * @author Haotian Wang
 */
public class DoTimes implements Expression {
    private Token myToken;
    private Token start;
    private Variable var;
    private Expression limit;
    private Token end;
    private ExpressionList expressionList;

    public DoTimes(Token token, Token firstStart, Variable variable, Expression varLit, Token firstEnd, ExpressionList list) {
        myToken = token;
        start = firstStart;
        var = variable;
        limit = varLit;
        end = firstEnd;
        expressionList = list;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s %s %s %s %s %s}", myToken.getString(), start.getString(), var.toString(), limit.toString(), end.getString(), expressionList.toString());
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
        if (myToken.getString().equals("DoTimes")) {
            int limitInt = (int) limit.evaluate(turtleManager);
            if (limitInt < 1) {
                return 0;
            } else {
                String variableName = var.getVariableName();

                boolean reset = turtleManager.memory().containsVariable(variableName);
                var old = 0;
                if (reset){
                    old = (int)turtleManager.memory().getValue(variableName);
                }

                double ret = 0;
                for (int i = 1; i <= limitInt; i++) {
                    turtleManager.memory().setInteger(variableName, i);
                    ret = expressionList.interpret(turtleManager);
                }
                if (reset){
                    turtleManager.memory().setVariable(variableName, old, VariableType.DOUBLE);
                } else{
                    turtleManager.memory().removeVariable(variableName);
                }

                return ret;
            }
        }
        return 0;
    }
}
