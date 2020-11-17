package io.github.jhipster.user.infrastructure.primary.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.jhipster.JhipsterMinimalApp;
import io.github.jhipster.common.infrastructure.primary.TestUtil;
import io.github.jhipster.security.AuthoritiesConstants;
import io.github.jhipster.service.mapper.UserMapper;
import io.github.jhipster.user.infrastructure.primary.dto.ManagedUserDTO;
import io.github.jhipster.user.infrastructure.primary.dto.UserDTO;
import io.github.jhipster.user.infrastructure.primary.rest.UserResource;
import io.github.jhipster.user.infrastructure.secondary.AuthorityEntity;
import io.github.jhipster.user.infrastructure.secondary.UserEntity;
import io.github.jhipster.user.infrastructure.secondary.UserRepository;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@SpringBootTest(classes = JhipsterMinimalApp.class)
class UserEntityResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "jhipster";

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_PASSWORD = "passjohndoe";
    private static final String UPDATED_PASSWORD = "passjhipster";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MockMvc restUserMockMvc;

    private UserEntity userEntity;

    @BeforeEach
    public void setup() {
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).clear();
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).clear();
    }

    /**
     * Create a User.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static UserEntity createEntity(EntityManager em) {
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        userEntity.setPassword(RandomStringUtils.random(60));
        userEntity.setActivated(true);
        userEntity.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        userEntity.setFirstName(DEFAULT_FIRSTNAME);
        userEntity.setLastName(DEFAULT_LASTNAME);
        userEntity.setImageUrl(DEFAULT_IMAGEURL);
        userEntity.setLangKey(DEFAULT_LANGKEY);
        return userEntity;
    }

    @BeforeEach
    public void initTest() {
        userEntity = createEntity(em);
        userEntity.setLogin(DEFAULT_LOGIN);
        userEntity.setEmail(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin(DEFAULT_LOGIN);
        managedUserDTO.setPassword(DEFAULT_PASSWORD);
        managedUserDTO.setFirstName(DEFAULT_FIRSTNAME);
        managedUserDTO.setLastName(DEFAULT_LASTNAME);
        managedUserDTO.setEmail(DEFAULT_EMAIL);
        managedUserDTO.setActivated(true);
        managedUserDTO.setImageUrl(DEFAULT_IMAGEURL);
        managedUserDTO.setLangKey(DEFAULT_LANGKEY);
        managedUserDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserDTO)))
            .andExpect(status().isCreated());

        // Validate the User in the database
        assertPersistedUsers(
            users -> {
                assertThat(users).hasSize(databaseSizeBeforeCreate + 1);
                UserEntity testUserEntity = users.get(users.size() - 1);
                assertThat(testUserEntity.getLogin()).isEqualTo(DEFAULT_LOGIN);
                assertThat(testUserEntity.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
                assertThat(testUserEntity.getLastName()).isEqualTo(DEFAULT_LASTNAME);
                assertThat(testUserEntity.getEmail()).isEqualTo(DEFAULT_EMAIL);
                assertThat(testUserEntity.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
                assertThat(testUserEntity.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
            }
        );
    }

    @Test
    @Transactional
    void createUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setId(1L);
        managedUserDTO.setLogin(DEFAULT_LOGIN);
        managedUserDTO.setPassword(DEFAULT_PASSWORD);
        managedUserDTO.setFirstName(DEFAULT_FIRSTNAME);
        managedUserDTO.setLastName(DEFAULT_LASTNAME);
        managedUserDTO.setEmail(DEFAULT_EMAIL);
        managedUserDTO.setActivated(true);
        managedUserDTO.setImageUrl(DEFAULT_IMAGEURL);
        managedUserDTO.setLangKey(DEFAULT_LANGKEY);
        managedUserDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc
            .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void createUserWithExistingLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userEntity);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin(DEFAULT_LOGIN); // this login should already be used
        managedUserDTO.setPassword(DEFAULT_PASSWORD);
        managedUserDTO.setFirstName(DEFAULT_FIRSTNAME);
        managedUserDTO.setLastName(DEFAULT_LASTNAME);
        managedUserDTO.setEmail("anothermail@localhost");
        managedUserDTO.setActivated(true);
        managedUserDTO.setImageUrl(DEFAULT_IMAGEURL);
        managedUserDTO.setLangKey(DEFAULT_LANGKEY);
        managedUserDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // Create the User
        restUserMockMvc
            .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void createUserWithExistingEmail() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userEntity);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin("anotherlogin");
        managedUserDTO.setPassword(DEFAULT_PASSWORD);
        managedUserDTO.setFirstName(DEFAULT_FIRSTNAME);
        managedUserDTO.setLastName(DEFAULT_LASTNAME);
        managedUserDTO.setEmail(DEFAULT_EMAIL); // this email should already be used
        managedUserDTO.setActivated(true);
        managedUserDTO.setImageUrl(DEFAULT_IMAGEURL);
        managedUserDTO.setLangKey(DEFAULT_LANGKEY);
        managedUserDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // Create the User
        restUserMockMvc
            .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userEntity);

        // Get all the users
        restUserMockMvc
            .perform(get("/api/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGEURL)))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANGKEY)));
    }

    @Test
    @Transactional
    void getAllUsersSortedByParameters() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userEntity);

        restUserMockMvc.perform(get("/api/users?sort=resetKey,desc").accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
        restUserMockMvc.perform(get("/api/users?sort=password,desc").accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
        restUserMockMvc
            .perform(get("/api/users?sort=resetKey,id,desc").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
        restUserMockMvc.perform(get("/api/users?sort=id,desc").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userEntity);

        assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(userEntity.getLogin())).isNull();

        // Get the user
        restUserMockMvc
            .perform(get("/api/users/{login}", userEntity.getLogin()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.login").value(userEntity.getLogin()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGEURL))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANGKEY));

        assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(userEntity.getLogin())).isNotNull();
    }

    @Test
    @Transactional
    void getNonExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userEntity);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        UserEntity updatedUserEntity = userRepository.findById(userEntity.getId()).get();

        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setId(updatedUserEntity.getId());
        managedUserDTO.setLogin(updatedUserEntity.getLogin());
        managedUserDTO.setPassword(UPDATED_PASSWORD);
        managedUserDTO.setFirstName(UPDATED_FIRSTNAME);
        managedUserDTO.setLastName(UPDATED_LASTNAME);
        managedUserDTO.setEmail(UPDATED_EMAIL);
        managedUserDTO.setActivated(updatedUserEntity.isActivated());
        managedUserDTO.setImageUrl(UPDATED_IMAGEURL);
        managedUserDTO.setLangKey(UPDATED_LANGKEY);
        managedUserDTO.setCreatedBy(updatedUserEntity.getCreatedBy());
        managedUserDTO.setCreatedDate(updatedUserEntity.getCreatedDate());
        managedUserDTO.setLastModifiedBy(updatedUserEntity.getLastModifiedBy());
        managedUserDTO.setLastModifiedDate(updatedUserEntity.getLastModifiedDate());
        managedUserDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserDTO)))
            .andExpect(status().isOk());

        // Validate the User in the database
        assertPersistedUsers(
            users -> {
                assertThat(users).hasSize(databaseSizeBeforeUpdate);
                UserEntity testUserEntity = users.stream().filter(usr -> usr.getId().equals(updatedUserEntity.getId())).findFirst().get();
                assertThat(testUserEntity.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
                assertThat(testUserEntity.getLastName()).isEqualTo(UPDATED_LASTNAME);
                assertThat(testUserEntity.getEmail()).isEqualTo(UPDATED_EMAIL);
                assertThat(testUserEntity.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
                assertThat(testUserEntity.getLangKey()).isEqualTo(UPDATED_LANGKEY);
            }
        );
    }

    @Test
    @Transactional
    void updateUserLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userEntity);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        UserEntity updatedUserEntity = userRepository.findById(userEntity.getId()).get();

        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setId(updatedUserEntity.getId());
        managedUserDTO.setLogin(UPDATED_LOGIN);
        managedUserDTO.setPassword(UPDATED_PASSWORD);
        managedUserDTO.setFirstName(UPDATED_FIRSTNAME);
        managedUserDTO.setLastName(UPDATED_LASTNAME);
        managedUserDTO.setEmail(UPDATED_EMAIL);
        managedUserDTO.setActivated(updatedUserEntity.isActivated());
        managedUserDTO.setImageUrl(UPDATED_IMAGEURL);
        managedUserDTO.setLangKey(UPDATED_LANGKEY);
        managedUserDTO.setCreatedBy(updatedUserEntity.getCreatedBy());
        managedUserDTO.setCreatedDate(updatedUserEntity.getCreatedDate());
        managedUserDTO.setLastModifiedBy(updatedUserEntity.getLastModifiedBy());
        managedUserDTO.setLastModifiedDate(updatedUserEntity.getLastModifiedDate());
        managedUserDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserDTO)))
            .andExpect(status().isOk());

        // Validate the User in the database
        assertPersistedUsers(
            users -> {
                assertThat(users).hasSize(databaseSizeBeforeUpdate);
                UserEntity testUserEntity = users.stream().filter(usr -> usr.getId().equals(updatedUserEntity.getId())).findFirst().get();
                assertThat(testUserEntity.getLogin()).isEqualTo(UPDATED_LOGIN);
                assertThat(testUserEntity.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
                assertThat(testUserEntity.getLastName()).isEqualTo(UPDATED_LASTNAME);
                assertThat(testUserEntity.getEmail()).isEqualTo(UPDATED_EMAIL);
                assertThat(testUserEntity.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
                assertThat(testUserEntity.getLangKey()).isEqualTo(UPDATED_LANGKEY);
            }
        );
    }

    @Test
    @Transactional
    void updateUserExistingEmail() throws Exception {
        // Initialize the database with 2 users
        userRepository.saveAndFlush(userEntity);

        UserEntity anotherUserEntity = new UserEntity();
        anotherUserEntity.setLogin("jhipster");
        anotherUserEntity.setPassword(RandomStringUtils.random(60));
        anotherUserEntity.setActivated(true);
        anotherUserEntity.setEmail("jhipster@localhost");
        anotherUserEntity.setFirstName("java");
        anotherUserEntity.setLastName("hipster");
        anotherUserEntity.setImageUrl("");
        anotherUserEntity.setLangKey("en");
        userRepository.saveAndFlush(anotherUserEntity);

        // Update the user
        UserEntity updatedUserEntity = userRepository.findById(userEntity.getId()).get();

        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setId(updatedUserEntity.getId());
        managedUserDTO.setLogin(updatedUserEntity.getLogin());
        managedUserDTO.setPassword(updatedUserEntity.getPassword());
        managedUserDTO.setFirstName(updatedUserEntity.getFirstName());
        managedUserDTO.setLastName(updatedUserEntity.getLastName());
        managedUserDTO.setEmail("jhipster@localhost"); // this email should already be used by anotherUser
        managedUserDTO.setActivated(updatedUserEntity.isActivated());
        managedUserDTO.setImageUrl(updatedUserEntity.getImageUrl());
        managedUserDTO.setLangKey(updatedUserEntity.getLangKey());
        managedUserDTO.setCreatedBy(updatedUserEntity.getCreatedBy());
        managedUserDTO.setCreatedDate(updatedUserEntity.getCreatedDate());
        managedUserDTO.setLastModifiedBy(updatedUserEntity.getLastModifiedBy());
        managedUserDTO.setLastModifiedDate(updatedUserEntity.getLastModifiedDate());
        managedUserDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void updateUserExistingLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userEntity);

        UserEntity anotherUserEntity = new UserEntity();
        anotherUserEntity.setLogin("jhipster");
        anotherUserEntity.setPassword(RandomStringUtils.random(60));
        anotherUserEntity.setActivated(true);
        anotherUserEntity.setEmail("jhipster@localhost");
        anotherUserEntity.setFirstName("java");
        anotherUserEntity.setLastName("hipster");
        anotherUserEntity.setImageUrl("");
        anotherUserEntity.setLangKey("en");
        userRepository.saveAndFlush(anotherUserEntity);

        // Update the user
        UserEntity updatedUserEntity = userRepository.findById(userEntity.getId()).get();

        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setId(updatedUserEntity.getId());
        managedUserDTO.setLogin("jhipster"); // this login should already be used by anotherUser
        managedUserDTO.setPassword(updatedUserEntity.getPassword());
        managedUserDTO.setFirstName(updatedUserEntity.getFirstName());
        managedUserDTO.setLastName(updatedUserEntity.getLastName());
        managedUserDTO.setEmail(updatedUserEntity.getEmail());
        managedUserDTO.setActivated(updatedUserEntity.isActivated());
        managedUserDTO.setImageUrl(updatedUserEntity.getImageUrl());
        managedUserDTO.setLangKey(updatedUserEntity.getLangKey());
        managedUserDTO.setCreatedBy(updatedUserEntity.getCreatedBy());
        managedUserDTO.setCreatedDate(updatedUserEntity.getCreatedDate());
        managedUserDTO.setLastModifiedBy(updatedUserEntity.getLastModifiedBy());
        managedUserDTO.setLastModifiedDate(updatedUserEntity.getLastModifiedDate());
        managedUserDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userEntity);
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        restUserMockMvc
            .perform(delete("/api/users/{login}", userEntity.getLogin()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(userEntity.getLogin())).isNull();

        // Validate the database is empty
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeDelete - 1));
    }

    @Test
    @Transactional
    void getAllAuthorities() throws Exception {
        restUserMockMvc
            .perform(get("/api/users/authorities").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").value(hasItems(AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN)));
    }

    @Test
    void testUserEquals() throws Exception {
        TestUtil.equalsVerifier(UserEntity.class);
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1L);
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(userEntity1.getId());
        assertThat(userEntity1).isEqualTo(userEntity2);
        userEntity2.setId(2L);
        assertThat(userEntity1).isNotEqualTo(userEntity2);
        userEntity1.setId(null);
        assertThat(userEntity1).isNotEqualTo(userEntity2);
    }

    @Test
    void testUserDTOtoUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setCreatedBy(DEFAULT_LOGIN);
        userDTO.setLastModifiedBy(DEFAULT_LOGIN);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        UserEntity userEntity = userMapper.userDTOToUser(userDTO);
        assertThat(userEntity.getId()).isEqualTo(DEFAULT_ID);
        assertThat(userEntity.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userEntity.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userEntity.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userEntity.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userEntity.isActivated()).isTrue();
        assertThat(userEntity.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userEntity.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userEntity.getCreatedBy()).isNull();
        assertThat(userEntity.getCreatedDate()).isNotNull();
        assertThat(userEntity.getLastModifiedBy()).isNull();
        assertThat(userEntity.getLastModifiedDate()).isNotNull();
        assertThat(userEntity.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    void testUserToUserDTO() {
        userEntity.setId(DEFAULT_ID);
        userEntity.setCreatedBy(DEFAULT_LOGIN);
        userEntity.setCreatedDate(Instant.now());
        userEntity.setLastModifiedBy(DEFAULT_LOGIN);
        userEntity.setLastModifiedDate(Instant.now());
        Set<AuthorityEntity> authorities = new HashSet<>();
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setName(AuthoritiesConstants.USER);
        authorities.add(authorityEntity);
        userEntity.setAuthorities(authorities);

        UserDTO userDTO = userMapper.userToUserDTO(userEntity);

        assertThat(userDTO.getId()).isEqualTo(DEFAULT_ID);
        assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.isActivated()).isTrue();
        assertThat(userDTO.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userDTO.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userDTO.getCreatedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getCreatedDate()).isEqualTo(userEntity.getCreatedDate());
        assertThat(userDTO.getLastModifiedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(userEntity.getLastModifiedDate());
        assertThat(userDTO.getAuthorities()).containsExactly(AuthoritiesConstants.USER);
        assertThat(userDTO.toString()).isNotNull();
    }

    @Test
    void testAuthorityEquals() {
        AuthorityEntity authorityEntityA = new AuthorityEntity();
        assertThat(authorityEntityA).isNotEqualTo(null).isNotEqualTo(new Object());
        assertThat(authorityEntityA.hashCode()).isZero();
        assertThat(authorityEntityA.toString()).isNotNull();

        AuthorityEntity authorityEntityB = new AuthorityEntity();
        assertThat(authorityEntityA).isEqualTo(authorityEntityB);

        authorityEntityB.setName(AuthoritiesConstants.ADMIN);
        assertThat(authorityEntityA).isNotEqualTo(authorityEntityB);

        authorityEntityA.setName(AuthoritiesConstants.USER);
        assertThat(authorityEntityA).isNotEqualTo(authorityEntityB);

        authorityEntityB.setName(AuthoritiesConstants.USER);
        assertThat(authorityEntityA).isEqualTo(authorityEntityB).hasSameHashCodeAs(authorityEntityB);
    }

    private void assertPersistedUsers(Consumer<List<UserEntity>> userAssertion) {
        userAssertion.accept(userRepository.findAll());
    }
}
