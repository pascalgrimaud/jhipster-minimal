package io.github.jhipster.user.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jhipster.user.infrastructure.primary.dto.UserDTO;
import io.github.jhipster.user.infrastructure.secondary.database.UserEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link UserMapper}.
 */
class UserEntityMapperTest {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final Long DEFAULT_ID = 1L;

    private UserMapper userMapper;
    private UserEntity userEntity;
    private UserDTO userDto;

    @BeforeEach
    public void init() {
        userMapper = new UserMapper();
        userEntity = new UserEntity();
        userEntity.setLogin(DEFAULT_LOGIN);
        userEntity.setPassword(RandomStringUtils.random(60));
        userEntity.setActivated(true);
        userEntity.setEmail("johndoe@localhost");
        userEntity.setFirstName("john");
        userEntity.setLastName("doe");
        userEntity.setImageUrl("image_url");
        userEntity.setLangKey("en");

        userDto = new UserDTO(userEntity);
    }

    @Test
    void usersToUserDTOsShouldMapOnlyNonNullUsers() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity);
        userEntities.add(null);

        List<UserDTO> userDTOS = userMapper.usersToUserDTOs(userEntities);

        assertThat(userDTOS).isNotEmpty().size().isEqualTo(1);
    }

    @Test
    void userDTOsToUsersShouldMapOnlyNonNullUsers() {
        List<UserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);
        usersDto.add(null);

        List<UserEntity> userEntities = userMapper.userDTOsToUsers(usersDto);

        assertThat(userEntities).isNotEmpty().size().isEqualTo(1);
    }

    @Test
    void userDTOsToUsersWithAuthoritiesStringShouldMapToUsersWithAuthoritiesDomain() {
        Set<String> authoritiesAsString = new HashSet<>();
        authoritiesAsString.add("ADMIN");
        userDto.setAuthorities(authoritiesAsString);

        List<UserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);

        List<UserEntity> userEntities = userMapper.userDTOsToUsers(usersDto);

        assertThat(userEntities).isNotEmpty().size().isEqualTo(1);
        assertThat(userEntities.get(0).getAuthorities()).isNotNull();
        assertThat(userEntities.get(0).getAuthorities()).isNotEmpty();
        assertThat(userEntities.get(0).getAuthorities().iterator().next().getName()).isEqualTo("ADMIN");
    }

    @Test
    void userDTOsToUsersMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
        userDto.setAuthorities(null);

        List<UserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);

        List<UserEntity> userEntities = userMapper.userDTOsToUsers(usersDto);

        assertThat(userEntities).isNotEmpty().size().isEqualTo(1);
        assertThat(userEntities.get(0).getAuthorities()).isNotNull();
        assertThat(userEntities.get(0).getAuthorities()).isEmpty();
    }

    @Test
    void userDTOToUserMapWithAuthoritiesStringShouldReturnUserWithAuthorities() {
        Set<String> authoritiesAsString = new HashSet<>();
        authoritiesAsString.add("ADMIN");
        userDto.setAuthorities(authoritiesAsString);

        UserEntity userEntity = userMapper.userDTOToUser(userDto);

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getAuthorities()).isNotNull();
        assertThat(userEntity.getAuthorities()).isNotEmpty();
        assertThat(userEntity.getAuthorities().iterator().next().getName()).isEqualTo("ADMIN");
    }

    @Test
    void userDTOToUserMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
        userDto.setAuthorities(null);

        UserEntity userEntity = userMapper.userDTOToUser(userDto);

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getAuthorities()).isNotNull();
        assertThat(userEntity.getAuthorities()).isEmpty();
    }

    @Test
    void userDTOToUserMapWithNullUserShouldReturnNull() {
        assertThat(userMapper.userDTOToUser(null)).isNull();
    }

    @Test
    void testUserFromId() {
        assertThat(userMapper.userFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(userMapper.userFromId(null)).isNull();
    }
}
