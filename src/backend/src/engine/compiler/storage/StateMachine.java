package engine.compiler.storage;

import engine.compiler.slogoast.Expression;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;

import java.io.Serializable;
import java.util.Map;

/**
 * This interface handles the storage of variables and function predicates in Variable AST nodes.
 *
 * @author Haotian Wang
 */
public interface StateMachine {
    /**
     * Set a double value for a variable.
     *
     * @param key
     * @param value
     */
    void setDouble(String key, double value);

    /**
     * Set an int value for a variable.
     *
     * @param key
     * @param value
     */
    void setInteger(String key, int value);

    /**
     * Set a String value for a variable.
     *
     * @param key
     * @param value
     */
    void setString(String key, String value);

    /**
     * Set an Expression value for a variable.
     *
     * @param key
     * @param function
     */
    void setExpression(String key, Expression function);

    /**
     * Set a local integer value for a key.
     *
     * @param key
     * @param value
     */

    void setVariable(String key, Object value, VariableType type);

    /**
     * Get the type of the variable, either a double, an integer or a function.
     *
     * @param key
     * @return The type of the variable.
     */
    VariableType getVariableType(String key) throws UndefinedKeywordException;

    /**
     * Get the value of the variable as an Object from the aggregate map.
     *
     * @param key
     * @return An Object representation of the value of the variable.
     */
    Object getValueInGeneralForm(String key) throws UndefinedKeywordException;

    /**
     * Remove the key entry from the map.
     *
     * @param key
     */
    void removeVariable(String key) throws UndefinedKeywordException;

    /**
     * Clear all state variables in the state machine.
     */
    void resetState();

    /**
     * Present the internal storage of the StateMachine in a map format.
     * It is strongly suggested to make the returned map unmodifiable.
     *
     * @return A Map representation of the StateMachine.
     */
    Map<String, Object> listOfVariables();

    /**
     *  Allow any observers to register as an observer to this StateMachine.
     */
    void register(StateMachineObserver observer);

    /**
     * Returns true if the variable is already defined in the StateMachine and false otherwise.
     *
     * @param key: The String name of the variable to be queried.
     * @return A boolean value indicating whether the variable key is defined in the StateMachine.
     */
    boolean containsVariable(String key);

    /**
     * Push notifications to observers whenever there's change within the StateMachine.
     */
    void pushAlarm();

    /**
     * Present the internal storage of the StateMachine in a list format, separated by newline.
     *
     * @return A String representation of the StateMachine.
     */
    String toString();
}
