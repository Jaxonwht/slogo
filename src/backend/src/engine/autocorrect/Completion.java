package engine.autocorrect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class handles the automatic completion of Strings at backend.
 *
 * @author Haotian Wang
 */
public class Completion implements AutoCorrect {
    private Set<String> set;

    public Completion() {

    }

    /**
     * Reads in a set of vocabulary in which the closest Strings are to be found.
     *
     * @param vocab : A Set of Strings that the interface checks to find the closest String or a list of closest Strings.
     */
    @Override
    public void readSet(Set<String> vocab) {
        set = vocab;
    }

    /**
     * This gives a closest String to the input String.
     *
     * @param input : The input String
     * @return A String that is closest to the input String.
     */
    @Override
    public String closestString(String input) {
        Set<String> realVocab = new HashSet<>();
        for (String word : set) {
            if (word.startsWith(input)) {
                realVocab.add(word);
            }
        }
        
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
