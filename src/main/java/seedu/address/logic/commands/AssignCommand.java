package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Assigns a nurse to a patient.
 */
public class AssignCommand extends Command {

    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns a nurse to a patient. "
            + "Parameters: PATIENT_INDEX NURSE_INDEX\n"
            + "Example: " + COMMAND_WORD + " 2 1";

    public static final String MESSAGE_SUCCESS = "Assigned nurse %s to patient %s.";

    public static final String MESSAGE_INVALID_PATIENT = "The person at index %d is not a patient.";

    public static final String MESSAGE_INVALID_NURSE = "The person at index %d is not a nurse.";

    private static final int MAX_NURSES_PER_PATIENT = 2;

    private final Index patientIndex;

    private final Index nurseIndex;

    /**
     * Creates an AssignCommand to assign a nurse to a patient.
     *
     * @param patientIndex
     * @param nurseIndex
     */
    public AssignCommand(Index patientIndex, Index nurseIndex) {
        this.patientIndex = patientIndex;
        this.nurseIndex = nurseIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (patientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (nurseIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person patient = lastShownList.get(patientIndex.getZeroBased());
        Person nurse = lastShownList.get(nurseIndex.getZeroBased());

        if (!patient.getAppointment().toString().equals("Patient")) {
            throw new CommandException(String.format(MESSAGE_INVALID_PATIENT, patientIndex.getOneBased()));
        }

        if (!nurse.getAppointment().toString().equals("Nurse")) {
            throw new CommandException(String.format(MESSAGE_INVALID_NURSE, nurseIndex.getOneBased()));
        }

        long nurseCount = patient.getTags().stream().filter(tag -> tag.tagName.startsWith("Nurse")).count();

        if (nurseCount >= MAX_NURSES_PER_PATIENT) {
            throw new CommandException("This patient already has " + MAX_NURSES_PER_PATIENT + " assigned nurses!");
        }

        Set<Tag> updatedTags = new HashSet<>(patient.getTags());
        updatedTags.add(new Tag("Nurse" + nurse.getName().fullName.replaceAll(" ", "")));

        Person updatedPatient = new Person(
                patient.getName(), patient.getPhone(), patient.getEmail(), patient.getAddress(), patient.getBloodType(),
                patient.getAppointment(), updatedTags, patient.getNextOfKin(), patient.getMedicalHistory(), patient.getCheckups());

        model.setPerson(patient, updatedPatient);
        return new CommandResult(String.format(MESSAGE_SUCCESS, nurse.getName(), patient.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AssignCommand)) {
            return false;
        }

        AssignCommand otherAssignCommand = (AssignCommand) other;

        if (!patientIndex.equals(otherAssignCommand.patientIndex)) {
            return false;
        }

        if (!nurseIndex.equals(otherAssignCommand.nurseIndex)) {
            return false;
        }

        return true;
    }
}
