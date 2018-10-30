package engine.compiler.slogoast;

import engine.compiler.storage.StateMachine;
import engine.compiler.storage.StateMachineV2;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

/**
 * The class handles the UserFunction AST node. For example, :drawSquare [4 5] is such a UserFunction.
 *
 * @author Haotian Wang
 */
public class UserFunction implements Expression {
    private Variable myVariable;
    private ExpressionList parameters;

    public UserFunction(Variable var, ExpressionList list) {
        myVariable = var;
        parameters = list;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s %s}", myVariable.toString(), parameters.toString());
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
        MakeUserInstruction function = (MakeUserInstruction) turtleManager.memory().getValueInGeneralForm(myVariable.getVariableName());
        VariableList desiredParameters = function.getParameters();
        ExpressionList desiredExpressions = function.getExpressionList();
        if (desiredParameters.getListOfVariables().size() != parameters.getListOfExpressions().size()) {
            throw new InterpretationException(String.format("The number of expressions passed in, %d, does not match the number of desired parameters defined earlier, %d", parameters.getListOfExpressions().size(), desiredParameters.getListOfVariables().size()));
        }
        if (parameters.getListOfExpressions().isEmpty()) {
            return desiredExpressions.interpret(turtleManager);
        }

        StateMachine oldGlobalMemory = new StateMachineV2();

        for (int i = 0; i < parameters.getListOfExpressions().size(); i++) {
            var desiredParameter = desiredParameters.getListOfVariables().get(i).getVariableName();
            if (turtleManager.memory().containsVariable(desiredParameter)){
                oldGlobalMemory.setDouble(desiredParameter, (double)turtleManager.memory().getValue(desiredParameter));
            }
            turtleManager.memory().setDouble(desiredParameter, parameters.getListOfExpressions().get(i).evaluate(turtleManager));
        }
        double ret = desiredExpressions.interpret(turtleManager);
        for (int i = 0; i < desiredParameters.getListOfVariables().size(); i++) {
            var desiredParameter = desiredParameters.getListOfVariables().get(i).getVariableName();
            turtleManager.memory().removeVariable(desiredParameters.getListOfVariables().get(i).getVariableName());
            if (oldGlobalMemory.containsVariable(desiredParameter)){
                turtleManager.memory().setDouble(desiredParameter, (double)oldGlobalMemory.getValue(desiredParameter));
            }
        }
        return ret;
    }
}
