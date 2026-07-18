import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Q10) Merge Intervals
 *
 * AI-BEST:
 * Sort by start then sweep once.
 * Time: O(n log n), Space: O(n)
 *
 * AI-EASY:
 * Repeated pairwise merges until stable.
 * Time: O(n^2) or worse, Space: O(n)
 */
public class q10_merge_intervals_medium {
    static List<int[]> mergeBest(int[][] intervals) {
        if (intervals.length <= 1) {
            List<int[]> copy = new ArrayList<>();
            for (int[] interval : intervals) copy.add(Arrays.copyOf(interval, 2));
            return copy;
        }

        int[][] sorted = Arrays.stream(intervals)
                .map(x -> Arrays.copyOf(x, 2))
                .toArray(int[][]::new);
        Arrays.sort(sorted, Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();
        merged.add(sorted[0]);

        for (int i = 1; i < sorted.length; i++) {
            int[] current = sorted[i];
            int[] last = merged.get(merged.size() - 1);
            if (current[0] <= last[1]) {
                last[1] = Math.max(last[1], current[1]);
            } else {
                merged.add(current);
            }
        }
        return merged;
    }

    static List<int[]> mergeEasy(int[][] intervals) {
        List<int[]> arr = new ArrayList<>();
        for (int[] interval : intervals) arr.add(Arrays.copyOf(interval, 2));

        boolean changed = true;
        while (changed) {
            changed = false;
            outer:
            for (int i = 0; i < arr.size(); i++) {
                for (int j = i + 1; j < arr.size(); j++) {
                    if (overlap(arr.get(i), arr.get(j))) {
                        int[] merged = {
                                Math.min(arr.get(i)[0], arr.get(j)[0]),
                                Math.max(arr.get(i)[1], arr.get(j)[1])
                        };
                        arr.set(i, merged);
                        arr.remove(j);
                        changed = true;
                        break outer;
                    }
                }
            }
        }
        arr.sort(Comparator.comparingInt(a -> a[0]));
        return arr;
    }

    static boolean overlap(int[] a, int[] b) {
        return a[0] <= b[1] && b[0] <= a[1];
    }

    static String format(List<int[]> intervals) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < intervals.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(Arrays.toString(intervals.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Q10: Merge Intervals");
        int[][][] cases = {
                {{1, 3}, {2, 6}, {8, 10}, {15, 18}},
                {{1, 4}, {4, 5}},
                {{1, 4}, {0, 2}, {3, 5}}
        };

        for (int[][] intervals : cases) {
            System.out.println("Input: " + Arrays.deepToString(intervals));
            System.out.println("  BEST: " + format(mergeBest(intervals)));
            System.out.println("  EASY: " + format(mergeEasy(intervals)));
        }
    }
}


