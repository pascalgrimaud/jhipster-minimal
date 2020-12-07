package io.github.jhipster.common.domain.error;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JHipsterException extends RuntimeException {
    private final Map<String, String> arguments;
    private final ErrorHttpStatus status;
    private final JHipsterMessage jhipsterMessage;

    protected JHipsterException(JHipsterExceptionBuilder builder) {
        super(getMessage(builder), getCause(builder));
        this.arguments = getArguments(builder);
        this.status = getStatus(builder);
        this.jhipsterMessage = getJHipsterMessage(builder);
    }

    public static JHipsterExceptionBuilder builder(JHipsterMessage message) {
        return new JHipsterExceptionBuilder(message);
    }

    private static String getMessage(JHipsterExceptionBuilder builder) {
        if (builder == null) {
            return null;
        }

        return builder.message;
    }
    private static Throwable getCause(JHipsterExceptionBuilder builder) {
        if (builder == null) {
            return null;
        }

        return builder.cause;
    }
    private static Map<String, String> getArguments(JHipsterExceptionBuilder builder) {
        if (builder == null) {
            return null;
        }

        return Collections.unmodifiableMap(builder.arguments);
    }
    private static ErrorHttpStatus getStatus(JHipsterExceptionBuilder builder) {
        if (builder == null) {
            return null;
        }

        return builder.status;
    }
    private static JHipsterMessage getJHipsterMessage(JHipsterExceptionBuilder builder) {
        if (builder == null) {
            return null;
        }

        return builder.jHipsterMessage;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public ErrorHttpStatus getStatus() {
        return status;
    }

    public JHipsterMessage getJhipsterMessage() {
        return jhipsterMessage;
    }

    public static class JHipsterExceptionBuilder {
        private final Map<String, String> arguments = new HashMap<>();
        private ErrorHttpStatus status;
        private JHipsterMessage jHipsterMessage;
        private Throwable cause;
        private String message;

        public JHipsterExceptionBuilder(JHipsterMessage jHipsterMessage) {
            this.jHipsterMessage = jHipsterMessage;
        }

        public JHipsterException build() {
            return new JHipsterException(this);
        }

        public JHipsterExceptionBuilder argument(String key, Object value) {
            arguments.put(key, getStringValue(value));
            return this;
        }

        private String getStringValue(Object value) {
            if (value == null) {
                return "null";
            }

            return value.toString();
        }

        public JHipsterExceptionBuilder status(ErrorHttpStatus status) {
            this.status = status;
            return this;
        }

        public JHipsterExceptionBuilder jHipsterMessage(JHipsterMessage jHipsterMessage) {
            this.jHipsterMessage = jHipsterMessage;
            return this;
        }

        public JHipsterExceptionBuilder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public JHipsterExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }
    }
}
