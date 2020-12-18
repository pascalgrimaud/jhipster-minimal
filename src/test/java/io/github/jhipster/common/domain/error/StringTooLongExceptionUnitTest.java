package io.github.jhipster.common.domain.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StringTooLongExceptionUnitTest {

    @Test
    void shouldGetExceptionInformation() {
        StringTooLongException exception = StringTooLongException.builder().field("field").currentLength(5).maxLength(4).build();

        assertThat(exception.getMessage()).contains("\"field\"").contains("5").contains("4");
        assertThat(exception.getArguments()).containsOnly(entry("field", "field"), entry("currentLength", "5"), entry("maxLength", "4"));
        assertThat(exception.getStatus()).isEqualTo(ErrorHttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getJhipsterMessage()).isEqualTo(StandardMessage.STRING_TOO_LONG);
    }

}
