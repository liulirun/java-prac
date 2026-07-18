import java.util.Arrays;

/**
 * Q13) Product of Array Except Self
 *
 * AI-BEST:
 * Prefix + suffix products in two passes, no division.
 * Time: O(n), Space: O(1) extra (excluding output)
 *
 * AI-EASY:
 * Nested loop and multiply all indices except current.
 * Time: O(n^2), Space: O(1) extra (excluding output)
 */
public class q13_product_of_array_except_self_medium {
    static int[] productExceptSelfBest(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, 1);

        // Step 1: Fill prefix products.
        // Why: result[i] should contain product of all elements left of i.
        int prefix = 1;
        for (int i = 0; i < n; i++) {
            result[i] = prefix;
            prefix *= nums[i];
        }

        // Step 2: Multiply suffix products from right to left.
        // Why: result[i] becomes left-product * right-product.
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= suffix;
            suffix *= nums[i];
        }

        // Step 3: Return final product array.
        return result;
    }

    static int[] productExceptSelfEasy(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        // Step 1: For each index, multiply all other indices.
        // Why: this is the most direct interpretation of the requirement.
        for (int i = 0; i < n; i++) {
            int product = 1;
            for (int j = 0; j < n; j++) {
                if (j != i) product *= nums[j];
            }
            // Step 2: Save product for i.
            result[i] = product;
        }

        // Step 3: Return computed array.
        return result;
    }

    public static void main(String[] args) {
        System.out.println("Q13: Product of Array Except Self");
        int[][] cases = {
                {1, 2, 3, 4},
                {-1, 1, 0, -3, 3},
                {0, 4, 0},
                {5}
        };

        for (int[] nums : cases) {
            System.out.println("Input: nums=" + Arrays.toString(nums));
            System.out.println("  BEST: " + Arrays.toString(productExceptSelfBest(nums)));
            System.out.println("  EASY: " + Arrays.toString(productExceptSelfEasy(nums)));
        }
    }
}
