# Technical Reflection & Complexity Analysis — Lab 7 Part B

## 1. HashMap Behavior and Key-Value Storage

The Student Directory uses `java.util.HashMap<String, Student>` as its backing store. This means student IDs (String keys) are hashed to determine bucket placement, and Student objects are stored as values. HashMap provides O(1) average-case performance for put, get, and remove operations because the hash function distributes keys across an internal array of buckets.

### Collisions and Chaining

When two different keys produce the same hash code (or hash to the same bucket index after modulus), a **collision** occurs. Java's HashMap resolves collisions using chaining: each bucket holds a linked list (or treeified list for high-collision scenarios) of key-value entries. When retrieving a value, HashMap computes the bucket, then walks the chain comparing keys with `equals()` until a match is found.

- **Average case (good hash distribution):** O(1) — each bucket has few entries.
- **Worst case (all keys collide):** O(n) — every operation walks a chain of length n.

This is why the Student Directory's `addStudent`, `getStudent`, `updateStudent`, `removeStudent`, and `containsStudent` are all O(1) average and O(n) worst case.

## 2. Immutability of the Student Model

`Student` is declared `final` with `private final` fields (`id`, `name`, `grade`) and no setters. This immutability is important because:

- The directory stores references to Student objects. If a Student were mutable and a caller modified its fields after insertion, the directory's internal state could become inconsistent without the directory knowing.
- `equals()` and `hashCode()` are based only on `id`. If a mutable field used in `hashCode()` changed after insertion, the object could become unretrievable from the HashMap (the hash bucket would no longer match).
- The `snapshot()` method returns a shallow copy of the map. Because `Student` is immutable, a shallow copy is safe: callers cannot mutate Student objects through the returned map.

## 3. Duplicate ID Rejection

`addStudent` returns `false` when a duplicate ID is supplied rather than silently overwriting the existing value. This enforces the uniqueness requirement: student IDs are identifiers, and inserting a second student with the same ID is a programming error that should be surfaced to the caller as a boolean return value.

`updateStudent` is the intentional path for changing a student's data. It requires two conditions:
1. The requested ID must already exist in the directory.
2. The supplied Student's `getId()` must equal the requested ID.

The second check prevents a caller from accidentally "renaming" a student by passing a Student object with a different ID than the map key being updated.

## 4. Defensive Snapshots

`snapshot()` returns `new HashMap<>(students)` — a shallow copy of the backing map. This means:
- Callers can read entries from the snapshot without affecting the directory.
- Callers cannot add, remove, or replace entries in the directory through the snapshot (the copy is a separate HashMap instance).
- Because `Student` is immutable, the shared Student references are safe to expose without deep-copying.

Without this defensive copy, returning the backing map directly would allow external mutation of the directory's internal state, breaking encapsulation.

## 5. Character Frequency Counter Design

The frequency counter uses `HashMap<Character, Integer>` where each `Character` is a key and its `Integer` value is the occurrence count. Design decisions:

- **Case-sensitive:** `'A'` (Unicode 65) and `'a'` (Unicode 97) have different hash codes and are distinct keys.
- **All characters counted:** Spaces, punctuation, digits, and control characters are all counted. Nothing is silently filtered or normalized.
- **Null input:** Throws `NullPointerException` — counting null text is undefined behavior.
- **Defensive copy:** `count()` returns `new HashMap<>(freq)`, so callers cannot mutate internal state.

For a string of length n with k distinct characters:
- Time: O(n) average (one HashMap operation per character).
- Auxiliary space: O(k) for the frequency map.
- Worst-case time: O(n²) if every character collides into the same bucket.

## 6. Test Coverage Assessment

The test suite covers 26 tests across six required acceptance cases plus boundary tests:

1. **Empty Map:** size 0, get→null, contains→false, remove→false, snapshot empty
2. **Duplicate Key:** addStudent returns false on duplicate, size unchanged
3. **Missing Key:** getStudent→null, containsStudent→false, removeStudent→false
4. **Record Update:** updateStudent returns false on missing ID, verifies ID match, true on success
5. **Removal:** removeStudent returns true, get→null after removal, size decremented
6. **Repeated Characters:** "banana" → a:3, n:2, b:1; case-sensitive; spaces/punctuation counted

A student-authored assertion (`testAddThenRemoveAll`) verifies an end-to-end property: add 4 students, remove all 4, verify size is 0 and snapshot is empty.

Boundary tests verify that null IDs and null Student arguments throw `NullPointerException` on all directory methods, and `count(null)` throws `NullPointerException`.

## 7. Limitations

- **No custom HashMap implementation:** The project uses Java's built-in `java.util.HashMap` per the approved plan. Collision handling and chaining are delegated to the JDK implementation.
- **No benchmark measurements:** Complexity is analyzed theoretically; no actual timing experiments were conducted.
- **Shallow snapshot:** While `Student` is immutable making shallow copies safe, if `Student` were later made mutable, `snapshot()` would need deep copying.
- **`getCount()` not implemented:** A `getCount(char)` helper was considered but rejected as unnecessary — no test requires it, and callers can inspect the returned map directly.

## 8. Personal Understanding

Through this lab, I learned that:

1. **HashMap is not O(1) in all cases.** The O(1) guarantee depends on a good hash function and reasonable load factor. Poor hash distribution or adversarial inputs can degrade performance to O(n).

2. **Immutability matters for hash-based collections.** If an object used as a HashMap key or value is mutable and its hashCode-relevant fields change, the collection's invariants break. Making `Student` final and immutable eliminates this entire class of bugs.

3. **Defensive copying is an encapsulation boundary.** Returning internal collections directly exposes the object's internals to external mutation. A defensive copy (or unmodifiable wrapper) is the standard mitigation.

4. **Duplicate rejection vs. update are different operations.** `addStudent` rejects duplicates to preserve uniqueness; `updateStudent` intentionally replaces existing data. Conflating these would silently lose data or violate invariants.

5. **Case sensitivity is a design decision, not a default.** `'A'` and `'a'` are different Unicode characters with different hash codes. Treating them as the same would require explicit normalization (e.g., `Character.toLowerCase()`), which was deliberately not done here.

## 9. Complexity Summary

| Operation | Time (Average) | Time (Worst) | Auxiliary Space |
|-----------|---------------|--------------|-----------------|
| `addStudent` | O(1) | O(n) | O(1) |
| `getStudent` | O(1) | O(n) | O(1) |
| `updateStudent` | O(1) | O(n) | O(1) |
| `removeStudent` | O(1) | O(n) | O(1) |
| `containsStudent` | O(1) | O(n) | O(1) |
| `size` | O(1) | O(1) | O(1) |
| `snapshot` | O(n) | O(n) | O(n) |
| `count` (n chars) | O(n) | O(n²) | O(k) |
