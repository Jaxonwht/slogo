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

    void registerSelectionListener(SelectionListener listener);
    void registerUIListener(UIListener listener);

    /**
     * This method returns the index of the image that gets stamped, to stamp the current turtles at where they are.
     *
     * @return Index of the image.
     */
    double stamp();

    /**
     * This method clears the stamps on the current canvas.
     *
     * @return 1 if stamps are cleared and 0 otherwise.
     */
    double clearStamps();
}
