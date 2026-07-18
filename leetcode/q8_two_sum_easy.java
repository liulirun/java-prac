import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Q8) Two Sum
 *
 * AI-BEST:
 * One-pass hash map value -> index.
 * Time: O(n), Space: O(n)
 *
 * AI-EASY:
 * Nested loops for all pairs.
 * Time: O(n^2), Space: O(1)
 */
public class q8_two_sum_easy {
    static int[] twoSumBest(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (seen.containsKey(need)) return new int[]{seen.get(need), i};
            seen.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    static int[] twoSumEasy(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) return new int[]{i, j};
            }
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        System.out.println("Q8: Two Sum");
        int[][] numsCases = {
                {2, 7, 11, 15},
                {3, 2, 4},
                {3, 3}
        };
        int[] targets = {9, 6, 6};

        for (int i = 0; i < numsCases.length; i++) {
            int[] nums = numsCases[i];
            int target = targets[i];
            System.out.println("Input: nums=" + Arrays.toString(nums) + ", target=" + target);
            System.out.println("  BEST: " + Arrays.toString(twoSumBest(nums, target)));
            System.out.println("  EASY: " + Arrays.toString(twoSumEasy(nums, target)));
        }
    }
}


