package engine.autocorrect;

import engine.errors.UndefinedKeywordException;

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
     * This add a word to the set of vocabulary of the correction machine.
     *
     * @param word: A String to be added.
     */
    void addWord(String word);

    /**
     * This removes a word from the set of vocabulary of the correction machine.
     *
     * @param word: A String to be removed.
     * @throws UndefinedKeywordException
     */
    void removeWord(String word) throws UndefinedKeywordException;

    /**
     * This gives a closest String to the input String.
     *
     * @param input: The input String
     * @return A String that is closest to the input String.
     */
    String closestString(String input);

    /**
     * This gives a list of Strings that are viable, in ascending order of distance.
     *
     * @param input: The input String.
     * @return A List of Strings.
     */
    List<String> listClosestStrings(String input);
}
