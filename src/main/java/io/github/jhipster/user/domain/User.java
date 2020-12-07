package io.github.jhipster.user.domain;

import io.github.jhipster.common.domain.Constants;
import io.github.jhipster.common.domain.error.Assert;

import java.time.Instant;
import java.util.Set;

public class User {
    private final String login;
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String imageUrl;
    private final boolean activated = false;
    private final String langKey;
    private final String createdBy;
    private final Instant createdDate;
    private final String lastModifiedBy;
    private final Instant lastModifiedDate;
    private final Set<String> authorities;

    private User(UserBuilder builder) {
        assertFields(builder);

        this.id = builder.id;
        this.login = builder.login;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.imageUrl = builder.imageUrl;
        this.langKey = builder.langKey;
        this.createdBy = builder.createdBy;
        this.createdDate = builder.createdDate;
        this.lastModifiedBy = builder.lastModifiedBy;
        this.lastModifiedDate = builder.lastModifiedDate;
        this.authorities = builder.authorities;
    }

    private void assertFields(UserBuilder builder) {
        Assert.field("login", builder.login)
            .notBlank()
            .minLength(1)
            .maxLength(50)
            .format(Constants.LOGIN_REGEX);
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        private String login;
        private String firstName;
        private String lastName;
        private String email;
        private String imageUrl;
        private String langKey;
        private String createdBy;
        private Instant createdDate;
        private String lastModifiedBy;
        private Instant lastModifiedDate;
        private Set<String>authorities;

        public User build() {
            return new User(this);
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder login(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public UserBuilder langKey(String langKey) {
            this.langKey = langKey;
            return this;
        }

        public UserBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public UserBuilder createdDate(Instant createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public UserBuilder lastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
            return this;
        }

        public UserBuilder lastModifiedDate(Instant lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
            return this;
        }

        public UserBuilder authorities(Set<String> authorities) {
            this.authorities = authorities;
            return this;
        }
    }
}
