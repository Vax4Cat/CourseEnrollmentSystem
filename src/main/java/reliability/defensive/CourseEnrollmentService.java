package reliability.defensive;

import reliability.entity.Student;
import reliability.entity.Course;
import reliability.entity.EnrollmentRecord;
import reliability.exception.EnrollmentConflictException;
import reliability.exception.CourseFullException;
import reliability.exception.NotEnrolledException;
import reliability.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.List;

/**
 * Course enrollment service using defensive programming.
 * Rep invariant:
 * 1. No duplicate student-course pairs.
 * 2. The enrollment count of each course does not exceed its max capacity.
 * 3. All enrollment records are non-null.
 * 4. Courses with the same ID have consistent information.
 */
public class CourseEnrollmentService {

    private final List<EnrollmentRecord> records = new ArrayList<>();

    /**
     * Enroll a student into a course.
     * @requires true; all inputs are validated internally
     * @modifies this
     * @effects add a new EnrollmentRecord
     * @throws InvalidEntityException if student or course is invalid
     * @throws EnrollmentConflictException if duplicate enrollment
     * @throws CourseFullException if course reaches max capacity
     */
    public void enroll(Student student, Course course) {
        validateStudent(student);
        validateCourse(course);

        if (hasEnrolled(student, course)) {
            throw new EnrollmentConflictException(
                    "Student has already enrolled in this course.");
        }

        if (getEnrolledCount(course) >= course.getMaxCapacity()) {
            throw new CourseFullException("Course is full.");
        }

        records.add(new EnrollmentRecord(student, course));
        checkRep();
    }

    /**
     * Drop a selected course.
     * @requires true; all inputs are validated internally
     * @modifies this
     * @effects remove the corresponding EnrollmentRecord
     * @throws InvalidEntityException if student or course is invalid
     * @throws NotEnrolledException if the student has not enrolled
     */
    public void drop(Student student, Course course) {
        validateStudent(student);
        validateCourse(course);

        if (!hasEnrolled(student, course)) {
            throw new NotEnrolledException(
                    "Student has not enrolled in this course.");
        }

        records.removeIf(record ->
                record.getStudent().getSid().equals(student.getSid())
                        && record.getCourse().getCid().equals(course.getCid()));

        checkRep();
    }

    /**
     * Query all courses taken by one student.
     * @requires true; all inputs are validated internally
     * @modifies nothing
     * @effects no side effects
     * @throws InvalidEntityException if student is invalid
     * @return immutable list of courses
     */
    public List<Course> getCoursesOfStudent(Student student) {
        validateStudent(student);

        return records.stream()
                .filter(record -> record.getStudent().getSid()
                        .equals(student.getSid()))
                .map(EnrollmentRecord::getCourse)
                .toList();
    }

    /**
     * Count current enrolled students of a course.
     * @requires true; all inputs are validated internally
     * @modifies nothing
     * @effects no side effects
     * @throws InvalidEntityException if course is invalid
     * @return current enrollment count
     */
    public int getEnrolledCount(Course course) {
        validateCourse(course);

        return (int) records.stream()
                .filter(record -> record.getCourse().getCid()
                        .equals(course.getCid()))
                .count();
    }

    /**
     * Check whether a student has enrolled in a course.
     * @requires true; all inputs are validated internally
     * @modifies nothing
     * @effects no side effects
     * @throws InvalidEntityException if student or course is invalid
     * @return true if the enrollment exists, otherwise false
     */
    public boolean hasEnrolled(Student student, Course course) {
        validateStudent(student);
        validateCourse(course);

        return records.stream()
                .anyMatch(record ->
                        record.getStudent().getSid().equals(student.getSid())
                                && record.getCourse().getCid().equals(course.getCid()));
    }

    /**
     * Check whether a student has not enrolled in a course.
     * @requires true; all inputs are validated internally
     * @modifies nothing
     * @effects no side effects
     * @throws InvalidEntityException if student or course is invalid
     * @return true if the enrollment does not exist, otherwise false
     */
    public boolean hasNotEnrolled(Student student, Course course) {
        return !hasEnrolled(student, course);
    }

    /**
     * Validate student parameters.
     */
    private void validateStudent(Student student) {
        if (student == null) {
            throw new InvalidEntityException("Student cannot be null.");
        }

        if (student.getSid() == null || student.getSid().isBlank()) {
            throw new InvalidEntityException("Student ID cannot be blank.");
        }
    }

    /**
     * Validate course parameters and course information.
     */
    private void validateCourse(Course course) {
        if (course == null) {
            throw new InvalidEntityException("Course cannot be null.");
        }

        if (course.getCid() == null || course.getCid().isBlank()) {
            throw new InvalidEntityException("Course ID cannot be blank.");
        }

        if (course.getMaxCapacity() < 1) {
            throw new InvalidEntityException(
                    "Max capacity must be at least 1.");
        }

        for (EnrollmentRecord record : records) {
            Course existing = record.getCourse();

            if (existing.getCid().equals(course.getCid())
                    && hasDifferentCourseInfo(existing, course)) {
                throw new InvalidEntityException(
                        "Inconsistent information for the same course ID.");
            }
        }
    }

    /**
     * Check whether two courses have consistent information.
     */
    private boolean hasDifferentCourseInfo(Course first, Course second) {
        return !first.getName().equals(second.getName())
                || first.getCredit() != second.getCredit()
                || first.getMaxCapacity() != second.getMaxCapacity();
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
        for (EnrollmentRecord record : records) {
            if (record == null
                    || record.getStudent() == null
                    || record.getCourse() == null) {
                return false;
            }
        }

        for (int i = 0; i < records.size(); i++) {
            EnrollmentRecord current = records.get(i);
            Course course = current.getCourse();

            long count = records.stream()
                    .filter(record -> record.getCourse().getCid()
                            .equals(course.getCid()))
                    .count();

            if (count > course.getMaxCapacity()) {
                return false;
            }

            for (int j = i + 1; j < records.size(); j++) {
                EnrollmentRecord other = records.get(j);

                if (course.getCid().equals(other.getCourse().getCid())) {

                    if (hasDifferentCourseInfo(course, other.getCourse())) {
                        return false;
                    }

                    if (current.getStudent().getSid()
                            .equals(other.getStudent().getSid())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}