import java.util.HashMap;
import java.util.Map;

/**
 * Q1) Minimum Window Substring
 *
 * AI-BEST:
 * Sliding window + frequency maps.
 * Time: O(n + m), Space: O(k)
 *
 * AI-EASY:
 * Check all substrings and validate.
 * Time: O(n^3) naive, Space: O(k)
 */
public class q1_min_window_substring_hard {
    static String minWindowBest(String s, String t) {
        if (s == null || t == null || s.isEmpty() || t.isEmpty()) return "";

        Map<Character, Integer> need = new HashMap<>();
        for (char ch : t.toCharArray()) {
            need.put(ch, need.getOrDefault(ch, 0) + 1);
        }

        int required = need.size();
        int formed = 0;
        int left = 0;
        Map<Character, Integer> window = new HashMap<>();
        int bestLen = Integer.MAX_VALUE;
        int bestL = 0;
        int bestR = 0;

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            window.put(ch, window.getOrDefault(ch, 0) + 1);

            if (need.containsKey(ch) && window.get(ch).intValue() == need.get(ch).intValue()) {
                formed++;
            }

            while (left <= right && formed == required) {
                int len = right - left + 1;
                if (len < bestLen) {
                    bestLen = len;
                    bestL = left;
                    bestR = right;
                }

                char leftChar = s.charAt(left);
                window.put(leftChar, window.get(leftChar) - 1);
                if (need.containsKey(leftChar) && window.get(leftChar) < need.get(leftChar)) {
                    formed--;
                }
                left++;
            }
        }

        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestL, bestR + 1);
    }

    static String minWindowEasy(String s, String t) {
        if (s == null || t == null || s.isEmpty() || t.isEmpty() || t.length() > s.length()) return "";

        String best = "";
        for (int start = 0; start < s.length(); start++) {
            for (int end = start; end < s.length(); end++) {
                String candidate = s.substring(start, end + 1);
                if (coversAll(candidate, t)) {
                    if (best.isEmpty() || candidate.length() < best.length()) {
                        best = candidate;
                    }
                }
            }
        }
        return best;
    }

    static boolean coversAll(String candidate, String t) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char ch : candidate.toCharArray()) {
            counts.put(ch, counts.getOrDefault(ch, 0) + 1);
        }
        for (char ch : t.toCharArray()) {
            int next = counts.getOrDefault(ch, 0) - 1;
            if (next < 0) return false;
            counts.put(ch, next);
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Q1: Minimum Window Substring");
        String[][] samples = {
                {"ADOBECODEBANC", "ABC"},
                {"a", "a"},
                {"a", "aa"}
        };
        for (String[] sample : samples) {
            String s = sample[0];
            String t = sample[1];
            System.out.println("Input: s=\"" + s + "\", t=\"" + t + "\"");
            System.out.println("  BEST: " + minWindowBest(s, t));
            System.out.println("  EASY: " + minWindowEasy(s, t));
        }
    }
}


