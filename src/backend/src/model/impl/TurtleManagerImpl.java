package model.impl;

import engine.compiler.storage.StateMachine;
import engine.errors.IllegalParameterException;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TurtleManagerImpl implements TurtleManager {
    private StateMachine memory;
    private ObservableMap<Integer, TurtleModel> turtleModels;
    private List<Integer> selected;
    private List<SelectionListener> selectionListeners;

    public TurtleManagerImpl() {
        turtleModels = FXCollections.observableMap(new HashMap<>());
        selected = new ArrayList<>(ModelModule.INITIAL_TURTLE_ID);
        selectionListeners = new ArrayList<>();
    }

    @Override
    public int id() {
        if(selected.isEmpty()) return -1;
        else return selected.get(selected.size()-1);
    }

    @Override
    public List<Integer> selected() { return selected; }

    @Override
    public int size() { return turtleModels.size(); }

    @Override
    public int addTurtle(int id) throws IllegalParameterException {
        if(id <= 0) throw new IllegalParameterException("Turtle's ID must be STRICTLY bigger than 0");
        var newTurtle = new TurtleModelImpl(memory);
        turtleModels.put(id, newTurtle);
        return id;
    }

    @Override
    public ObservableMap<Integer, TurtleModel> turtleModels() { return turtleModels; }

    private List<Integer> checkWildcard(List<Integer> indices) {
        return indices.contains(ALL) ? new ArrayList<>(turtleModels.keySet()) : indices;
    }

    @Override
    public int tell(List<Integer> turtleIDs) {
        turtleIDs.stream().filter(idx -> !turtleModels.containsKey(idx)).forEach(idx -> {
            try {
                addTurtle(idx);
            } catch (IllegalParameterException e) { }
        });

        if(turtleIDs.isEmpty()) selected.clear();
        else selected = checkWildcard(turtleIDs);
        selectionListeners.forEach(listener -> listener.selectionUpdated(selected));
        return id();
    }

    @Override
    public StateMachine memory() { return memory; }

    @Override
    public void equipMemory(StateMachine memory) {
        turtleModels.values().forEach(model -> model.equipMemory(memory));
        this.memory = memory;
    }

    @Override
    public void registerSelectionListener(SelectionListener listener) {
        selectionListeners.add(listener);
    }


    private <T> T batchOperation(TurtleOperations<T> ops) throws InterpretationException, UndefinedKeywordException {
        if(selected.size() == 0) throw new InterpretationException("None of the turtles were selected.");
        var results = new ArrayList<T>();
        ObservableMap<Integer, TurtleModel> integerTurtleModelObservableMap = turtleModels;
        for (Integer integer : selected) {
            TurtleModel turtleModel = integerTurtleModelObservableMap.get(integer);
            T op = ops.op(turtleModel);
            results.add(op);
        }
        return results.get(results.size()-1);
    }

    @Override
    public double setPenDown(boolean down) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.setPenDown(down));
    }

    @Override
    public double setVisible(boolean visible) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.setVisible(visible));
    }

    @Override
    public double forward(double by) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.forward(by));
    }

    @Override
    public double moveTo(double x, double y, boolean forcePenUp) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.moveTo(x, y, forcePenUp));
    }

    @Override
    public double setAngle(double angle) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.setAngle(angle));
    }

    @Override
    public double leftBy(double angle) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.leftBy(angle));
    }

    @Override
    public double getX() throws InterpretationException, UndefinedKeywordException { return batchOperation(TurtleModel::getX); }

    @Override
    public double getY() throws InterpretationException, UndefinedKeywordException { return batchOperation(TurtleModel::getY); }

    @Override
    public double getAngle() throws InterpretationException, UndefinedKeywordException { return batchOperation(TurtleModel::getAngle); }

    @Override
    public boolean isPenDown() throws InterpretationException, UndefinedKeywordException { return batchOperation(TurtleModel::isPenDown);}

    @Override
    public boolean isVisible() throws InterpretationException, UndefinedKeywordException { return batchOperation(TurtleModel::isVisible); }

    @Override
    public double clear() throws InterpretationException, UndefinedKeywordException { return batchOperation(TurtleModel::clear); }

    @Override
    public int setBackground(int index) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.setBackground(index));
    }

    @Override
    public int setPenColor(int index) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.setPenColor(index));
    }

    @Override
    public int setPenSize(int pixels) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.setPenSize(pixels));
    }

    @Override
    public int setShape(int index) throws InterpretationException, UndefinedKeywordException {
        return batchOperation(t -> t.setShape(index));
    }

    /**
     *  Used for individual turtles only
     */
    @Override
    public SimpleBooleanProperty isPenDownModel() { return null; }
    @Override
    public SimpleBooleanProperty isVisibleModel() { return null; }
    @Override
    public PosAndAngle posAndAngleModel() { return null; }
    @Override
    public void registerClearListener(ClearListener cl) { }
    @Override
    public void registerUIListener(UIListener listener) { }

    /**
     * This method returns the index of the image that gets stamped, to stamp the current turtles at where they are.
     *
     * @return Index of the image that gets stamped.
     */
    @Override
    public double stamp() {
        return 1;
    }

    /**
     * This method clears the stamps on the current canvas.
     *
     * @return 1 if stamps are cleared and 0 otherwise.
     */
    @Override
    public double clearStamps() {
        return 0;
    }
}
