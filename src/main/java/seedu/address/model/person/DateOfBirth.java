package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

/**
 * Represents a person's date of birth.
 */
public class DateOfBirth {
    public static final String MESSAGE_CONSTRAINTS = "Date Of Birth can only take the format:"
            + "DD/MM/YYYY, and must be an existing date";
    public static final String FUTURE_DOB = "Date Of Birth cannot be in the future!";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Logger logger = LogsCenter.getLogger(DateOfBirth.class);
    private static final String DATE_VALIDATION_REGEX = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";


    public final LocalDate dob;

    /**
     * Constructs a DateOfBirth object from a valid date string.
     *
     * @param dateOfBirth The date of birth in the format "dd-MM-yyyy".
     * @throws NullPointerException if dateOfBirth is null.
     * @throws IllegalArgumentException if dateOfBirth does not conform to the required format.
     */
    public DateOfBirth(String dateOfBirth) {
        requireNonNull(dateOfBirth);
        checkArgument(isValidDate(dateOfBirth), MESSAGE_CONSTRAINTS);
        checkArgument(isNotFutureDate(dateOfBirth), FUTURE_DOB);
        this.dob = parseDate(dateOfBirth);
    }


    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidDate(String dateString) {
        try {
            String[] dateParts = dateString.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            if (day < 1 || day > LocalDate.of(year, month, 1).lengthOfMonth()) {
                return false;
            }
            logger.info("Date of birth string = " + dateString);
            LocalDate doob = LocalDate.parse(dateString, FORMATTER);
            logger.info("dob = " + doob.format(FORMATTER));
            return true;
        } catch (Exception e) {
            logger.warning(e.getMessage());
            return false;
        }
    }

    /**
     * Returns true if a given string is not a date in the future
     *
     * @param dateString The date of birth
     * @return True if date is not in the future, False if date is in the future.
     */
    public static boolean isNotFutureDate(String dateString) {
        LocalDate doob = LocalDate.parse(dateString, FORMATTER);
        return !doob.isAfter(LocalDate.now());
    }

    private LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, FORMATTER);
    }

    @Override
    public String toString() {
        return this.dob.format(FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DateOfBirth otherDateOfBirth)) {
            return false;
        }

        return this.dob.equals(otherDateOfBirth.dob);
    }

    @Override
    public int hashCode() {
        return this.dob.hashCode();
    }
}
