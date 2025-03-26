package seedu.address.storage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.checkup.Checkup;

/**
 * Adapts a Checkup object for JSON serialization and deserialization.
 */
public class JsonAdaptedCheckup {
    private final String date;
    private final String time;

    /**
     * Constructs a JsonAdaptedCheckup with the specified date and time.
     *
     * @param date The date of the checkup in dd/MM/yyyy format.
     * @param time The time of the checkup in HH:mm format.
     */
    @JsonCreator
    public JsonAdaptedCheckup(@JsonProperty("date") String date,
                              @JsonProperty("time") String time) {
        this.date = date;
        this.time = time;
    }

    /**
     * Constructs a JsonAdaptedCheckup from a Checkup object.
     *
     * @param source The Checkup object to adapt.
     */
    public JsonAdaptedCheckup(Checkup source) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        date = source.getCheckupDate().format(dateFormatter);
        time = source.getCheckupTime().format(timeFormatter);
    }

    /**
     * Converts this JsonAdaptedCheckup back into a Checkup object.
     *
     * @return A Checkup object corresponding to this JSON representation.
     * @throws IllegalValueException If the date or time is invalid.
     */
    public Checkup toModelType() throws IllegalValueException {
        LocalDate checkupDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalTime checkupTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));

        return new Checkup(checkupDate, checkupTime);
    }
}
