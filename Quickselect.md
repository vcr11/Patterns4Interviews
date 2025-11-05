
````mdx
---
title: "Quickselect"
date: "2025-11-05"
tags: ["Algorithms", "DSA", "Quickselect", "Selection", "Interview Prep", "Python"]
---

# Quickselect — The Fast Path to the k-th Element

If you only need the **k-th smallest/largest** element, **sorting** the entire array is overkill.  
**Quickselect** is the “surgical” version of Quicksort: it **partitions** the array and **recurses only into the half** that can contain the answer.

> TL;DR: Average **O(n)** time, **O(1)** extra space (in-place), same partition idea as Quicksort, but **one side only**.

---

## Why Quickselect Exists (Real Intuition)

Imagine a messy stack of papers with scores, and someone asks: “What’s the **7th best** score?”  
You wouldn’t sort everything alphabetically first. You’d:

1. **Pick a pivot** (a score to compare others against).
2. **Split**: put smaller scores on one side, larger on the other.  
3. If the pivot landed exactly at **index k**, you’re done.  
   If it landed **left of k**, search only the **right** side.  
   If it landed **right of k**, search only the **left** side.

You’re **throwing away half** the work at each step.

---

## Partition Mechanics (The Beating Heart)

We’ll use the classic **Lomuto partition** (simple, interview-friendly):

```python
def partition(nums, left, right):
    pivot = nums[right]                 # choose last element as pivot
    i = left                            # place for next <= pivot
    for j in range(left, right):
        if nums[j] <= pivot:
            nums[i], nums[j] = nums[j], nums[i]
            i += 1
    nums[i], nums[right] = nums[right], nums[i]
    return i                            # pivot's final index
````

* After `partition`, every index `< pivot_index` has `<= pivot`,
  and every index `> pivot_index` has `> pivot`.
* That single property lets Quickselect **home in** on the k-th element.

---

## In-Place Quickselect (k-th Smallest, 0-Indexed)

```python
import random

def quickselect(nums, k):
    """Return the k-th smallest element (k is 0-indexed). Modifies nums in-place."""
    left, right = 0, len(nums) - 1

    while left <= right:
        # Randomized pivot reduces worst-case risk on adversarial inputs
        pivot_index = random.randint(left, right)
        nums[pivot_index], nums[right] = nums[right], nums[pivot_index]

        p = partition(nums, left, right)  # pivot final index

        if p == k:
            return nums[p]
        elif p < k:
            left = p + 1
        else:
            right = p - 1

    raise ValueError("k out of range")
```

> For **k-th largest**, target index is `k_large = len(nums) - k`.

---

## Complexity & Practical Notes

* **Average time**: **O(n)** (random pivot → expected linear).
* **Worst case**: **O(n²)** (unlucky pivots every time).

  * Avoided in practice via **random pivot** or **median-of-medians**.
* **Space**: **O(1)** extra (in-place; recursion-free loop above).
* **Not stable**: rearranges elements; that’s OK for selection.

---

## Randomized vs Deterministic Pivots

* **Randomized pivot (common in interviews)**: simple & fast on average.
* **Median of Medians**: deterministic **O(n)** worst-case; longer code, rarely required unless explicitly asked.

---

## When to Prefer Heaps

* Need **Top-K** streaming: use a **size-K heap** (O(n log K)).
* Need **ordered** Top-K: Quickselect to carve out K, then sort those K (O(n + K log K)).

---

# 5 Interview Problems (Step-by-Step)

Each problem shows **why Quickselect fits**, gives **correct code**, and includes a **clear walkthrough**.

---

## 1) K-th **Largest** Element in an Array (LeetCode 215)

**Prompt**

> Given `nums` and integer `k`, return the **k-th largest** element.

**Key idea**
k-th largest ↔ **(n − k)-th smallest** (0-indexed).

```python
import random

def kth_largest(nums, k):
    n = len(nums)
    target = n - k                       # turn into kth smallest index
    left, right = 0, n - 1

    while left <= right:
        # random pivot
        pivot = random.randint(left, right)
        nums[pivot], nums[right] = nums[right], nums[pivot]

        p = partition(nums, left, right)

        if p == target:
            return nums[p]
        elif p < target:
            left = p + 1
        else:
            right = p - 1
```

**Dry run (tiny example)**
`nums = [3,2,1,5,6,4], k = 2` → target index `6-2=4`
After a few partitions, pivot lands at index 4 with value `5` → answer `5`.

**Why this is correct**
Partition guarantees all indices `< p` are `<= pivot` and all `> p` are `> pivot`.
When `p == target`, the element at `p` is exactly the value that would sit there in a fully sorted array.

---

## 2) Median of an **Unsorted** Array

**Prompt**

> Return the median of `nums`. If even length, return average of the two middles.

**Key idea**

* If `n` is odd → index `mid = n//2`.
* If `n` is even → average of indices `n//2 - 1` and `n//2`.
  We can Quickselect for those indices.

```python
def median_unsorted(nums):
    n = len(nums)
    if n == 0:
        raise ValueError("Empty array has no median")

    if n % 2 == 1:
        # need one index
        return quickselect(nums, n // 2)
    else:
        # need two indices (copy to avoid destroying first result)
        left = quickselect(nums, n // 2 - 1)
        right = quickselect(nums, n // 2)
        return (left + right) / 2
```

