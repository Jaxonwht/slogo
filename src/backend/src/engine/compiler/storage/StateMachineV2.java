package engine.compiler.storage;

import engine.compiler.slogoast.Expression;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;

import java.util.*;

/**
 * This is the version 2 of the StateMachine implementation. It does not build a different map for a different type of variable. Instead it stores everything in two maps, the global and the local.
 *
 * @author Haotian Wang
 */
public class StateMachineV2 implements StateMachine{
    private Map<String, VariableType> typeMap;
    private Map<String, Object> valueMap;
    private List<StateMachineObserver> observers;

    public StateMachineV2() {
        typeMap = new HashMap<>();
        valueMap = new HashMap<>();
        observers = new ArrayList<>();

        valueMap.put("ColorIndex1", "#FF0000");
        valueMap.put("ColorIndex2", "#00FF00");
        valueMap.put("ColorIndex3", "#0000FF");
    }

    /**
     * Set a double value for a variable.
     *
     * @param key
     * @param value
     */
    @Override
    public void setDouble(String key, double value) {
        setVariable(key, value, VariableType.DOUBLE);
    }

    /**
     * Set an int value for a variable.
     *
     * @param key
     * @param value
     */
    @Override
    public void setInteger(String key, int value) {
        setVariable(key, value, VariableType.INTEGER);
    }

    /**
     * Set a String value for a variable.
     *
     * @param key
     * @param value
     */
    @Override
    public void setString(String key, String value) {
        setVariable(key, value, VariableType.STRING);
    }

    /**
     * Set an Expression value for a variable.
     *
     * @param key
     * @param function
     */
    @Override
    public void setExpression(String key, Expression function) {
        setVariable(key, function, VariableType.EXPRESSION);
    }

    /**
     * Set the value of a variable in the StateMachine by taking in three parameters, identifying automatically what type the variable is.
     *
     * @param key   : The String name of the variable.
     * @param value : The value of the variable to be stored in the Object format.
     * @param type  : The type of the variable to be stored.
     */
    @Override
    public void setVariable(String key, Object value, VariableType type) {
        valueMap.put(key, value);
        typeMap.put(key, type);
        pushAlarm();
    }

    /**
     * Get the type of the variable, either a double, an integer or a function.
     *
     * @param key
     * @return The type of the variable.
     */
    @Override
    public VariableType getVariableType(String key) throws UndefinedKeywordException {
        if (!typeMap.containsKey(key)) {
            throw new UndefinedKeywordException(String.format("The variable \"%s\" is not defined in the global scope, therefore its type cannot be determined", key));
        }
        return typeMap.get(key);
    }

    /**
     * Get the value of the variable as an Object from the aggregate map.
     *
     * @param key
     * @return An Object representation of the value of the variable.
     */
    @Override
    public Object getValueInGeneralForm(String key) throws UndefinedKeywordException {
        if (!valueMap.containsKey(key)) {
            throw new UndefinedKeywordException(String.format("The variable \"%s\" is not defined in the global scope, therefore its value cannot be determined", key));
        }
        return valueMap.get(key);
    }

    /**
     * Remove the key entry from the map.
     *
     * @param key
     */
    @Override
    public void removeVariable(String key) throws UndefinedKeywordException {
        if (!valueMap.containsKey(key)) {
            throw new UndefinedKeywordException(String.format("The variable \"%s\" is not defined in the global scope, therefore it cannot be removed",key));
        }
        valueMap.remove(key);
        typeMap.remove(key);
        pushAlarm();
    }

    /**
     * Clear all state variables in the state machine.
     */
    @Override
    public void resetState() {
        valueMap.clear();
        typeMap.clear();
        pushAlarm();
    }

    /**
     * Present the internal storage of the StateMachine in a map format.
     * It is strongly suggested to make the returned map unmodifiable.
     *
     * @return A Map representation of the StateMachine.
     */
    @Override
    public Map<String, Object> listOfVariables() {
        return Collections.unmodifiableMap(valueMap);
    }

    /**
     * Allow any observers to register as an observer to this StateMachine.
     *
     * @param observer
     */
    @Override
    public void register(StateMachineObserver observer) {
        observers.add(observer);
    }

    /**
     * Returns true if the variable is already defined in the StateMachine and false otherwise.
     *
     * @param key : The String name of the variable to be queried.
     * @return A boolean value indicating whether the variable key is defined in the StateMachine.
     */
    @Override
    public boolean containsVariable(String key) {
        return valueMap.containsKey(key);
    }

    /**
     * Push notifications to observers whenever there's change within the StateMachine.
     */
    @Override
    public void pushAlarm() {
        observers.forEach(StateMachineObserver::notifyListener);
    }

    /**
     * Present the internal storage of the StateMachine in a list format, separated by newline.
     *
     * @return A String representation of the StateMachine.
     */
    @Override
    public String toString() {
        String ans = "The variables are\n";
        for (Map.Entry entry : valueMap.entrySet()) {
            ans += entry.getKey() + " = " + entry.getValue() + "\n";
        }
        return ans;
    }
}
