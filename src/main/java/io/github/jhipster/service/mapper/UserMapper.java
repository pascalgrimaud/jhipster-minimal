package io.github.jhipster.service.mapper;

import io.github.jhipster.user.infrastructure.primary.dto.UserDTO;
import io.github.jhipster.user.infrastructure.secondary.AuthorityEntity;
import io.github.jhipster.user.infrastructure.secondary.UserEntity;
import java.util.*;
import java.util.stream.Collectors;
import org.mapstruct.*;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link UserEntity} and its DTO called {@link UserDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {

    public List<UserDTO> usersToUserDTOs(List<UserEntity> userEntities) {
        return userEntities.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(UserEntity userEntity) {
        return new UserDTO(userEntity);
    }

    public List<UserEntity> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
    }

    public UserEntity userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(userDTO.getId());
            userEntity.setLogin(userDTO.getLogin());
            userEntity.setFirstName(userDTO.getFirstName());
            userEntity.setLastName(userDTO.getLastName());
            userEntity.setEmail(userDTO.getEmail());
            userEntity.setImageUrl(userDTO.getImageUrl());
            userEntity.setActivated(userDTO.isActivated());
            userEntity.setLangKey(userDTO.getLangKey());
            Set<AuthorityEntity> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            userEntity.setAuthorities(authorities);
            return userEntity;
        }
    }

    private Set<AuthorityEntity> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<AuthorityEntity> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities =
                authoritiesAsString
                    .stream()
                    .map(
                        string -> {
                            AuthorityEntity auth = new AuthorityEntity();
                            auth.setName(string);
                            return auth;
                        }
                    )
                    .collect(Collectors.toSet());
        }

        return authorities;
    }

    public UserEntity userFromId(Long id) {
        if (id == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        return userEntity;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public UserDTO toDtoId(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(userEntity.getId());
        return userDto;
    }

    @Named("login")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public UserDTO toDtoLogin(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(userEntity.getId());
        userDto.setLogin(userEntity.getLogin());
        return userDto;
    }
}
