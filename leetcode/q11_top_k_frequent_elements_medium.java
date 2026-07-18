import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Q11) Top K Frequent Elements
 *
 * AI-BEST:
 * Frequency map + bucket sort.
 * Time: O(n) average, Space: O(n)
 *
 * AI-EASY:
 * Frequency map + sort by frequency.
 * Time: O(n log n), Space: O(n)
 */
public class q11_top_k_frequent_elements_medium {
    static List<Integer> topKBest(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.put(n, freq.getOrDefault(n, 0) + 1);

        List<List<Integer>> buckets = new ArrayList<>();
        for (int i = 0; i <= nums.length; i++) buckets.add(new ArrayList<>());
        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            buckets.get(entry.getValue()).add(entry.getKey());
        }

        List<Integer> result = new ArrayList<>();
        for (int count = buckets.size() - 1; count >= 0 && result.size() < k; count--) {
            for (int value : buckets.get(count)) {
                result.add(value);
                if (result.size() == k) break;
            }
        }
        return result;
    }

    static List<Integer> topKEasy(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.put(n, freq.getOrDefault(n, 0) + 1);

        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(freq.entrySet());
        entries.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, entries.size()); i++) {
            result.add(entries.get(i).getKey());
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("Q11: Top K Frequent Elements");
        int[][] numsCases = {
                {1, 1, 1, 2, 2, 3},
                {1},
                {4, 4, 2, 2, 2, 3, 3}
        };
        int[] kCases = {2, 1, 2};

        for (int i = 0; i < numsCases.length; i++) {
            int[] nums = numsCases[i];
            int k = kCases[i];
            System.out.println("Input: nums=" + Arrays.toString(nums) + ", k=" + k);
            System.out.println("  BEST: " + topKBest(nums, k));
            System.out.println("  EASY: " + topKEasy(nums, k));
        }
    }
}


