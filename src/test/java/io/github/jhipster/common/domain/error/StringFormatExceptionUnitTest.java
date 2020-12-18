package io.github.jhipster.common.domain.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StringFormatExceptionUnitTest {
    @Test
    void shouldGetInvalidRegexException() {
        StringFormatException exception = StringFormatException.invalidRegex("format");

        assertThat(exception.getMessage()).contains("invalid regex: \"format\"");
        assertThat(exception.getStatus()).isEqualTo(ErrorHttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getArguments()).containsExactly(entry("regex", "format"));
        assertThat(exception.getJhipsterMessage()).isEqualTo(StandardMessage.INVALID_REGEX);
    }

    @Test
    void shouldGetWrongFormatException() {
        StringFormatException exception = StringFormatException.wrongFormat("field", "format", "value");

        assertThat(exception.getMessage()).contains("field").contains("\"format\"").contains("\"value\"");
        assertThat(exception.getStatus()).isEqualTo(ErrorHttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getArguments()).containsOnly(entry("field", "field"), entry("regex", "format"), entry("value", "value"));
        assertThat(exception.getJhipsterMessage()).isEqualTo(StandardMessage.INVALID_FORMAT);
    }
}
