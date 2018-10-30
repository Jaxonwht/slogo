package engine.autocorrect;

import java.util.*;

/**
 * This class handles the automatic completion of Strings at backend. Closeness is simply determined by the length difference between a vocabulary word and a target word.
 *
 * @author Haotian Wang
 */
public class Completion implements AutoCorrect {
        private Set<String> set;

    public Completion() {}

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
        String ret = "";
        int min = Integer.MAX_VALUE;
        for (String word : set) {
            if (word.startsWith(input)) {
                if (word.length() < min) {
                    min = word.length();
                    ret = word;
                }
            }
        }
        return ret;
    }

    /**
     * This gives a list of Strings that are viable, in ascending order of distance.
     *
     * @param input : The input String.
     * @return A List of Strings.
     */
    @Override
    public List<String> listClosestStrings(String input) {
        Set<String> tempSet = new TreeSet<>(Comparator.comparingInt(String::length));
        for (String word : set) {
            if (word.startsWith(input)) {
                tempSet.add(word);
            }
        }
        return new ArrayList<>(tempSet);
    }
}
