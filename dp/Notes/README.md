# Dynamic Programming (DP)
**Use when:** subproblems repeat (overlap) and the optimal answer can be built from optimal sub-answers.

## Solve-any-DP checklist
1) **State:** What does `dp[...]` represent in words?
2) **Base cases:** Smallest inputs with obvious answers.
3) **Transition:** How to build from smaller states.
4) **Order:** Top-down (memo) or bottom-up (tab).
5) **Space:** Can we compress (1D or rolling rows)?

## Common patterns
- 1D DP (Fibonacci, House Robber, Min Cost Stair)
- 0/1 Knapsack (downward capacity)
- Unbounded Knapsack / Coin Change (upward capacity)
- LIS / LCS / LPS
- Grid DP (paths/costs)

## Pitfalls
- Wrong state meaning
- Mixing 0/1 vs unbounded transitions
- Wrong iteration order when compressing
