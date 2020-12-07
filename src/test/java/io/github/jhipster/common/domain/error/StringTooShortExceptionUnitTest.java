package io.github.jhipster.common.domain.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StringTooShortExceptionUnitTest {

    @Test
    void shouldGetExceptionInformation() {
        StringTooShortException exception = StringTooShortException.builder()
            .field("field")
            .currentLength(5)
            .minLength(4)
            .build();

        assertThat(exception.getMessage()).contains("\"field\"").contains("5").contains("4");
        assertThat(exception.getArguments()).containsOnly(entry("field", "field"), entry("currentLength", "5"), entry("minLength", "4"));
        assertThat(exception.getStatus()).isEqualTo(ErrorHttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getJhipsterMessage()).isEqualTo(StandardMessage.STRING_TOO_SHORT);
    }

}
