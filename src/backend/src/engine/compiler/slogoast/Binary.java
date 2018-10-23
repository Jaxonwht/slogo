package engine.compiler.slogoast;

import engine.compiler.Token;

/**
 * This class is an AST node representing binary operation that takes two expressions as commands.
 *
 * @author Haotian Wang
 */
public class Binary extends Expression {
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
}