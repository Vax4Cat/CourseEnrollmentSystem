package reliability.contract;

import reliability.entity.Student;
import reliability.entity.Course;
import reliability.entity.EnrollmentRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Course enrollment service using Design by Contract.
 * Rep invariant:
 * 1. No duplicate student-course pairs.
 * 2. The enrollment count of each course does not exceed its max capacity.
 * 3. All enrollment records are non-null.
 */
public class CourseEnrollmentService {

    private final List<EnrollmentRecord> records = new ArrayList<>();

    /**
     * Enroll a student into a course.
     * @requires student != null && course != null
     *           && this.hasNotEnrolled(student, course)
     *           && this.getEnrolledCount(course) < course.getMaxCapacity()
     * @modifies this
     * @effects add a new EnrollmentRecord
     * @throws AssertionError if the rep invariant is violated
     *         and assertions are enabled
     */
    public void enroll(Student student, Course course) {
        records.add(new EnrollmentRecord(student, course));
        checkRep();
    }

    /**
     * Drop a selected course.
     * @requires student != null && course != null
     *           && this.hasEnrolled(student, course)
     * @modifies this
     * @effects remove the corresponding EnrollmentRecord
     * @throws AssertionError if the rep invariant is violated
     *         and assertions are enabled
     */
    public void drop(Student student, Course course) {
        records.removeIf(record ->
                record.getStudent().getSid().equals(student.getSid())
                        && record.getCourse().getCid().equals(course.getCid()));

        checkRep();
    }

    /**
     * Query all courses taken by one student.
     * @requires student != null
     * @modifies nothing
     * @effects no side effects
     * @return immutable list of courses
     */
    public List<Course> getCoursesOfStudent(Student student) {
        return records.stream()
                .filter(record -> record.getStudent().getSid()
                        .equals(student.getSid()))
                .map(EnrollmentRecord::getCourse)
                .toList();
    }

    /**
     * Count current enrolled students of a course.
     * @requires course != null
     * @modifies nothing
     * @effects no side effects
     * @return current enrollment count
     */
    public int getEnrolledCount(Course course) {
        return (int) records.stream()
                .filter(record -> record.getCourse().getCid()
                        .equals(course.getCid()))
                .count();
    }

    /**
     * Check whether a student has enrolled in a course.
     * @requires student != null && course != null
     * @modifies nothing
     * @effects no side effects
     * @return true if the enrollment exists, otherwise false
     */
    public boolean hasEnrolled(Student student, Course course) {
        return records.stream()
                .anyMatch(record ->
                        record.getStudent().getSid().equals(student.getSid())
                                && record.getCourse().getCid().equals(course.getCid()));
    }

    /**
     * Check whether a student has not enrolled in a course.
     * @requires student != null && course != null
     * @modifies nothing
     * @effects no side effects
     * @return true if the enrollment does not exist, otherwise false
     */
    public boolean hasNotEnrolled(Student student, Course course) {
        return !hasEnrolled(student, course);
    }

    /**
     * Check the representation invariant.
     */
    private void checkRep() {
        assert isValidRep() : "Enrollment service invariant violated.";
    }

    /**
     * Determine whether the representation invariant holds.
     */
    private boolean isValidRep() {
        for (int i = 0; i < records.size(); i++) {
            EnrollmentRecord current = records.get(i);

            if (current == null) {
                return false;
            }

            Course course = current.getCourse();

            // Check course capacity.
            if (getEnrolledCount(course) > course.getMaxCapacity()) {
                return false;
            }

            // Check duplicate student-course pairs.
            for (int j = i + 1; j < records.size(); j++) {
                EnrollmentRecord other = records.get(j);

                if (other == null) {
                    return false;
                }

                if (current.getStudent().getSid()
                        .equals(other.getStudent().getSid())
                        && current.getCourse().getCid()
                        .equals(other.getCourse().getCid())) {
                    return false;
                }
            }
        }
        return true;
    }
}