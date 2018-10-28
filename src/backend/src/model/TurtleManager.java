package model;

import engine.compiler.storage.StateMachine;
import engine.errors.IllegalParameterException;
import javafx.collections.ObservableMap;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

/**
 *  An extension to the TurtleModel;
 *  It has all the methods within the TurtleModel but it applies the method to
 *  all the selected turtles.
 */
public interface TurtleManager extends TurtleModel {
    /**
     *  SPECIAL ID to represent ALL TURTLES within this model
     */
    int ALL = 0;

    /**
     * @return id of the LAST turtle within the selected group, always > 0
     */
    int id();

    /**
     * @return id of the LAST turtle within the selected group, always > 0
     */
    List<Integer> selected();

    /**
     * @return total number of turtles created
     */
    int size();

    /**
     * Adds a turtle with the given ID
     * REJECTS ID <= 0
     * @return the ID of the turtle that we just added
     */
    int addTurtle(int id) throws IllegalParameterException;

    /**
     * Returns ObservableMap of (ID, TurtleModel)
     */
    ObservableMap<Integer, TurtleModel> turtleModels();

    /**
     *  Runs ops on every selected turtles
     */
    <T> T foreach(TurtleOperations<T> ops);

    /**
     * Selects all the turtles with given IDs
     * All operations will operate only to these selected turtles
     * @return id()
     */
    int tell(List<Integer> turtleIDs);

    /**
     * @return StateMachine
     */
    StateMachine memory();
    void equipMemory(StateMachine memory);

    /**
     * I honestly feel like this shouldn't be here ... but it's easy to do
     * @blame inchan hwang
     */
    int setBackground(int index);

    /**
     * But these should definitely be here
     */
    int setPenColor(int index);
    int setPenSize(int pixels);
    int setShape(int index);
    int setPalette(int index, int r, int g, int b);
    int penColor();
    int shape();

    void registerSelectionListener(SelectionListener listener);
}
