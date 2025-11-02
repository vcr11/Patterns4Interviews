Absolutely—here’s a **single, GitHub-friendly MDX file** you can copy-paste as `binary-search-invariants.mdx`.

* It renders cleanly on **GitHub, Notion MDX, Next.js MDX, Docusaurus** (no fancy CSS required).
* Tone is human + narrative, but the code is interview-ready.
* Includes clean, **invariant template** and **all 6 problems** solved (no lambdas).
* Problem links included.
* Uses the `r > l + 1` pattern consistently.

---

````mdx
---
title: "Binary Search — The Hidden Art of Dividing and Conquering"
description: "From classic confusion (< vs <=) to one invariant template that solves every binary search problem."
tags: [DSA, Binary Search, Invariants, Python, LeetCode]
---

# 🧠 Binary Search — The Hidden Art of Dividing and Conquering

> A story and a template you won’t forget.

Everyone learns Binary Search.  
Few truly **trust** it.

You remember the chaos:
- “Should it be `<` or `<=`? `mid+1` or `mid-1`?”
- “Why do edge cases break *every single time*? 😅”

Here’s the shift: **Binary Search isn’t about the middle. It’s about boundaries.**  
Once you think in boundaries, edge cases disappear.

---

## 🌌 The Mental Model (Invariants)

Split the world into two zones:

- 🟥 **Left zone** — everything that’s **False** (does **not** satisfy the condition)  
- 🟩 **Right zone** — everything that’s **True** (does satisfy the condition)

**Invariant:**  
> Left is always False. Right is always True. We never violate this.

We keep shrinking the gap until the zones meet at the first **True** index.

---

## 🔧 The One Template (r > l + 1)

```python
def first_true(lo: int, hi: int, predicate) -> int:
    """
    Returns the smallest index in [lo, hi] for which predicate(index) is True.
    Assumes the array of predicate results is some F...F T...T (False then True).
    If none are True, returns hi + 1 (caller can check bounds).
    """
    l, r = lo - 1, hi + 1   # l = False zone, r = True zone
    while r > l + 1:
        mid = (l + r) // 2
        if predicate(mid):
            r = mid        # mid is True → pull right boundary inward
        else:
            l = mid        # mid is False → push left boundary outward
    return r               # first True
````

> Every binary-search problem is this loop with a different **predicate**.

---

## 🧭 Quick Compass

* “Find **smallest X** that works” → use **first_true**
* “Find **largest X** that fails” → compute `first_true` and subtract 1
* “Find first/last occurrence” → tune predicate (`>= target`, `> target`)
* “Find peaks/boundaries” → pick the side that must contain the answer

---

## 🔮 Six Canonical Problems (with clean code)

Each solution uses the invariant idea (or the classic half-elimination where appropriate).

---

### 1) 🎯 34. Find First and Last Position of Element in Sorted Array

**Link:** [https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/)

* First index `>= target` → `first`
* First index `> target`  → `last = that - 1`

```python
class Solution:
    def searchRange(self, nums, target):
        n = len(nums)
        if n == 0:
            return [-1, -1]

        # first index >= target
        l, r = -1, n
        while r > l + 1:
            mid = (l + r) // 2
            if nums[mid] >= target:
                r = mid
            else:
                l = mid
        first = r
        if first == n or nums[first] != target:
            return [-1, -1]

        # first index > target  → last = r - 1
        l, r = -1, n
        while r > l + 1:
            mid = (l + r) // 2
            if nums[mid] > target:
                r = mid
            else:
                l = mid
        last = r - 1
        return [first, last]
```

---

### 2) 🔁 33. Search in Rotated Sorted Array

**Link:** [https://leetcode.com/problems/search-in-rotated-sorted-array/](https://leetcode.com/problems/search-in-rotated-sorted-array/)

Rotation breaks a simple monotonic predicate, so use **half-elimination**:
At each step, one side is sorted → decide if the target is inside that half.

```python
class Solution:
    def search(self, nums, target):
        l, r = 0, len(nums) - 1
        while l <= r:
            mid = (l + r) // 2
            if nums[mid] == target:
                return mid

            # Left half sorted
            if nums[l] <= nums[mid]:
                if nums[l] <= target < nums[mid]:
                    r = mid - 1
                else:
                    l = mid + 1
            else:
                # Right half sorted
                if nums[mid] < target <= nums[r]:
                    l = mid + 1
                else:
                    r = mid - 1
        return -1
