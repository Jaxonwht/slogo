package engine.compiler.slogoast;

import engine.compiler.utils.Token;
import model.TurtleManager;

/**
 * This implements the MakeUserInstruction grammar in AST.
 *
 * @author Haotian Wang
 */
public class MakeUserInstruction implements Expression {
    private Token action;
    private Variable myVar;
    private VariableList variableList;
    private ExpressionList expressionList;


    public MakeUserInstruction(Token a, Variable variable, VariableList varList, ExpressionList exprList) {
        action = a;
        myVar = variable;
        variableList = varList;
        expressionList = exprList;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s %s %s %s}", action.getString(), myVar.toString(), variableList.toString(), expressionList.toString());
    }

    /**
     * This method lets the AST act on a Turtle model.
     *
     * @param turtleManager : The TurtleManager that is affected by applying the abstract syntax tree.
     * @return A double value returned by evaluating the expression.
     */
    @Override
    public double interpret(TurtleManager turtleManager) {
        if (action.getString().equals("MakeUserInstruction")) {
            turtleManager.memory().setExpression(myVar.getVariableName(), this);
        }
        return 0;
    }

    /**
     * Get the variableList object associated with this MakeUserInstruction class.
     *
     * @return A VariableList object.
     */
    VariableList getParameters() {
        return variableList;
    }

    /**
     * @return An ExpressionList object associated with this MakeUserInstruction object.
     */
    ExpressionList getExpressionList() {
        return expressionList;
    }
}
