package io.github.jhipster.common.domain.error;

import java.io.Serializable;

@FunctionalInterface
public interface JHipsterMessage extends Serializable {
    String getMessageKey();
}
