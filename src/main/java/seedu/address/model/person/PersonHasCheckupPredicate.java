package seedu.address.model.person;

import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s has a checkup scheduled.
 */
public class PersonHasCheckupPredicate implements Predicate<Person> {
    @Override
    public boolean test(Person person) {
        return person.hasCheckup();
    }
}
