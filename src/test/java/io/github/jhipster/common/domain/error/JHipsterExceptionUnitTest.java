package io.github.jhipster.common.domain.error;

import io.github.jhipster.common.domain.error.JHipsterException.JHipsterExceptionBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JHipsterExceptionUnitTest {

    @Test
    public void shouldGetUnmodifiableArguments() {
        assertThatThrownBy(() -> fullBuilder().build().getArguments().clear()).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void shouldBuildWithoutBuilder() {
        JHipsterException exception = new JHipsterException(null);

        assertThat(exception.getMessage()).isNull();
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getArguments()).isNull();
        assertThat(exception.getStatus()).isNull();
        assertThat(exception.getJhipsterMessage()).isNull();
    }

    @Test
    public void shouldGetExceptionInformation() {
        JHipsterException exception = fullBuilder().build();

        assertThat(exception.getMessage()).isEqualTo("This is an error message");
        assertThat(exception.getCause()).isExactlyInstanceOf(RuntimeException.class);
        assertThat(exception.getArguments()).hasSize(2).contains(entry("key", "value"), entry("key1", "value1"));
        assertThat(exception.getStatus()).isEqualTo(ErrorHttpStatus.BAD_REQUEST);
        assertThat(exception.getJhipsterMessage()).isEqualTo(StandardMessage.BAD_REQUEST);
    }

    @Test
    void shouldMapNullArgumentAsNullString() {
        assertThat(fullBuilder().argument("nullable", null).build().getArguments().get("nullable")).isEqualTo("null");
    }

    @Test
    void shouldGetObjectsToString() {
        assertThat(fullBuilder().argument("object", 4).build().getArguments().get("object")).isEqualTo("4");
    }

    private JHipsterExceptionBuilder fullBuilder() {
        return JHipsterException
            .builder(StandardMessage.BAD_REQUEST)
            .argument("key", "value")
            .argument("key1", "value1")
            .status(ErrorHttpStatus.BAD_REQUEST)
            .message("This is an error message")
            .cause(new RuntimeException());
    }
}
