package engine.compiler.parser;

import engine.compiler.Token;
import engine.errors.CommandSyntaxException;
import engine.compiler.slogoast.*;
import javafx.util.Pair;

import java.util.*;

/**
 * A version 1 implementation of the Parser interface. It takes a list of Tokens and output ASTs or store variables.
 *
 * @author Haotian Wang
 */
public class CrudeParser implements Parser {
    private static final Token listStart = new Token("[", "ListStart");
    private static final Token listEnd = new Token("]", "ListEnd");
    private static final Token groupStart = new Token("(", "GroupStart");
    private static final Token groupEnd = new Token(")", "GroupEnd");

    private List<Token> myTokens;
    private Expression myAST;
    private String tokenStream;

    public CrudeParser() {
        myTokens = new ArrayList<>();
    }

    /**
     * Reads in the queue of Tokens from the Lexer into the internal logic of the parser.
     *
     * @param tokens : A queue of Tokens read from the Lexer.
     */
    @Override
    public void readTokens(List<Token> tokens) throws CommandSyntaxException {
        myTokens = tokens;
        tokenStream = Arrays.toString(tokens.toArray(new Token[tokens.size()]));
        myAST = parseGoal();
    }

    /**
     * Clear the stored tokens in the internal parser.
     */
    @Override
    public void clearTokens() {
        myTokens.clear();
        tokenStream = null;
    }

    /**
     * @return An SLogoAST for the interpreter to process.
     */
    @Override
    public Expression returnAST() {
        return myAST;
    }

    /**
     * This method generates a 3-liner error message. The first line is written by the user. The second line is the index of list of Tokens where the error occurred. The third line is a list of Tokens for reference.
     *
     * @param message: The user-defined error message such as "Missing parenthesis".
     * @param index: The index in the list of Tokens where the error occurred.
     * @return A CommandSyntaxException.
     */
    private CommandSyntaxException generateSyntaxException(String message, int index) {
        return new CommandSyntaxException(message + "\nIndex in the list of Tokens: " + index + "\nThe list of Tokens is: " + tokenStream);
    }

    /**
     * This method returns the complete syntax tree if the input command is grammatically correct.
     *
     * @return A root Expression node passed to the Interpreter.
     * @throws CommandSyntaxException
     */
    private Expression parseGoal() throws CommandSyntaxException {
        Pair<Expression, Integer> resultPair = parseExpression(0);
        if (resultPair.getKey() == null || resultPair.getValue() != myTokens.size()) {
            myTokens.add(listEnd);
            myTokens.add(0, listStart);
            resultPair = parseExpressionList(0);
            if (resultPair.getKey() == null || resultPair.getValue() != myTokens.size()) {
                throw new CommandSyntaxException("The input command cannot be parsed.");
            }
            return resultPair.getKey();
        } else {
            return resultPair.getKey();
        }
    }

    /**
     * The following methods are a family of parsing methods using recursive descent.
     *
     * @param index: The starting index of this parse query.
     * @return An Expression AST node, which is used for Expression grammar, and paired with it, an index after the parse.
     */
    private Pair<Expression, Integer> parseExpression(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        if (index >= myTokens.size()) {
            return nullPair;
        }
        Pair<Expression, Integer> groupPair = parseGroup(index);
        if (groupPair.getKey() != null) {
            return groupPair;
        }
        Pair<Expression, Integer> unaryPair = parseUnary(index);
        if (unaryPair.getKey() != null) {
            return unaryPair;
        }
        Pair<Expression, Integer> binaryPair = parseBinary(index);
        if (binaryPair.getKey() != null) {
            return binaryPair;
        }
        Pair<Expression, Integer> makeVariablePair = parseMakeVariable(index);
        if (makeVariablePair.getKey() != null) {
            return makeVariablePair;
        }
        Pair<Expression, Integer> conditionPair = parseCondition(index);
        if (conditionPair.getKey() != null) {
            return conditionPair;
        }
        Pair<Expression, Integer> doTimesPair = parseDoTimes(index);
        if (doTimesPair.getKey() != null) {
            return doTimesPair;
        }
        Pair<Expression, Integer> forPair = parseFor(index);
        if (forPair.getKey() != null) {
            return forPair;
        }
        Pair<Expression, Integer> ifElsePair = parseIfElse(index);
        if (ifElsePair.getKey() != null) {
            return ifElsePair;
        }
        Pair<Expression, Integer> makeUserInstructionPair = parseMakeUserInstruction(index);
        if (makeUserInstructionPair.getKey() != null) {
            return makeUserInstructionPair;
        }
        Pair<Expression, Integer> twoListPair = parseTwoList(index);
        if (twoListPair.getKey() != null) {
            return twoListPair;
        }
        Pair<Expression, Integer> tellPair = parseTell(index);
        if (tellPair.getKey() != null) {
            return tellPair;
        }
        Pair<Expression, Integer> ternaryPair = parseQuaternary(index);
        if (ternaryPair.getKey() != null) {
            return ternaryPair;
        }
        Pair<Expression, Integer> expressionListPair = parseExpressionList(index);
        if (expressionListPair.getKey() != null) {
            return expressionListPair;
        }
        Pair<Expression, Integer> userFunctionPair = parseUserFunction(index);
        if (userFunctionPair.getKey() != null) {
            return userFunctionPair;
        }
        Pair<Expression, Integer> directPair = parseDirect(index);
        if (directPair.getKey() != null) {
            return directPair;
        }
        Pair<Expression, Integer> variablePair = parseVariable(index);
        if (variablePair.getKey() != null) {
            return variablePair;
        }
        return nullPair;
    }

