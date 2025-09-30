# Dynamic Programming (DP) — Notes

> **What is DP?**  
> A method to solve problems by **reusing solutions to overlapping subproblems** when the overall problem has **optimal substructure** (the optimal answer is made from optimal answers to smaller parts).

---

## When to reach for DP
- You can phrase a “best / count / possible?” question over prefixes, indices, or capacities.
- The solution for state *S* depends on a **small set** of “earlier” states.
- Greedy or divide-and-conquer fails because choices interact.

**Quick test (Y/N)**
- Do subproblems repeat? → **Overlapping** ✓  
- Can the optimal solution be built from optimal sub-solutions? → **Optimal substructure** ✓

---

## The 5-step DP routine
1. **State** — Describe `dp[...]` **in words**.  
   *Example*: `dp[i] = max value using the first i items` or `dp[c] = best value at capacity c`.
2. **Base cases** — What’s true for the emptiest inputs?  
   *Example*: `dp[0] = 0`, `dp[i][0] = 0`, `dp[0][j] = j`, etc.
3. **Transition** — Express `dp[...]` using smaller states.  
   *Example (0/1 Knapsack)*: `dp[c] = max(dp[c], v[i] + dp[c - w[i]])`.
4. **Order** — Memoization (top-down recursion + cache) **or** Tabulation (bottom-up loops).  
   Choose an iteration order that guarantees dependencies are ready.
5. **Space** — Compress if each row only depends on a previous row (e.g., 2-row or 1D array).

---

## Memoization vs Tabulation
|                     | Memoization (Top-Down)           | Tabulation (Bottom-Up)               |
|---------------------|-----------------------------------|--------------------------------------|
| Style               | recursive + cache                 | iterative + table                    |
| Good for            | sparse states, easy correctness   | tight loops, easy space optimization |
| Gotcha              | recursion depth/stack limits      | getting the fill order right         |

**Pseudocode (top-down)**  

---

## Patterns at a glance

### 1) **1D DP**
- `dp[i]` depends on a few previous indices.  
- **Examples**: Fibonacci, **House Robber**, Min Cost Climbing Stairs.  
- **Template**: keep rolling variables (`prev2`, `prev1`) when only last 1–2 states matter.

### 2) **0/1 Knapsack** (take each item **at most once**)
- 1D capacity DP; iterate **capacity downward**.
- Transition: `dp[c] = max(dp[c], value[i] + dp[c - weight[i]])`.

### 3) **Unbounded Knapsack / Coin Change** (reuse items)
- 1D capacity DP; iterate **capacity upward**.
- Min coins: `dp[a] = min(dp[a], 1 + dp[a - coin])`.  
- Ways: `dp[a] += dp[a - coin]`.

### 4) **LIS**
- `O(n^2)`: `dp[i] = 1 + max(dp[j]) for j<i and a[j] < a[i]`.  
- `O(n log n)`: patience sorting with `tails` array.

### 5) **LCS**
- `dp[i][j]` = LCS length of `A[:i]` and `B[:j]`.  
- If `A[i-1]==B[j-1]`: `1 + dp[i-1][j-1]` else `max(dp[i-1][j], dp[i][j-1])`.  
- Space: two rows.

### 6) **LPS** (Longest Palindromic Subsequence)
- Either LCS(`s`, `reverse(s)`) **or** substring DP:  
  if `s[i]==s[j]` → `2 + dp[i+1][j-1]` else `max(dp[i+1][j], dp[i][j-1])`.

### 7) **Grid DP**
- `dp[r][c]` from neighbors (top/left/diag), respecting walls/weights.  
- Space: one row if each cell depends only on left/top.

---

## Common pitfalls
- **Wrong iteration direction**  
  - **0/1**: capacity **↓** (prevent reusing same item).  
  - **Unbounded**: capacity **↑** (allow reuse).
- **State meaning not precise** → transitions don’t match.  
- **Off-by-one** on indices or capacity bounds.  
- **Double counting** in counting problems (order of loops matters).

---

## Tiny cheat sheet

- **0/1 Knapsack (1D)**  
  `for i in items: for c in C..w[i]: dp[c] = max(dp[c], v[i] + dp[c-w[i]])`
- **Unbounded Knapsack (1D)**  
  `for i in items: for c in w[i]..C: dp[c] = max(dp[c], v[i] + dp[c-w[i]])`
- **House Robber (rolling)**  
  `cur = max(prev1, prev2 + x); prev2 = prev1; prev1 = cur`
- **LCS (2-row)**  
  `if a[i-1]==b[j-1]: cur[j]=1+prev[j-1] else cur[j]=max(prev[j],cur[j-1])`

---

## What NOT to force into DP
- Purely greedy-friendly problems (choice is locally optimal and provably safe).
- Problems without overlapping subproblems (each subproblem unique).

---

## Practice shortlist
- 0/1 Knapsack, Coin Change (min & count), House Robber (I/II), LIS, LCS, LPS, Edit Distance, Partition Equal Subset Sum, Target Sum, Unique Paths (+ obstacles), Min Path Sum.
