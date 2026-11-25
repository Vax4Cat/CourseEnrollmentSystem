package reliability.entity;

import reliability.exception.InvalidEntityException;

/**
 * Immutable student ADT.
 * Rep invariant: sid is non-null and non-blank.
 * name is non-null and non-blank.
 */
public class Student {

    private final String sid;
    private final String name;

    /**
     * Create a student.
     * @requires sid and name are non-null and non-blank
     * @modifies this
     * @effects initialize a new student
     * @throws InvalidEntityException if sid or name is invalid
     */
    public Student(String sid, String name) {
        if (sid == null || sid.isBlank()) {
            throw new InvalidEntityException("Student ID cannot be blank.");
        }

        if (name == null || name.isBlank()) {
            throw new InvalidEntityException("Student name cannot be blank.");
        }

        this.sid = sid;
        this.name = name;

        checkRep();
    }

    /**
     * Check the representation invariant.
     */
    private void checkRep() {
        assert sid != null && !sid.isBlank();
        assert name != null && !name.isBlank();
    }

    /**
     * Get student ID.
     * @requires true
     * @modifies nothing
     * @effects no side effects
     * @return student ID
     */
    public String getSid() {
        return sid;
    }

    /**
     * Get student name.
     * @requires true
     * @modifies nothing
     * @effects no side effects
     * @return student name
     */
    public String getName() {
        return name;
    }
}