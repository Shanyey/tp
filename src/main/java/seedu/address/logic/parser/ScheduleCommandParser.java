package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.ScheduleCommand.MESSAGE_USAGE;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a ScheduleCommand.
 */
public class ScheduleCommandParser implements Parser<ScheduleCommand> {
    private static final String DATE_VALIDATION_REGEX = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
    private static final String TIME_VALIDATION_REGEX = "^([01][0-9]|2[0-3])[0-5][0-9]$";

    /**
     * Parses the given {@code String} representation of arguments
     * and returns a ScheduleCommand object for execution.
     *
     * @param args The user input args to be parsed.
     * @return A ScheduleCommand based on the parsed arguments.
     * @throws ParseException If the input does not conform to expected formats.
     */
    public ScheduleCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim().toLowerCase();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE));
        }

        Index patientIndex = getPatientIndex(trimmedArgs);
        LocalDate checkupDate = getCheckupDate(trimmedArgs);
        LocalTime checkupTime = getCheckupTime(trimmedArgs);
        return new ScheduleCommand(patientIndex, checkupDate, checkupTime);
    }

    /**
     * Retrieves and parses the patient index from the input arguments.
     *
     * @param trimmedArgs The input arguments from which to retrieve the index.
     * @return The Patient Index extracted from the arguments.
     * @throws ParseException If the index is not valid.
     */
    public Index getPatientIndex(String trimmedArgs) throws ParseException {
        String[] parsedArgument = parseArguments(trimmedArgs);
        return ParserUtil.parseIndex(parsedArgument[0]);
    }

    /**
     * Retrieves and parses the checkup date from the input arguments.
     *
     * @param trimmedArgs The input arguments from which to retrieve the date.
     * @return The LocalDate object representing the checkup date.
     * @throws ParseException If the date is not in the expected format or is invalid.
     */
    public LocalDate getCheckupDate(String trimmedArgs) throws ParseException {
        String[] parsedArgument = parseArguments(trimmedArgs);
        String dateString = parsedArgument[1];

        if (!dateString.matches(DATE_VALIDATION_REGEX)) {
            throw new ParseException("Invalid date format. Use dd/MM/yyyy");
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid date values");
        }
    }

    /**
     * Retrieves and parses the checkup time from the input arguments.
     *
     * @param trimmedArgs The input arguments from which to retrieve the time.
     * @return The LocalTime object representing the checkup time.
     * @throws ParseException If the time is not in the expected format or is invalid.
     */
    public LocalTime getCheckupTime(String trimmedArgs) throws ParseException {
        String[] parsedArgument = parseArguments(trimmedArgs);
        String timeString = parsedArgument[2];
        if (!timeString.matches(TIME_VALIDATION_REGEX)) {
            throw new ParseException("Invalid date format. Use dd/MM/yyyy");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
            return LocalTime.parse(timeString, formatter);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid time values");
        }
    }

    /**
     * Parses the input arguments to ensure the correct number of parameters are provided.
     *
     * @param trimmedArgs The input arguments to be split.
     * @return An array of arguments extracted from the input string.
     * @throws ParseException If the number of arguments is incorrect.
     */
    public String[] parseArguments(String trimmedArgs) throws ParseException {
        String[] parsedArguments = trimmedArgs.split("\\s+");
        if (parsedArguments.length != 3) {
            throw new ParseException(MESSAGE_USAGE);
        }
        return parsedArguments;
    }
}
