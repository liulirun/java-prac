import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Q14) Group Anagrams
 *
 * AI-BEST:
 * Sort chars in each word to build canonical key.
 * Time: O(n * k log k), Space: O(n * k)
 *
 * AI-EASY:
 * Compare each word against existing groups via char counts.
 * Time: O(n^2 * k), Space: O(n * k)
 */
public class q14_group_anagrams_medium {
    static List<List<String>> groupAnagramsBest(String[] words) {
        // Step 1: Build map from canonical key to grouped words.
        // Why: anagrams share the same sorted-character signature.
        Map<String, List<String>> groupsByKey = new HashMap<>();
        for (String word : words) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            groupsByKey.computeIfAbsent(key, ignored -> new ArrayList<>()).add(word);
        }

        // Step 2: Return grouped values.
        return new ArrayList<>(groupsByKey.values());
    }

    static List<List<String>> groupAnagramsEasy(String[] words) {
        // Step 1: Place each word into first matching group by manual check.
        // Why: this keeps logic straightforward but performs more comparisons.
        List<List<String>> groups = new ArrayList<>();
        for (String word : words) {
            boolean placed = false;
            for (List<String> group : groups) {
                if (isAnagramByCount(word, group.get(0))) {
                    group.add(word);
                    placed = true;
                    break;
                }
            }
            // Step 2: Create new group if no match was found.
            if (!placed) {
                List<String> newGroup = new ArrayList<>();
                newGroup.add(word);
                groups.add(newGroup);
            }
        }

        // Step 3: Return final groups.
        return groups;
    }

    static boolean isAnagramByCount(String a, String b) {
        if (a.length() != b.length()) return false;
        int[] freq = new int[26];
        for (int i = 0; i < a.length(); i++) {
            freq[a.charAt(i) - 'a']++;
            freq[b.charAt(i) - 'a']--;
        }
        for (int value : freq) {
            if (value != 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Q14: Group Anagrams");
        String[][] cases = {
                {"eat", "tea", "tan", "ate", "nat", "bat"},
                {""},
                {"a"},
                {"abc", "bca", "cab", "foo", "ofo"}
        };

        for (String[] words : cases) {
            System.out.println("Input: words=" + Arrays.toString(words));
            System.out.println("  BEST: " + groupAnagramsBest(words));
            System.out.println("  EASY: " + groupAnagramsEasy(words));
        }
    }
}
