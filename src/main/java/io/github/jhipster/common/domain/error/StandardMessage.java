package io.github.jhipster.common.domain.error;

public enum StandardMessage implements JHipsterMessage {

    BAD_REQUEST("user.bad-request"),
    INTER_SERVER_ERROR("server.internal-server-error"),
    SERVER_MANDATORY_NULL("server.mandatory-null"),
    SERVER_MANDATORY_EMPTY("server.mandatory-empty"),
    SERVER_MANDATORY_BLANK("server.mandatory-blank"),
    STRING_TOO_LONG("server.string-too-long"),
    STRING_TOO_SHORT("server.string-too-short"),
    VALUE_OVER_MAX("server.value-over-max"),
    VALUE_UNDER_MIN("server.value-under-min"),
    INVALID_FORMAT("server.invalid-format"),
    INVALID_REGEX("server.invalid-regex");
    private final String messageKey;

    private StandardMessage(String code) { this.messageKey = code; }

    @Override
    public String getMessageKey() { return messageKey; }
}
