package io.github.jhipster.common.domain.error;

public class StringTooLongException extends JHipsterException {

    StringTooLongException(StringTooLongExceptionBuilder builder) {
        super(
            JHipsterException.builder(StandardMessage.STRING_TOO_LONG)
                .argument("field", builder.field)
                .argument("currentLength", builder.currentLength)
                .argument("maxLength", builder.maxLength)
                .message(builder.message())
                .status(ErrorHttpStatus.INTERNAL_SERVER_ERROR)

        );
    }

    public static StringTooLongExceptionBuilder builder() {
        return new StringTooLongExceptionBuilder();
    }

    public static class StringTooLongExceptionBuilder {
        private String field;
        private int currentLength;
        private int maxLength;

        public StringTooLongException build() {
            return new StringTooLongException(this);
        }

        public StringTooLongExceptionBuilder field(String field) {
            this.field = field;
            return this;
        }

        public StringTooLongExceptionBuilder currentLength(int currentLength) {
            this.currentLength = currentLength;
            return this;
        }

        public StringTooLongExceptionBuilder maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        private String message() { return "Length of \"" + field + "\" must be under " + maxLength + " but was " + currentLength; }
    }
}
