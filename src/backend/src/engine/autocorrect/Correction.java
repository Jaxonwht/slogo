package engine.autocorrect;

import java.util.List;
import java.util.Set;

public class Correction implements AutoCorrect {
    /**
     * Reads in a set of vocabulary in which the closest Strings are to be found.
     *
     * @param vocab : A Set of Strings that the interface checks to find the closest String or a list of closest Strings.
     */
    @Override
    public void readSet(Set<String> vocab) {

    }

    /**
     * This gives a closest String to the input String.
     *
     * @param string : The input String
     * @return A String that is closest to the input String.
     */
    @Override
    public String closestString(String string) {
        return null;
    }

    /**
     * This gives a list of Strings that are viable, in ascending order of distance.
     *
     * @param string : The input String.
     * @return A List of Strings.
     */
    @Override
    public List<String> listClosestStrings(String string) {
        return null;
    }
}
