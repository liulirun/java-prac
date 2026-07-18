import java.util.HashMap;
import java.util.Map;

/**
 * Q3) First Non-Repeating Character Index
 *
 * AI-BEST:
 * Two pass frequency map.
 * Time: O(n), Space: O(k)
 *
 */
public class q3_first_non_repeating_char_easy {
    static int firstUniqueIndexBest(String s) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char ch : s.toCharArray()) {
            counts.put(ch, counts.getOrDefault(ch, 0) + 1);
        }
        for (int i = 0; i < s.length(); i++) {
            if (counts.getOrDefault(s.charAt(i), 0) == 1) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("Q3: First Non-Repeating Character Index");
        String[] samples = {"aabcc", "aacc", "leetcode", "aabbccd"};
        for (String s : samples) {
            System.out.println("Input: \"" + s + "\"");
            System.out.println("  BEST: " + firstUniqueIndexBest(s));
        }
    }
}


