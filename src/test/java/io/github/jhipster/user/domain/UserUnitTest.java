package io.github.jhipster.user.domain;

import io.github.jhipster.common.domain.error.MissingMandatoryValueException;
import io.github.jhipster.common.domain.error.StringFormatException;
import io.github.jhipster.common.domain.error.StringTooLongException;
import io.github.jhipster.common.domain.error.StringTooShortException;
import io.github.jhipster.user.domain.User.UserBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

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
            assertThatThrownBy(() -> User.builder().login("ＪＨＩＰＳＴＥＲ").build())
                .hasMessageContaining("login")
                .isExactlyInstanceOf(StringFormatException.class);
        }

        @Test
        void shouldBuildWithCorrectLogin() {
            User user = fullBuilder().login("jhiLogin").build();
            assertThat(user.getLogin()).isEqualTo("jhiLogin");
        }
    }

    @Nested
    class IdUnitTest {
        @Test
        void shouldBuildWithCorrectId() {
            User user = fullBuilder()
                .id(42L).build();
            assertThat(user.getId()).isEqualTo(42L);
        }
    }

    @Nested
    class FirstNameUnitTest {
        @Test
        void shouldNotBuildWithFirstNameLongerThanFifty() {
            assertThatThrownBy(() -> fullBuilder().firstName("AVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongName").build())
                .hasMessageContaining("firstName")
                .isExactlyInstanceOf(StringTooLongException.class);
        }

        @Test
        void shouldBuildWithProperFirstName() {
            User user = fullBuilder().firstName("Pascal").build();
            assertThat(user.getFirstName()).isEqualTo("Pascal");
        }
    }

    @Nested
    class LastNameUnitTest {

        @Test
        void shouldNotBuildWithLastNameLongerThanFifty() {
            assertThatThrownBy(() -> fullBuilder().lastName("AVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongName").build())
                .hasMessageContaining("lastName")
                .isExactlyInstanceOf(StringTooLongException.class);
        }

        @Test
        void shouldBuildWithProperLastName() {
            User user = fullBuilder().lastName("DUBOIS").build();
            assertThat(user.getLastName()).isEqualTo("DUBOIS");
        }
    }

    @Nested
    class EmailUnitTest {
        @Test
        void shouldNotBuildWithEmailShorterThanFive() {
            assertThatThrownBy(() -> fullBuilder().email("j").build())
                .hasMessageContaining("email")
                .isExactlyInstanceOf(StringTooShortException.class);

        }

        @Test
        void shouldNotBuildWithEmailLongerThanTwoHundredFiftyFour() {
            assertThatThrownBy(() -> fullBuilder().email("AVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongName").build())
                .hasMessageContaining("email")
                .isExactlyInstanceOf(StringTooLongException.class);
        }

        @Test
        void shouldNotBuildWithNonConformEmail() {
            assertThatThrownBy(() -> fullBuilder().email("totallyConformEmail").build())
                .hasMessageContaining("email")
                .isExactlyInstanceOf(StringFormatException.class)
            ;
        }

        @Test
        void shouldBuildWithProperEmail() {
            User user = fullBuilder().email("jhipster.dude@jhipster.io").build();
            assertThat(user.getEmail()).isEqualTo("jhipster.dude@jhipster.io");
        }
    }

    @Nested
    class ImageUrlUnitTest {
        @Test
        void shouldNotBuilderWithImageUrlLongerThanTwoHundredFiftySix() {
            assertThatThrownBy(() -> fullBuilder().imageUrl("AVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongName").build())
                .hasMessageContaining("imageUrl")
                .isExactlyInstanceOf(StringTooLongException.class);
        }

        @Test
        void shouldBuildWithProperImageUrl() {
            User user = fullBuilder().imageUrl("DefinitelyImageURL").build();
            assertThat(user.getImageUrl()).isEqualTo("DefinitelyImageURL");
        }
    }

    @Nested
    class ActivatedUnitTest {
        @Test
        void shouldBuildWithActivatedFalseByDefault() {
            User user = fullBuilder().build();
            assertThat(user.isActivated()).isFalse();
        }
    }

    @Nested
    class LangKeyUnitTest {
        @Test
        void shouldNotBuildWithLangKeyShorterThanTwo() {
            assertThatThrownBy(() -> fullBuilder().langKey("J").build())
                .hasMessageContaining("langKey")
                .isExactlyInstanceOf(StringTooShortException.class);
        }

        @Test
        void shouldNotBuildWithLangKeyLongerThanTen() {
            assertThatThrownBy(() -> fullBuilder().langKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ").build())
                .hasMessageContaining("langKey")
                .isExactlyInstanceOf(StringTooLongException.class);
        }

        @Test
        void shouldBuildWithProperLangKey() {
            User user = fullBuilder().langKey("FR").build();
            assertThat(user.getLangKey()).isEqualTo("FR");
        }
    }

    @Nested
    class CreatedByUnitTest {
        @Test
        void shouldBuildWithProperCreatedBy() {
            User user = fullBuilder().createdBy("jhipsterAdmin").build();
            assertThat(user.getCreatedBy()).isEqualTo("jhipsterAdmin");
        }
    }

    @Nested
    class CreatedDataUnitTest {
        @Test
        void shouldBuildWithProperCreatedDate() {
            User user = fullBuilder().createdDate(Instant.EPOCH).build();
            assertThat(user.getCreatedDate()).isEqualTo(Instant.EPOCH);
        }
    }

    @Nested
    class LastModifiedByUnitTest {
        @Test
        void shouldBuildWithProperLastModifiedBy() {
            User user = fullBuilder().lastModifiedBy("jhipsterAdmin").build();
            assertThat(user.getLastModifiedBy()).isEqualTo("jhipsterAdmin");
        }
    }

    @Nested
    class LastModifiedDateUnitTest {
        @Test
        void shouldBuildWithProperLastModifiedDate() {
            User user = fullBuilder().lastModifiedDate(Instant.MAX).build();
            assertThat(user.getLastModifiedDate()).isEqualTo(Instant.MAX);
        }
    }

    @Nested
    class AuthoritiesUnitTest {
        @Test
        void shouldBuildWithProperAuthorities() {
            User user = fullBuilder().authority("USER").build();
            assertThat(user.getAuthorities().contains("USER")).isTrue();
        }
    }

    private UserBuilder fullBuilder() {
        return User.builder()
            .id(42L)
            .login("jhiLogin")
            .firstName("jhi")
            .lastName("pster")
            .email("totally.verified@email.com")
            .imageUrl("https://start.jhipster.tech/")
            .langKey("langKey")
            .createdBy("createdBy")
            .createdDate(Instant.EPOCH)
            .lastModifiedBy("lastModifiedBy")
            .lastModifiedDate(Instant.EPOCH);
    }
}
