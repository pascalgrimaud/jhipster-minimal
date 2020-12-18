package io.github.jhipster.common.domain.error;

import java.math.BigDecimal;

public class ValueUnderMinException extends JHipsterException{

    private ValueUnderMinException(ValueUnderMinExceptionBuilder builder) {
        super(
            JHipsterException
                .builder(StandardMessage.VALUE_UNDER_MIN)
                .argument("field", builder.field)
                .argument("min", builder.minValue)
                .argument("value", builder.value)
                .message(builder.message())
                .status(ErrorHttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    public static ValueUnderMinExceptionBuilder builder() {
        return new ValueUnderMinExceptionBuilder();
    }

    public static class ValueUnderMinExceptionBuilder {
        private String field;
        private BigDecimal value;
        private long minValue;

        public ValueUnderMinException build() {
            return new ValueUnderMinException(this);
        }

        public ValueUnderMinExceptionBuilder field(String field) {
            this.field = field;
            return this;
        }

        public ValueUnderMinExceptionBuilder value(BigDecimal value) {
            this.value = value;
            return this;
        }

        public ValueUnderMinExceptionBuilder minValue(long minValue) {
            this.minValue = minValue;
            return this;
        }

        private String message() {
            return "Value of \"" + field + "\" must be over " + minValue + " but was " + value;
        }

    }
}
