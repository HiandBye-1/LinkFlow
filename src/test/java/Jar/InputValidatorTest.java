
package Jar;

import Jar.InputValidator.InputValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    private final InputValidator validator = new InputValidator();

    // Username tests

    @Test
    void validUsernameShouldReturnTrue() {
        assertTrue(validator.usernameIsValid("john123"));
    }

    @Test
    void usernameTooShortShouldReturnFalse() {
        assertFalse(validator.usernameIsValid("ab"));
    }

    @Test
    void usernameTooLongShouldReturnFalse() {
        assertFalse(validator.usernameIsValid("abcdefghijklmnopqrstu"));
    }

    @Test
    void usernameWithWhitespaceShouldReturnFalse() {
        assertFalse(validator.usernameIsValid("john doe"));
    }

    @Test
    void nullUsernameShouldReturnFalse() {
        assertFalse(validator.usernameIsValid(null));
    }

    // Password tests

    @Test
    void validPasswordShouldReturnTrue() {
        assertTrue(validator.passwordIsValid("StrongPassword123"));
    }

    @Test
    void passwordTooShortShouldReturnFalse() {
        assertFalse(validator.passwordIsValid("short"));
    }

    @Test
    void passwordWithWhitespaceShouldReturnFalse() {
        assertFalse(validator.passwordIsValid("Strong Password123"));
    }

    @Test
    void passwordExceeding72Utf8BytesShouldReturnFalse() {
        String password = "a".repeat(73);
        assertFalse(validator.passwordIsValid(password));
    }

    @Test
    void nullPasswordShouldReturnFalse() {
        assertFalse(validator.passwordIsValid(null));
    }

    // Email tests

    @Test
    void validEmailShouldReturnTrue() {
        assertTrue(validator.isValidEmail("test@example.com"));
    }

    @Test
    void invalidEmailShouldReturnFalse() {
        assertFalse(validator.isValidEmail("invalid-email"));
    }

    @Test
    void nullEmailShouldReturnFalse() {
        assertFalse(validator.isValidEmail(null));
    }

    @Test
    void emailWithWhitespaceAroundItShouldBeAccepted() {
        assertTrue(validator.isValidEmail(" test@example.com "));
    }

    // URL tests

    @Test
    void validHttpsLinkShouldReturnTrue() {
        assertTrue(validator.linkIsValid("https://example.com"));
    }

    @Test
    void validHttpLinkShouldReturnTrue() {
        assertTrue(validator.linkIsValid("http://example.com"));
    }

    @Test
    void ftpLinkShouldReturnFalse() {
        assertFalse(validator.linkIsValid("ftp://example.com"));
    }

    @Test
    void javascriptLinkShouldReturnFalse() {
        assertFalse(validator.linkIsValid("javascript:alert(1)"));
    }

    @Test
    void noDomainLinkShouldReturnFalse() {
        assertFalse(validator.linkIsValid("https://"));
    }

    @Test
    void nullLinkShouldReturnFalse() {
        assertFalse(validator.linkIsValid(null));
    }

    @Test
    void malformedLinkShouldReturnFalse() {
        assertFalse(validator.linkIsValid("https://example.com:invalid"));
    }

    @Test
    void protocolRelativeLinkShouldReturnFalse() {
        assertFalse(validator.linkIsValid("//example.com"));
    }
}
