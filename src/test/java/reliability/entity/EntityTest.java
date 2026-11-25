package reliability.entity;

import org.junit.jupiter.api.Test;
import reliability.exception.InvalidEntityException;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {
    @Test
    void testValidEntities() {
        Student student = new Student("S001", "Alice");
        Course course = new Course("C001", "Java", 3, 2);
        EnrollmentRecord record = new EnrollmentRecord(student, course);
        assertEquals("S001", student.getSid());
        assertEquals("Alice", student.getName());
        assertEquals("C001", course.getCid());
        assertEquals("Java", course.getName());
        assertEquals(3, course.getCredit());
        assertEquals(2, course.getMaxCapacity());
        assertSame(student, record.getStudent());
        assertSame(course, record.getCourse());
        assertNotNull(record.getEnrollmentTime());
    }

    @Test
    void testInvalidStudentId() {
        assertThrows(InvalidEntityException.class,
                () -> new Student("", "Alice"));
        assertThrows(InvalidEntityException.class,
                () -> new Student(null, "Alice"));
    }

    @Test
    void testInvalidCourseId() {
        assertThrows(InvalidEntityException.class,
                () -> new Course(" ", "Java", 3, 2));
        assertThrows(InvalidEntityException.class,
                () -> new Course(null, "Java", 3, 2));
    }

    @Test
    void testInvalidCapacity() {
        assertThrows(InvalidEntityException.class,
                () -> new Course("C001", "Java", 3, 0));
    }

    @Test
    void testInvalidEnrollmentRecord() {
        Student student = new Student("S001", "Alice");
        Course course = new Course("C001", "Java", 3, 2);
        assertThrows(InvalidEntityException.class,
                () -> new EnrollmentRecord(null, course));
        assertThrows(InvalidEntityException.class,
                () -> new EnrollmentRecord(student, null));
    }
}