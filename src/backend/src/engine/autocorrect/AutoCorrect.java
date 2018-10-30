package engine.autocorrect;

import java.util.List;
import java.util.Set;

/**
 * This interface handles both automatic correction and completion. Both procedures take the same form, therefore they are grouped under one interface.
 *
 * @author Haotian Wang
 */
public interface AutoCorrect {
    /**
     * Reads in a set of vocabulary in which the closest Strings are to be found.
     *
     * @param vocab: A Set of Strings that the interface checks to find the closest String or a list of closest Strings.
     */
    void readSet(Set<String> vocab);

    /**
     * This gives a closest String to the input String.
     *
     * @param string: The input String
     * @return A String that is closest to the input String.
     */
    String closestString(String string);

    /**
     * This gives a list of Strings that are viable, in ascending order of distance.
     *
     * @param string: The input String.
     * @return A List of Strings.
     */
    List<String> listClosestStrings(String string);
}
