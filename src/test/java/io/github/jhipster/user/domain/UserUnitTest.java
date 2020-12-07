package io.github.jhipster.user.domain;

import io.github.jhipster.common.domain.error.MissingMandatoryValueException;
import io.github.jhipster.common.domain.error.StringFormatException;
import io.github.jhipster.common.domain.error.StringTooLongException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserUnitTest {

    @Nested
    class LoginUnitTest {
        @Test
        void shouldNotBuildWithoutLogin() {
            assertThatThrownBy(() -> User.builder().login(null).build())
                .hasMessageContaining("login")
                .isExactlyInstanceOf(MissingMandatoryValueException.class);
        }

        @Test
        void shouldNotBuildWithBlankLogin() {
            assertThatThrownBy(() -> User.builder().login(" ").build())
                .hasMessageContaining("login")
                .isExactlyInstanceOf(MissingMandatoryValueException.class);
        }

        @Test
        void shouldNotBuildWithLoginSmallerThanOne() {
            assertThatThrownBy(() -> User.builder().login("").build())
                .hasMessageContaining("login")
                .isExactlyInstanceOf(MissingMandatoryValueException.class);
        }

        @Test
        void shouldNotBuildWithLoginLongerThanFifty() {
            assertThatThrownBy(() -> User.builder().login("jhipsterLoginThatIsVeryVeryVeryVeryVeryVeryVeryLong").build())
                .hasMessageContaining("login")
                .isExactlyInstanceOf(StringTooLongException.class);
        }

        @Test
        void shouldNotBuildWithNonConformRegex() {
            assertThatThrownBy(() -> User.builder().login("\""))
                .hasMessageContaining("login")
                .isExactlyInstanceOf(StringFormatException.class);
        }
    }
}
