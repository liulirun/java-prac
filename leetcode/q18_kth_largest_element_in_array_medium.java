import java.util.Arrays;
import java.util.Random;

/**
 * Q18) Kth Largest Element in an Array
 *
 * AI-BEST:
 * Quickselect with partition.
 * Time: O(n) average, O(n^2) worst; Space: O(1) extra
 *
 * AI-EASY:
 * Sort descending and read index k - 1.
 * Time: O(n log n), Space: sort-dependent
 */
public class q18_kth_largest_element_in_array_medium {
    private static final Random RANDOM = new Random();

    static int kthLargestBest(int[] nums, int k) {
        int[] arr = Arrays.copyOf(nums, nums.length);
        int targetIndex = arr.length - k;
        int left = 0, right = arr.length - 1;

        // Step 1: Partition repeatedly until pivot hits target index.
        // Why: only one side can contain target, so we avoid full sorting.
        while (left <= right) {
            int pivotIndex = partition(arr, left, right);
            if (pivotIndex == targetIndex) return arr[pivotIndex];
            if (pivotIndex < targetIndex) left = pivotIndex + 1;
            else right = pivotIndex - 1;
        }
        return -1;
    }

    static int partition(int[] arr, int left, int right) {
        int randomIndex = left + RANDOM.nextInt(right - left + 1);
        swap(arr, randomIndex, right);
        int pivot = arr[right];

        int store = left;
        for (int i = left; i < right; i++) {
            if (arr[i] <= pivot) {
                swap(arr, i, store);
                store++;
            }
        }
        swap(arr, store, right);
        return store;
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static int kthLargestEasy(int[] nums, int k) {
        // Step 1: Clone and sort ascending.
        // Why: easiest reliable implementation to write and verify quickly.
        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted);

        // Step 2: Read kth largest via n - k index.
        return sorted[sorted.length - k];
    }

    public static void main(String[] args) {
        System.out.println("Q18: Kth Largest Element in an Array");
        int[][] numsCases = {
                {3, 2, 1, 5, 6, 4},
                {3, 2, 3, 1, 2, 4, 5, 5, 6},
                {9}
        };
        int[] kCases = {2, 4, 1};

        for (int i = 0; i < numsCases.length; i++) {
            int[] nums = numsCases[i];
            int k = kCases[i];
            System.out.println("Input: nums=" + Arrays.toString(nums) + ", k=" + k);
            System.out.println("  BEST: " + kthLargestBest(nums, k));
            System.out.println("  EASY: " + kthLargestEasy(nums, k));
        }
    }
}
