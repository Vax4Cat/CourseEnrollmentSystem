package reliability.contract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reliability.entity.Student;
import reliability.entity.Course;
import reliability.entity.EnrollmentRecord;
import reliability.exception.InvalidEntityException;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseEnrollmentServiceTest {
    private CourseEnrollmentService service;
    private Student alice;
    private Student bob;
    private Course javaCourse;

    @BeforeEach
    void setUp() {
        service = new CourseEnrollmentService();
        alice = new Student("S001", "Alice");
        bob = new Student("S002", "Bob");
        javaCourse = new Course("C001", "Java", 3, 1);
    }

    @Test
    void testNormalEnrollmentAndDrop() {
        service.enroll(alice, javaCourse);
        assertTrue(service.hasEnrolled(alice, javaCourse));
        assertEquals(1, service.getEnrolledCount(javaCourse));
        assertEquals(1, service.getCoursesOfStudent(alice).size());
        service.drop(alice, javaCourse);
        assertTrue(service.hasNotEnrolled(alice, javaCourse));
        assertEquals(0, service.getEnrolledCount(javaCourse));
        assertTrue(service.getCoursesOfStudent(alice).isEmpty());
    }

    @Test
    void testDuplicateEnrollment() {
        Course course = new Course("C002", "Database", 3, 5);
        service.enroll(alice, course);

        if (CourseEnrollmentService.class.desiredAssertionStatus()) {
            assertThrows(AssertionError.class,
                    () -> service.enroll(alice, course));
        } else {
            assertDoesNotThrow(() -> service.enroll(alice, course));
            assertEquals(2, service.getEnrolledCount(course));
        }
    }

    @Test
    void testCourseFull() {
        service.enroll(alice, javaCourse);

        if (CourseEnrollmentService.class.desiredAssertionStatus()) {
            assertThrows(AssertionError.class,
                    () -> service.enroll(bob, javaCourse));
        } else {
            assertDoesNotThrow(() -> service.enroll(bob, javaCourse));
            assertEquals(2, service.getEnrolledCount(javaCourse));
        }
    }

    @Test
    void testDropNotEnrolled() {
        assertDoesNotThrow(() -> service.drop(alice, javaCourse));
        assertEquals(0, service.getEnrolledCount(javaCourse));
    }

    @Test
    void testNullEntity() {
        assertThrows(InvalidEntityException.class,
                () -> service.enroll(null, javaCourse));
        assertThrows(InvalidEntityException.class,
                () -> service.enroll(alice, null));
    }

    @Test
    void testImmutableCourseList() {
        service.enroll(alice, javaCourse);
        List<Course> courses = service.getCoursesOfStudent(alice);
        assertThrows(UnsupportedOperationException.class,
                () -> courses.add(javaCourse));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testManualInvariantViolation() throws Exception {
        Field field = CourseEnrollmentService.class
                .getDeclaredField("records");
        field.setAccessible(true);
        List<EnrollmentRecord> records =
                (List<EnrollmentRecord>) field.get(service);
        // Manually insert duplicate enrollment records.
        records.add(new EnrollmentRecord(alice, javaCourse));
        records.add(new EnrollmentRecord(alice, javaCourse));
        Course anotherCourse = new Course("C002", "Database", 3, 5);

        if (CourseEnrollmentService.class.desiredAssertionStatus()) {
            assertThrows(AssertionError.class,
                    () -> service.enroll(bob, anotherCourse));
        } else {
            assertDoesNotThrow(
                    () -> service.enroll(bob, anotherCourse));
            assertEquals(2, service.getEnrolledCount(javaCourse));
        }
    }
}