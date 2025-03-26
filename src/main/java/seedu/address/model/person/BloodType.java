package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's blood type in the address book.
 */
public class BloodType {

    public static final String MESSAGE_CONSTRAINTS =
            "Blood type should only be A+, A-, B+, B-, AB+, AB-, O+ or O-";

    /*
     * The string must match any of the given types
     */
    public static final String VALIDATION_REGEX = "^(A\\+|A\\-|B\\+|B\\-|AB\\+|AB\\-|O\\+|O\\-)$";

    public final String bloodType;

    /**
     * Constructs a {@code BloodType}.
     *
     * @param bloodType A valid bloodType.
     */
    public BloodType(String bloodType) {
        requireNonNull(bloodType);
        checkArgument(isValidBloodType(bloodType.toUpperCase()), MESSAGE_CONSTRAINTS);
        this.bloodType = bloodType.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid blood type.
     */
    public static boolean isValidBloodType(String test) {
        return test.toUpperCase().matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.bloodType;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BloodType)) {
            return false;
        }

        BloodType otherBlood = (BloodType) other;
        return bloodType.equals(otherBlood.bloodType);
    }

    @Override
    public int hashCode() {
        return bloodType.hashCode();
    }
}
