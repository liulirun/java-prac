import java.util.HashMap;
import java.util.Map;

/**
 * Q5) Longest Substring Without Repeating Characters
 *
 * AI-BEST:
 * Sliding window + last seen index map.
 * Time: O(n), Space: O(k)
 *
 * AI-EASY:
 * String window with indexOf + substring.
 * Time: can degrade to O(n^2), Space: moderate
 */
public class q5_longest_substring_without_repeating_characters_medium {
    static String longestSubstringBest(String s) {
        int left = 0;
        int bestStart = 0;
        int bestLen = 0;
        Map<Character, Integer> lastSeen = new HashMap<>();

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            if (lastSeen.containsKey(ch) && lastSeen.get(ch) >= left) {
                // ABCB , when Second B found,  lastSeen.get(ch) == 1, 
                // so make the left window to go one bit after first B

                //AVOID: Example: A B B A
                // Window becomes [B] at the second B.
                // When you hit the second A, its "last seen" was at index 0.
                // If you didn't check Step 2, you'd move your left pointer back to index 1, making your window B B A (which has two Bs!).
                left = lastSeen.get(ch) + 1;
            }
            lastSeen.put(ch, right);

            int currentLen = right - left + 1;
            if (currentLen > bestLen) {
                bestLen = currentLen;
                bestStart = left;
            }
        }

        return s.substring(bestStart, bestStart + bestLen);
    }

    static String longestSubstringEasy(String s) {
        String currentWindow = "";
        String longestFound = "";

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            int duplicateIndex = currentWindow.indexOf(ch);

            if (duplicateIndex >= 0) {
                currentWindow = currentWindow.substring(duplicateIndex + 1) + ch;
            } else {
                currentWindow += ch;
            }

            if (currentWindow.length() > longestFound.length()) {
                longestFound = currentWindow;
            }
        }
        return longestFound;
    }

    public static void main(String[] args) {
        System.out.println("Q5: Longest Substring Without Repeating Characters");
        String[] samples = {"abcdbcabcbb", "bbbbb", "pwwkew", "", "au"};
        for (String s : samples) {
            System.out.println("Input: \"" + s + "\"");
            System.out.println("  BEST: " + longestSubstringBest(s));
            System.out.println("  EASY: " + longestSubstringEasy(s));
        }
    }
}


