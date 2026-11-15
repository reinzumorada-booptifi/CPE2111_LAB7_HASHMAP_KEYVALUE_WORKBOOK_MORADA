/**
 * Lab 7 - Dependency-free acceptance test runner for Part B.
 *
 * TEST-STAGE STATE: the source stubs in src/ throw
 * UnsupportedOperationException. This runner is the RED side of the
 * test-first workflow: every behavior-dependent test must fail clearly,
 * then turn green when BUILD fills in the implementations.
 *
 * Assertion style: dependency-free explicit helpers only (no JUnit).
 *   checkTrue / checkEquals / checkNull / checkNotNull
 * Thrown helpers are AssertionError; the runner reports them as FAIL.
 * Any RuntimeException (including UnsupportedOperationException from stubs)
 * is caught and reported as a FAIL with the exception name.
 *
 * Exit code: 0 when every test passes, 1 otherwise.
 */
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class LabTestRunner {

    private static int totalTests;
    private static int failedTests;
    private static final List<String> failureDetails = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== LAB 7 | HASHMAP AND KEY-VALUE STRUCTURES - TEST RUNNER ===");
        System.out.println("TEST-STAGE: source stubs throw UnsupportedOperationException (RED baseline)");
        System.out.println();

        // ------------------------------------------------------------------
        // Part B acceptance: Student Directory - Empty Map (Acceptance Case 1)
        // ------------------------------------------------------------------
        test("empty | new directory has size 0", LabTestRunner::testEmptySize);
        test("empty | getStudent on empty returns null", LabTestRunner::testEmptyGetNull);
        test("empty | containsStudent on empty returns false", LabTestRunner::testEmptyContainsFalse);
        test("empty | removeStudent on empty returns false", LabTestRunner::testEmptyRemoveFalse);
        test("empty | snapshot on empty is empty", LabTestRunner::testEmptySnapshot);

        // ------------------------------------------------------------------
        // Part B acceptance: Duplicate Key (Acceptance Case 2)
        // ------------------------------------------------------------------
        test("dup   | addStudent with duplicate ID returns false", LabTestRunner::testDuplicateKeyRejected);
        test("dup   | size unchanged after duplicate add attempt", LabTestRunner::testDuplicateSizeUnchanged);

        // ------------------------------------------------------------------
        // Part B acceptance: Missing Key (Acceptance Case 3)
        // ------------------------------------------------------------------
        test("miss  | getStudent on missing ID returns null", LabTestRunner::testMissingGetNull);
        test("miss  | containsStudent on missing ID returns false", LabTestRunner::testMissingContainsFalse);
        test("miss  | removeStudent on missing ID returns false", LabTestRunner::testMissingRemoveFalse);

        // ------------------------------------------------------------------
        // Part B acceptance: Record Update (Acceptance Case 4)
        // ------------------------------------------------------------------
        test("upd   | updateStudent on missing ID returns false", LabTestRunner::testUpdateMissingFalse);
        test("upd   | updateStudent verifies student ID matches requested ID", LabTestRunner::testUpdateIdMismatch);
        test("upd   | updateStudent on existing ID returns true", LabTestRunner::testUpdateExistingTrue);
        test("upd   | getStudent returns updated student", LabTestRunner::testUpdateGetUpdated);

        // ------------------------------------------------------------------
        // Part B acceptance: Removal (Acceptance Case 5)
        // ------------------------------------------------------------------
        test("rem   | removeStudent on existing ID returns true", LabTestRunner::testRemoveExistingTrue);
        test("rem   | getStudent returns null after removal", LabTestRunner::testRemoveGetNull);
        test("rem   | size decremented after removal", LabTestRunner::testRemoveSizeDecremented);

        // ------------------------------------------------------------------
        // Part B acceptance: Repeated Characters (Acceptance Case 6)
        // ------------------------------------------------------------------
        test("char  | count('banana') gives a:3, n:2, b:1", LabTestRunner::testBananaCounts);
        test("char  | case-sensitive: 'A' and 'a' are distinct keys", LabTestRunner::testCaseSensitive);
        test("char  | spaces and punctuation are counted", LabTestRunner::testSpacesPunctuation);

        // ------------------------------------------------------------------
        // Student-authored assertion
        // ------------------------------------------------------------------
        test("stud  | add several, then remove all: size 0 and snapshot empty", LabTestRunner::testAddThenRemoveAll);

        // ------------------------------------------------------------------
        // Boundary and invalid input
        // ------------------------------------------------------------------
        test("bound | addStudent with null ID throws", LabTestRunner::testNullIdAdd);
        test("bound | addStudent with null student throws", LabTestRunner::testNullStudentAdd);
        test("bound | count(null text) throws", LabTestRunner::testNullTextCount);
        test("bound | updateStudent with null ID throws", LabTestRunner::testNullIdUpdate);
        test("bound | removeStudent with null ID throws", LabTestRunner::testNullIdRemove);

        // ------------------------------------------------------------------
        // Summary
        // ------------------------------------------------------------------
        System.out.println();
        System.out.println("=== SUMMARY ===");
        System.out.println("tests run: " + totalTests
                + " | passed: " + (totalTests - failedTests)
                + " | failed: " + failedTests);
        if (failedTests > 0) {
            System.out.println();
            System.out.println("--- failure details ---");
            for (String detail : failureDetails) {
                System.out.println("  " + detail);
            }
            System.out.println();
            System.out.println("TEST-STAGE EXPECTATION: source stubs throw UnsupportedOperationException,");
            System.out.println("so every behavior-dependent test fails (reported as FAIL - never as a pass).");
            System.out.println("That is the intended RED state; BUILD replaces stubs until all pass.");
        } else {
            System.out.println("All tests passed (GREEN).");
        }
        System.exit(failedTests == 0 ? 0 : 1);
    }

    // ------------------------------------------------------------------
    // Registration + assertion helpers
    // ------------------------------------------------------------------

    private static void test(String name, Runnable body) {
        totalTests++;
        try {
            body.run();
            System.out.println("PASS  " + name);
        } catch (AssertionError ae) {
            fail(name, "assertion failed: " + String.valueOf(ae.getMessage()));
        } catch (RuntimeException re) {
            fail(name, "threw " + re.getClass().getSimpleName() + ": " + re.getMessage());
        }
    }

    private static void fail(String name, String reason) {
        failedTests++;
        String detail = name + " -- " + reason;
        failureDetails.add(detail);
        System.out.println("FAIL  " + detail);
    }

    private static void checkTrue(boolean condition, String detail) {
        if (!condition) {
            throw new AssertionError(detail);
        }
    }

    private static void checkEquals(Object expected, Object actual, String detail) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(detail + ": expected <" + expected + "> but was <" + actual + ">");
        }
    }

    private static void checkNull(Object actual, String detail) {
        if (actual != null) {
            throw new AssertionError(detail + ": expected null but was <" + actual + ">");
        }
    }

    private static void checkNotNull(Object actual, String detail) {
        if (actual == null) {
            throw new AssertionError(detail + ": expected non-null but was null");
        }
    }

    // ------------------------------------------------------------------
    // Part B acceptance: Empty Map (Acceptance Case 1)
    // ------------------------------------------------------------------

    private static void testEmptySize() {
        StudentDirectory dir = new StudentDirectory();
        checkEquals(0, dir.size(), "empty directory size must be 0");
    }

    private static void testEmptyGetNull() {
        StudentDirectory dir = new StudentDirectory();
        checkNull(dir.getStudent("any-id"), "getStudent on empty must return null");
    }

    private static void testEmptyContainsFalse() {
        StudentDirectory dir = new StudentDirectory();
        checkTrue(!dir.containsStudent("any-id"), "containsStudent on empty must be false");
    }

    private static void testEmptyRemoveFalse() {
        StudentDirectory dir = new StudentDirectory();
        checkTrue(!dir.removeStudent("any-id"), "removeStudent on empty must return false");
    }

    private static void testEmptySnapshot() {
        StudentDirectory dir = new StudentDirectory();
        Map<String, Student> snap = dir.snapshot();
        checkNotNull(snap, "snapshot on empty must be non-null");
        checkEquals(0, snap.size(), "snapshot on empty must be empty");
    }

    // ------------------------------------------------------------------
    // Part B acceptance: Duplicate Key (Acceptance Case 2)
    // ------------------------------------------------------------------

    private static void testDuplicateKeyRejected() {
        StudentDirectory dir = new StudentDirectory();
        Student s1 = new Student("S001", "Amy", 85.0);
        Student s2 = new Student("S001", "Bella", 90.0);
        boolean first = dir.addStudent("S001", s1);
        boolean second = dir.addStudent("S001", s2);
        checkTrue(first, "first addStudent must return true");
        checkTrue(!second, "duplicate addStudent must return false");
    }

    private static void testDuplicateSizeUnchanged() {
        StudentDirectory dir = new StudentDirectory();
        Student s1 = new Student("S001", "Amy", 85.0);
        Student s2 = new Student("S001", "Bella", 90.0);
        dir.addStudent("S001", s1);
        int before = dir.size();
        dir.addStudent("S001", s2);
        int after = dir.size();
        checkEquals(before, after, "size must be unchanged after duplicate add attempt");
        checkEquals(1, after, "size must remain 1 after duplicate add attempt");
    }

    // ------------------------------------------------------------------
    // Part B acceptance: Missing Key (Acceptance Case 3)
    // ------------------------------------------------------------------

    private static void testMissingGetNull() {
        StudentDirectory dir = new StudentDirectory();
        dir.addStudent("S001", new Student("S001", "Amy", 85.0));
        checkNull(dir.getStudent("S999"), "getStudent on missing ID must return null");
    }

    private static void testMissingContainsFalse() {
        StudentDirectory dir = new StudentDirectory();
        dir.addStudent("S001", new Student("S001", "Amy", 85.0));
        checkTrue(!dir.containsStudent("S999"), "containsStudent on missing ID must be false");
    }

    private static void testMissingRemoveFalse() {
        StudentDirectory dir = new StudentDirectory();
        dir.addStudent("S001", new Student("S001", "Amy", 85.0));
        checkTrue(!dir.removeStudent("S999"), "removeStudent on missing ID must return false");
    }

    // ------------------------------------------------------------------
    // Part B acceptance: Record Update (Acceptance Case 4)
    // ------------------------------------------------------------------

    private static void testUpdateMissingFalse() {
        StudentDirectory dir = new StudentDirectory();
        checkTrue(!dir.updateStudent("S999", new Student("S999", "Zed", 70.0)),
                "updateStudent on missing ID must return false");
    }

    private static void testUpdateIdMismatch() {
        StudentDirectory dir = new StudentDirectory();
        dir.addStudent("S001", new Student("S001", "Amy", 85.0));
        Student mismatched = new Student("S002", "Bella", 90.0);
        checkTrue(!dir.updateStudent("S001", mismatched),
                "updateStudent must reject when student ID does not match requested ID");
    }

    private static void testUpdateExistingTrue() {
        StudentDirectory dir = new StudentDirectory();
        dir.addStudent("S001", new Student("S001", "Amy", 85.0));
        Student updated = new Student("S001", "Amy", 92.0);
        checkTrue(dir.updateStudent("S001", updated), "updateStudent on existing ID must return true");
    }

    private static void testUpdateGetUpdated() {
        StudentDirectory dir = new StudentDirectory();
        dir.addStudent("S001", new Student("S001", "Amy", 85.0));
        dir.updateStudent("S001", new Student("S001", "Amy", 92.0));
        Student got = dir.getStudent("S001");
        checkNotNull(got, "getStudent after update must return non-null");
        checkEquals(92.0, got.getGrade(), "getStudent must return updated grade");
    }

    // ------------------------------------------------------------------
    // Part B acceptance: Removal (Acceptance Case 5)
    // ------------------------------------------------------------------

    private static void testRemoveExistingTrue() {
        StudentDirectory dir = new StudentDirectory();
        dir.addStudent("S001", new Student("S001", "Amy", 85.0));
        checkTrue(dir.removeStudent("S001"), "removeStudent on existing ID must return true");
    }

    private static void testRemoveGetNull() {
        StudentDirectory dir = new StudentDirectory();
        dir.addStudent("S001", new Student("S001", "Amy", 85.0));
        dir.removeStudent("S001");
        checkNull(dir.getStudent("S001"), "getStudent after removal must return null");
    }

    private static void testRemoveSizeDecremented() {
        StudentDirectory dir = new StudentDirectory();
        dir.addStudent("S001", new Student("S001", "Amy", 85.0));
        dir.addStudent("S002", new Student("S002", "Bella", 90.0));
        int before = dir.size();
        dir.removeStudent("S001");
        int after = dir.size();
        checkEquals(before - 1, after, "size must decrement after removal");
        checkEquals(1, after, "size must be 1 after removing one of two");
    }

    // ------------------------------------------------------------------
    // Part B acceptance: Repeated Characters (Acceptance Case 6)
    // ------------------------------------------------------------------

    private static void testBananaCounts() {
        CharFrequencyCounter counter = new CharFrequencyCounter();
        Map<Character, Integer> freq = counter.count("banana");
        checkNotNull(freq, "count must return non-null map");
        checkEquals(3, freq.get('a'), "count('banana')['a'] must be 3");
        checkEquals(2, freq.get('n'), "count('banana')['n'] must be 2");
        checkEquals(1, freq.get('b'), "count('banana')['b'] must be 1");
        checkEquals(6, (int) freq.values().stream().mapToInt(Integer::intValue).sum(),
                "total counts for 'banana' must be 6");
    }

    private static void testCaseSensitive() {
        CharFrequencyCounter counter = new CharFrequencyCounter();
        Map<Character, Integer> freq = counter.count("Aa");
        checkEquals(1, freq.get('A'), "count('Aa')['A'] must be 1 (case-sensitive)");
        checkEquals(1, freq.get('a'), "count('Aa')['a'] must be 1 (case-sensitive)");
        checkEquals(2, freq.size(), "count('Aa') must have 2 distinct keys (case-sensitive)");
    }

    private static void testSpacesPunctuation() {
        CharFrequencyCounter counter = new CharFrequencyCounter();
        Map<Character, Integer> freq = counter.count("a! a");
        checkEquals(2, freq.get('a'), "count('a! a')['a'] must be 2 (spaces not filtered)");
        checkEquals(1, freq.get(' '), "count('a! a')[' '] must be 1 (spaces counted)");
        checkEquals(1, freq.get('!'), "count('a! a')['!'] must be 1 (punctuation counted)");
    }

    // ------------------------------------------------------------------
    // Student-authored assertion
    // ------------------------------------------------------------------

    /**
     * Student-authored: populate the directory with several students,
     * remove them all one by one, and verify the directory is empty.
     * This is a meaningful end-to-end property the student can explain
     * during a live defense: add N, remove N, size must be 0 and
     * snapshot must be empty.
     */
    private static void testAddThenRemoveAll() {
        StudentDirectory dir = new StudentDirectory();
        String[] ids = { "S001", "S002", "S003", "S004" };
        for (String id : ids) {
            checkTrue(dir.addStudent(id, new Student(id, "Name" + id, 80.0)),
                    "addStudent(" + id + ") must succeed");
        }
        checkEquals(4, dir.size(), "size must be 4 after adding 4 students");
        for (String id : ids) {
            checkTrue(dir.removeStudent(id), "removeStudent(" + id + ") must return true");
        }
        checkEquals(0, dir.size(), "size must be 0 after removing all students");
        Map<String, Student> snap = dir.snapshot();
        checkEquals(0, snap.size(), "snapshot must be empty after removing all students");
    }

    // ------------------------------------------------------------------
    // Boundary and invalid input
    // ------------------------------------------------------------------

    private static void testNullIdAdd() {
        StudentDirectory dir = new StudentDirectory();
        try {
            dir.addStudent(null, new Student("S001", "Amy", 85.0));
            throw new AssertionError("addStudent(null, student) must throw");
        } catch (NullPointerException expected) {
            // expected invalid-input exception
        }
    }

    private static void testNullStudentAdd() {
        StudentDirectory dir = new StudentDirectory();
        try {
            dir.addStudent("S001", null);
            throw new AssertionError("addStudent(id, null) must throw");
        } catch (NullPointerException expected) {
            // expected invalid-input exception
        }
    }

    private static void testNullTextCount() {
        CharFrequencyCounter counter = new CharFrequencyCounter();
        try {
            counter.count(null);
            throw new AssertionError("count(null) must throw");
        } catch (NullPointerException expected) {
            // expected invalid-input exception
        }
    }

    private static void testNullIdUpdate() {
        StudentDirectory dir = new StudentDirectory();
        try {
            dir.updateStudent(null, new Student("S001", "Amy", 85.0));
            throw new AssertionError("updateStudent(null, student) must throw");
        } catch (NullPointerException expected) {
            // expected invalid-input exception
        }
    }

    private static void testNullIdRemove() {
        StudentDirectory dir = new StudentDirectory();
        try {
            dir.removeStudent(null);
            throw new AssertionError("removeStudent(null) must throw");
        } catch (NullPointerException expected) {
            // expected invalid-input exception
        }
    }
}
