package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindNurseCommand;
import seedu.address.logic.commands.FindPatientCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim().toLowerCase();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (trimmedArgs.startsWith("nurse of patient")) {
            return findNurseScenario(trimmedArgs);
        } else if (trimmedArgs.startsWith("patient of nurse")) {
            return findPatientScenario(trimmedArgs);
        }

        return findScenario(trimmedArgs);
    }

    /**
     * test
     * @param args test
     * @param commandType test
     * @return test
     * @throws ParseException test
     */
    public String[] splitArguments(String args, String commandType) throws ParseException {
        String[] splitArgs = args.split("\\s+");
        System.out.println(splitArgs.length);
        if (splitArgs.length != 4) {
            String indexType = commandType.equals("nurse of patient") ? "PATIENT_INDEX" : "NURSE_INDEX";
            throw new ParseException(String.format("Usage: find %s %s", commandType, indexType));
        }
        return splitArgs;
    }

    /**
     * test
     * @param trimmedArgs test
     * @return test
     * @throws ParseException test
     */
    public FindNurseCommand findNurseScenario(String trimmedArgs) throws ParseException {
        String[] splitArgs = splitArguments(trimmedArgs, "nurse of patient");

        Index nurseIndex = ParserUtil.parseIndex(splitArgs[3]);
        return new FindNurseCommand(nurseIndex);

    }

    /**
     * test
     * @param trimmedArgs test
     * @return test
     * @throws ParseException test
     */
    public FindPatientCommand findPatientScenario(String trimmedArgs) throws ParseException {
        String[] splitArgs = splitArguments(trimmedArgs, "patient of nurse");

        Index patientIndex = ParserUtil.parseIndex(splitArgs[3]);
        return new FindPatientCommand(patientIndex);

    }

    /**
     * test
     * @param trimmedArgs test
     * @return test
     * @throws ParseException test
     */
    public FindCommand findScenario(String trimmedArgs) throws ParseException {
        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
