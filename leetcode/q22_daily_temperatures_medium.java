import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Q22) Daily Temperatures
 *
 * AI-BEST:
 * Monotonic decreasing stack of indices.
 * Time: O(n), Space: O(n)
 *
 * AI-EASY:
 * For each day, scan forward for first warmer day.
 * Time: O(n^2), Space: O(1) extra
 */
public class q22_daily_temperatures_medium {
    static int[] dailyTemperaturesBest(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        // Step 1: Iterate once and resolve waiting indices when warmer day appears.
        // Why: each index is pushed/popped once, giving linear complexity.
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prev = stack.pop();
                answer[prev] = i - prev;
            }

            // Step 2: Push unresolved day index.
            stack.push(i);
        }

        // Step 3: Remaining stack entries already default to 0.
        return answer;
    }

    static int[] dailyTemperaturesEasy(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];

        // Step 1: For each day, scan days ahead.
        // Why: straightforward baseline, easy to understand and debug.
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (temperatures[j] > temperatures[i]) {
                    answer[i] = j - i;
                    break;
                }
            }
        }

        // Step 2: Return waits (or 0 where no warmer day exists).
        return answer;
    }

    public static void main(String[] args) {
        System.out.println("Q22: Daily Temperatures");
        int[][] cases = {
                {73, 74, 75, 71, 69, 72, 76, 73},
                {30, 40, 50, 60},
                {30, 60, 90}
        };

        for (int[] temperatures : cases) {
            System.out.println("Input: temperatures=" + Arrays.toString(temperatures));
            System.out.println("  BEST: " + Arrays.toString(dailyTemperaturesBest(temperatures)));
            System.out.println("  EASY: " + Arrays.toString(dailyTemperaturesEasy(temperatures)));
        }
    }
}
