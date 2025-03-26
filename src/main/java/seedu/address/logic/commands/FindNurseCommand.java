package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Changes the remark of an existing person in the address book.
 */
public class FindNurseCommand extends FindCommand {

    public static final String COMMAND_WORD = "find nurse of patient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all patients assigned to the nurse at "
            + "the specified index and displays them as a list with index numbers.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_INVALID_PATIENT = "The person at index %d is not a patient.";
    public static final String MESSAGE_NO_NURSE_ASSIGNED = "No nurse assigned to the patient at index %d.";
    public static final String MESSAGE_NURSE_FOUND = "Nurse(s) assigned to patient %s: %s.";

    private final Index patientIndex;

    /**
     * Constructs a FindNurseCommand with the specified patient index.
     *
     * @param patientIndex The index of the patient in the filtered person list.
     */
    public FindNurseCommand(Index patientIndex) {
        super(new NameContainsKeywordsPredicate(Arrays.asList("nurse")));
        this.patientIndex = patientIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person patient = getPatientFromModel(model);
        List<String> nurseNames = getAssignedNurseNames(patient);

        if (nurseNames.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_NURSE_ASSIGNED, patientIndex.getOneBased()));
        }

        return new CommandResult(String.format(MESSAGE_NURSE_FOUND, patient.getName(), String.join(", ", nurseNames)));
    }

    /**
     * Retrieves the patient from the model based on the specified index.
     *
     * @param model The model containing the filtered person list.
     * @return The patient at the specified index.
     * @throws CommandException If the index is invalid or the person is not a patient.
     */
    private Person getPatientFromModel(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (patientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person patient = lastShownList.get(patientIndex.getZeroBased());

        if (!patient.getAppointment().toString().equalsIgnoreCase("Patient")) {
            throw new CommandException(String.format(MESSAGE_INVALID_PATIENT, patientIndex.getOneBased()));
        }

        return patient;
    }

    /**
     * Retrieves the names of nurses assigned to the specified patient.
     *
     * @param patient The patient whose assigned nurses are to be retrieved.
     * @return A list of nurse names assigned to the patient.
     */
    private List<String> getAssignedNurseNames(Person patient) {
        return patient.getTags().stream()
                .filter(tag -> tag.tagName.startsWith("Nurse"))
                .map(tag -> tag.tagName.substring(5))
                .collect(Collectors.toList());
    }
}
