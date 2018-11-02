package engine.compiler.slogoast;

import engine.compiler.utils.Token;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

/**
 * This class handles the expression that takes in only one expression as parameter.
 *
 * @author Haotian Wang
 */
public class Unary implements Expression {
    private final Token myToken;
    private final Expression myExpr;

    public Unary(Token token, Expression a) {
        myToken = token;
        myExpr = a;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s %s}", myToken.getString(), myExpr.toString());
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
        double head = turtleManager.getAngle(); //in radians
        double value = myExpr.evaluate(turtleManager);
        if (myToken.getString().equals("Forward")){
            return turtleManager.forward(value);
        }
        else if (myToken.getString().equals("Backward")){
            return turtleManager.forward(-value);
        }
        else if (myToken.getString().equals("Right")){
            return turtleManager.leftBy(-value);
        }
        else if (myToken.getString().equals("Left")){
            return turtleManager.leftBy(value);
        }
        else if (myToken.getString().equals("SetHeading")){
            return turtleManager.setAngle(value);
        }
        else if (myToken.getString().equals("Minus")){
            return -1*value;
        }
        else if (myToken.getString().equals("Sin")){
            return Math.sin(Math.toRadians(value));
        }
        else if (myToken.getString().equals("Cos")) {
            return Math.cos(Math.toRadians(value));
        }
        else if (myToken.getString().equals("Tangent")){
            return Math.tan(Math.toRadians(value));
        }
        else if (myToken.getString().equals("ArcTangent")){
            return Math.atan(Math.toRadians(value));
        }
        else if (myToken.getString().equals("NaturalLog")){
            return Math.log(value);
        }
        else if (myToken.getString().equals("Not")){
            return (value == 0) ? 1:0;
        }
        else if (myToken.getString().equals("SetBackground")) {
            return turtleManager.setBackground((int) value);
        }
        else if (myToken.getString().equals("SetPenColor")) {
            return turtleManager.setPenColor((int) value);
        }
        else if (myToken.getString().equals("SetPenSize")) {
            return turtleManager.setPenSize((int) value);
        }
        else if(myToken.getString().equals("SetShape")) {
            return turtleManager.setShape((int) value);
        }
        return 0;
    }
}
