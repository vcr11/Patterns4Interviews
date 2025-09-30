
# 0/1 Knapsack — Notes 

### Problem

You have `n` items. Item `i` has `value[i]` and `weight[i]`. A bag can carry at most `C`.
Pick a **subset** (each item 0 or 1 time) to **maximize total value** without exceeding `C`.

Why “0/1”?
Because each item is indivisible: take it (1) or leave it (0).

---

## When to reach for it

* There is a **capacity** (weight / time / budget) and **indivisible** choices.
* You need the **best subset**, not just “something that fits”.
* Greedy fails because locally best ≠ globally best (the “big TV” trap).

---

## Two DP formulations

### A) 2D (teaching form)

**State:** `dp[i][c]` = best value using the **first `i` items** with capacity `c`.
**Transition:**

```
dp[i][c] = dp[i-1][c]                                  # skip item i
if weight[i-1] ≤ c:
    dp[i][c] = max(dp[i][c], value[i-1] + dp[i-1][c - weight[i-1]])  # take
```

**Base:** `dp[0][*] = 0`, `dp[*][0] = 0`.
**Answer:** `dp[n][C]`.

### B) 1D (space-optimized, what you usually code)

Observation: row `i` depends only on row `i-1` → compress to one array over capacity.

> **Key rule:** iterate capacity **downward** for 0/1, so each item is used at most once.

```
dp[0..C] = 0
for i in 0..n-1:
  for c in C..weight[i]:
    dp[c] = max(dp[c], value[i] + dp[c - weight[i]])
return dp[C]
```

Time for both: `O(n*C)`. Space: `O(n*C)` (2D) → `O(C)` (1D).

---

## A worked example (with a small recursion tree)

Let’s use four items:

| Item    | Value | Weight |
| ------- | ----- | ------ |
| Laptop  | 14    | 6      |
| Vase    | 10    | 5      |
| Jewelry | 9     | 4      |
| TV      | 20    | 9      |

Capacity `C = 10`.

**Brute-force thinking** (the idea we optimize): for each item, you either **take** or **skip**.

Small recursion tree (showing only the first few levels):

```
recur(i=0, cap=10, val=0)   // choose for item 0 (Laptop)
├─ SKIP 0  → recur(i=1, cap=10, val=0)
│  ├─ SKIP 1  → recur(i=2, cap=10, val=0)
│  │  ├─ SKIP 2  → recur(i=3, cap=10, val=0)
│  │  │  ├─ SKIP 3  → END  val=0
│  │  │  └─ TAKE 3  → recur(i=4, cap=1,  val=20) → END  val=20
│  │  └─ TAKE 2  → recur(i=3, cap=6,  val=9)
│  │     ├─ SKIP 3  → END  val=9
│  │     └─ TAKE 3  (w=9 > cap=6) ✗  → invalid
│  └─ TAKE 1  → recur(i=2, cap=5,  val=10)
│     ├─ SKIP 2  → recur(i=3, cap=5,  val=10)
│     │  ├─ SKIP 3  → END  val=10
│     │  └─ TAKE 3  (w=9 > cap=5) ✗  → invalid
│     └─ TAKE 2  → recur(i=3, cap=1,  val=19)
│        ├─ SKIP 3  → END  val=19
│        └─ TAKE 3  (w=9 > cap=1) ✗  → invalid
└─ TAKE 0  → recur(i=1, cap=4,  val=14)
   ├─ SKIP 1  → recur(i=2, cap=4,  val=14)
   │  ├─ SKIP 2  → recur(i=3, cap=4,  val=14)
   │  │  ├─ SKIP 3  → END  val=14
   │  │  └─ TAKE 3  (w=9 > cap=4) ✗  → invalid
   │  └─ TAKE 2  → recur(i=3, cap=0,  val=23)  ← **BEST**
   │     ├─ SKIP 3  → END  val=23
   │     └─ TAKE 3  (cap=0) ✗ → invalid
   └─ TAKE 1  (w=5 > cap=4) ✗  → invalid

```

We don’t actually compute every node—**DP** caches results so each `(i, cap)` is solved once.
But this tree explains **why** the best is **Laptop + Jewelry = 14 + 9 = 23**, not the lone **TV (20)**.

---

## How to teach your brain to spot the transition

* At `(i, c)`, you have two legal moves: **skip** item `i`, or **take** it (if `weight[i] ≤ c`).
* The best is the **max** of those two worlds.
* That sentence *is* the recurrence.

---

## Typical pitfalls (and how to avoid them)

* **Wrong loop direction** in 1D:

  * 0/1 → **downward** (`for c from C down to weight[i]`)
  * Unbounded → **upward** (reuse allowed)
* **State off-by-one**: using `i` but indexing `value[i-1]`. Keep it consistent.
* **Mixed arrays**: `value`/`weight` must align per item.
* **Forgetting bases**: capacity 0 or 0 items → value 0.

---

## One minute pseudo-templates

**Top-down (memo):**

```
f(i, c):
  if i == n or c == 0: return 0
  skip = f(i+1, c)
  take = -∞
  if weight[i] ≤ c: take = value[i] + f(i+1, c - weight[i])
  return max(skip, take)
```

**Bottom-up 1D (preferred in interviews):**

```
dp[0..C] = 0
for i in 0..n-1:
  for c in C..weight[i]:
    dp[c] = max(dp[c], value[i] + dp[c - weight[i]])
return dp[C]
```

---

## Practice set (0/1-style on LeetCode)

* **Partition Equal Subset Sum** – split into two equal sums
  [https://leetcode.com/problems/partition-equal-subset-sum/](https://leetcode.com/problems/partition-equal-subset-sum/)
* **Target Sum** – assign +/− to reach target (turns into knapsack on sums)
  [https://leetcode.com/problems/target-sum/](https://leetcode.com/problems/target-sum/)
* **Last Stone Weight II** – minimize difference (subset partition)
  [https://leetcode.com/problems/last-stone-weight-ii/](https://leetcode.com/problems/last-stone-weight-ii/)
* **Ones and Zeroes** – 0/1 knapsack with **two capacities** (zeros, ones)
  [https://leetcode.com/problems/ones-and-zeroes/](https://leetcode.com/problems/ones-and-zeroes/)
* **Subset Sum (classic)** – many implementations, appears inside others
  (Use Partition/Target Sum to practice the pattern.)

*Tip:* For each, write the **state in words** first. If you can’t say what `dp[...]` means, the code will fight you.

---

## A compact Python reference (1D)

```python
def knapsack_01(values, weights, C):
    dp = [0]*(C+1)
    for i in range(len(values)):
        for c in range(C, weights[i]-1, -1):  # go DOWN to enforce 0/1
            dp[c] = max(dp[c], values[i] + dp[c - weights[i]])
    return dp[C]
```

---

### Final takeaways

* 0/1 Knapsack is the **portfolio** version of picking items under a limit.
* The important bit is not syntax—it’s the **choice** at each state and the **direction** of the capacity loop.
* Once you internalize “**skip vs take, then max**” and “**capacity goes down for 0/1**,” you can adapt this to dozens of interview problems.

