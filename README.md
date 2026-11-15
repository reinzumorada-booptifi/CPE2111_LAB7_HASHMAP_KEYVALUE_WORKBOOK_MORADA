# Lab 7 — HashMap and Key-Value Structures (Part B)

## 1. Project Overview

This project implements the Part B deliverable for Lab 7: a keyed **Student Directory** backed by Java's built-in `java.util.HashMap<String, Student>`, plus a **Character Frequency Counter** backed by `java.util.HashMap<Character, Integer>`.

The implementation follows a test-first workflow: tests were written first against stub implementations (RED baseline), then the source was implemented to make all tests pass (GREEN).

## 2. Project Structure

```
starter-kit/labs/07-hashmap-key-value/
├── src/
│   ├── Student.java              # Immutable Student POJO (id, name, grade)
│   ├── StudentDirectory.java     # HashMap<String, Student> wrapper (7 methods)
│   └── CharFrequencyCounter.java # HashMap<Character, Integer> counter (count method)
├── test/
│   └── LabTestRunner.java        # Dependency-free acceptance test runner (26 tests)
├── out/                          # Compiled bytecode (gitignored)
├── AGENT_LOG.md                  # Phase-by-phase log (PLAN, TEST, BUILD, AUDIT)
├── README.md                     # This file
├── REFLECTION.md                 # Learning insights and complexity analysis
└── .gitignore                    # Excludes out/, *.class, IDE/OS files
```

## 3. Features

### Student Directory (`StudentDirectory`)
- Backed by `java.util.HashMap<String, Student>`
- Student IDs are `String` and must be unique
- `addStudent(id, student)` — returns `false` on duplicate ID, `true` on success
- `updateStudent(id, student)` — returns `false` on missing ID or ID mismatch, `true` on success
- `getStudent(id)` — returns the `Student` or `null` if absent
- `containsStudent(id)` — returns whether the ID exists
- `removeStudent(id)` — returns `true` if removed, `false` if missing
- `size()` — returns the number of students
- `snapshot()` — returns a defensive copy; the mutable backing map is never exposed
- All methods reject `null` IDs and `null` Student arguments with `NullPointerException`

### Character Frequency Counter (`CharFrequencyCounter`)
- Backed by `java.util.HashMap<Character, Integer>`
- `count(text)` — counts every character including spaces and punctuation
- Case-sensitive: `'A'` and `'a'` are distinct keys
- Empty string produces an empty map
- Null input throws `NullPointerException`
- Returns a defensive copy; internal state is not exposed

### Student Model (`Student`)
- Immutable: `final` fields, no setters
- Fields: `id` (String), `name` (String), `grade` (double)
- `equals()` and `hashCode()` based on student ID

## 4. Compilation and Execution

From the project root (`starter-kit/labs/07-hashmap-key-value/`):

```powershell
javac -d out src/*.java test/*.java
java -ea -cp out LabTestRunner
```

### Expected Output (GREEN)

```
=== LAB 7 | HASHMAP AND KEY-VALUE STRUCTURES - TEST RUNNER ===
TEST-STAGE: source stubs throw UnsupportedOperationException (RED baseline)

PASS  empty | new directory has size 0
...
PASS  bound | removeStudent with null ID throws

=== SUMMARY ===
tests run: 26 | passed: 26 | failed: 0
All tests passed (GREEN).
```

Exit code `0` indicates all tests passed.

## 5. Complexity Analysis

| Operation | Time (Average) | Time (Worst) | Auxiliary Space |
|-----------|---------------|--------------|-----------------|
| `addStudent` | O(1) | O(n) | O(1) |
| `getStudent` | O(1) | O(n) | O(1) |
| `updateStudent` | O(1) | O(n) | O(1) |
| `removeStudent` | O(1) | O(n) | O(1) |
| `containsStudent` | O(1) | O(n) | O(1) |
| `size` | O(1) | O(1) | O(1) |
| `snapshot` | O(n) | O(n) | O(n) |
| `count` (n characters) | O(n) | O(n²) pathological | O(k) where k = distinct chars |

- **Average case** assumes a good hash function distributing keys evenly across buckets.
- **Worst case** occurs when all keys collide into a single bucket, degrading to O(n) per operation.
- `snapshot()` is O(n) time and O(n) auxiliary space because it copies all entries into a new `HashMap`.
- `count()` is O(n) average for n characters; O(n²) pathological if every character hashes to the same bucket.

## 6. Test Coverage

26 dependency-free acceptance tests covering:

| Category | Tests |
|----------|-------|
| Empty Map (Case 1) | 5 |
| Duplicate Key (Case 2) | 2 |
| Missing Key (Case 3) | 3 |
| Record Update (Case 4) | 4 |
| Removal (Case 5) | 3 |
| Repeated Characters (Case 6) | 3 |
| Student-authored assertion | 1 |
| Boundary / invalid input | 5 |
| **Total** | **26** |

## 7. Design Decisions

- **String student IDs** (per workbook correction): not Integer
- **Immutable Student**: final fields, no setters; equals/hashCode by ID
- **`addStudent` rejects duplicates**: returns `false` rather than silently overwriting
- **`updateStudent` verifies ID match**: `student.getId().equals(id)` required
- **Defensive snapshots**: `new HashMap<>(students)` prevents caller mutation of the backing map
- **Case-sensitive character counting**: `'A'` and `'a'` are distinct; spaces and punctuation counted
- **Minimal API**: no `getCount()`, `mostFrequentChar()`, or `entrySet()` — not required by plan or tests
