package io.github.jhipster.common.domain.error;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ValueUnderMinExceptionUnitTest {
    @Test
    void shouldGetExceptionInformation() {
        ValueUnderMinException exception = ValueUnderMinException.builder().field("field").value(BigDecimal.valueOf(42)).minValue(51).build();

        assertThat(exception.getMessage()).contains("\"field\"").contains("42").contains("51");
        assertThat(exception.getStatus()).isEqualTo(ErrorHttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exception.getArguments()).containsOnly(entry("field", "field"), entry("min", "51"), entry("value", "42"));
        assertThat(exception.getJhipsterMessage()).isEqualTo(StandardMessage.VALUE_UNDER_MIN);
    }
}