**Walkthrough**

* `n=5` → `mid=2` → find 2-nd smallest, done.
* `n=6` → find 2-nd & 3-rd smallest, average them.

**Notes**
If mutation is a concern, work on a copy. For interviews, **in-place** is acceptable unless stated otherwise.

---

## 3) K Closest Points to Origin (LeetCode 973)

**Prompt**

> Given `points = [[x,y], ...]` and `k`, return **any order** of the `k` closest points to `(0,0)`.

**Why Quickselect fits**
We only need the **cut** between the k-closest and the rest; order among the first K doesn’t matter.

```python
import random

def k_closest(points, k):
    def d2(p): return p[0]*p[0] + p[1]*p[1]  # squared distance

    def partition_points(arr, left, right):
        pivot_idx = random.randint(left, right)
        arr[pivot_idx], arr[right] = arr[right], arr[pivot_idx]
        pivot_val = d2(arr[right])

        i = left
        for j in range(left, right):
            if d2(arr[j]) <= pivot_val:
                arr[i], arr[j] = arr[j], arr[i]
                i += 1
        arr[i], arr[right] = arr[right], arr[i]
        return i

    left, right = 0, len(points) - 1
    while True:
        p = partition_points(points, left, right)
        if p == k - 1:
            return points[:k]            # first k are the k closest (unordered)
        elif p < k - 1:
            left = p + 1
        else:
            right = p - 1
```

**Step-by-step**

1. Partition by **distance**.
2. If pivot ends at index `p`:

   * `p == k-1` → first `k` elements are the k closest (in any order).
   * `p < k-1` → search right.
   * `p > k-1` → search left.

**Complexity**
Average **O(n)**, no extra memory beyond in-place swaps.

---

## 4) Return the **Smallest K** Elements (Unordered)

**Prompt**

> Return the **k** smallest elements of `nums` (order doesn’t matter).

**Quickselect trick**
Find the element at index `k-1`. Partition guarantees everything **before** that index is `<=` it. Just slice.

```python
def k_smallest_unordered(nums, k):
    if k <= 0: return []
    if k >= len(nums): return nums[:]     # all elements

    # find the cut element
    cutoff = quickselect(nums, k - 1)

    # compact <= cutoff to front in one linear pass (optional: stable not required)
    i = 0
    for j in range(len(nums)):
        if nums[j] <= cutoff:
            nums[i], nums[j] = nums[j], nums[i]
            i += 1
    # Now first i elements are <= cutoff; i may be > k due to duplicates
    return nums[:k]                        # any k among these is valid
```

**Why duplicates are fine**
If `cutoff` appears multiple times, there may be **more than k** elements `<= cutoff`.
Returning **any** k of them is valid unless the problem demands a specific tie-break.

**When interviewer wants sorted output**
Sort the returned slice: `return sorted(nums[:k])` (O(k log k) post-step).

---

## 5) k-th Element by a **Custom Key** (e.g., absolute value)

**Prompt**

> Find the element that is the **k-th smallest by absolute value** (ties break arbitrarily).

**Why this matters**
Many interviews throw a twist: “k-th by **some function**.” Use the same partition, but compare by `key(x)`.

```python
import random

def quickselect_by_key(nums, k, key):
    """k-th smallest by key; k is 0-indexed."""
    left, right = 0, len(nums) - 1
    while left <= right:
        pivot = random.randint(left, right)
        nums[pivot], nums[right] = nums[right], nums[pivot]
        pivot_val = key(nums[right])

        i = left
        for j in range(left, right):
            if key(nums[j]) <= pivot_val:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1
        nums[i], nums[right] = nums[right], nums[i]

        if i == k:
            return nums[i]
        elif i < k:
            left = i + 1
        else:
            right = i - 1

def kth_by_abs(nums, k):
    return quickselect_by_key(nums, k, key=abs)
```

**Walkthrough**

* Replace every comparison `a <= b` with `key(a) <= key(b)`.
* Everything else (pivoting, narrowing) is identical.

---

## Pitfalls & How to Sound Like a Pro

* **Off-by-one**: Decide early—**0-indexed or 1-indexed** k? Stick to it.
* **k-th largest** conversion: `target = n - k` (0-indexed smallest).
* **Mutation**: In-place partition **rearranges** `nums`. If problem forbids mutation, copy first.
* **Duplicates**: Not a problem—partition correctness still holds.
* **Worst-case O(n²)**: Mention **random pivot** or **median-of-medians** as mitigation.
* **Stability**: Not stable—selection doesn’t require it.

---

## Quick Proof Sketch (Why It Works)

After `partition`, the pivot is at its **sorted position p**:

* All elements left are `<= pivot`.
* All elements right are `> pivot`.

If the k-th element in sorted order sits at `p`, we’ve found it.
If `k < p`, it must be in the left region; if `k > p`, it must be in the right region.
This invariant **shrinks** the search to the side that could possibly contain the answer.

---

## Cheat Sheet (Interview Summary)

* Use Quickselect when you need a **rank** (k-th) not the full order.
* Average **O(n)**, in-place.
* k-th largest ↔ index `n - k`.
* For **Top-K unordered**: Quickselect to position `k-1`, then slice.
* For **Top-K ordered**: Quickselect → slice → sort the slice (`O(k log k)`).


