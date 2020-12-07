package io.github.jhipster.common.domain.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MissingMandatoryValueExceptionUnitTest {

    @Test
    public void shouldGetExceptionForBlankValue() {
        MissingMandatoryValueException exception = MissingMandatoryValueException.forBlankValue("field");

        assertThat(exception.getStatus()).isEqualTo(ErrorHttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getJhipsterMessage()).isEqualTo(StandardMessage.SERVER_MANDATORY_BLANK);
        assertThat(exception.getArguments()).containsExactly(entry("field", "field"));
        assertThat(exception.getMessage()).isEqualTo("The field \"field\" is mandatory and wasn't set (blank)");
    }

    @Test
    public void shouldGetExceptionForNullValue() {
        MissingMandatoryValueException exception = MissingMandatoryValueException.forNullValue("field");

        assertThat(exception.getStatus()).isEqualTo(ErrorHttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getJhipsterMessage()).isEqualTo(StandardMessage.SERVER_MANDATORY_NULL);
        assertThat(exception.getArguments()).containsExactly(entry("field", "field"));
        assertThat(exception.getMessage()).isEqualTo("The field \"field\" is mandatory and wasn't set (null)");
    }

    @Test
    public void shouldGetExceptionForEmptyValue() {
        MissingMandatoryValueException exception = MissingMandatoryValueException.forEmptyValue("field");

        assertThat(exception.getStatus()).isEqualTo(ErrorHttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getJhipsterMessage()).isEqualTo(StandardMessage.SERVER_MANDATORY_EMPTY);
        assertThat(exception.getArguments()).containsExactly(entry("field", "field"));
        assertThat(exception.getMessage()).isEqualTo("The field \"field\" is mandatory and wasn't set (empty)");
    }

}
