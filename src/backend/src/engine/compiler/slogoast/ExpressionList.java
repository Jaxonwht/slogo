package engine.compiler.slogoast;

import engine.compiler.Token;
import engine.compiler.storage.StateMachine;
import engine.errors.InterpretationException;
import model.TurtleModel;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the list of Expression grammar.
 *
 * @author Haotian Wang
 */
public class ExpressionList implements Expression {
    private Token listStart;
    private List<Expression> expressionList;
    private Token listEnd;

    public ExpressionList(Token start, List<Expression> list, Token end) {
        listStart = start;
        expressionList = list;
        listEnd = end;
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
     * @param turtle : The TurtleModel that is affected by applying the abstract syntax tree.
     * @param state  : The StateMachine that records the variables.
     * @return A double value returned by evaluating the expression.
     * @throws InterpretationException
     */
    @Override
    public double interpret(TurtleModel turtle, StateMachine state) throws InterpretationException {
        return 0;
    }

    /**
     * This method evaluates the return value of the expression, without applying actual effects on the turtle.
     *
     * @param state : The StateMachine that records the variables.
     * @return A double value returned by evaluating the expression.
     * @throws InterpretationException
     */
    @Override
    public double evaluate(StateMachine state) throws InterpretationException {
        return 0;
    }


}
