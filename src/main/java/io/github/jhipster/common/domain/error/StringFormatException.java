package io.github.jhipster.common.domain.error;

public class StringFormatException extends JHipsterException {
    protected StringFormatException(JHipsterExceptionBuilder builder) {
        super(builder.status(ErrorHttpStatus.INTERNAL_SERVER_ERROR));
    }

    public static StringFormatException invalidRegex(String format) {
        return new StringFormatException(
            JHipsterException
                .builder(StandardMessage.INVALID_REGEX)
                .message("Can't validate format, invalid regex: \"" + format + "\"")
                .argument("regex", format)
        );
    }

    public static StringFormatException wrongFormat(String field, String format, String value) {
        return new StringFormatException(
            JHipsterException
                .builder(StandardMessage.INVALID_FORMAT)
                .message("Can't validate \"" + field + "\" value, expected format is \"" + format + "\" for \"" + value + "\"")
                .argument("regex", format)
                .argument("value", value)
                .argument("field", field)
        );
    }
}
