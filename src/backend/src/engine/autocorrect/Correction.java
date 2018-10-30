package engine.autocorrect;

import engine.errors.UndefinedKeywordException;

import java.util.*;

public class Correction implements AutoCorrect {
    private Set<String> set;

    public Correction() {
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
        int min = Integer.MAX_VALUE;
        String ret = "";
        for (String word : set) {
            int distance = calculateDistance(word, input);
            if (distance < min) {
                min = distance;
                ret = word;
            } else if (distance == min && Math.abs(word.length() - input.length()) < Math.abs(ret.length() - input.length())) {
                ret = word;
            }
        }
        return ret;
    }

    /**
     * This method calculates the "distance" between any two Strings. A smaller "distance" indicates that the two Strings are more "similar" and a larger distance indicates that the two Strings are more "different". When the lengths of the two strings are different, their distance is calculated using the sliding screen algorithm.
     *
     * @param a: The first String to be compared.
     * @param b: The second String to be compared.
     * @return An int value suggesting how similar these two Strings are. This value should be non-negative and only zero when the two Strings are exactly the same.
     */
    private int calculateDistance(String a, String b) {
        int result = 0;
        if (a.length() == b.length()) {
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    result++;
                }
            }
            return result;
        } else if (a.length() > b.length()) {
            String temp = b;
            b = a;
            a = temp;
        }
        int slider = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                slider++;
            }
        }
        result = slider;
        for (int i = 1; i <= b.length() - a.length(); i++) {
            slider = slider - a.charAt(i - 1) != b.charAt(i - 1) ? 1 : 0 + a.charAt(i + a.length() - 1) != b.charAt(i + a.length() - 1) ? 1 : 0;
            if (slider < result) {
                result = slider;
            }
        }
        return result;
    }

    /**
     * This gives a list of Strings that are viable, in ascending order of distance.
     *
     * @param input : The input String.
     * @return A List of Strings.
     */
    @Override
    public List<String> listClosestStrings(String input) {
        Set<String> tempSet = new TreeSet<>((a, b) -> {
            int distanceA = calculateDistance(a, input);
            int distanceB = calculateDistance(b, input);
            if (distanceA != distanceB) {
                return distanceA - distanceB;
            } else {
                return Math.abs(a.length() - input.length()) - Math.abs(b.length() - input.length());
            }
        });
        for (String word : set) {
            tempSet.add(word);
        }
        return new ArrayList<>(tempSet);
    }
}
