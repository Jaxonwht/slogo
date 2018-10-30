package engine.compiler.storage;

import engine.compiler.slogoast.Expression;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;

import java.util.*;

/**
 * This StateMachine records variables that the parser can use. For example, it records "x = 3".
 *
 * @author Haotian Wang
 */
public class CrudeStateMachine implements StateMachine {
    private Map<String, VariableType> typeMap;
    private Map<String, Double> doubleMap;
    private Map<String, Integer> integerMap;
    private Map<String, String> stringMap;
    private Map<String, Expression> functionMap;

    private Map<String, Object> aggregateMap;

    private List<StateMachineObserver> observers;

    public CrudeStateMachine() {
        typeMap = new HashMap<>();
        doubleMap = new HashMap<>();
        integerMap = new HashMap<>();
        stringMap = new HashMap<>();
        functionMap = new HashMap<>();
        aggregateMap = new HashMap<>();

        observers = new ArrayList<>();
    }

    public void register(StateMachineObserver observer) { observers.add(observer); }

    /**
     * Returns true if the variable is already defined in the StateMachine and false otherwise.
     *
     * @param key : The String name of the variable to be queried.
     * @return A boolean value indicating whether the variable key is defined in the StateMachine.
     */
    @Override
    public boolean containsVariable(String key) {
        return aggregateMap.containsKey(key);
    }

    public void pushAlarm() { observers.forEach(StateMachineObserver::notifyListener);}

    /**
     * Set a double value for a variable.
     *
     * @param key
     * @param value
     */
    public void setDouble(String key, double value) {
        if (typeMap.containsKey(key)) {
            VariableType type = typeMap.get(key);
            if (type == VariableType.INTEGER) {
                integerMap.remove(key);
            } else if (type == VariableType.STRING) {
                stringMap.remove(key);
            } else if (type == VariableType.EXPRESSION) {
                functionMap.remove(key);
            }
        }
        doubleMap.put(key, value);
        aggregateMap.put(key, value);
        typeMap.put(key, VariableType.DOUBLE);
        pushAlarm();
    }

    /**
     * Set an int value for a variable.
     *
     * @param key
     * @param value
     */
    public void setInteger(String key, int value) {
        if (typeMap.containsKey(key)) {
            VariableType type = typeMap.get(key);
            if (type == VariableType.DOUBLE) {
                doubleMap.remove(key);
            } else if (type == VariableType.STRING) {
                stringMap.remove(key);
            } else if (type == VariableType.EXPRESSION) {
                functionMap.remove(key);
            }
        }
        integerMap.put(key, value);
        aggregateMap.put(key, value);
        typeMap.put(key, VariableType.INTEGER);
        pushAlarm();
    }

    /**
     * Set a String value for a variable.
     *
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        if (typeMap.containsKey(key)) {
            VariableType type = typeMap.get(key);
            if (type == VariableType.DOUBLE) {
                doubleMap.remove(key);
            } else if (type == VariableType.INTEGER) {
                integerMap.remove(key);
            } else if (type == VariableType.EXPRESSION) {
                functionMap.remove(key);
            }
        }
        stringMap.put(key, value);
        aggregateMap.put(key, value);
        typeMap.put(key, VariableType.INTEGER);
        pushAlarm();
    }

    /**
     * Set an Expression value for a variable.
     *
     * @param key
     * @param function
     */
    @Override
    public void setExpression(String key, Expression function) {
        if (typeMap.containsKey(key)) {
            VariableType type = typeMap.get(key);
            if (type == VariableType.DOUBLE) {
                doubleMap.remove(key);
            } else if (type == VariableType.INTEGER) {
                integerMap.remove(key);
            } else if (type == VariableType.STRING) {
                stringMap.remove(key);
            }
        }
        functionMap.put(key, function);
        aggregateMap.put(key, function);
        typeMap.put(key, VariableType.EXPRESSION);
        pushAlarm();
    }

    @Override
    public void setVariable(String key, Object value, VariableType type) {
        if (type == VariableType.INTEGER) {
            setInteger(key, (int) value);
        } else if (type == VariableType.DOUBLE) {
            setDouble(key, (double) value);
        } else if (type == VariableType.STRING) {
            setString(key, (String) value);
        } else if (type == VariableType.EXPRESSION) {
            setExpression(key, (Expression) value);
        }
    }

    /**
     * Get the type of the variable, either a double, an integer or a function.
     *
     * @param key
     * @return A String representation of the type of the variable.
     */
    @Override
    public VariableType getVariableType(String key) throws UndefinedKeywordException {
        if (!typeMap.containsKey(key)) {
            throw new UndefinedKeywordException(String.format("The variable %s is not defined, therefore its type cannot be determined", key));
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
        if (!aggregateMap.containsKey(key)) {
            throw new UndefinedKeywordException(String.format("The variable %s is not defined, therefore its value cannot be returned", key));
        }
        return aggregateMap.get(key);
    }

    /**
     * Remove the key entry from the map.
     *
     * @param key
     */
    public void removeVariable(String key) throws UndefinedKeywordException {
        if (!typeMap.containsKey(key)) {
            throw new UndefinedKeywordException(String.format("The variable %s is not defined, therefore cannot be removed", key));
        }
        VariableType type = typeMap.get(key);
        if (type == VariableType.DOUBLE) {
            doubleMap.remove(key);
        } else if (type == VariableType.INTEGER) {
            integerMap.remove(key);
        } else if (type == VariableType.EXPRESSION) {
            functionMap.remove(key);
        } else if (type == VariableType.STRING) {
            stringMap.remove(key);
        }
        aggregateMap.remove(key);
        pushAlarm();
    }

    /**
     * Clear all state variables in the state machine.
     */
    public void resetState() {
        typeMap.clear();
        integerMap.clear();
        doubleMap.clear();
        functionMap.clear();
        stringMap.clear();
        aggregateMap.clear();
        pushAlarm();
    }

    @Override
    public Map<String, Object> listOfVariables() { return Collections.unmodifiableMap(aggregateMap); }

    /**
     * Present the internal storage of the StateMachine in a list format, separated by newline.
     *
     * @return A String representation of the StateMachine.
     */
    @Override
    public String toString() {
        String ans = "";
        for (Map.Entry entry : integerMap.entrySet()) {
            ans += entry.getKey() + " = " + entry.getValue() + "\n";
        }
        for (Map.Entry entry : doubleMap.entrySet()) {
            ans += entry.getKey() + " = " + entry.getValue() + "\n";
        }
        for (Map.Entry entry : stringMap.entrySet()) {
            ans += entry.getKey() + " = " + entry.getValue() + "\n";
        }
        for (Map.Entry entry : functionMap.entrySet()) {
            ans += entry.getKey() + " = " + entry.getValue() + "\n";
        }
        return ans;
    }
}
