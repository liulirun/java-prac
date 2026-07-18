import java.util.Arrays;

/**
 * Q16) Best Time to Buy and Sell Stock
 *
 * AI-BEST:
 * Track lowest price so far and best profit.
 * Time: O(n), Space: O(1)
 */
public class q16_best_time_to_buy_and_sell_stock_easy {
    static int maxProfitBest(int[] prices) {
        // Step 1: Track minimum buy price while scanning.
        // Why: best profit on any day depends on cheapest prior day.
        int minPrice = Integer.MAX_VALUE;
        int bestProfit = 0;

        // Step 2: Update min and best profit in one pass.
        for (int price : prices) {
            minPrice = Math.min(minPrice, price);
            bestProfit = Math.max(bestProfit, price - minPrice);
        }

        // Step 3: Return maximum one-transaction profit.
        return bestProfit;
    }

    public static void main(String[] args) {
        System.out.println("Q16: Best Time to Buy and Sell Stock");
        int[][] cases = {
                {7, 1, 5, 3, 6, 4},
                {7, 6, 4, 3, 1},
                {2, 4, 1}
        };

        for (int[] prices : cases) {
            System.out.println("Input: prices=" + Arrays.toString(prices));
            System.out.println("  BEST: " + maxProfitBest(prices));
        }
    }
}
