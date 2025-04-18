package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.logic.commands.FindNurseCommand.MESSAGE_INVALID_PATIENT;
import static seedu.address.logic.commands.ScheduleCommand.MESSAGE_CHECKUP_CLASH;
import static seedu.address.logic.commands.ScheduleCommand.MESSAGE_CHECKUP_CREATED;
import static seedu.address.logic.commands.ScheduleCommand.MESSAGE_CHECKUP_DELETED;
import static seedu.address.logic.commands.ScheduleCommand.MESSAGE_CHECKUP_DOES_NOT_EXIST;
import static seedu.address.logic.commands.ScheduleCommand.MISSING_ASSIGNED_NURSE;
import static seedu.address.model.checkup.Checkup.MESSAGE_PAST_DATE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class ScheduleCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Person patientZero;
    private Person patientTwo;
    private ScheduleCommand command;
    private ScheduleCommand diffTimeCommad;
    private ScheduleCommand deleteCommand;
    private Index invalidIndex;
    private Index invalidPatientIndex;
    private Index patientIndexZero;
    private Index patientIndexTwo;
    private LocalDate checkupDate;
    private LocalDate pastDate;
    private LocalTime checkupTimeTen;
    private LocalTime checkupTimeOne;
    private DateTimeFormatter dateFormatter;
    private Boolean addCheckup;
    private Boolean deleteCheckup;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        patientZero = model.getFilteredPersonList().get(0);
        patientTwo = model.getFilteredPersonList().get(2);

        invalidIndex = Index.fromZeroBased(1000000);
        invalidPatientIndex = Index.fromZeroBased(1);
        patientIndexZero = Index.fromZeroBased(0);
        patientIndexTwo = Index.fromZeroBased(2);
        checkupDate = LocalDate.of(2025, 12, 24);
        pastDate = LocalDate.of(2025, 1, 1);
        checkupTimeTen = LocalTime.of(10, 0);
        checkupTimeOne = LocalTime.of(10, 15);
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        addCheckup = true;
        deleteCheckup = false;

        command = new ScheduleCommand(addCheckup, patientIndexZero, checkupDate, checkupTimeTen);
        diffTimeCommad = new ScheduleCommand(addCheckup, patientIndexTwo, checkupDate, checkupTimeOne);

        deleteCommand = new ScheduleCommand(deleteCheckup, patientIndexZero, checkupDate, checkupTimeTen);
    }

    @Test
    public void validPatientIndex_nonConflictingCheckup() {

        // checkup scheduled successfully for patient with assigned nurse
        try {
            CommandResult result = command.execute(model);
            assertEquals(String.format(MESSAGE_CHECKUP_CREATED, patientZero.getName(),
                            checkupDate.format(dateFormatter),
                            checkupTimeTen),
                    result.getFeedbackToUser());
        } catch (CommandException e) {
            fail("Execution should not throw an exception: " + e.getMessage());
        }

        // checkup scheduled successfully for patient without assigned nurse
        try {
            CommandResult result = diffTimeCommad.execute(model);
            assertEquals(String.format(MESSAGE_CHECKUP_CREATED, patientTwo.getName(),
                            checkupDate.format(dateFormatter),
                            checkupTimeOne) + "\n" + MISSING_ASSIGNED_NURSE,
                    result.getFeedbackToUser());
        } catch (CommandException e) {
            fail("Execution should not throw an exception: " + e.getMessage());
        }

        // checkup deleted successfully for patient with assigned nurse
        try {
            CommandResult result = deleteCommand.execute(model);
            assertEquals(String.format(MESSAGE_CHECKUP_DELETED, patientZero.getName(),
                            checkupDate.format(dateFormatter),
                            checkupTimeTen),
                    result.getFeedbackToUser());
        } catch (CommandException e) {
            fail("Execution should not throw an exception: " + e.getMessage());
        }

        // checkup deleted failed because checkup is not present
        CommandException exception = assertThrows(CommandException.class, () -> {
            deleteCommand.execute(model);
        });

        assertEquals(String.format(MESSAGE_CHECKUP_DOES_NOT_EXIST),
                exception.getMessage());

    }

    @Test
    public void validPatientIndex_conflictingCheckup() {
        try {
            CommandResult result = command.execute(model);
            assertEquals(String.format(MESSAGE_CHECKUP_CREATED, patientZero.getName(),
                            checkupDate.format(dateFormatter),
                            checkupTimeTen),
                    result.getFeedbackToUser());
        } catch (CommandException e) {
            fail("Execution should not throw an exception: " + e.getMessage());
        }

        CommandException exception = assertThrows(CommandException.class, () -> {
            command.execute(model);
        });
        assertEquals("A checkup is already scheduled at this datetime.", exception.getMessage());
    }

    @Test
    public void validPatientIndex_hasCheckupClash() {
        try {
            CommandResult result = command.execute(model);
            assertEquals(String.format(MESSAGE_CHECKUP_CREATED, patientZero.getName(),
                            checkupDate.format(dateFormatter),
                            checkupTimeTen),
                    result.getFeedbackToUser());
        } catch (CommandException e) {
            fail("Execution should not throw an exception: " + e.getMessage());
        }
        Command clashCommand = new ScheduleCommand(addCheckup,
                patientIndexZero,
                LocalDate.of(2025, 12, 24),
                LocalTime.of(10, 15));
        CommandException exception = assertThrows(CommandException.class, () -> {
            clashCommand.execute(model);
        });
        assertEquals(String.format(MESSAGE_CHECKUP_CLASH, "24/12/2025 10:00"), exception.getMessage());
    }

    @Test
    public void validPatientIndex_pastDateTime() {
        Command pastDateCommand = new ScheduleCommand(addCheckup, patientIndexZero, pastDate, checkupTimeTen);
        CommandException exception = assertThrows(CommandException.class, () -> {
            pastDateCommand.execute(model);
        });
        assertEquals(MESSAGE_PAST_DATE, exception.getMessage());

    }

    @Test
    public void invalidPatientIndex() {
        Command invalidPatientCommand = new ScheduleCommand(addCheckup, invalidPatientIndex, checkupDate,
                checkupTimeTen);
        CommandException exception = assertThrows(CommandException.class, () -> {
            invalidPatientCommand.execute(model);
        });
        assertEquals(String.format(MESSAGE_INVALID_PATIENT, invalidPatientIndex.getOneBased()),
                exception.getMessage());
    }

    @Test
    public void invalidIndex() {
        Command invalidIndexCommand = new ScheduleCommand(addCheckup, invalidIndex, checkupDate, checkupTimeTen);
        CommandException exception = assertThrows(CommandException.class, () -> {
            invalidIndexCommand.execute(model);
        });
        String msg = String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, 7);

        assertEquals(msg,
                exception.getMessage());
    }
}
