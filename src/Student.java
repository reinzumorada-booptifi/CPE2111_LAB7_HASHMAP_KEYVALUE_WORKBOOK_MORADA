/**
 * Lab 7 - Student model (immutable).
 *
 * TEST-STAGE: this is a simple data holder and is fully implemented.
 * It carries a String student ID, a name, and a numeric grade.
 * Equality and hashCode are based on the student ID so that the
 * directory can enforce uniqueness on ID.
 */
public final class Student {

    private final String id;
    private final String name;
    private final double grade;

    public Student(String id, String name, double grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Student)) {
            return false;
        }
        Student other = (Student) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Student[id=" + id + ", name=" + name + ", grade=" + grade + "]";
    }
}
