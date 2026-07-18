import java.util.Arrays;

/**
 * Q21) Coin Change
 *
 * AI-BEST:
 * Bottom-up DP for minimum coins per amount.
 * Time: O(amount * coins.length), Space: O(amount)
 *
 * AI-EASY:
 * Greedy by largest coin first.
 * Time: roughly O(amount / minCoin), Space: O(1)
 * Note: not always optimal for arbitrary coin sets.
 */
public class q21_coin_change_medium {
    static int coinChangeBest(int[] coins, int amount) {
        // Step 1: dp[x] = min coins needed to form x.
        // Why: each state reuses smaller solved subproblems.
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        // Step 2: Relax transitions for each amount and coin.
        for (int x = 1; x <= amount; x++) {
            for (int coin : coins) {
                if (x - coin >= 0) {
                    dp[x] = Math.min(dp[x], dp[x - coin] + 1);
                }
            }
        }

        // Step 3: Return computed minimum or -1 if unreachable.
        return dp[amount] <= amount ? dp[amount] : -1;
    }

    static int coinChangeEasy(int[] coins, int amount) {
        // Step 1: Sort descending and repeatedly use largest coin.
        // Why: this is simple to implement and explain quickly.
        int[] sorted = Arrays.copyOf(coins, coins.length);
        Arrays.sort(sorted);
        reverse(sorted);

        int remaining = amount;
        int used = 0;
        for (int coin : sorted) {
            while (remaining >= coin) {
                remaining -= coin;
                used++;
            }
        }

        // Step 2: Return greedy count if exact amount reached.
        // Step 3: Otherwise return -1.
        return remaining == 0 ? used : -1;
    }

    static void reverse(int[] arr) {
        int i = 0, j = arr.length - 1;
        while (i < j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            i++;
            j--;
        }
    }

    public static void main(String[] args) {
        System.out.println("Q21: Coin Change");
        int[][] coinCases = {
                {1, 2, 5},
                {2},
                {1, 3, 4}
        };
        int[] amounts = {11, 3, 6};

        for (int i = 0; i < coinCases.length; i++) {
            int[] coins = coinCases[i];
            int amount = amounts[i];
            System.out.println("Input: coins=" + Arrays.toString(coins) + ", amount=" + amount);
            System.out.println("  BEST: " + coinChangeBest(coins, amount));
            System.out.println("  EASY: " + coinChangeEasy(coins, amount));
        }
    }
}
