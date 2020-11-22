package io.github.jhipster.user.application.error;

import io.github.jhipster.common.domain.ErrorConstants;
import io.github.jhipster.common.infrastructure.primary.BadRequestAlertException;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "userexists");
    }
}
