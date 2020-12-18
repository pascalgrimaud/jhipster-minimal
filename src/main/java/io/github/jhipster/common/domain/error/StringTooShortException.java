package io.github.jhipster.common.domain.error;

public class StringTooShortException extends JHipsterException {

    StringTooShortException(StringTooShortExceptionBuilder builder) {
        super(
            JHipsterException.builder(StandardMessage.STRING_TOO_SHORT)
                .argument("field", builder.field)
                .argument("currentLength", builder.currentLength)
                .argument("minLength", builder.minLength)
                .message(builder.message())
                .status(ErrorHttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    public static StringTooShortExceptionBuilder builder() {
        return new StringTooShortExceptionBuilder();
    }

    public static class StringTooShortExceptionBuilder {
        private String field;
        private int currentLength;
        private int minLength;

        public StringTooShortException build() {
            return new StringTooShortException(this);
        }

        public StringTooShortExceptionBuilder field(String field) {
            this.field = field;
            return this;
        }

        public StringTooShortExceptionBuilder currentLength(int currentLength) {
            this.currentLength = currentLength;
            return this;
        }

        public StringTooShortExceptionBuilder minLength(int minLength) {
            this.minLength = minLength;
            return this;
        }

        private String message() {
            return "Length of \"" + field + "\" must be over " + minLength + " but was " + currentLength;
        }
    }
}
