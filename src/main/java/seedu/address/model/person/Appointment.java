package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAppointment(String)}
 */
public class Appointment {
    public static final String MESSAGE_CONSTRAINTS =
            "Appointment should only be Nurse or Patient";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^(nurse|patient)$";

    public final String appointment;

    /**
     * Constructs a {@code Appointment}.
     *
     * @param appointment A valid appointment.
     */
    public Appointment(String appointment) {
        requireNonNull(appointment);
        checkArgument(isValidAppointment(appointment), MESSAGE_CONSTRAINTS);
        this.appointment = appointment.substring(0, 1).toUpperCase() + appointment.substring(1).toLowerCase();
    }

    public static boolean isValidAppointment(String test) {
        return test.toLowerCase().matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the appointment is nurse and false otherwise.
     */
    public boolean isNurse() {
        return this.toString().equalsIgnoreCase("nurse");
    }

    @Override
    public String toString() {
        return appointment;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppointment = (Appointment) other;
        return appointment.equals(otherAppointment.appointment);
    }

    @Override
    public int hashCode() {
        return appointment.hashCode();
    }
}
