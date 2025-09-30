/*
  0/1 Knapsack — Java
  profit[i] = value of item i
  weight[i] = weight of item i
  capacity  = max weight the bag can carry
  Each item can be taken at most once.

  Four approaches (same style as Python):
  1) recursion    : Brute-force DFS (O(2^n))
  2) memoization  : Top-down DP with caching (O(n*m))
  3) Tabulation   : Bottom-up 2-D DP (O(n*m) time, O(n*m) space)
  4) optimizedDp  : Bottom-up 1-D DP (O(n*m) time, O(m) space)
*/

public class Knapsack01 {

    // 1) Brute Force (DFS) — O(2^n)
    public static int recursion(int[] profit, int[] weight, int capacity) {
        return rec(0, capacity, profit, weight);
    }

    private static int rec(int i, int cap, int[] profit, int[] weight) {
        if (i == profit.length || cap == 0) return 0;
        int best = rec(i + 1, cap, profit, weight); // skip
        if (weight[i] <= cap) {                      // take
            best = Math.max(best, profit[i] + rec(i + 1, cap - weight[i], profit, weight));
        }
        return best;
    }

    // 2) Memoization (Top-Down) — O(n*m)
    public static int memoization (int[] profit, int[] weight, int capacity) {
        int n = profit.length, C = capacity;
        if (n == 0 || C == 0) return 0;
        int[][] cache = new int[n][C + 1];
        for (int i = 0; i < n; i++) {
            for (int c = 0; c <= C; c++) cache[i][c] = -1;
        }
        return memo(0, C, profit, weight, cache); // overloaded helper named 'memo'
    }

    private static int memo(int i, int cap, int[] profit, int[] weight, int[][] cache) {
        if (i == profit.length || cap == 0) return 0;
        if (cache[i][cap] != -1) return cache[i][cap];
        int best = memo(i + 1, cap, profit, weight, cache); // skip
        if (weight[i] <= cap) {                              // take
            best = Math.max(best, profit[i] + memo(i + 1, cap - weight[i], profit, weight, cache));
        }
        cache[i][cap] = best;
        return best;
    }

    // 3) Tabulation (2-D DP) — O(n*m) time, O(n*m) space
    public static int Tabulation(int[] profit, int[] weight, int capacity) {
        int n = profit.length, C = capacity;
        if (n == 0 || C == 0) return 0;

        int[][] tbl = new int[n][C + 1]; // tbl[i][c] = best value using items up to i with capacity c

        // base row for item 0
        for (int c = weight[0]; c <= C; c++) {
            tbl[0][c] = profit[0];
        }

        for (int i = 1; i < n; i++) {
            for (int c = 0; c <= C; c++) {
                int skip = tbl[i - 1][c];
                int take = 0;
                if (weight[i] <= c) take = profit[i] + tbl[i - 1][c - weight[i]];
                tbl[i][c] = Math.max(skip, take);
            }
        }
        return tbl[n - 1][C];
    }

    // 4) Tabulation (1-D DP, space-optimized) — O(n*m) time, O(m) space
    //    IMPORTANT for 0/1: iterate capacity DOWN so each item is used at most once.
    public static int optimizedDp(int[] profit, int[] weight, int capacity) {
        int n = profit.length, C = capacity;
        if (n == 0 || C == 0) return 0;

        int[] dp = new int[C + 1]; // dp[c] = best value at capacity c

        for (int i = 0; i < n; i++) {
            for (int c = C; c >= weight[i]; c--) { // go DOWN to enforce 0/1
                dp[c] = Math.max(dp[c], profit[i] + dp[c - weight[i]]);
            }
        }
        return dp[C];
    }

    // Quick self-test
    public static void main(String[] args) {
        int[] profit = {14, 10, 9, 20}; // Laptop, Vase, Jewelry, TV
        int[] weight = { 6,  5, 4,  9};
        int C = 10;
        int expected = 23; // Laptop(6,14) + Jewelry(4,9)

        System.out.println("recursion:   " + recursion(profit, weight, C));
        System.out.println("memo:        " + memoization (profit, weight, C));
        System.out.println("Tabulation:  " + Tabulation(profit, weight, C));
        System.out.println("optimizedDp: " + optimizedDp(profit, weight, C));
        System.out.println("Expected:    " + expected);
    }
}
