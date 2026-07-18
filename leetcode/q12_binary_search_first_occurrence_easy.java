import java.util.Arrays;

/**
 * Q12) Binary Search (Find First Occurrence)
 *
 * AI-BEST:
 * Modified binary search that continues left after match.
 * Time: O(log n), Space: O(1)
 */
public class q12_binary_search_first_occurrence_easy {
    static int firstOccurrenceBest(int[] nums, int target) {
        // Step 1: Keep search range and a stored answer.
        // Why: we need the LEFTMOST index, not just any matching index.
        int left = 0;
        int right = nums.length - 1;
        int answer = -1;

        // Step 2: Binary search; move left even after a match.
        // Why: there might be an earlier occurrence on the left side.
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                answer = mid;
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Step 3: Return stored leftmost index or -1.
        return answer;
    }

    public static void main(String[] args) {
        System.out.println("Q12: Binary Search First Occurrence");
        int[][] numsCases = {
                {1, 2, 2, 2, 3, 4},
                {1, 3, 5, 7},
                {8, 8, 8, 8}
        };
        int[] targets = {2, 4, 8};

        for (int i = 0; i < numsCases.length; i++) {
            int[] nums = numsCases[i];
            int target = targets[i];
            System.out.println("Input: nums=" + Arrays.toString(nums) + ", target=" + target);
            System.out.println("  BEST: " + firstOccurrenceBest(nums, target));
        }
    }
}


