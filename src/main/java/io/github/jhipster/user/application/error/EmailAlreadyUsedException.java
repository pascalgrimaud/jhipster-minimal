package io.github.jhipster.user.application.error;

import io.github.jhipster.common.domain.ErrorConstants;
import io.github.jhipster.common.infrastructure.primary.BadRequestAlertException;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
