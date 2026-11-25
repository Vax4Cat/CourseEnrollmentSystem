package reliability.entity;

import reliability.exception.InvalidEntityException;

/**
 * Immutable course ADT.
 * Rep invariant: cid and name are non-null and non-blank.
 * credit > 0 and maxCapacity >= 1.
 */
public class Course {

    private final String cid;
    private final String name;
    private final int credit;
    private final int maxCapacity;

    /**
     * Create a course.
     * @requires cid and name are non-null and non-blank,
     *           credit > 0 and maxCapacity >= 1
     * @modifies this
     * @effects initialize a new course
     * @throws InvalidEntityException if any parameter is invalid
     */
    public Course(String cid, String name, int credit, int maxCapacity) {
        if (cid == null || cid.isBlank()) {
            throw new InvalidEntityException("Course ID cannot be blank.");
        }

        if (name == null || name.isBlank()) {
            throw new InvalidEntityException("Course name cannot be blank.");
        }

        if (credit <= 0) {
            throw new InvalidEntityException("Credit must be positive.");
        }

        if (maxCapacity < 1) {
            throw new InvalidEntityException("Max capacity must be at least 1.");
        }

        this.cid = cid;
        this.name = name;
        this.credit = credit;
        this.maxCapacity = maxCapacity;

        checkRep();
    }

    /**
     * Check the representation invariant.
     */
    private void checkRep() {
        assert cid != null && !cid.isBlank();
        assert name != null && !name.isBlank();
        assert credit > 0;
        assert maxCapacity >= 1;
    }

    /**
     * Get course ID.
     * @requires true
     * @modifies nothing
     * @effects no side effects
     * @return course ID
     */
    public String getCid() {
        return cid;
    }

    /**
     * Get course name.
     * @requires true
     * @modifies nothing
     * @effects no side effects
     * @return course name
     */
    public String getName() {
        return name;
    }

    /**
     * Get course credit.
     * @requires true
     * @modifies nothing
     * @effects no side effects
     * @return course credit
     */
    public int getCredit() {
        return credit;
    }

    /**
     * Get maximum enrollment capacity.
     * @requires true
     * @modifies nothing
     * @effects no side effects
     * @return maximum enrollment capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
}