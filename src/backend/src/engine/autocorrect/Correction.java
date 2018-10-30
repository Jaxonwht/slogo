package engine.autocorrect;

import java.util.List;
import java.util.Set;

public class Correction implements AutoCorrect {
    private Set<String> set;

    public Correction() {}

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
     * This method calculates the "distance" between any two Strings. A smaller "distance" indicates that the two Strings are more "similar" and a larger distance indicates that the two Strings are more "different".
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
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt()) {
        }
    }

    /**
     * This gives a list of Strings that are viable, in ascending order of distance.
     *
     * @param input : The input String.
     * @return A List of Strings.
     */
    @Override
    public List<String> listClosestStrings(String input) {
        return null;
    }
}
