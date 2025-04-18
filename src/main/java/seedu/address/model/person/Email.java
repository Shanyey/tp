package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    private static final String SPECIAL_CHARACTERS = "+_.-";
    public static final String MESSAGE_CONSTRAINTS = "Emails should be of the format local-part@domain.com "
           + "and adhere to the following constraints without spaces:\n"
           + "1. The local-part should only contain alphanumeric characters (min 2 characters) and these "
           + "special characters, excluding the parentheses, (" + SPECIAL_CHARACTERS + ").\n"
           + "2. The local-part must start with an alphanumeric character.\n"
           + "3. This is followed by a '@' and then a domain name. The domain name is made up of domain labels "
           + "separated by periods.\n"
           + "The domain name must (min 1 character):\n"
           + "    - end with a domain label at least 1 character long\n"
           + "    - have each domain label start and end with alphanumeric characters\n"
           + "    - have each domain label consist of alphanumeric characters, separated only by hyphens, if any.\n"
           + "4. The .com after domain name can be replaced with any other words like .org, .net, etc. (min 2 letters)";
    // alphanumeric and special characters
    // the regex format (line 27 to 34) is partly done with the assistance of chatGPT
    private static final String START = "[A-Za-z0-9]";
    private static final String LOCAL_ALLOWED = "[A-Za-z0-9_.+-]";
    private static final String DOMAIN_ALLOWED = "[A-Za-z0-9]";
    private static final String LOCAL_PART_REGEX = START + LOCAL_ALLOWED + "+(?:[+_.-]*" + LOCAL_ALLOWED + "+)*";
    private static final String DOMAIN_PART_REGEX = DOMAIN_ALLOWED + "(?:[A-Za-z0-9-]*"
                                                                   + DOMAIN_ALLOWED + "*" + DOMAIN_ALLOWED + ")?";
    private static final String TLD_REGEX = DOMAIN_ALLOWED + "{2,}";
    private static final String DOMAIN_REGEX = DOMAIN_PART_REGEX + "(?:\\." + DOMAIN_PART_REGEX + ")*\\." + TLD_REGEX;
    public static final String FULL_EMAIL_VALIDATION_REGEX = "^" + LOCAL_PART_REGEX + "@" + DOMAIN_REGEX + "$";

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        if (email.isEmpty() || email.equals("nil")) {
            value = "nil";
        } else {
            checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
            value = email;
        }
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        return test.matches(FULL_EMAIL_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