    /**
     * @param index
     * @param type
     * @return This method parses single Token and return a pair of Token together with the new index.
     */
    private Pair<Token, Integer> parseToken(int index, String type) {
        if (index >= myTokens.size() || !myTokens.get(index).getType().equals(type)) {
            return new Pair<>(null, index);
        } else {
            return new Pair<>(myTokens.get(index), index + 1);
        }
    }

    /**
     * @param index
     * @return A pair of Expression and index for Quaternary grammar.
     */
    private Pair<Expression, Integer> parseQuaternary(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> quaternaryPair = parseToken(index, "Quaternary");
        if (quaternaryPair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> firstPair = parseExpression(quaternaryPair.getValue());
        if (firstPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for the the first part of expression in quaternary grammar", firstPair.getValue());
        }
        Pair<Expression, Integer> secondPair = parseExpression(firstPair.getValue());
        if (secondPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for the the second part of expression in quaternary grammar", secondPair.getValue());
        }
        Pair<Expression, Integer> thirdPair = parseExpression(secondPair.getValue());
        if (thirdPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for the the third part of expression in quaternary grammar", thirdPair.getValue());
        }
        Pair<Expression, Integer> fourthPair = parseExpression(thirdPair.getValue());
        if (fourthPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for the the fourth part of expression in quaternary grammar", thirdPair.getValue());
        }
        return new Pair<>(new Quaternary(quaternaryPair.getKey(), firstPair.getKey(), secondPair.getKey(), thirdPair.getKey(), fourthPair.getKey()), fourthPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for Tell grammar.
     */
    private Pair<Expression, Integer> parseTell(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> tellPair = parseToken(index, "Tell");
        if (tellPair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> listPair = parseExpressionList(tellPair.getValue());
        if (listPair.getKey() == null) {
            throw generateSyntaxException(String.format("The list of expressions following the keyword \"%s\" is illegal", tellPair.getKey().getString()), listPair.getValue());
        }
        return new Pair<>(new Tell(tellPair.getKey(), (ExpressionList) listPair.getKey()), listPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for TwoList grammar.
     */
    private Pair<Expression, Integer> parseTwoList(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> twoListPair = parseToken(index, "TwoList");
        if (twoListPair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> listOnePair = parseExpressionList(twoListPair.getValue());
        if (listOnePair.getKey() == null) {
            throw generateSyntaxException(String.format("The first list of expressions after the keyword \"%s\" is illegal", twoListPair.getKey().getString()), listOnePair.getValue());
        }
        Pair<Expression, Integer> listTwoPair = parseExpressionList(listOnePair.getValue());
        if (listTwoPair.getKey() == null) {
            throw generateSyntaxException(String.format("The second list of expressions after the keyword \"%s\" is illegal", twoListPair.getKey().getString()), listTwoPair.getValue());
        }
        return new Pair<>(new TwoList(twoListPair.getKey(), (ExpressionList) listOnePair.getKey(), (ExpressionList) listTwoPair.getKey()), listTwoPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for IfElse grammar.
     */
    private Pair<Expression, Integer> parseIfElse(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> ifElsePair = parseToken(index, "IfElse");
        if (ifElsePair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> expressionPair = parseExpression(ifElsePair.getValue());
        if (expressionPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for an expression after the \"ifelse\" keyword", expressionPair.getValue());
        }
        if (expressionPair.getKey() instanceof UserFunction) {
            throw generateSyntaxException("If the expression directly after the keyword in an ifelse statement is a user-defined function, it must be bracketed by \"()\"", ifElsePair.getValue());
        }
        Pair<Expression, Integer> listAPair = parseExpressionList(expressionPair.getValue());
        if (listAPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for a list of expressions that is run when the ifelse expression is evaluated true", listAPair.getValue());
        }
        Pair<Expression, Integer> listBPair = parseExpressionList(listAPair.getValue());
        if (listBPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for a list of expressions that is run when the ifelse expression is evaluated false", listBPair.getValue());
        }
        return new Pair<>(new IfElse(ifElsePair.getKey(), expressionPair.getKey(), (ExpressionList) listAPair.getKey(), (ExpressionList) listBPair.getKey()), listBPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for MakeUserInstruction grammar.
     */
    private Pair<Expression, Integer> parseMakeUserInstruction(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> makeUserInstructionPair = parseToken(index, "MakeUserInstruction");
        if (makeUserInstructionPair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> commandPair = parseVariable(makeUserInstructionPair.getValue());
        if (commandPair.getKey() == null) {
            throw generateSyntaxException("Missing a valid variable name to store the user-made function after the \"to\" keyword", commandPair.getValue());
        }
        Variable var = (Variable) commandPair.getKey();
        if (var.getVariableName().startsWith(":")) {
            throw generateSyntaxException("The name of a user defined function cannot start with :", commandPair.getValue());
        }
        Pair<Expression, Integer> variableListPair = parseVariableList(commandPair.getValue());
        if (variableListPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for defining a list of variables for use with the user-defined function", variableListPair.getValue());
        }
        Pair<Expression, Integer> expressionListPair = parseExpressionList(variableListPair.getValue());
        if (expressionListPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for defining a list of expressions for use with the user-defined function", expressionListPair.getValue());
        }
        return new Pair<>(new MakeUserInstruction(makeUserInstructionPair.getKey(), (Variable) commandPair.getKey(), (VariableList) variableListPair.getKey(), (ExpressionList) expressionListPair.getKey()), expressionListPair.getValue());
    }

    /**
     * @param index
     * @return A pair of VariableList and index for the VariableList grammar.
     */
    private Pair<Expression, Integer> parseVariableList(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> listStartPair = parseToken(index, "ListStart");
        if (listStartPair.getKey() == null) {
            return nullPair;
        }
        int pointer = listStartPair.getValue();
        List<Variable> variableList = new ArrayList<>();
        if (pointer == myTokens.size()) {
            throw generateSyntaxException("An expression cannot be closed by a \"[\"", pointer);
        }
        if (myTokens.get(pointer).getType().equals("ListEnd")) {
            return new Pair<>(new VariableList(variableList), pointer + 1);
        }
        while (true) {
            Pair<Expression, Integer> listPair = parseVariable(pointer);
            if (listPair.getKey() == null) {
                break;
            }
            variableList.add((Variable) listPair.getKey());
            pointer = listPair.getValue();
        }
        if (variableList.isEmpty()) {
            throw generateSyntaxException("Missing a valid variable to constitute a valid list of variables", pointer);
        }
        Pair<Token, Integer> listEndPair = parseToken(pointer, "ListEnd");
        if (listEndPair.getKey() == null) {
            throw generateSyntaxException("Missing \"]\" symbol to end a list of expressions", listEndPair.getValue());
        }
        return new Pair<>(new VariableList(variableList), listEndPair.getValue());
    }

    /**
     * @param index
     * @return A pair of UserFunction and index for the UserFunction grammar.
     */
    private Pair<Expression, Integer> parseUserFunction(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Expression, Integer> variablePair = parseVariable(index);
        if (variablePair.getKey() == null) {
            return nullPair;
        }
        Variable var = (Variable) variablePair.getKey();
        if (var.getVariableName().startsWith(":")) {
            return nullPair;
        }
        Pair<Expression, Integer> expressionListPair = parseExpressionList(variablePair.getValue());
        if (expressionListPair.getKey() == null) {
            return nullPair;
        }
        return new Pair<>(new UserFunction((Variable) variablePair.getKey(), (ExpressionList) expressionListPair.getKey()), expressionListPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the For loop grammar.
     */
    private Pair<Expression, Integer> parseFor(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> forPair = parseToken(index, "For");
        if (forPair.getKey() == null) {
            return nullPair;
        }
        Pair<Token, Integer> listStartPair = parseToken(forPair.getValue(), "ListStart");
        if (listStartPair.getKey() == null) {
            throw generateSyntaxException("Missing \"[\" after the \"for\" keyword", listStartPair.getValue());
        }
        Pair<Expression, Integer> variablePair = parseVariable(listStartPair.getValue());
        if (variablePair.getKey() == null) {
            throw generateSyntaxException("Illegal variable format after \"[\" in a for loop", variablePair.getValue());
        }
        Pair<Expression, Integer> startPair = parseExpression(variablePair.getValue());
        if (startPair.getKey() == null) {
            throw generateSyntaxException("Illegal expression for the starting value of the variable in a for loop", startPair.getValue());
        }
        Pair<Expression, Integer> endPair = parseExpression(startPair.getValue());
        if (endPair.getKey() == null) {
            throw generateSyntaxException("Illegal expression for the ending value of the variable in a for loop", endPair.getValue());
        }
        Pair<Expression, Integer> stepPair = parseExpression(endPair.getValue());
        if (endPair.getKey() == null) {
            throw generateSyntaxException("Illegal expression for the increment value of the variable in a for loop", stepPair.getValue());
        }
        Pair<Token, Integer> listEndPair = parseToken(stepPair.getValue(), "ListEnd");
        if (listEndPair.getKey() == null) {
            throw generateSyntaxException("Missing \"]\" after the increment expression in a for loop", listEndPair.getValue());
        }
        Pair<Expression, Integer> expressionListPair = parseExpressionList(listEndPair.getValue());
        if (expressionListPair.getKey() == null) {
            throw generateSyntaxException("Illegal list of commands in a for loop", expressionListPair.getValue());
        }
        return new Pair<>(new For(forPair.getKey(), (Variable) variablePair.getKey(), startPair.getKey(), endPair.getKey(), stepPair.getKey(), (ExpressionList) expressionListPair.getKey()), expressionListPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the DoTimes grammar.
     */
    private Pair<Expression, Integer> parseDoTimes(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> doTimesPair = parseToken(index, "DoTimes");
        if (doTimesPair.getKey() == null) {
            return nullPair;
        }
        Pair<Token, Integer> listStartPair = parseToken(doTimesPair.getValue(), "ListStart");
        if (listStartPair.getKey() == null) {
            throw generateSyntaxException("Missing \"[\" symbol after dotimes keyword in a dotimes loop", listStartPair.getValue());
        }
        Pair<Expression, Integer> variablePair = parseVariable(listStartPair.getValue());
        if (variablePair.getKey() == null) {
            throw generateSyntaxException("Illegal variable format after \"[\" in a dotimes loop", variablePair.getValue());
        }
        Pair<Expression, Integer> limitPair = parseExpression(variablePair.getValue());
        if (limitPair.getKey() == null) {
            throw generateSyntaxException("Illegal expression for the upper limit value of the variable in a dotimes loop", limitPair.getValue());
        }
        Pair<Token, Integer> listEndPair = parseToken(limitPair.getValue(), "ListEnd");
        if (listEndPair.getKey() == null) {
            throw generateSyntaxException("Missing \"]\" symbol after the limit value in a dotimes loop", limitPair.getValue());
        }
        Pair<Expression, Integer> expressionListPair = parseExpressionList(listEndPair.getValue());
        if (expressionListPair.getKey() == null) {
            throw generateSyntaxException("Illegal format of a list of commands in a dotimes loop", expressionListPair.getValue());
        }
        return new Pair<>(new DoTimes(doTimesPair.getKey(), listStart, (Variable) variablePair.getKey(), limitPair.getKey(), listEnd, (ExpressionList) expressionListPair.getKey()), expressionListPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the ExpressionList grammar.
     */
    private Pair<Expression, Integer> parseExpressionList(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> listStartPair = parseToken(index, "ListStart");
        if (listStartPair.getKey() == null) {
            return nullPair;
        }
        int pointer = listStartPair.getValue();
        List<Expression> expressionList = new ArrayList<>();
        if (pointer == myTokens.size()) {
            throw generateSyntaxException("An expression cannot be closed by a \"[\"", pointer);
        }
        if (myTokens.get(pointer).getType().equals("ListEnd")) {
            return new Pair<>(new ExpressionList(expressionList), pointer + 1);
        }
        while (true) {
            Pair<Expression, Integer> listPair = parseExpression(pointer);
            if (listPair.getKey() == null) {
                break;
            }
            expressionList.add(listPair.getKey());
            pointer = listPair.getValue();
        }
        if (expressionList.isEmpty()) {
            throw generateSyntaxException("Missing a valid expression to constitute a valid list of expressions", pointer);
        }
        Pair<Token, Integer> listEndPair = parseToken(pointer, "ListEnd");
        if (listEndPair.getKey() == null) {
            throw generateSyntaxException("Missing \"]\" symbol to end a list of expressions", listEndPair.getValue());
        }
        return new Pair<>(new ExpressionList(expressionList), listEndPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the Condition grammar.
     */
    private Pair<Expression, Integer> parseCondition(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> conditionPair = parseToken(index, "Condition");
        if (conditionPair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> expressionPair = parseExpression(conditionPair.getValue());
        if (expressionPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for expression value after a keyword in the repeat or if loop", expressionPair.getValue());
        }
        if (expressionPair.getKey() instanceof UserFunction) {
            throw generateSyntaxException("If the expression directly after the keyword in an ifelse statement is a user-defined function, it must be bracketed by \"()\"", conditionPair.getValue());
        }
        Pair<Expression, Integer> expressionListPair = parseExpressionList(expressionPair.getValue());
        if (expressionListPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for a list of expressions in a repeat or if loop", expressionListPair.getValue());
        }
        return new Pair<>(new Condition(conditionPair.getKey(), expressionPair.getKey(), (ExpressionList) expressionListPair.getKey()), expressionListPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the MakeVariable grammar.
     */
    private Pair<Expression, Integer> parseMakeVariable(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> makeVariablePair = parseToken(index, "MakeVariable");
        if (makeVariablePair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> variablePair = parseVariable(makeVariablePair.getValue());
        if (variablePair.getKey() == null) {
            throw generateSyntaxException("Illegal variable format after \"make\" command in a MakeVariable command", variablePair.getValue());
        }
        Pair<Expression, Integer> expressionPair = parseExpression(variablePair.getValue());
        if (expressionPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for an expression that is assigned to the variable in a MakeVariable command", expressionPair.getValue());
        }
        return new Pair<>(new MakeVariable(makeVariablePair.getKey(), (Variable) variablePair.getKey(), expressionPair.getKey()), expressionPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the Binary grammar.
     */
    private Pair<Expression, Integer> parseBinary(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> binaryPair = parseToken(index, "Binary");
        if (binaryPair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> firstPair = parseExpression(binaryPair.getValue());
        if (firstPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for the the first part of expression in binary grammar", firstPair.getValue());
        }
        Pair<Expression, Integer> secondPair = parseExpression(firstPair.getValue());
        if (secondPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for the second part of a binary expression", secondPair.getValue());
        }
        return new Pair<>(new Binary(binaryPair.getKey(), firstPair.getKey(), secondPair.getKey()), secondPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the Variable grammar.
     */
    private Pair<Expression, Integer> parseVariable(int index) {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> variablePair = parseToken(index, "Variable");
        if (variablePair.getKey() == null) {
            return nullPair;
        }
        return new Pair<>(new Variable(variablePair.getKey()), variablePair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the Unary grammar.
     */
    private Pair<Expression, Integer> parseUnary(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> unaryPair = parseToken(index, "Unary");
        if (unaryPair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> secondPair = parseExpression(unaryPair.getValue());
        if (secondPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for the expression after a unary operator", secondPair.getValue());
        }
        return new Pair<>(new Unary(unaryPair.getKey(), secondPair.getKey()), secondPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the Group grammar.
     */
    private Pair<Expression, Integer> parseGroup(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> groupStartPair = parseToken(index, "GroupStart");
        if (groupStartPair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> middlePair = parseExpression(groupStartPair.getValue());
        if (middlePair.getKey() == null) {
            throw generateSyntaxException("Illegal expression for a Group after the \"(\" symbol", middlePair.getValue());
        }
        Pair<Token, Integer> groupEndPair = parseToken(middlePair.getValue(), "GroupEnd");
        if (groupEndPair.getKey() == null) {
            throw generateSyntaxException("Missing \")\" symbol for a Group after a valid expression", groupEndPair.getValue());
        }
        return new Pair<>(new Group(middlePair.getKey()), groupEndPair.getValue());
    }

    /**
     * @param index
     * @return A pair of Expression and index for the Direct grammar.
     */
    private Pair<Expression, Integer> parseDirect(int index) {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> directPair = parseToken(index, "Direct");
        if (directPair.getKey() == null) {
            directPair = parseToken(index, "Constant");
            if (directPair.getKey() == null) {
                return nullPair;
            }
        }
        return new Pair<>(new Direct(directPair.getKey()), directPair.getValue());
    }
}
