import java.util.Arrays;

/**
 * Q15) Maximum Subarray
 *
 * AI-BEST:
 * Kadane's algorithm.
 * Time: O(n), Space: O(1)
 */
public class q15_maximum_subarray_easy {
    static int maxSubarrayBest(int[] nums) {
        if (nums.length == 0) return 0;

        // Step 1: Initialize with first number.
        // Why: this correctly handles all-negative arrays.
        int currentSum = nums[0];
        int bestSum = nums[0];

        // Step 2: Decide extend vs restart at each position.
        // Why: optimal subarray ending at i depends on i - 1 only.
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            bestSum = Math.max(bestSum, currentSum);
        }

        // Step 3: Return maximum contiguous sum found.
        return bestSum;
    }

    public static void main(String[] args) {
        System.out.println("Q15: Maximum Subarray");
        int[][] cases = {
                {-2, 1, -3, 4, -1, 2, 1, -5, 4},
                {1},
                {5, 4, -1, 7, 8},
                {-3, -2, -7}
        };

        for (int[] nums : cases) {
            System.out.println("Input: nums=" + Arrays.toString(nums));
            System.out.println("  BEST: " + maxSubarrayBest(nums));
        }
    }
}
