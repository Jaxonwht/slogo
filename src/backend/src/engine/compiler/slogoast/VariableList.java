package engine.compiler.slogoast;

import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the expression, a list of variables. For example, [ :x :y] is a list of variables.
 *
 * @author Haotian Wang
 */
public class VariableList implements Expression {
    private List<Variable> variableList;

    public VariableList(List<Variable> list) {
        variableList = list;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return Arrays.toString(variableList.toArray(new Variable[variableList.size()]));
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
        if (variableList.isEmpty()) {
            return ret;
        }
        for (Variable var : variableList) {
            ret = var.interpret(turtleManager);
        }
        return ret;
    }

    /**
     * @return The List of Variable objects contained in this VariableList object.
     */
    List<Variable> getListOfVariables() {
        return variableList;
    }
}