```

---

### 3) 🏔️ 162. Find Peak Element

**Link:** [https://leetcode.com/problems/find-peak-element/](https://leetcode.com/problems/find-peak-element/)

A peak exists where slope flips from up to down.
Define True as “we’re on or to the right of the peak,” which happens when `nums[mid] > nums[mid + 1]`.

```python
class Solution:
    def findPeakElement(self, nums):
        l, r = -1, len(nums) - 1
        while r > l + 1:
            mid = (l + r) // 2
            if nums[mid] > nums[mid + 1]:
                r = mid      # descending side (or at peak) → True zone
            else:
                l = mid      # ascending side → False zone
        return r
```

---

### 4) 🍌 875. Koko Eating Bananas

**Link:** [https://leetcode.com/problems/koko-eating-bananas/](https://leetcode.com/problems/koko-eating-bananas/)

Predicate: with speed `k`, can Koko finish within `h` hours?
Find the first `k` where this is True → **minimum valid speed**.

```python
import math

class Solution:
    def minEatingSpeed(self, piles, h):
        # search k in [1, max(piles)]
        l, r = 0, max(piles) + 1
        while r > l + 1:
            mid = (l + r) // 2  # speed
            hours = 0
            for p in piles:
                hours += math.ceil(p / mid)
            if hours <= h:
                r = mid   # True → can try slower
            else:
                l = mid   # False → need faster
        return r
```

---

### 5) 🚢 1011. Capacity to Ship Packages Within D Days

**Link:** [https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/)

Predicate: with capacity `C`, can we finish in `D` days?
Find smallest `C` with True.

```python
class Solution:
    def shipWithinDays(self, weights, days):
        l, r = 0, sum(weights) + 1
        while r > l + 1:
            mid = (l + r) // 2  # capacity
            used_days, cur = 1, 0
            for w in weights:
                if cur + w > mid:
                    used_days += 1
                    cur = 0
                cur += w
            if used_days <= days:
                r = mid    # True → capacity works
            else:
                l = mid    # False → too small
        return r
```

---

### 6) 📚 410. Split Array Largest Sum

**Link:** [https://leetcode.com/problems/split-array-largest-sum/](https://leetcode.com/problems/split-array-largest-sum/)

Same structure as shipping:
Predicate True if we can split into **≤ k** groups with max subarray sum ≤ `S`.
Find the smallest such `S`.

```python
class Solution:
    def splitArray(self, nums, k):
        l, r = 0, sum(nums) + 1
        while r > l + 1:
            mid = (l + r) // 2  # candidate max sum
            groups, cur = 1, 0
            for x in nums:
                if cur + x > mid:
                    groups += 1
                    cur = 0
                cur += x
            if groups <= k:
                r = mid     # True → feasible bound
            else:
                l = mid     # False → too tight
        return r
```

---

## 🧩 The Tiny Cheat Sheet (print-this-in-your-head)

| Task                     | False (Left) | True (Right)                     | Return     |
| ------------------------ | ------------ | -------------------------------- | ---------- |
| First index **≥ x**      | `< x`        | `≥ x`                            | `r`        |
| Last index **≤ x**       | `≤ x`        | `> x`                            | `r - 1`    |
| Min valid speed/capacity | invalid      | valid                            | `r`        |
| Peak (162)               | increasing   | descending (incl. peak)          | `r`        |
| Rotated search (33)      | —            | delete half by sorted-side logic | index / -1 |

> **Mantra:** Keep **Left always False**, **Right always True**.
> Let the loop squeeze the truth out.

---

## 🧪 Practice Order (fastest skill gain)

1. 34 — *first & last position* (two predicates)
2. 875 — *min speed* (first True)
3. 1011 — *min capacity* (same pattern)
4. 410 — *min largest subarray sum* (same engine)
5. 162 — *peak* (slope boundary)
6. 33 — *rotated* (half elimination intuition)

---

## 🏁 Parting Line

Binary Search doesn’t care about the middle.
It cares about your **boundaries**.

Once you guard the invariant, the answers walk to you.

**#HappyCoding ✨**
*Binary Search • Invariants • Python • LeetCode • Systematic Thinking*




