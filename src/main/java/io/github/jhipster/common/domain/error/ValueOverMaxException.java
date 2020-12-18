package io.github.jhipster.common.domain.error;

import java.math.BigDecimal;

public class ValueOverMaxException extends JHipsterException {

    private ValueOverMaxException(ValueOverMaxExceptionBuilder builder) {
        super(
            JHipsterException.builder(StandardMessage.VALUE_OVER_MAX)
                .argument("field", builder.field)
                .argument("max", builder.maxValue)
                .argument("value", builder.value)
                .message(builder.message())
                .status(ErrorHttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    public static ValueOverMaxExceptionBuilder builder() {
        return new ValueOverMaxExceptionBuilder();
    }

    public static class ValueOverMaxExceptionBuilder {
        private String field;
        private BigDecimal value;
        private long maxValue;

        public ValueOverMaxException build() {
            return new ValueOverMaxException(this);
        }

        public ValueOverMaxExceptionBuilder field(String field) {
            this.field = field;
            return this;
        }

        public ValueOverMaxExceptionBuilder value(BigDecimal value) {
            this.value = value;
            return this;
        }

        public ValueOverMaxExceptionBuilder maxValue(long maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        private String message() {
            return "Value of \"" + field + "\" must be under " + maxValue + " but was " + value;
        }
    }
}
