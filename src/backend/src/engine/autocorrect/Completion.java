package engine.autocorrect;

import engine.errors.UndefinedKeywordException;

import java.util.*;

/**
 * This class handles the automatic completion of Strings at backend. Closeness is simply determined by the length difference between a vocabulary word and a target word.
 *
 * @author Haotian Wang
 */
public class Completion implements AutoCorrect {
        private Set<String> set;

    public Completion() {
        set = new HashSet<>();
    }

    /**
     * Reads in a set of vocabulary in which the closest Strings are to be found.
     *
     * @param vocab : A Set of Strings that the interface checks to find the closest String or a list of closest Strings.
     */
    @Override
    public void readSet(Set<String> vocab) {
        set.addAll(vocab);
    }

    /**
     * This add a word to the set of vocabulary of the correction machine.
     *
     * @param word : A String to be added.
     */
    @Override
    public void addWord(String word) {
        set.add(word);
    }

    /**
     * This removes a word from the set of vocabulary of the correction machine.
     *
     * @param word : A String to be removed.
     * @throws UndefinedKeywordException
     */
    @Override
    public void removeWord(String word) throws UndefinedKeywordException {
        if (!set.contains(word)) {
            throw new UndefinedKeywordException(String.format("The word \"%s\" is not defined, therefore it cannot be removed"));
        }
        set.remove(word);
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
            if (word.toLowerCase().startsWith(input.toLowerCase())) {
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
            if (word.toLowerCase().startsWith(input.toLowerCase())) {
                tempSet.add(word);
            }
        }
        return new ArrayList<>(tempSet);
    }
}
