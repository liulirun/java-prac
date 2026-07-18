import java.util.Arrays;

/**
 * Q19) Move Zeroes
 *
 * AI-BEST:
 * Two pointers (read/write) in-place.
 * Time: O(n), Space: O(1)
 */
public class q19_move_zeroes_easy {
    static int[] moveZeroesBest(int[] nums) {
        // Step 1: Write non-zero values to front in original order.
        // Why: this preserves stable ordering of non-zero elements.
        int write = 0;
        for (int read = 0; read < nums.length; read++) {
            if (nums[read] != 0) {
                nums[write] = nums[read];
                write++;
            }
        }

        // Step 2: Fill the remaining tail with zeroes.
        // Why: everything beyond write is logically empty after compaction.
        while (write < nums.length) {
            nums[write] = 0;
            write++;
        }

        // Step 3: Return for demo display.
        return nums;
    }

    public static void main(String[] args) {
        System.out.println("Q19: Move Zeroes");
        int[][] cases = {
                {0, 1, 0, 3, 12},
                {0},
                {4, 1, 2},
                {0, 0, 0, 5}
        };

        for (int[] nums : cases) {
            int[] copy = Arrays.copyOf(nums, nums.length);
            System.out.println("Input: nums=" + Arrays.toString(nums));
            System.out.println("  BEST: " + Arrays.toString(moveZeroesBest(copy)));
        }
    }
}
