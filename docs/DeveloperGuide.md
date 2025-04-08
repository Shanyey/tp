---
layout: page
title: Developer Guide
---
## MediBook Developer's Guide

## Table of Contents

1. [Acknowledgements](#acknowledgements)
1. [Setting up, getting started](#setting-up-getting-started)
1. [Design](#design)
   * [Architecture](#architecture)
   * [UI Component](#ui-component)
   * [Logic Component](#logic-component)
   * [Model Component](#model-component)
   * [Storage Component](#storage-component)
1. [Implementation](#implementation)
   * [Add](#add-feature)
   * [Edit](#edit-feature)
   * [List](#list-feature)
   * [Find](#find-feature)
   * [Assign](#assign-feature)
   * [Schedule](#schedule-feature)
1. [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)
1. [Appendix: Requirements](#appendix-requirements)
   * [Product Scope](#product-scope)
   * [User Stories](#user-stories)
   * [Use Cases](#use-cases)
   * [Non-Functional Requirements](#non-functional-requirements)
   * [Glossary](#glossary)
1. [Appendix: Instructions for manual testing](#appendix-instructions-for-manual-testing)
   * [Launch and Shutdown](#launch-and-shutdown)
   * [Deleting a Person](#deleting-a-person)
   * [Editing a Person](#editing-a-person)
   * [Listing Persons](#listing-persons)
   * [Finding Persons](#finding-persons)
   * [Assigning a nurse to a patient](#assigning-a-nurse-to-a-patient)
   * [Removing nurse assignment from a patient](#removing-nurse-assignment-from-a-patient)
   * [Schedule Checkups](#schedule-checkups)
   * [View nurses / patients](#viewing-nurses--patients)
   * [Saving Data](#saving-data)
1. [Appendix: Planned Enhancements](#appendix-planned-enhancements)
1. [Appendix: Effort](#appendix-effort)

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* [AB3](https://github.com/nus-cs2103-AY2425S2/tp) for being the base we build our project on.
* [JavaFX](https://openjfx.io/) for creating the Graphic User Interface of MediBook.
* [JUnit5](https://github.com/junit-team/junit5) for testing capability.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<img src="images/ArchitectureDiagram.png" width="280"/>

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below represents a generic model for command execution within the Logic component, specifically illustrating the interaction for the execute("...") API call. This model serves as a foundational framework that can be used for various command types, demonstrating how the LogicManager invokes the execute method.
![Interactions Inside the Logic Component for the `...` Command](images/GenericDiagram.png)

**Note:** The lifeline for `CommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)




The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<img src="images/ModelClassDiagram.png" width="550">

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add Feature

The `add` command allows the user to add a new person to the address book.

1. `LogicManager` receives the command text and passes it to `AddressBookParser`.
1. `AddressBookParser` parses the command and returns an `AddCommand` object.
1. `AddCommand#execute()` adds the person to the model and returns a `CommandResult`.

![Sequence Diagram](images/AddGenericDiagram.png)

#### Design considerations:

We chose to implement parsing with a `ParserUtil` helper class to simplify each command parser. An alternative would be using a central parser for all commands, but this was less modular.

### Edit Feature

The `edit` command allows the user to edit an existing person in the address book.

1. `LogicManager` receives the command text and passes it to `AddressBookParser`.
1. `AddressBookParser` parses the command and returns an `EditCommandParser` object.
1. `EditCommandParser#parse()` creates an `EditCommand` object.
1. `EditCommand#execute()` edits the person in the model and returns a `CommandResult`.

![Sequence Diagram](images/EditGenericDiagram.png)

#### Design considerations:

We chose to implement parsing with a `ParserUtil` helper class to simplify each command parser. An alternative would be using a central parser for all commands, but this was less modular.

### List Feature

The `list` command allows users to display a subset of people in the address book based on optional filters.

It supports the following use cases:
* `list` — Lists **all persons** in the address book (patients and nurses).
* `list nurse` or `list patient` — Lists **only nurses** or **only patients**, respectively.
* `list checkup` — Lists all patients with scheduled **checkups**, sorted by earliest checkup date.

#### Execution Flow:
1. `LogicManager` receives the command text (e.g., `"list checkup"`) and passes it to `AddressBookParser`.
1. `AddressBookParser` parses the command and returns an `ListCommandParser` object.
1. `ListCommandParser#parse()` constructs a `ListCommand` object, based on the input string.
1. `ListCommand#execute()` evaluates the internal flags:
   * If the command was `list checkup`, it calls `updateFilteredPersonListByEarliestCheckup(...)` with a `PersonHasCheckupPredicate`.
   * If no filter was provided, it lists all persons using `Model.PREDICATE_SHOW_ALL_PERSONS`.
   * If a specific appointment filter was provided (e.g., `"nurse"`), it filters with `PersonHasAppointmentPredicate`.
1. A `CommandResult` is returned with a success message indicating what was listed.

![Sequence Diagram](images/ListGenericDiagram.png)

#### Design considerations:

We chose to centralize filtering logic inside `ListCommand`, separating parsing (`ListCommandParser`) from behavior. This approach improves maintainability and makes it easy to extend filtering options (e.g., by tag or medical history) in the future.

### Find Feature

The `find` command enables users to search for specific entities in the address book, including:
* Nurses assigned to the patients.
* Patients associated with the nurses.
* Users whose names contain the specified search terms.

This functionality improves user experience by allowing quick access to relevant information.

#### Execution Flow:
1. `LogicManager` receives the command text (e.g. `find nurse of patient 8`) from the user and passes it to `AddressBookParser`.
1. `AddressBookParser` parses the command and returns a `FindCommandParser` object.
1. Depending on the arguments, `FindCommandParser#parse()` will return one of the following:
   * `FindNurseCommand`: for searching nurses assigned to a specific patient.
   * `FindPatientCommand`: for searching patients assigned to a specific nurse.
   * `FindCommand`: a general command for searching based on keywords in contacts' names.
1. `FindCommand#execute()` retrieves the relevant entries from the model and returns a `CommandResult`.
1. For `FindNurseCommand`, it finds and returns all nurses assigned to the specified patient.
1. For `FindPatientCommand`, it finds and returns all patients assigned to the specified nurse.
1. For `FindCommand`, it allows the user to search by keywords. For example, executing `find tom harry` will return all users that contain either "tom" or "harry" in their names.

Using this command, users can effortlessly navigate and manage their address book, finding relevant information quickly and efficiently.

![Sequence Diagram](images/FindGenericDiagram.png)

#### Design considerations:

We chose to implement parsing with a `ParserUtil` helper class to simplify each command parser. An alternative would be using a central parser for all commands, but this was less modular.

### Assign Feature

The `assign` command allows the user to assign a nurse to a patient.

#### Execution Flow:

1. `LogicManager` receives the command text and passes it to `AddressBookParser`.
1. `AddressBookParser` parses the command and returns an `AssignCommand` object.
1. `AssignCommand#execute()` assigns the nurse to the patient and returns a `CommandResult`.

![Sequence Diagram](images/AssignGenericDiagram.png)

#### Design considerations:

We chose to implement parsing with a `ParserUtil` helper class to simplify each command parser. An alternative would be using a central parser for all commands, but this was less modular.

### Schedule Feature

The `schedule` command allows the user to create a checkup between a patient and a nurse.

#### Execution Flow:

1. `LogicManager` receives the command text and passes it to `AddressBookParser`.
1. `AddressBookParser` parses the command and returns an `ScheduleCommand` object.
1. `ScheduleCommand#execute()` creates or deletes the checkup from the patient and returns a `CommandResult`.

![Sequence Diagram](images/ScheduleGenericDiagram.png)

#### Design considerations:

We chose to implement parsing with a `ParserUtil` helper class to simplify each command parser. An alternative would be using a central parser for all commands, but this was less modular.




--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Manager or nurse at a private nurse agency
* has a need to manage a significant number of nurses and/or patients
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:
1. Manage nurse and patients faster than a typical mouse/GUI driven app
1. Allows faster creation and storage of details compared to traditional pen and paper methods
1. Enables easy transfer and tracking of patients compared to current system where it is inefficient to do so
1. Saves time from having to log into centralised system from healthcare system in Singapore each time data is needed.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                       | I want to …​                                                                         | So that I can…​                                                                                 |
|----------|-------------------------------|--------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| `* * *`  | Manager                       | add nurse contacts                                                                   | add new nurses contacts who joined the team                                                     |
| `* * *`  | Manager                       | delete nurse contacts                                                                | remove contact of nurses who leave the agency                                                   |
| `* * *`  | Manager                       | add patients contacts                                                                | keep track of new patients who register with the agency                                         |
| `* * *`  | Manager                       | delete patient contacts                                                              | remove patients who are no longer registered with the agency                                    |
| `* * *`  | Manager                       | view all nurses                                                                      | see all nurses details at once                                                                  |
| `* * *`  | Manager                       | view all patients                                                                    | see all registered patients at once                                                             |
| `* * *`  | Nurse                         | view patients details                                                                | view the needs of the patient I'm caring for                                                    |
| `* * *`  | Nurse                         | exit the application quickly                                                         | resume other tasking at hands                                                                   |
| `* *`    | Manager                       | view all patients attached to a certain nurse                                        | check which patients a nurse is currently assigned to                                           |
| `* *`    | Manager                       | view the nurse assigned to a patient                                                 | check who is in charge of a certain patient                                                     |
| `* *`    | Manager                       | schedule appointments for a patient                                                  | ensure the patient has an appointment and a nurse                                               |
| `* *`    | Manager                       | assign a nurse to a patient                                                          | ensure the patient has a specified nurse                                                        |
| `* *`    | Manager                       | sort patient details                                                                 | sort my patients according to various criteria such as blood type and severity level            |
| `* *`    | Manager                       | assign categories to patients                                                        | add the severity of each patient                                                                |
| `* *`    | Manager                       | adjust categories of patients                                                        | lower or increase the severity / priority of patients over time                                 |
| `* *`    | Nurse                         | find patient details                                                                 | check details about a specific nurse                                                            |
| `* *`    | Nurse                         | sort patient details                                                                 | quickly find details about a specific patient                                                   |
| `* *`    | Nurse                         | transfer the patients under me to another nurse                                      | ensure my patients are not neglected during my absence                                          |
| `*`      | Manager                       | add roles of nurses                                                                  | see which nurse has a larger responsibility                                                     |
| `*`      | Forgetful Nurse               | schedule automatic reminders for task like checkups and medications times            | task are always done on time                                                                    |
| `*`      | Nurse during a midnight shift | activate night mode interface with darker colours and larger text to enhance visuals | reduce eye strain while ensuring accuracy when recording patient data in dimly lit environments |
| `*`      | Manager                       | log in using my staff credential                                                     | Securely access patient records                                                                 |

### Use cases

(For all use cases below, the **System** is the `MediBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case 1: Delete a nurse / patient**

**MSS**

1.  User requests to list nurses / patients
1.  AddressBook shows the list of nurses / patients
1.  User requests to delete a specific nurse / patient in the list
1.  AddressBook deletes the nurse / patient

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case 2: Add a nurse / patient**

**MSS**

1.  User requests to list nurses / patients
1.  AddressBook shows the list of nurses / patients
1.  User requests to add a nurse / patient in the list
1.  AddressBook adds the nurse / patient

    Use case ends.

**Extensions**

* 2a. The list is empty.

    Use case ends.

* 3a. The user enters incorrect command format.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case 3: Edit a nurse / patient**

**MSS**

1. User requests to list nurses / patients
1. AddressBook shows the list of nurses / patients
1. User requests to edit a nurse's / patient's details
1. AddressBook edits the nurse's / patient's details

    Use case ends.

**Extensions**

* 2a. The list is empty.

    Use case ends.

* 3a. The user enters incorrect command format.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case 4: Exit the app**

**MSS**

1. User requests to exit app
1. AddressBook closes

    Use case ends.

**Extensions**

* 1a. The user enters incorrect command format.
    * 1a1. AddressBook shows an error message.

      Use case resumes at step 1.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
1.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
1.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

### Glossary

* **Patient Contact**: Refers to the information stored about a patient in the system (e.g: Name, Phone number, Email, Address, Appointment, Blood Type, next-of-kin)
* **Appointment**: The role of the person
* **Manager**: Manages the nurses
* **Nurse**: Tends to the patients
* **Checkup**: A scheduled appointment for nurse to visit and treat the patient.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar MediBook.jar` command to run the application.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by going into the terminal, `cd` into the folder you put the jar file in, and use the `java -jar MediBook.jar`.<br>
       Expected: The most recent window size and location is retained.

1. Shutdown
   1. Type exit into the app CLI<br>
      Expected: The MediBook application closes.

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Adding a person
1. Adding a person while all persons are being shown
   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.
   1. Test case: `add n/John Doe dob/01/01/2001 p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 b/AB+ ap/Patient nok/Jane 91234567 t/newcomer mh/Diabetes mh/High Blood Pressure` <br>
   Expected: A new contact is created and the displayed person list is updated.
1. Adding a person using only compulsory fields
   1. Test case: `add n/John Sim dob/01/01/2025 p/98765432 a/123 Block 7 b/AB+ ap/patient` <br>
    Expected: Creates a new patient contact with the minimum fields included.
1. Adding a duplicate person
   1. Prerequisites: A person by the name of John Sim has been created either through the previous test case or by manual testing.
   1. Test case: `add n/John Sim dob/01/01/2025 p/98765432 a/123 Block 7 b/AB+ ap/patient` <br>
   Expected: No person is created. Error message shows "This person already exists in the address book"
1. Other incorrect commands to try:
   1. Invalid names: names containing non-alphabetical symbols
   1. Invalid number: Less than 3 digits or non integer inputs
   1. Invalid Date of Birth: Non integer and non slash inputs, incorrect date format (DD/MM/YYYY)
   1. Invalid Blood type: Not matching any of the 8 specified blood types.
   1. Invalid Appointment: Not matching patient or nurse, non-alphabetical inputs

### Editing a Person
1. Editing any field of a person currently being displayed
    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.
    1. Test case: `edit 2 n/Samantha`<br>
       Expected: Changed the name of the person at index 2 of the displayed list to Samantha.
    1. Testing other fields that can be edited:
       1. Editing tags
          1. Test case: `edit 1 t/discharge t/No family` <br>
          Expected: Removes all tags of the person at index 1 and creates 2 tags for that person.
       1. Medical History
          * Use `View` command to check medical history
          1. Test case: `edit 1 mh/Diabetes`<br>
             Expected: Removes the medical history of the patient at index 1 and creates a new medical history containing diabetes. If the person is a nurse, an error will occur as medical history should not be added to a nurse.
          1. Test case: `edit 1 mh/`<br>
             Expected: Clears the medical history of the person.
       1. Appointment
          1. **NOTE** Patients contacts can only be converted to Nurse appointment if it does not contain any medical history
          1. Test case: `edit 2 ap/nurse`
          1. Expected: Changes the patients appointment to a nurse if there is no medical history. Returns an error if the patient does have medical history.
       1. Other tags to try: name, phone_number, blood_type, address, next_of_kin and email.

### Listing persons
1. Listing all people, people based on appointments (nurse or patient), or based on checkups scheduled
   1. Prerequisites: Multiple persons have been created and can be displayed.
   1. Test case: `list`<br>
      Expected: Displays all contacts (nurses and patients).
   1. Test case: `list patient`<br>
      Expected: Displays all patients.
   1. Test case: `list nurse`<br>
      Expected: Displays all nurses.
   1. Test case: `list checkup`<br>
      Expected: Displays all patients with checkups scheduled, sorted from earliest to latest.

### Finding persons
1. Finding people by name or part of name
   1. Prerequisites: List all persons using any `list` command. Multiple persons in the list.
   1. Test case: `find John`<br>
      Expected: Displays contacts whose names contain `John` in any part of their name.
   1. Test case: `find hn`<br>
      Expected: Displays contacts whose names contain `hn` in any part of their name. E.g. `John` will be displayed.
   2. Test case: `find hn ce`<br>
      Expected: Displays contacts whose name contain `hn` or `ce` in any part of their name. E.g. `John` and Alice will be displayed.
1. Finding nurse(s) assigned to a patient
   1. Prerequisites: List persons using any `list` command. Multiple persons in the list. At least 1 patient has a nurse assigned to them.
   1. Test case: `find nurse of patient 2`<br>
      Expected: Displays nurse(s) assigned to patient at index 2 in the result box.
1. Finding patient(s) who have a specified nurse assigned to them
   1. Prerequisites: List persons using any `list` command. Multiple persons in the list. At least 1 patient has a nurse assigned to them.
   1. Test case: `find patient of nurse 2`<br>
      Expected: Displays patients(s) assigned to nurse at index 2 in the result box.

### Assigning a nurse to a patient
1. Assigning a nurse to a patient by their index numbers
   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list where at least 1 nurse and at least 1 patient person exists.
   2. Test case: `assign 6 2`<br>
      Expected: Patient at index 6 is assigned to nurse at index 2.
   3. Test case: `assign`<br>
      Expected: Shows an invalid command format error message with usage instructions.
   4. Other incorrect commands to try:
      1. Missing an argument: no `NURSE_INDEX` specified
      2. Invalid index: using non-numeric characters for index values
      3. Assigning a third nurse to a patient that has already been assigned 2 nurses.

### Removing nurse assignment from a patient
1. Removing the assignment of a nurse from a patient
   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list. Patients with assigned nurses exist.
   2. Test case: `assign delete John Doe 6`<br>
      Expected: Removes the assigned nurse with name `John Doe` from the patient at index 6.
   3. Test case: `assign delete`<br>
      Expected: Shows an invalid command format error message with usage instructions.
   4. Other incorrect commands to try:
      1. Missing an argument: no `NURSE_NAME` or `PATIENT_INDEX` specified
      2. Invalid index: using non-numeric characters for the index value

### Schedule checkups
1. Adding a checkup to a patient
   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list. At least 1 person with appointment of `Patient`.
   1. Test case 1: `schedule add for patient 6 12/12/2025 1200`<br>
      Expected: Creates a checkup for the patient at index 1 at the given time.
   1. Test case 2: `schedule add for patient 6 01/01/2026 1105`<br>
      Expected: Error message showing "Please use a time in blocks of 00, 15, 30, or 45 minutes (e.g., 1000, 1015, 1030, 1045)."
   1. Test case: `schedule add for patient 6 12/12/2025 1200`
      Expected: Error message saying "A checkup is already scheduled at this datetime." (if you executed test case 1 resulting in a duplicate checkup).
   1. Test case: `schedule add for patient 6 12/12/2025 1205`<br>
      Expected: Error message saying "There's a checkup scheduled on 12/12/2025 12:00! Please choose another time / date" (if you have executed test case 1 resulting in a checkup at 1200 on the same day).
   1. Invalid inputs to test out
      1. Date or time missing e.g. `schedule add for patient 6`
      1. Person at given index is not a patient
      1. Missing syntax e.g. `schedule 6 12/12/2025 1205`
1. Removing a checkup from a patient
   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list. At least 1 person with appointment of `Patient` with at least 1 scheduled checkup.
   1. Test case: `schedule delete for patient 6 12/12/2025 1200`<br>
      Expected: Checkup at given date time will be removed from the patient at index 6.
   1. Test case: `schedule delete for patient 6 12/12/2025 1700`<br>
      Expected: If there is no checkup created at that time yet, an error message saying that there is no such checkup will be shown. Else, same as test case above.

### Viewing nurses / patients
1. Viewing nurse or patient details
   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.
   2. Test case: `view INDEX`<br>
      Expected: Displays details for the contact at `INDEX`. If this contact is a patient and has medical history details, the medical history will be listed.
   3. Test case: `view`<br>
      Expected: Shows an invalid command format error message with usage instructions.
   4. Other incorrect commands to try:
      1. Missing index value
      2. Invalid index: using non-numeric characters for the index value
      3. Too many arguments: entering multiple index values

### Saving data

1. Dealing with missing/corrupted data files

   1. Simulate a corrupted file by editing the saved .json file such that it is no longer in json format. This should result in a empty screen upon start up.
   1. Delete the file and restart the app to recover and start with a small list of sample contacts.

## **Appendix: Planned Enhancements**

These are some features / improvements our team has planned to implement in the future due to lack of time.
1. Patient medical history can be edited and added on to existing medical history.
   * Currently, editing patient medical history will remove the existing medical history, meaning that if the user wants to keep the existing medical history, they would need to re-enter everything again.
   * For future enhancements, we plan to allow users to add on to the existing medical history when they edit a patient's medical history.
   * There will also be a separate command that will allow users to delete specific medical history entries for a patient.
2. The `assign` command will have a command format that is more intuitive and be easier to remember for users.
   * Currently, the `assign` command has the format: `assign PATIENT_INDEX NURSE_INDEX`, which is slightly hard to understand and is not the most intuitive.
   * For future enhancements, we plan to modify to command format to something similar to the following: `assign nurse NURSE_INDEX to patient PATIENT_INDEX`.
3. More nurses can be assigned to one patient.
   * Currently, the maximum number of nurses that can be assigned to a patient is 2.
   * For future enhancements, we may increase the number of nurses that can be assigned to a patient, as this would be more realistic for cases where multiple nurses would be needed to attend to one patient.
4. Leap year DOB / February DOB will be handled correctly.
   * Currently, DOB for leap years is not handled correctly, and dates such as 30/02/2000 are allowed in the DOB field.
   * For future enhancements, such dates will be handled correctly to only allow logical date inputs.
5. Corrupted JSON file will be handled gracefully.
   * Currently, a blank page will be displayed if the JSON file is corrupted.
   * For future enhancements, we may display a warning message to the user, and instructions would be given for reloading the app or retrieving sample data.
6. Reminders can be added for checkups.
   * Currently, checkups can be scheduled for a patient, but nurses will not receive a reminder about the checkups for the patients that they are assigned to.
   * For future enhancements, a reminder feature would allow nurses to receive reminders in advance for the patient checkups that they are assigned to.
7. Warning messages will be displayed when scheduling checkups for patients who do not have any assigned nurses.
   * Currently, users are allowed to schedule checkups for patients even if the patient does not have any nurses assigned to them.
   * For future enhancements, a warning will be displayed to alert the user that the patient they are scheduling a checkup for does not have any assigned nurses.
8. Nearly similar person `edit` handling is not consistent due to the search functionality.
   * A nearly identical person refers to a person that has all of the same person attributes as another person, except
   for any one of these attributes: `name`, `phone number` and `date of birth`.
   * Currently, the `edit` command **may** not work if it is applied to a nearly similar person, that is not the first person in line among the nearly identical person, shown in the current list. 
   * `edit` will tend to edit the first person in line among the nearly identical persons, instead of the one that is
   intended to be edited (i.e. the nearly identical person not first in line).

## **Appendix: Effort**

### Difficulty Level
Our project was highly complex as it expanded on the AddressBook 3 (AB3) baseline . We had to manage multiple new entity types such as patients, nurses, appointments, checkups and medical history compared to AB3 which only manages 1 person entity. Our project required careful coordination and encapsulation of all related data attributes.

### Challenges Faced
1. Multi-entity Integration
   * We introduced distinct roles like patient and nurse via an appointment field and had to integrate these new fields into the system. E.g. adjusting both add and edit functions to allow the changes to these fields.
   * We then had to design features specific to these such as nurse assignment and checkup scheduling.
1. Checkup Scheduling
   * Implementing checkup scheduling requires the introduction of a Checkup entity and validations like conflicting checkups.
1. Optional and Validated Fields
   * We added some optional fields like NextOfKin, which necessitated custom validation logic while ensuring the rest of the systems remained robust.
1. Command Complexity
   * We had to design new commands like assign and schedule, as well as enhance the current existing commands in AB3 (add, edit, list, etc.). These had to handle entity-specific behavior such as enforcing rules and validations.

### Effort Required
Our project required effort in these 4 main aspects:
1. Design and Refactoring
   * Significant refactoring was done to support distinct entity behavior while keeping the core model extensible.

1. Command System
   * Multiple new commands and parser classes were developed to enable features like nurse-patient assignment and checkup scheduling.

1. Validation and Edge Cases 
   * Custom checks were built into the logic and parser layers to prevent invalid operations (e.g., assigning a non-nurse or scheduling duplicate checkups).

1. Testing and Debugging
   * Each new command and behavior introduced unique test cases. We implemented extensive unit and integration tests to ensure system reliability.

### Achievements
Despite the complexity and effort required, our final project offers a user-friendly and extensible system. Our main achievements in this project were: implementing scheduling of medical checkups with conflict management implemented, list command with important filters such as checkup existence, allowing more optional fields like next of kin and email to provide more flexibility to the user, and last but not least adjusting requirements of duplicate persons to better match real world situations.

