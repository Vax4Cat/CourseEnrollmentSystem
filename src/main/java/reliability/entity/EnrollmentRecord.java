package reliability.entity;

import reliability.exception.InvalidEntityException;

import java.time.LocalDateTime;

/**
 * Immutable enrollment record ADT.
 * Rep invariant: student, course and enrollmentTime
 * are all non-null.
 */
public class EnrollmentRecord {

    private final Student student;
    private final Course course;
    private final LocalDateTime enrollmentTime;

    /**
     * Create an enrollment record.
     * @requires student != null and course != null
     * @modifies this
     * @effects create an enrollment record with current time
     * @throws InvalidEntityException if student or course is null
     */
    public EnrollmentRecord(Student student, Course course) {
        if (student == null || course == null) {
            throw new InvalidEntityException("Student and course cannot be null.");
        }

        this.student = student;
        this.course = course;
        this.enrollmentTime = LocalDateTime.now();

        checkRep();
    }

    /**
     * Check the representation invariant.
     */
    private void checkRep() {
        assert student != null;
        assert course != null;
        assert enrollmentTime != null;
    }

    /**
     * Get the student.
     * @requires true
     * @modifies nothing
     * @effects no side effects
     * @return student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Get the course.
     * @requires true
     * @modifies nothing
     * @effects no side effects
     * @return course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Get enrollment time.
     * @requires true
     * @modifies nothing
     * @effects no side effects
     * @return enrollment timestamp
     */
    public LocalDateTime getEnrollmentTime() {
        return enrollmentTime;
    }
}