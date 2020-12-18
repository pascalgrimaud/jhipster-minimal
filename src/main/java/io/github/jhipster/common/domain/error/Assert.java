package io.github.jhipster.common.domain.error;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.regex.PatternSyntaxException;

public final class Assert {

    private Assert() {}

    public static void notNull(String fieldName, Object input) {
        if (input == null) {
            throw MissingMandatoryValueException.forNullValue(fieldName);
        }
    }

    public static void notBlank(String fieldName, String input) {
        notNull(fieldName, input);

        if (input.isBlank()) {
            throw MissingMandatoryValueException.forBlankValue(fieldName);
        }
    }

    public static void notEmpty(String fieldName, Collection<?> input) {
        notNull(fieldName, input);

        if (input.isEmpty()) {
            throw MissingMandatoryValueException.forEmptyValue(fieldName);
        }
    }

    /**
     * Allow fluent assertions on Long. Usage:
     *
     * <code>Assert.field("myField", 5L).max(4L);</code>
     *
     * @param fieldName
     *          Name of the field to make assertions on to
     * @param value
     *          Value to assert
     * @return An {@link BigDecimalAsserter} for fluent assertions
     */
    public static BigDecimalAsserter field(String fieldName, Long value) {
        BigDecimal bigDecimalValue = null;

        if (value != null) {
            bigDecimalValue = BigDecimal.valueOf(value);
        }

        return new BigDecimalAsserter(fieldName, bigDecimalValue);
    }

    /**
     * Allow fluent assertions on Integer. Usage:
     *
     * <code>Assert.field("myField", 5).max(4);</code>
     *
     * @param fieldName
     *          Name of the field to make assertions on to
     * @param value
     *          Value to assert
     * @return An {@link BigDecimalAsserter} for fluent assertions
     */
    public static BigDecimalAsserter field(String fieldName, Integer value) {
        BigDecimal bigDecimalValue = null;

        if (value != null) {
            bigDecimalValue = BigDecimal.valueOf(value);
        }

        return new BigDecimalAsserter(fieldName, bigDecimalValue);
    }

    /**
     * Allow fluent assertions on String. Usage:
     *
     * <code>Assert.field("myField", "value").notBlank().lengthUnder(4);</code>
     *
     * @param fieldName
     *          Name of the field to make assertions on to
     * @param value
     *          Value to assert
     * @return An {@link StringAsserter} for fluent assertions
     */
    public static StringAsserter field(String fieldName, String value) {
        return new StringAsserter(fieldName, value);
    }

    public static class BigDecimalAsserter {
        private final String fieldName;
        private final BigDecimal value;

        private BigDecimalAsserter(String fieldName, BigDecimal value) {
            this.fieldName = fieldName;
            this.value = value;
        }

        public BigDecimalAsserter max(long maxValue) {
            if (value != null && value.compareTo(new BigDecimal(maxValue)) > 0) {
                throw ValueOverMaxException.builder().field(fieldName).value(value).maxValue(maxValue).build();
            }

            return this;
        }

        public BigDecimalAsserter min(long minValue) {
            if (value != null && value.compareTo(new BigDecimal(minValue)) < 0) {
                throw ValueUnderMinException.builder().field(fieldName).value(value).minValue(minValue).build();
            }

            return this;
        }
    }

    public static class StringAsserter {
        private final String fieldName;
        private final String value;

        private StringAsserter(String fieldName, String value) {
            this.fieldName = fieldName;
            this.value = value;
        }

        public StringAsserter notBlank() {
            Assert.notBlank(fieldName, value);

            return this;
        }

        public StringAsserter maxLength(int maxLength) {
            if (value != null && value.length() > maxLength) {
                throw StringTooLongException.builder().field(fieldName).currentLength(value.length()).maxLength(maxLength).build();
            }

            return this;
        }

        public StringAsserter minLength(int minLength) {
            if (value != null && value.length() < minLength) {
                throw StringTooShortException.builder().field(fieldName).currentLength(value.length()).minLength(minLength).build();
            }

            return this;
        }

        public StringAsserter format(String regex) {
            Assert.notNull("regex", regex);

            if (invalidFormat(regex)) {
                throw StringFormatException.wrongFormat(fieldName, regex, value);
            }

            return this;
        }

        private boolean invalidFormat(String regex) {
            try {
                return !value.matches(regex);
            } catch (PatternSyntaxException e) {
                throw StringFormatException.invalidRegex(regex);
            }
        }
    }
}
