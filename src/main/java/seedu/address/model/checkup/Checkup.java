package seedu.address.model.checkup;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Represents a checkup appointment for a patient with a nurse.
 */
public class Checkup {

    public static final String MESSAGE_CONSTRAINTS = "Date Of Birth can only take the format:"
            + "DD/MM/YYYY, and must be an existing date";
    public static final String MESSAGE_INVALID_DATETIME = "Time slot is not available";
    public static final String MESSAGE_FIFTEEN = "Please use a time in blocks of 00, 15, 30, or 45 minutes"
        + " (e.g., 1000, 1015, 1030, 1045).";
    public static final String MESSAGE_OUTSIDE_BUSINESS_HOURS =
            "Checkup must be scheduled between 9:00 AM and 5:00 PM";
    public static final String MESSAGE_PAST_DATE =
            "Checkup cannot be scheduled in the past";
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);
    public final LocalDateTime checkupDateTime;
    public final Boolean isAdding;


    /**
     * Constructs a Checkup with the specified date and time.
     *
     * @param checkupDate The date of the checkup.
     * @param checkupTime The time of the checkup.
     * @param isAdd If the checkup is getting added or deleted from the patient
     * @throws ParseException If the checkup date or time is invalid.
     */
    public Checkup(LocalDate checkupDate, LocalTime checkupTime, Boolean isAdd) throws ParseException {
        allNonNull(checkupDate, checkupTime);
        checkArgument(isValidCheckup(checkupDate, checkupTime, isAdd), MESSAGE_CONSTRAINTS);
        this.isAdding = isAdd;
        this.checkupDateTime = createCheckupDateTime(checkupDate, checkupTime);
    }

    /**
     * Checks that both date and time are non-null.
     *
     * @param checkupDate The date to check.
     * @param checkupTime The time to check.
     */
    public void allNonNull(LocalDate checkupDate, LocalTime checkupTime) {
        requireNonNull(checkupDate);
        requireNonNull(checkupTime);
    }

    /**
     * Validates the checkup date and time.
     *
     * @param checkupDate The date of the checkup.
     * @param checkupTime The time of the checkup.
     * @param isAdd If the checkup is to be added or deleted.
     * @return True if the checkup date and time are valid; false otherwise.
     * @throws ParseException If the checkup date or time is invalid.
     */
    public static boolean isValidCheckup(LocalDate checkupDate, LocalTime checkupTime, Boolean isAdd)
            throws ParseException {
        LocalDateTime checkupDateTime = createCheckupDateTime(checkupDate, checkupTime);
        if (!isWithinBusinessHours(checkupDateTime)) {
            throw new ParseException(MESSAGE_OUTSIDE_BUSINESS_HOURS);
        }
        if (!isNotInPast(checkupDateTime) && isAdd) {
            throw new ParseException(MESSAGE_PAST_DATE);
        }
        if (!isNotFifteen(checkupDateTime) && isAdd) {
            throw new ParseException(MESSAGE_FIFTEEN);
        }
        return true;
    }

    /**
     * Creates a LocalDateTime object from the provided date and time.
     *
     * @param date The date of the checkup.
     * @param time The time of the checkup.
     * @return A LocalDateTime object representing the checkup date and time.
     */
    private static LocalDateTime createCheckupDateTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }

    /**
     * Checks if the given date and time are within business hours.
     *
     * @param dateTime The date and time to check.
     * @return True if the date and time are within business hours; false otherwise.
     */
    private static boolean isWithinBusinessHours(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        return !time.isBefore(START_TIME) && !time.isAfter(END_TIME);
    }

    /**
     * Checks if the provided checkup time is in blocks of 15 minutes.
     *
     * @param dateTime The time to check.
     * @return true if the checkup time is in blocks of 15 minutes, false otherwise.
     */
    private static boolean isNotFifteen(LocalDateTime dateTime) {
        int minutes = dateTime.getMinute();
        return minutes % 15 == 0;
    }

    /**
     * Checks if the given date and time are not in the past.
     *
     * @param dateTime The date and time to check.
     * @return True if the date and time are not in the past; false otherwise.
     */
    private static boolean isNotInPast(LocalDateTime dateTime) {
        return !dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Gets the date and time of the checkup
     * @return The LocalDateTime representing the checkup date and time
     */
    public LocalDateTime getDateTime() {
        return this.checkupDateTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Checkup otherCheckup)) {
            return false;
        }

        return checkupDateTime.equals(otherCheckup.checkupDateTime);
    }

    /**
     * Gets the date of the checkup.
     *
     * @return The LocalDate representing the checkup date.
     */
    public LocalDate getCheckupDate() {
        return this.checkupDateTime.toLocalDate();
    }

    /**
     * Gets the time of the checkup.
     *
     * @return The LocalTime representing the checkup time.
     */
    public LocalTime getCheckupTime() {
        return this.checkupDateTime.toLocalTime();
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkupDateTime);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return checkupDateTime.format(formatter);
    }
}
