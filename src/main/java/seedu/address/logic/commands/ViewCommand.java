package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Person;

/**
 * View nurse or patient details.
 * Any available medical history will be displayed for a patient.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views the nurse or patient at specified index. "
            + "If the person at the index is a patient and has medical history, the history will be shown.\n"
            + "Parameters: INDEX\n"
            + "Example: " + COMMAND_WORD + " 2";

    public static final String MESSAGE_SUCCESS = "Displaying details for: %s.";
    public static final String MESSAGE_MEDICAL_HISTORY = "Medical History for %s: %s";

    private static Predicate<Person> lastShownListPredicate;

    private final Index index;

    /**
     * Creates a ViewCommand to display the details of the nurse or patient at the specified index.
     *
     * @param index Index of the person to view.
     */
    public ViewCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                    lastShownList.size()));
        }

        Person viewedPerson = lastShownList.get(index.getZeroBased());
        int zeroBasedIndex = index.getZeroBased();

        model.updateFilteredPersonList(person -> model.getFilteredPersonList().indexOf(person) == index.getZeroBased());

        String responseMessage = String.format(MESSAGE_SUCCESS, viewedPerson.getName());
        if (viewedPerson.getAppointment().toString().equals("Patient")) {
            String medicalHistory = viewedPerson.getMedicalHistory().isEmpty()
                    ? "No medical history available."
                    : viewedPerson.getMedicalHistory().stream()
                    .map(MedicalHistory::toString)
                    .map(history -> "\n- " + history.replace("[", "").replace("]", ""))
                    .collect(Collectors.joining(""));
            String medicalHistoryMessage = String.format(MESSAGE_MEDICAL_HISTORY, viewedPerson.getName(),
                    medicalHistory);
            responseMessage += "\n\n" + medicalHistoryMessage;
        }
        ListCommand.clearCheckupFilter();
        ListCommand.clearAppointmentFilter();
        FindCommand.clearLastFindPredicate();
        return new CommandResult(responseMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ViewCommand)) {
            return false;
        }

        ViewCommand otherViewCommand = (ViewCommand) other;

        if (!index.equals(otherViewCommand.index)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the predicate used to filter the last shown list.
     *
     * @return Predicate for the last shown list.
     */
    public static Predicate<Person> getLastShownListPredicate() {
        return lastShownListPredicate;
    }

    /** Clears the last shown list predicate. */
    public static void clearLastShownListPredicate() {
        lastShownListPredicate = null;
    }
}
