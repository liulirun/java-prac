import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Q9) Valid Anagram
 *
 * AI-BEST:
 * Frequency counting map.
 * Time: O(n), Space: O(k)
 *
 * AI-EASY:
 * Sort both strings and compare.
 * Time: O(n log n), Space: depends on sorting implementation
 */
public class q9_valid_anagram_easy {
    static boolean isAnagramBest(String s, String t) {
        if (s.length() != t.length()) return false;

        Map<Character, Integer> counts = new HashMap<>();
        for (char ch : s.toCharArray()) {
            counts.put(ch, counts.getOrDefault(ch, 0) + 1);
        }
        for (char ch : t.toCharArray()) {
            int next = counts.getOrDefault(ch, 0) - 1;
            if (next < 0) return false;
            counts.put(ch, next);
        }

        for (int value : counts.values()) {
            if (value != 0) return false;
        }
        return true;
    }

    static boolean isAnagramEasy(String s, String t) {
        if (s.length() != t.length()) return false;

        char[] a = s.toCharArray();
        char[] b = t.toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        return Arrays.equals(a, b);
    }

    public static void main(String[] args) {
        System.out.println("Q9: Valid Anagram");
        String[][] samples = {
                {"anagram", "nagaram"},
                {"rat", "car"},
                {"listen", "silent"}
        };
        for (String[] sample : samples) {
            String s = sample[0];
            String t = sample[1];
            System.out.println("Input: s=\"" + s + "\", t=\"" + t + "\"");
            System.out.println("  BEST: " + isAnagramBest(s, t));
            System.out.println("  EASY: " + isAnagramEasy(s, t));
        }
    }
}


