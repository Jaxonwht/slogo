package engine.compiler.slogoast;

import engine.compiler.utils.Token;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.PosAndAngle;
import model.TurtleManager;

/**
 * This class is a terminal node in the AST.
 *
 * @author Haotian Wang, Rahul Ramesh
 */
public class Direct implements Expression {
    private Token myToken;

    public Direct(Token token) {
        myToken = token;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s}", myToken.getString());
    }

    /**
     * This method lets the AST act on a Turtle model.
     *
     * @param turtleManager : The TurtleManager that is affected by applying the abstract syntax tree.
     * @return A double value returned by evaluating the expression.
     * @throws InterpretationException
     * @author Rahul Ramesh
     */
    @Override
    public double interpret(TurtleManager turtleManager) throws InterpretationException, UndefinedKeywordException {
        if (myToken.getString().equals("PenUp")) {
            return turtleManager.setPenDown(false);
        }
        else if (myToken.getString().equals("PenDown")) {
            return turtleManager.setPenDown(true);
        }
        else if (myToken.getString().equals("ShowTurtle")) {
            return turtleManager.setVisible(true);
        }
        else if (myToken.getString().equals("HideTurtle")) {
            return turtleManager.setVisible(false);
        }
        else if (myToken.getString().equals("Home")) {
            return turtleManager.moveTo(PosAndAngle.X_BUF,PosAndAngle.Y_BUF,false);
        }
        else if (myToken.getString().equals("ClearScreen")) {
            turtleManager.clear();
            turtleManager.setAngle(0);
            return turtleManager.moveTo(PosAndAngle.X_BUF,PosAndAngle.Y_BUF,false);
        }
        else if (myToken.getString().equals("XCoordinate")) {
            return turtleManager.getX();
        }
        else if (myToken.getString().equals("YCoordinate")) {
            return turtleManager.getY();
        }
        else if (myToken.getString().equals("Heading")) {
            return turtleManager.getAngle();
        }
        else if (myToken.getString().equals("IsPenDown")) {
            return turtleManager.isPenDown() ? turtleManager.TRUE : turtleManager.FALSE;
        }
        else if (myToken.getString().equals("IsShowing")) {
            return turtleManager.isVisible() ? turtleManager.TRUE : turtleManager.FALSE;
        }
        else if (myToken.getString().equals("Pi")){
            return Math.PI;
        }
        else if (myToken.getString().equals("ID")){
            return turtleManager.selected().get(turtleManager.selected().size() - 1);
        }
        else if (myToken.getString().equals("Turtles")){
            return turtleManager.turtleModels().keySet().size();
        }
        else if (myToken.getString().equals("Stamp")) {
            return turtleManager.stamp();
        }
        else if (myToken.getString().equals("ClearStamps")) {
            return turtleManager.clearStamps();
        }
        else if (myToken.getType().equals("Constant")){
            return Double.parseDouble(myToken.getString());
        }
        return 0;
    }
}
