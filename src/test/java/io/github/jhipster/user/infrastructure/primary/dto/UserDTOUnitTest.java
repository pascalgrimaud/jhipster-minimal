package io.github.jhipster.user.infrastructure.primary.dto;

import io.github.jhipster.user.domain.User;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;

import static io.github.jhipster.user.domain.User.toDomain;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserDTOUnitTest {

    @Test
    void shouldConvertMinimalDTOToDomain() {
        UserDTO dto = new UserDTO();
        dto.setLogin("Jhipster");
        dto.setEmail("pascal.grimaud@love.jhipster.com");
        dto.setLangKey("FR");

        User user = toDomain(dto);

        assertThat(user).isExactlyInstanceOf(User.class);
        assertThat(user.getLogin()).isEqualTo("Jhipster");
        assertThat(user.getEmail()).isEqualTo("pascal.grimaud@love.jhipster.com");
        assertThat(user.getLangKey()).isEqualTo("FR");

        assertThat(user.getFirstName()).isNull();
        assertThat(user.getLastName()).isNull();
        assertThat(user.getImageUrl()).isNull();
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getCreatedDate()).isNull();
        assertThat(user.getLastModifiedBy()).isNull();
        assertThat(user.getLastModifiedDate()).isNull();
        assertThat(user.getAuthorities()).isExactlyInstanceOf(HashSet.class);
        assertThat(user.getAuthorities().size()).isEqualTo(0);
        assertThat(user.isActivated()).isEqualTo(false);
    }

    @Test
    void shouldConvertFullDTOToDomain() {
        Date createdDate = new Date();
        Date lastModifiedDate = new Date();
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("Jhipster");
        userDTO.setEmail("pascal.grimaud@love.jhipster.com");
        userDTO.setLangKey("FR");
        userDTO.setLastName("GRIMAUD");
        userDTO.setFirstName("Pascal");
        userDTO.setActivated(true);
        userDTO.setCreatedBy("admin");
        userDTO.setCreatedDate(createdDate.toInstant());
        userDTO.setLastModifiedBy("admin");
        userDTO.setLastModifiedDate(lastModifiedDate.toInstant());
        userDTO.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5b/Rick_Astely.jpg/330px-Rick_Astely.jpg");
        userDTO.setAuthorities(new HashSet<>());

        User user = toDomain(userDTO);

        assertThat(user).isExactlyInstanceOf(User.class);
        assertThat(user.getLogin()).isEqualTo("Jhipster");
        assertThat(user.getEmail()).isEqualTo("pascal.grimaud@love.jhipster.com");
        assertThat(user.getLangKey()).isEqualTo("FR");
        assertThat(user.getLastName()).isEqualTo("GRIMAUD");
        assertThat(user.getFirstName()).isEqualTo("Pascal");
        assertThat(user.isActivated()).isTrue();
        assertThat(user.getCreatedBy()).isEqualTo("admin");
        assertThat(user.getCreatedDate()).isEqualTo(createdDate.toInstant());
        assertThat(user.getLastModifiedBy()).isEqualTo("admin");
        assertThat(user.getLastModifiedDate()).isEqualTo(lastModifiedDate.toInstant());
        assertThat(user.getImageUrl()).isEqualTo("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5b/Rick_Astely.jpg/330px-Rick_Astely.jpg");
        assertThat(user.getAuthorities().size()).isEqualTo(0);
    }
}
