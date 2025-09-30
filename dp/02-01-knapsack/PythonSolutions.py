"""
0/1 Knapsack
------------
profit[i]  -> value of item i
weight[i]  -> weight of item i
capacity   -> max weight the bag can carry
Each item can be taken at most once.

Four approaches:
1) recursion      : Brute-force DFS (O(2^n))
2) memoization    : Top-down DP with caching (O(n*m))
3) Tabulation     : Bottom-up 2-D DP (O(n*m) time, O(n*m) space)
4) optimizedDp    : Bottom-up 1-D DP (O(n*m) time, O(m) space)
"""

# 1) Brute Force (DFS) — O(2^n)
def recursion(profit, weight, capacity):
    n = len(profit)

    def rec(i, cap):
        if i == n or cap == 0:
            return 0
        # skip item i
        best = rec(i + 1, cap)
        # take item i (if it fits)
        if weight[i] <= cap:
            best = max(best, profit[i] + rec(i + 1, cap - weight[i]))
        return best

    return rec(0, capacity)


# 2) Memoization (Top-Down) — O(n*m) time, O(n*m) space
def memoization(profit, weight, capacity):
    n, C = len(profit), capacity
    if n == 0 or C == 0:
        return 0

    cache = [[-1] * (C + 1) for _ in range(n)]

    def memo(i, cap):  # inner helper named 'memo' as requested
        if i == n or cap == 0:
            return 0
        if cache[i][cap] != -1:
            return cache[i][cap]
        best = memo(i + 1, cap)  # skip
        if weight[i] <= cap:     # take
            best = max(best, profit[i] + memo(i + 1, cap - weight[i]))
        cache[i][cap] = best
        return best

    return memo(0, C)


# 3) Tabulation (2-D DP) — O(n*m) time, O(n*m) space
def Tabulation(profit, weight, capacity):
    n, C = len(profit), capacity
    if n == 0 or C == 0:
        return 0

    # tbl[i][c] = best value using items up to index i with capacity c
    tbl = [[0] * (C + 1) for _ in range(n)]

    # base row for item 0
    for c in range(weight[0], C + 1):
        tbl[0][c] = profit[0]

    for i in range(1, n):
        for c in range(0, C + 1):
            skip = tbl[i - 1][c]
            take = 0
            if weight[i] <= c:
                take = profit[i] + tbl[i - 1][c - weight[i]]
            tbl[i][c] = max(skip, take)

    return tbl[n - 1][C]


# 4) Tabulation (1-D DP, space-optimized) — O(n*m) time, O(m) space
#    IMPORTANT for 0/1: iterate capacity DOWN so each item is used at most once.
def optimizedDp(profit, weight, capacity):
    n, C = len(profit), capacity
    if n == 0 or C == 0:
        return 0

    dp = [0] * (C + 1)  # dp[c] = best value at capacity c

    for i in range(n):
        for c in range(C, weight[i] - 1, -1):  # go DOWN to enforce 0/1
            dp[c] = max(dp[c], profit[i] + dp[c - weight[i]])

    return dp[C]


# Example usage / quick self-test
if __name__ == "__main__":
    profit = [14, 10, 9, 20]  # Laptop, Vase, Jewelry, TV
    weight = [6,  5,  4,  9]
    C = 10
    expected = 23  # Laptop(6,14) + Jewelry(4,9)

    print("recursion:   ", recursion(profit, weight, C))
    print("memo:        ", memoization(profit, weight, C))
    print("Tabulation:  ", Tabulation(profit, weight, C))
    print("optimizedDp: ", optimizedDp(profit, weight, C))
    print("Expected:    ", expected)
