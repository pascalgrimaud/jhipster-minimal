package io.github.jhipster.common.domain.error;

public class MissingMandatoryValueException extends JHipsterException {

    private MissingMandatoryValueException(JHipsterExceptionBuilder builder) {
        super(builder);
    }

    private static JHipsterExceptionBuilder builder(JHipsterMessage jHipsterMessage, String field, String message) {
        return JHipsterException.builder(jHipsterMessage).status(ErrorHttpStatus.INTERNAL_SERVER_ERROR).argument("field", field).message(message);
    }

    private static String defaultMessage(String field) { return "The field \"" + field + "\" is mandatory and wasn't set"; }

    public static MissingMandatoryValueException forNullValue(String field) {
        return new MissingMandatoryValueException(
            builder(StandardMessage.SERVER_MANDATORY_NULL, field, defaultMessage(field) + " (null)")
        );
    }

    public static MissingMandatoryValueException forEmptyValue(String field) {
        return new MissingMandatoryValueException(
            builder(StandardMessage.SERVER_MANDATORY_EMPTY, field, defaultMessage(field) + " (empty)")
        );
    }

    public static MissingMandatoryValueException forBlankValue(String fieldName) {
        return new MissingMandatoryValueException(
            builder(StandardMessage.SERVER_MANDATORY_BLANK, fieldName, defaultMessage(fieldName) + " (blank)")
        );
    }
}
