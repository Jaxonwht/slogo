package engine.compiler.slogoast;

import engine.compiler.Token;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import model.TurtleManager;
import model.TurtleModel;
import model.TurtleOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the TwoList grammar.
 *
 * @author Haotian Wang
 */
public class TwoList implements Expression{
    private Token myToken;
    private ExpressionList listA;
    private ExpressionList listB;

    public TwoList(Token token, ExpressionList list1, ExpressionList list2) {
        myToken = token;
        listA = list1;
        listB = list2;
    }

    /**
     * This method gives a String representation of the Expression node enclosed by curly braces.
     *
     * @return A String representation of the abstract syntax tree node.
     */
    @Override
    public String toString() {
        return String.format("{%s %s %s}", myToken.getString(), listA.toString(), listB.toString());
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
        List<Integer> oldSelected = turtleManager.selected();
        if (myToken.getString().equals("Ask")) {

            List<Integer> indices = new ArrayList<>();
            for (Expression index: listA.getListOfExpressions()){
                indices.add((int)index.evaluate(turtleManager));
            }

            turtleManager.tell(indices);

            for (Expression op: listB.getListOfExpressions()){
                ret = op.evaluate(turtleManager);
            }

        } else if (myToken.getString().equals("AskWith")) {
            Expression check = listA.getListOfExpressions().get(0);

            List<Integer> indices = new ArrayList<>();

            List<Integer> testIndex = new ArrayList<>();
            for (var index: turtleManager.turtleModels().keySet()){
                testIndex.add(index);
                turtleManager.tell(testIndex);
                if (check.evaluate(turtleManager) != 0){
                    indices.add(index);
                }
                testIndex.clear();
            }

            if (indices.size() >= 1){
                turtleManager.tell(indices);
                for (Expression op: listB.getListOfExpressions()){
                    ret = op.interpret(turtleManager);
                }
            }
        }
        turtleManager.tell(oldSelected);
        return ret;
    }
}
