import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * Q6) Balanced Parentheses
 *
 * AI-BEST:
 * Stack-based parser.
 * Time: O(n), Space: O(n)
 *
 * AI-EASY:
 * Repeatedly remove (), {}, [] pairs until stable.
 * Time: usually O(n^2) or worse, Space: O(n)
 */
public class q6_balanced_parentheses_easy {
    static boolean isBalancedBest(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        Map<Character, Character> mapping = Map.of(
                ')', '(',
                '}', '{',
                ']', '['
        );

        for (char ch : s.toCharArray()) {
            if (mapping.containsValue(ch)) {
                stack.push(ch);
            } else if (mapping.containsKey(ch)) {
                if (stack.isEmpty() || stack.pop() != mapping.get(ch)) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    static boolean isBalancedEasy(String s) {
        String current = s;

        while (true) {
            String next = current
                    .replace("()", "")
                    .replace("{}", "")
                    .replace("[]", "");

            if (next.length() == current.length()) {
                break;
            }
            current = next;
        }

        return current.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println("Q6: Balanced Parentheses");
        String[] samples = {"{[()]}", "([)]", "(((", "", "a+(b*c)-{d/e}"};

        for (String sample : samples) {
            String filtered = sample.replaceAll("[^(){}\\[\\]]", "");
            System.out.println("Input: \"" + sample + "\"");
            System.out.println("  BEST: " + isBalancedBest(sample));
            System.out.println("  EASY: " + isBalancedEasy(filtered));
        }
    }
}


