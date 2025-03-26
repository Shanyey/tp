package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.MedicalHistory;

/**
 * Jackson-friendly version of {@link MedicalHistory}.
 */
public class JsonAdaptedMedicalHistory {

    private final String medicalHistory;

    /**
     * Constructs a {@code JsonAdaptedMedicalHistory} with the given {@code medicalHistory} details.
     */
    @JsonCreator
    public JsonAdaptedMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    /**
     * Converts a given {@code MedicalHistory} into this class for Jackson use.
     */
    public JsonAdaptedMedicalHistory(MedicalHistory source) {
        medicalHistory = source.medicalHistory;
    }

    @JsonValue
    public String getMedicalHistory() {
        return medicalHistory;
    }

    /**
     * Converts this Jackson-friendly adapted medical history object into the model's {@code MedicalHistory} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted medical history.
     */
    public MedicalHistory toModelType() throws IllegalValueException {
        if (!MedicalHistory.isValidMedicalHistory(medicalHistory)) {
            throw new IllegalValueException(MedicalHistory.MESSAGE_CONSTRAINTS);
        }
        return new MedicalHistory(medicalHistory);
    }
}
