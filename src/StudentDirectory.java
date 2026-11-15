/**
 * Lab 7 - Keyed Student Directory backed by java.util.HashMap<String, Student>.
 *
 * BUILD-STAGE: all methods are implemented per the approved plan.
 *
 * Contract (approved plan):
 *   - Student IDs are String and must be unique.
 *   - addStudent(id, student) returns false when id already exists (duplicate rejection).
 *   - updateStudent(id, student) returns false for a missing ID, true on success;
 *     it verifies student.getId().equals(id) before updating.
 *   - getStudent(id) returns the Student or null if absent.
 *   - removeStudent(id) returns true if removed, false if not found.
 *   - size() returns the number of students.
 *   - snapshot() returns a defensive copy; the mutable backing map is never exposed.
 *   - Null IDs and null Student arguments throw NullPointerException.
 */
import java.util.HashMap;
import java.util.Map;

public class StudentDirectory {

    private final Map<String, Student> students = new HashMap<>();

    /** Adds a student; returns false if the ID already exists. */
    public boolean addStudent(String id, Student student) {
        if (id == null) {
            throw new NullPointerException("id must not be null");
        }
        if (student == null) {
            throw new NullPointerException("student must not be null");
        }
        if (students.containsKey(id)) {
            return false;
        }
        students.put(id, student);
        return true;
    }

    /** Updates an existing student; returns false if ID is missing or IDs mismatch. */
    public boolean updateStudent(String id, Student student) {
        if (id == null) {
            throw new NullPointerException("id must not be null");
        }
        if (student == null) {
            throw new NullPointerException("student must not be null");
        }
        if (!student.getId().equals(id)) {
            return false;
        }
        if (!students.containsKey(id)) {
            return false;
        }
        students.put(id, student);
        return true;
    }

    /** Retrieves a student by ID; returns null if absent. */
    public Student getStudent(String id) {
        if (id == null) {
            throw new NullPointerException("id must not be null");
        }
        return students.get(id);
    }

    /** Returns true if a student with the given ID exists. */
    public boolean containsStudent(String id) {
        if (id == null) {
            throw new NullPointerException("id must not be null");
        }
        return students.containsKey(id);
    }

    /** Removes a student by ID; returns true if removed. */
    public boolean removeStudent(String id) {
        if (id == null) {
            throw new NullPointerException("id must not be null");
        }
        if (!students.containsKey(id)) {
            return false;
        }
        students.remove(id);
        return true;
    }

    /** Returns the number of students in the directory. */
    public int size() {
        return students.size();
    }

    /**
     * Returns a defensive copy of the backing map.
     * The caller must not be able to mutate the directory through the result.
     */
    public Map<String, Student> snapshot() {
        return new HashMap<>(students);
    }
}
