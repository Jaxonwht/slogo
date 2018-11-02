package engine.compiler.slogoast;

import engine.compiler.utils.Token;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;

/**
 * This class is an AST node representing binary operation that takes two expressions as commands.
 *
 * @author Haotian Wang
 */
public class Binary implements Expression {
    private Token myToken;
    private Expression myFirstExpr;
    private Expression mySecondExpr;

    public Binary(Token token, Expression a, Expression b) {
        myToken = token;
        myFirstExpr = a;
        mySecondExpr = b;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s %s %s}", myToken.getString(), myFirstExpr.toString(), mySecondExpr.toString());
    }


    /**
     * @author rr202
     * This method lets the AST act on a Turtle model.
     *
     * @param turtleManager : The TurtleManager that is affected by applying the abstract syntax tree.
     * @throws InterpretationException
     */
    @Override
    public double interpret(TurtleManager turtleManager) throws InterpretationException, UndefinedKeywordException {
        double currentX = turtleManager.getX();
        double currentY = turtleManager.getY();
        if (myToken.getString().equals("Sum")) {
            return myFirstExpr.evaluate(turtleManager) + mySecondExpr.evaluate(turtleManager);
        } else if (myToken.getString().equals("Difference")) {
            return myFirstExpr.evaluate(turtleManager) - mySecondExpr.evaluate(turtleManager);
        } else if (myToken.getString().equals("Quotient")) {
            if (mySecondExpr.evaluate(turtleManager) == 0) {
                throw new InterpretationException("The denominator in a Quotient operation cannot be zero");
            }
            return myFirstExpr.evaluate(turtleManager) / mySecondExpr.evaluate(turtleManager);
        } else if (myToken.getString().equals("Product")) {
            return myFirstExpr.evaluate(turtleManager) * mySecondExpr.evaluate(turtleManager);
        } else if (myToken.getString().equals("Remainder")) {
            double first = myFirstExpr.evaluate(turtleManager);
            double second = mySecondExpr.evaluate(turtleManager);
            if (second == 0) {
                throw new InterpretationException("The denominator in a Remainder operation cannot be zero");
            }
            int firstInt = (int) first;
            int secondInt = (int) second;
            if (firstInt != first || secondInt != second) {
                throw new InterpretationException("The two values used for a Remainder operation must be integers");
            } return firstInt % secondInt;
        } else if (myToken.getString().equals("Power")) {
            return Math.pow(myFirstExpr.evaluate(turtleManager), mySecondExpr.evaluate(turtleManager));
        } else if (myToken.getString().equals("LessThan")) {
            return myFirstExpr.evaluate(turtleManager) < mySecondExpr.evaluate(turtleManager) ? 1 : 0;
        } else if (myToken.getString().equals("GreaterThan")) {
            return myFirstExpr.evaluate(turtleManager) > mySecondExpr.evaluate(turtleManager) ? 1 : 0;
        } else if (myToken.getString().equals("Equal")) {
            return myFirstExpr.evaluate(turtleManager) == mySecondExpr.evaluate(turtleManager) ? 1 : 0;
        } else if (myToken.getString().equals("NotEqual")) {
            return mySecondExpr.evaluate(turtleManager) != mySecondExpr.evaluate(turtleManager) ? 1 : 0;
        } else if (myToken.getString().equals("And")) {
            return myFirstExpr.evaluate(turtleManager) != 0 && mySecondExpr.evaluate(turtleManager) != 0 ? 1 : 0;
        } else if (myToken.getString().equals("Or")) {
            return myFirstExpr.evaluate(turtleManager) != 0 || mySecondExpr.evaluate(turtleManager) != 0 ? 1 : 0;
        } else if (myToken.getString().equals("SetTowards")) {
            double newAngle = Math.toDegrees(Math.atan2(mySecondExpr.evaluate(turtleManager) - currentY, myFirstExpr.evaluate(turtleManager) - currentX));
            return turtleManager.setAngle(newAngle);
        } else if (myToken.getString().equals("SetPosition")){
            return turtleManager.moveTo(myFirstExpr.evaluate(turtleManager), mySecondExpr.evaluate(turtleManager), false);
        }
        return 0;
    }
}
