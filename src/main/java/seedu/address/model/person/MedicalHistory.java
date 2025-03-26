package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's medical history in the address book.
 */
public class MedicalHistory {

    public static final String MESSAGE_CONSTRAINTS =
            "Medical history should only contain alphanumeric characters and spaces, and it should not be blank";
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9 ]+$";

    public final String medicalHistory;

    /**
     * Constructs a {@code MedicalHistory}.
     *
     * @param medicalHistory A valid medical history (not blank).
     */
    public MedicalHistory(String medicalHistory) {
        requireNonNull(medicalHistory);
        this.medicalHistory = medicalHistory;
    }

    public static boolean isValidMedicalHistory(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return '[' + medicalHistory + ']';
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MedicalHistory)) {
            return false;
        }

        MedicalHistory otherMedicalHistory = (MedicalHistory) other;
        return medicalHistory.equals(otherMedicalHistory.medicalHistory);
    }

    @Override
    public int hashCode() {
        return medicalHistory.hashCode();
    }
}
