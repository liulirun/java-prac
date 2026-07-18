/**
 * Q4) Longest Palindromic Substring
 *
 * AI-BEST:
 * Manacher's algorithm.
 * Time: O(n), Space: O(n)
 *
 * AI-EASY:
 * Expand around center (odd/even).
 * Time: O(n^2), Space: O(1) extra
 */
public class q4_longest_palindromic_substring_medium {
    static String longestPalindromeBest(String s) {
        if (s == null || s.isEmpty()) return "";

        // 1. PRE-PROCESS: Add boundaries and separators (#) 
        // This turns "aba" into "^#a#b#a#$" to handle even/odd lengths uniformly.
        StringBuilder transformed = new StringBuilder("^");
        for (int i = 0; i < s.length(); i++) {
            transformed.append('#').append(s.charAt(i));
        }
        transformed.append("#$");

        String t = transformed.toString();
        int[] p = new int[t.length()]; // p[i] stores the radius of palindrome at index i
        int center = 0; // Center of the furthest reaching palindrome
        int right = 0;  // Right boundary of the furthest reaching palindrome

        // 2. CORE LOOP: Iterate through the transformed string
        for (int i = 1; i < t.length() - 1; i++) {
            // Calculate the 'mirror' of index i relative to the current center
            int mirror = 2 * center - i;

            // If i is within the 'right' boundary, jump-start p[i] using symmetry
            if (i < right) {
                p[i] = Math.min(right - i, p[mirror]);
            }

            // Attempt to expand the palindrome centered at i
            // The sentinels (^ and $) prevent this loop from going out of bounds
            while (t.charAt(i + 1 + p[i]) == t.charAt(i - 1 - p[i])) {
                p[i]++;
            }

            // If the newly expanded palindrome goes further right, update center/right
            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }
        }

        // 3. FIND MAXIMUM: Locate the largest radius in the p[] array
        int maxLen = 0;
        int centerIndex = 0;
        for (int i = 1; i < p.length - 1; i++) {
            if (p[i] > maxLen) {
                maxLen = p[i];
                centerIndex = i;
            }
        }

        // 4. MAP BACK: Convert the transformed index back to original string coordinates
        // (centerIndex - maxLen) / 2 correctly finds the original starting index
        int start = (centerIndex - maxLen) / 2;
        return s.substring(start, start + maxLen);
    }


    static String longestPalindromeEasy(String s) {
        if (s == null || s.isEmpty()) return "";

        String best = "";
        for (int i = 0; i < s.length(); i++) {
            String odd = expandAroundCenter(s, i, i);
            String even = expandAroundCenter(s, i, i + 1);
            if (odd.length() > best.length()) best = odd;
            if (even.length() > best.length()) best = even;
        }
        return best;
    }

    static String expandAroundCenter(String s, int left, int right) {
        int l = left;
        int r = right;
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        return s.substring(l + 1, r);
    }

    public static void main(String[] args) {
        System.out.println("Q4: Longest Palindromic Substring");
        String[] samples = {"baabad", "babad", "cbbd", "a", ""};
        for (String s : samples) {
            System.out.println("Input: \"" + s + "\"");
            System.out.println("  BEST: " + longestPalindromeBest(s));
            System.out.println("  EASY: " + longestPalindromeEasy(s));
        }
    }
}


