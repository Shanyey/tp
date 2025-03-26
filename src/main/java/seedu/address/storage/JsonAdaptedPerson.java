package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.checkup.Checkup;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.BloodType;
import seedu.address.model.person.Email;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String bloodType;
    private final String appointment;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedCheckup> checkups = new ArrayList<>();
    private final List<JsonAdaptedMedicalHistory> medicalHistory = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                             @JsonProperty("bloodType") String bloodType,
                             @JsonProperty("appointment") String appointment,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("medicalHistory") List<JsonAdaptedMedicalHistory> medicalHistory,
                             @JsonProperty("checkups") List<JsonAdaptedCheckup> checkups) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.bloodType = bloodType;
        this.appointment = appointment;

        if (tags != null) {
            this.tags.addAll(tags);
        }

        if (checkups != null) {
            this.checkups.addAll(checkups);
            if (medicalHistory != null) {
                this.medicalHistory.addAll(medicalHistory);
            }
        }

    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        bloodType = source.getBloodType().bloodType;
        appointment = source.getAppointment().appointment;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        checkups.addAll(source.getCheckups().stream()
                .map(JsonAdaptedCheckup::new)
                .collect(Collectors.toList()));
        medicalHistory.addAll(source.getMedicalHistory().stream().map(JsonAdaptedMedicalHistory::new)
                                                                 .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        final List<Checkup> personCheckups = new ArrayList<>();
        final List<MedicalHistory> personMedicalHistory = new ArrayList<>();

        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        for (JsonAdaptedCheckup checkup : checkups) {
            personCheckups.add(checkup.toModelType());
            for (JsonAdaptedMedicalHistory medicalHistory : medicalHistory) {
                personMedicalHistory.add(medicalHistory.toModelType());
            }
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (bloodType == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    BloodType.class.getSimpleName()));
        }
        if (!BloodType.isValidBloodType(bloodType)) {
            throw new IllegalValueException(BloodType.MESSAGE_CONSTRAINTS);
        }
        final BloodType modelBloodType = new BloodType(bloodType);

        if (appointment == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Appointment.class.getSimpleName()));
        }
        if (!Appointment.isValidAppointment(appointment)) {
            throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
        }
        final Appointment modelAppointment = new Appointment(appointment);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Set<Checkup> modelCheckups = new HashSet<>(personCheckups);
        final Set<MedicalHistory> modelMedicalHistory = new HashSet<>(personMedicalHistory);
        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelBloodType, modelAppointment, modelTags,
                          modelMedicalHistory, modelCheckups);
    }
}
