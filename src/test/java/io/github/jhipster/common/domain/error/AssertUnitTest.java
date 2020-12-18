package io.github.jhipster.common.domain.error;

import io.github.jhipster.common.domain.error.Assert.BigDecimalAsserter;
import io.github.jhipster.common.domain.error.Assert.StringAsserter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AssertUnitTest {
    @Test
    void shouldNotValidateNullInputs() {
        assertThatThrownBy(() -> Assert.notNull("field", null))
            .isExactlyInstanceOf(MissingMandatoryValueException.class)
            .hasMessageContaining("\"field\"");
    }

    @Test
    void shouldNotValidateNullString() {
        assertThatThrownBy(() -> Assert.notBlank("field", null))
            .isExactlyInstanceOf(MissingMandatoryValueException.class)
            .hasMessageContaining("\"field\"")
            .hasMessageContaining("(null)");
    }

    @Test
    void shouldNotValidateEmptyString() {
        assertNotBlankString("");
    }

    @Test
    void shouldNotValidateSpaceString() {
        assertNotBlankString(" ");
    }

    @Test
    void shouldNotValidateTabString() {
        assertNotBlankString("  ");
    }

    private void assertNotBlankString(String input) {
        assertThatThrownBy(() -> Assert.notBlank("field", input))
            .isExactlyInstanceOf(MissingMandatoryValueException.class)
            .hasMessageContaining("\"field\"")
            .hasMessageContaining("(blank)");
    }

    @Test
    void shouldNotValidateNullCollectionForEmptyValidation() {
        assertThatThrownBy(() -> Assert.notEmpty("field", null))
            .isExactlyInstanceOf(MissingMandatoryValueException.class)
            .hasMessageContaining("field");
    }

    @Test
    void shouldNotValidateEmptyCollectionForEmptyValidation() {
        assertThatThrownBy(() -> Assert.notEmpty("field", List.of()))
            .isExactlyInstanceOf(MissingMandatoryValueException.class)
            .hasMessageContaining("field")
            .hasMessageContaining("empty");
    }

    @Test
    void shouldNotValidateFormatWithoutFormat() {
        assertThatThrownBy(() -> Assert.field("field", "value").format(null))
            .isExactlyInstanceOf(MissingMandatoryValueException.class)
            .hasMessageContaining("regex");
    }

    @Test
    void shouldNotValidateStringWithInvalidRegex() {
        assertThatThrownBy(() -> Assert.field("field", "value").format("["))
            .isExactlyInstanceOf(StringFormatException.class)
            .hasMessageContaining("invalid regex: \"[\"");
    }

    @Test
    void shouldNotValidateStringWithMalformedValue() {
        assertThatThrownBy(() -> Assert.field("field", "dummy").format("[0-9]+"))
            .isExactlyInstanceOf(StringFormatException.class)
            .hasMessageContaining("field")
            .hasMessageContaining("\"[0-9]+\"");
    }

    @Test
    void shouldNotValidateLongFieldOverMaxValue() {
        assertThatThrownBy(() -> Assert.field("field", 4L).max(3))
            .isExactlyInstanceOf(ValueOverMaxException.class)
            .hasMessageContaining("\"field\"")
            .hasMessageContaining("3")
            .hasMessageContaining("4");
    }

    @Test
    void shouldNotValidateLongFieldUnderMinValue() {
        assertThatThrownBy(() -> Assert.field("field", 4L).min(5))
            .isExactlyInstanceOf(ValueUnderMinException.class)
            .hasMessageContaining("\"field\"")
            .hasMessageContaining("4")
            .hasMessageContaining("5");
    }

    @Test
    void shouldNotValidateBlankStringInFluentAssertions() {
        assertThatThrownBy(() -> Assert.field("field", " ").notBlank())
            .isExactlyInstanceOf(MissingMandatoryValueException.class)
            .hasMessageContaining("\"field\"")
            .hasMessageContaining("(blank)");
    }

    @Test
    void shouldValidateNullOverMaxValue() {
        assertThat(Assert.field("field", (Long) null).max(42)).isExactlyInstanceOf(BigDecimalAsserter.class);
    }

    @Test
    void shouldValidateNullOverMinValue() {
        assertThat(Assert.field("field", (Long) null).min(3)).isExactlyInstanceOf(BigDecimalAsserter.class);
    }

    @Test
    void shouldValidateNullStringMaxSize() {
        assertThat(Assert.field("field", (String) null).maxLength(42)).isExactlyInstanceOf(StringAsserter.class);
    }

    @Test
    void shouldNotValidateTooLongString() {
        assertThatThrownBy(() -> Assert.field("field", "value").maxLength(4))
            .isExactlyInstanceOf(StringTooLongException.class)
            .hasMessageContaining("\"field\"")
            .hasMessageContaining("5")
            .hasMessageContaining("4");
    }

    @Test
    void shouldNotValidateTooShortString() {
        assertThatThrownBy(() -> Assert.field("field", "value").minLength(10))
            .isExactlyInstanceOf(StringTooShortException.class)
            .hasMessageContaining("\"field\"")
            .hasMessageContaining("5")
            .hasMessageContaining("10");
    }

    @Test
    void shouldValidateIfNullInteger() {
        assertThatCode(() -> Assert.field("field", (Integer) null)).doesNotThrowAnyException();
    }
}
