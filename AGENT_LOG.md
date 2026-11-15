# Agent Log — Lab 7 Part B (Student Directory + Char Frequency Counter)

## 2026-11-15 — TEST phase (RED baseline)

### 1. Project location
`starter-kit/labs/07-hashmap-key-value/` under the workspace root
`C:\Users\Admin\stack and que\CPE211_LAB7_HASHMAP_KEYVALUE_WORKBOOK\starter-kit\labs\07-hashmap-key-value\`

### 2. Files inspected before TEST
No pre-existing files existed at the approved project location. The directory
`starter-kit/labs/07-hashmap-key-value/` did not exist; it was created during
this session. No starter source files or instructor-supplied acceptance tests
were present to preserve.

### 3. Files created during TEST

| Path | Purpose | Implementation state |
|------|---------|---------------------|
| `src/Student.java` | Immutable Student POJO (id, name, grade). Equality/hashCode by id. | Fully implemented (data holder only) |
| `src/StudentDirectory.java` | HashMap<String, Student> wrapper | All methods were stubs throwing `UnsupportedOperationException`. `entrySet()` removed (does not expose mutable backing map or entry set). |
| `src/CharFrequencyCounter.java` | HashMap<Character, Integer> counter | All methods were stubs throwing `UnsupportedOperationException`. `mostFrequentChar()` removed (not required by approved plan). |
| `test/LabTestRunner.java` | Dependency-free acceptance test runner (26 tests) | Fully implemented. Null-input tests require `NullPointerException`. All section comments relabeled as "Part B acceptance". |
| `AGENT_LOG.md` | This log. | — |

### 3b. Files created during AUDIT

| Path | Purpose |
|------|---------|
| `README.md` | Project overview, structure, compilation, execution, features, complexity |
| `REFLECTION.md` | Learning insights, HashMap behavior, collisions, immutability, defensive copies |
| `.gitignore` | Excludes out/, *.class, IDE/OS files |

### 4. Compile command (TEST)
```
javac -d out src/*.java test/*.java
```
Result: compilation succeeded ($LASTEXITCODE 0).

### 5. Run command (TEST)
```
java -ea -cp out LabTestRunner
```
Result: $LASTEXITCODE 1 (RED baseline). 26 run, 0 passed, 26 failed — all failures were
`UnsupportedOperationException` from stubs.

## 2026-11-15 — BUILD phase (implementation complete)

### 6. Files modified during BUILD

| Path | Changes |
|------|---------|
| `src/StudentDirectory.java` | Replaced all 7 stub methods with full implementations using `HashMap<String, Student>`. Added null checks (NullPointerException) for id and student on all methods. `addStudent` returns false on duplicate ID. `updateStudent` returns false on missing ID or ID mismatch, true on success. `snapshot()` returns `new HashMap<>(students)` (defensive copy). |
| `src/CharFrequencyCounter.java` | Replaced stub methods with full implementation using `HashMap<Character, Integer>`. `count()` counts every character (case-sensitive, including spaces and punctuation), throws `NullPointerException` for null input, returns defensive copy. Public API is minimal — only `count(String)`; no `getCount()` or `mostFrequentChar()` required by the plan or tests. |
| `src/Student.java` | No changes — already immutable with final fields, getters, equals/hashCode by ID. |
| `test/LabTestRunner.java` | No changes during BUILD. |

### 6b. Files created during AUDIT

See section 3b above (`README.md`, `REFLECTION.md`, `.gitignore`).

### 7. Compile command (BUILD)
```
javac -d out src/*.java test/*.java
```
Result: compilation succeeded ($LASTEXITCODE 0). No errors.

### 8. Run command (BUILD)
```
java -ea -cp out LabTestRunner
```
Result: $LASTEXITCODE 0 (GREEN).

### 9. Observed GREEN output (26 tests)

All 26 tests pass:

| Test | Result |
|------|--------|
| empty | size 0 | PASS |
| empty | getStudent null | PASS |
| empty | containsStudent false | PASS |
| empty | removeStudent false | PASS |
| empty | snapshot empty | PASS |
| dup | duplicate rejected | PASS |
| dup | size unchanged | PASS |
| miss | getStudent null | PASS |
| miss | containsStudent false | PASS |
| miss | removeStudent false | PASS |
| upd | update missing false | PASS |
| upd | ID mismatch | PASS |
| upd | update existing true | PASS |
| upd | get updated | PASS |
| rem | remove existing true | PASS |
| rem | get null after remove | PASS |
| rem | size decremented | PASS |
| char | banana counts | PASS |
| char | case-sensitive | PASS |
| char | spaces/punctuation | PASS |
| stud | add then remove all | PASS |
| bound | null ID add | PASS |
| bound | null student add | PASS |
| bound | null text count | PASS |
| bound | null ID update | PASS |
| bound | null ID remove | PASS |

Summary: 26 run, 26 passed, 0 failed. All six acceptance cases plus boundary tests pass.

### 10. Six acceptance cases covered (Part B)
1. Empty Map — size 0, get→null, contains→false, remove→false, snapshot empty
2. Duplicate Key — addStudent returns false on duplicate, size unchanged
3. Missing Key — getStudent→null, containsStudent→false, removeStudent→false
4. Record Update — updateStudent returns false on missing, true on existing, verifies ID match, get returns updated
5. Removal — removeStudent returns true, get→null after, size decremented
6. Repeated Characters — "banana" → a:3, n:2, b:1; case-sensitive; spaces/punctuation counted

### 11. Student-authored assertion
`testAddThenRemoveAll`: add 4 students (size→4), remove all 4 (size→0, snapshot empty).
Verifies end-to-end add-then-remove property.

### 12. Boundary / invalid-input tests (all 5 pass)
- `addStudent(null, student)` → NullPointerException
- `addStudent(id, null)` → NullPointerException
- `count(null)` → NullPointerException
- `updateStudent(null, student)` → NullPointerException
- `removeStudent(null)` → NullPointerException

### 13. BUILD status
COMPLETE. All implementation methods replaced stubs. All 26 tests pass (GREEN).

## 2026-11-15 — AUDIT phase (final verification)

### 14. Agent Limitations (recorded for transparency)

- **`@test-reviewer` skill:** Not available in the tool inventory (only `opencode` and `report` skills exist). Test review was performed directly by reviewing `LabTestRunner.java` against the six acceptance cases and boundary tests. No missing edge cases or weak assertions were identified beyond those already covered.
- **`@complexity-auditor` skill:** Not available in the tool inventory. Complexity analysis was performed directly using the approved plan's expected complexities (O(1) average / O(n) worst for HashMap operations; O(n) for snapshot; O(n) average / O(n²) pathological for frequency count). No benchmark measurements were invented or recorded.

### 15. Source Code Review (TASK 1)

| File | Status | Notes |
|------|--------|-------|
| `src/Student.java` | PASS | Immutable, final fields, getters only, equals/hashCode by ID |
| `src/StudentDirectory.java` | PASS | 7 methods, all null-checked, duplicate rejection, ID mismatch rejection, defensive snapshot copy |
| `src/CharFrequencyCounter.java` | PASS | Minimal API (only `count()`), case-sensitive, counts all chars including spaces/punctuation, defensive copy |
| `test/LabTestRunner.java` | PASS | 26 tests, "Part B acceptance" labels, null tests require NPE |

No unnecessary methods or classes exist. `entrySet()` and `mostFrequentChar()` were correctly excluded.

### 16. Complexity Review (TASK 3)

| Operation | Time (Average) | Time (Worst) | Auxiliary Space |
|-----------|---------------|--------------|-----------------|
| `addStudent` / `getStudent` / `updateStudent` / `removeStudent` / `containsStudent` | O(1) | O(n) | O(1) |
| `size` | O(1) | O(1) | O(1) |
| `snapshot` | O(n) | O(n) | O(n) |
| `count` (n characters) | O(n) | O(n²) pathological | O(k) |

Time complexity separated from auxiliary-space complexity. No benchmark measurements were recorded (measurements not required by the approved plan).

### 17. Final Verification (TASK 6)

**Compile:**
```
javac -d out src/*.java test/*.java
```
`$LASTEXITCODE: 0`

**Run:**
```
java -ea -cp out LabTestRunner
```
`$LASTEXITCODE: 0`

**Result:** 26 run, 26 passed, 0 failed (GREEN).

### 18. AUDIT status
COMPLETE. All phases (PLAN, TEST, BUILD, AUDIT) finished. All 26 tests pass. Documentation complete.

