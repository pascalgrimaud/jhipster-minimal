package io.github.jhipster.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.github.jhipster.JhipsterMinimalApp;
import io.github.jhipster.common.infrastructure.config.Constants;
import io.github.jhipster.user.application.UserApplicationService;
import io.github.jhipster.user.infrastructure.primary.dto.UserDTO;
import io.github.jhipster.user.infrastructure.secondary.UserEntity;
import io.github.jhipster.user.infrastructure.secondary.UserRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Integration tests for {@link UserApplicationService}.
 */
@SpringBootTest(classes = JhipsterMinimalApp.class)
@Transactional
class UserEntityServiceIT {

    private static final String DEFAULT_LOGIN = "johndoe";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";

    private static final String DEFAULT_LASTNAME = "doe";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

    private static final String DEFAULT_LANGKEY = "dummy";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private AuditingHandler auditingHandler;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    private UserEntity userEntity;

    @BeforeEach
    public void init() {
        userEntity = new UserEntity();
        userEntity.setLogin(DEFAULT_LOGIN);
        userEntity.setPassword(RandomStringUtils.random(60));
        userEntity.setActivated(true);
        userEntity.setEmail(DEFAULT_EMAIL);
        userEntity.setFirstName(DEFAULT_FIRSTNAME);
        userEntity.setLastName(DEFAULT_LASTNAME);
        userEntity.setImageUrl(DEFAULT_IMAGEURL);
        userEntity.setLangKey(DEFAULT_LANGKEY);

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
        auditingHandler.setDateTimeProvider(dateTimeProvider);
    }

    @Test
    @Transactional
    void assertThatUserMustExistToResetPassword() {
        userRepository.saveAndFlush(userEntity);
        Optional<UserEntity> maybeUser = userApplicationService.requestPasswordReset("invalid.login@localhost");
        assertThat(maybeUser).isNotPresent();

        maybeUser = userApplicationService.requestPasswordReset(userEntity.getEmail());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(maybeUser.orElse(null).getResetDate()).isNotNull();
        assertThat(maybeUser.orElse(null).getResetKey()).isNotNull();
    }

    @Test
    @Transactional
    void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        userEntity.setActivated(false);
        userRepository.saveAndFlush(userEntity);

        Optional<UserEntity> maybeUser = userApplicationService.requestPasswordReset(userEntity.getLogin());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(userEntity);
    }

    @Test
    @Transactional
    void assertThatResetKeyMustNotBeOlderThan24Hours() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        userEntity.setActivated(true);
        userEntity.setResetDate(daysAgo);
        userEntity.setResetKey(resetKey);
        userRepository.saveAndFlush(userEntity);

        Optional<UserEntity> maybeUser = userApplicationService.completePasswordReset("johndoe2", userEntity.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(userEntity);
    }

    @Test
    @Transactional
    void assertThatResetKeyMustBeValid() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        userEntity.setActivated(true);
        userEntity.setResetDate(daysAgo);
        userEntity.setResetKey("1234");
        userRepository.saveAndFlush(userEntity);

        Optional<UserEntity> maybeUser = userApplicationService.completePasswordReset("johndoe2", userEntity.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(userEntity);
    }

    @Test
    @Transactional
    void assertThatUserCanResetPassword() {
        String oldPassword = userEntity.getPassword();
        Instant daysAgo = Instant.now().minus(2, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        userEntity.setActivated(true);
        userEntity.setResetDate(daysAgo);
        userEntity.setResetKey(resetKey);
        userRepository.saveAndFlush(userEntity);

        Optional<UserEntity> maybeUser = userApplicationService.completePasswordReset("johndoe2", userEntity.getResetKey());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getResetDate()).isNull();
        assertThat(maybeUser.orElse(null).getResetKey()).isNull();
        assertThat(maybeUser.orElse(null).getPassword()).isNotEqualTo(oldPassword);

        userRepository.delete(userEntity);
    }

    @Test
    @Transactional
    void assertThatNotActivatedUsersWithNotNullActivationKeyCreatedBefore3DaysAreDeleted() {
        Instant now = Instant.now();
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(now.minus(4, ChronoUnit.DAYS)));
        userEntity.setActivated(false);
        userEntity.setActivationKey(RandomStringUtils.random(20));
        UserEntity dbUserEntity = userRepository.saveAndFlush(userEntity);
        dbUserEntity.setCreatedDate(now.minus(4, ChronoUnit.DAYS));
        userRepository.saveAndFlush(userEntity);
        Instant threeDaysAgo = now.minus(3, ChronoUnit.DAYS);
        List<UserEntity> userEntities = userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(threeDaysAgo);
        assertThat(userEntities).isNotEmpty();
        userApplicationService.removeNotActivatedUsers();
        userEntities = userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(threeDaysAgo);
        assertThat(userEntities).isEmpty();
    }

    @Test
    @Transactional
    void assertThatNotActivatedUsersWithNullActivationKeyCreatedBefore3DaysAreNotDeleted() {
        Instant now = Instant.now();
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(now.minus(4, ChronoUnit.DAYS)));
        userEntity.setActivated(false);
        UserEntity dbUserEntity = userRepository.saveAndFlush(userEntity);
        dbUserEntity.setCreatedDate(now.minus(4, ChronoUnit.DAYS));
        userRepository.saveAndFlush(userEntity);
        Instant threeDaysAgo = now.minus(3, ChronoUnit.DAYS);
        List<UserEntity> userEntities = userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(threeDaysAgo);
        assertThat(userEntities).isEmpty();
        userApplicationService.removeNotActivatedUsers();
        Optional<UserEntity> maybeDbUser = userRepository.findById(dbUserEntity.getId());
        assertThat(maybeDbUser).contains(dbUserEntity);
    }

    @Test
    @Transactional
    void assertThatAnonymousUserIsNotGet() {
        userEntity.setLogin(Constants.ANONYMOUS_USER);
        if (!userRepository.findOneByLogin(Constants.ANONYMOUS_USER).isPresent()) {
            userRepository.saveAndFlush(userEntity);
        }
        final PageRequest pageable = PageRequest.of(0, (int) userRepository.count());
        final Page<UserDTO> allManagedUsers = userApplicationService.getAllManagedUsers(pageable);
        assertThat(allManagedUsers.getContent().stream().noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin()))).isTrue();
    }
}
