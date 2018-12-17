package model;

/**
 * This interface is an event listener that listens to user input "stamp" and "clear stamps"
 *
 * @author Haotian Wang
 */
public interface StampListener {
    double updateOnStamp();

    double updateOnClearStamps();
}
