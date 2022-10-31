package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.SarathiUsers;
import com.one2n.sarathi.repository.SarathiUsersRepository;
import com.one2n.sarathi.service.dto.SarathiUsersDTO;
import com.one2n.sarathi.service.mapper.SarathiUsersMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SarathiUsersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SarathiUsersResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_USER_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_USER_TYPE_ID = 1;
    private static final Integer UPDATED_USER_TYPE_ID = 2;

    private static final String DEFAULT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DISABLED = false;
    private static final Boolean UPDATED_IS_DISABLED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sarathi-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SarathiUsersRepository sarathiUsersRepository;

    @Autowired
    private SarathiUsersMapper sarathiUsersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSarathiUsersMockMvc;

    private SarathiUsers sarathiUsers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SarathiUsers createEntity(EntityManager em) {
        SarathiUsers sarathiUsers = new SarathiUsers()
            .username(DEFAULT_USERNAME)
            .userType(DEFAULT_USER_TYPE)
            .userTypeId(DEFAULT_USER_TYPE_ID)
            .emailId(DEFAULT_EMAIL_ID)
            .password(DEFAULT_PASSWORD)
            .isDisabled(DEFAULT_IS_DISABLED)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return sarathiUsers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SarathiUsers createUpdatedEntity(EntityManager em) {
        SarathiUsers sarathiUsers = new SarathiUsers()
            .username(UPDATED_USERNAME)
            .userType(UPDATED_USER_TYPE)
            .userTypeId(UPDATED_USER_TYPE_ID)
            .emailId(UPDATED_EMAIL_ID)
            .password(UPDATED_PASSWORD)
            .isDisabled(UPDATED_IS_DISABLED)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return sarathiUsers;
    }

    @BeforeEach
    public void initTest() {
        sarathiUsers = createEntity(em);
    }

    @Test
    @Transactional
    void createSarathiUsers() throws Exception {
        int databaseSizeBeforeCreate = sarathiUsersRepository.findAll().size();
        // Create the SarathiUsers
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);
        restSarathiUsersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeCreate + 1);
        SarathiUsers testSarathiUsers = sarathiUsersList.get(sarathiUsersList.size() - 1);
        assertThat(testSarathiUsers.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testSarathiUsers.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
        assertThat(testSarathiUsers.getUserTypeId()).isEqualTo(DEFAULT_USER_TYPE_ID);
        assertThat(testSarathiUsers.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testSarathiUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testSarathiUsers.getIsDisabled()).isEqualTo(DEFAULT_IS_DISABLED);
        assertThat(testSarathiUsers.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSarathiUsers.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createSarathiUsersWithExistingId() throws Exception {
        // Create the SarathiUsers with an existing ID
        sarathiUsers.setId(1L);
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        int databaseSizeBeforeCreate = sarathiUsersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSarathiUsersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sarathiUsersRepository.findAll().size();
        // set the field null
        sarathiUsers.setUsername(null);

        // Create the SarathiUsers, which fails.
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        restSarathiUsersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sarathiUsersRepository.findAll().size();
        // set the field null
        sarathiUsers.setEmailId(null);

        // Create the SarathiUsers, which fails.
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        restSarathiUsersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = sarathiUsersRepository.findAll().size();
        // set the field null
        sarathiUsers.setPassword(null);

        // Create the SarathiUsers, which fails.
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        restSarathiUsersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSarathiUsers() throws Exception {
        // Initialize the database
        sarathiUsersRepository.saveAndFlush(sarathiUsers);

        // Get all the sarathiUsersList
        restSarathiUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sarathiUsers.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE)))
            .andExpect(jsonPath("$.[*].userTypeId").value(hasItem(DEFAULT_USER_TYPE_ID)))
            .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].isDisabled").value(hasItem(DEFAULT_IS_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getSarathiUsers() throws Exception {
        // Initialize the database
        sarathiUsersRepository.saveAndFlush(sarathiUsers);

        // Get the sarathiUsers
        restSarathiUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, sarathiUsers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sarathiUsers.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE))
            .andExpect(jsonPath("$.userTypeId").value(DEFAULT_USER_TYPE_ID))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.isDisabled").value(DEFAULT_IS_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSarathiUsers() throws Exception {
        // Get the sarathiUsers
        restSarathiUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSarathiUsers() throws Exception {
        // Initialize the database
        sarathiUsersRepository.saveAndFlush(sarathiUsers);

        int databaseSizeBeforeUpdate = sarathiUsersRepository.findAll().size();

        // Update the sarathiUsers
        SarathiUsers updatedSarathiUsers = sarathiUsersRepository.findById(sarathiUsers.getId()).get();
        // Disconnect from session so that the updates on updatedSarathiUsers are not directly saved in db
        em.detach(updatedSarathiUsers);
        updatedSarathiUsers
            .username(UPDATED_USERNAME)
            .userType(UPDATED_USER_TYPE)
            .userTypeId(UPDATED_USER_TYPE_ID)
            .emailId(UPDATED_EMAIL_ID)
            .password(UPDATED_PASSWORD)
            .isDisabled(UPDATED_IS_DISABLED)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(updatedSarathiUsers);

        restSarathiUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sarathiUsersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isOk());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeUpdate);
        SarathiUsers testSarathiUsers = sarathiUsersList.get(sarathiUsersList.size() - 1);
        assertThat(testSarathiUsers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSarathiUsers.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testSarathiUsers.getUserTypeId()).isEqualTo(UPDATED_USER_TYPE_ID);
        assertThat(testSarathiUsers.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testSarathiUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSarathiUsers.getIsDisabled()).isEqualTo(UPDATED_IS_DISABLED);
        assertThat(testSarathiUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSarathiUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSarathiUsers() throws Exception {
        int databaseSizeBeforeUpdate = sarathiUsersRepository.findAll().size();
        sarathiUsers.setId(count.incrementAndGet());

        // Create the SarathiUsers
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSarathiUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sarathiUsersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSarathiUsers() throws Exception {
        int databaseSizeBeforeUpdate = sarathiUsersRepository.findAll().size();
        sarathiUsers.setId(count.incrementAndGet());

        // Create the SarathiUsers
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSarathiUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSarathiUsers() throws Exception {
        int databaseSizeBeforeUpdate = sarathiUsersRepository.findAll().size();
        sarathiUsers.setId(count.incrementAndGet());

        // Create the SarathiUsers
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSarathiUsersMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSarathiUsersWithPatch() throws Exception {
        // Initialize the database
        sarathiUsersRepository.saveAndFlush(sarathiUsers);

        int databaseSizeBeforeUpdate = sarathiUsersRepository.findAll().size();

        // Update the sarathiUsers using partial update
        SarathiUsers partialUpdatedSarathiUsers = new SarathiUsers();
        partialUpdatedSarathiUsers.setId(sarathiUsers.getId());

        partialUpdatedSarathiUsers
            .username(UPDATED_USERNAME)
            .userType(UPDATED_USER_TYPE)
            .emailId(UPDATED_EMAIL_ID)
            .password(UPDATED_PASSWORD)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSarathiUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSarathiUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSarathiUsers))
            )
            .andExpect(status().isOk());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeUpdate);
        SarathiUsers testSarathiUsers = sarathiUsersList.get(sarathiUsersList.size() - 1);
        assertThat(testSarathiUsers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSarathiUsers.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testSarathiUsers.getUserTypeId()).isEqualTo(DEFAULT_USER_TYPE_ID);
        assertThat(testSarathiUsers.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testSarathiUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSarathiUsers.getIsDisabled()).isEqualTo(DEFAULT_IS_DISABLED);
        assertThat(testSarathiUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSarathiUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSarathiUsersWithPatch() throws Exception {
        // Initialize the database
        sarathiUsersRepository.saveAndFlush(sarathiUsers);

        int databaseSizeBeforeUpdate = sarathiUsersRepository.findAll().size();

        // Update the sarathiUsers using partial update
        SarathiUsers partialUpdatedSarathiUsers = new SarathiUsers();
        partialUpdatedSarathiUsers.setId(sarathiUsers.getId());

        partialUpdatedSarathiUsers
            .username(UPDATED_USERNAME)
            .userType(UPDATED_USER_TYPE)
            .userTypeId(UPDATED_USER_TYPE_ID)
            .emailId(UPDATED_EMAIL_ID)
            .password(UPDATED_PASSWORD)
            .isDisabled(UPDATED_IS_DISABLED)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSarathiUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSarathiUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSarathiUsers))
            )
            .andExpect(status().isOk());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeUpdate);
        SarathiUsers testSarathiUsers = sarathiUsersList.get(sarathiUsersList.size() - 1);
        assertThat(testSarathiUsers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSarathiUsers.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testSarathiUsers.getUserTypeId()).isEqualTo(UPDATED_USER_TYPE_ID);
        assertThat(testSarathiUsers.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testSarathiUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSarathiUsers.getIsDisabled()).isEqualTo(UPDATED_IS_DISABLED);
        assertThat(testSarathiUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSarathiUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSarathiUsers() throws Exception {
        int databaseSizeBeforeUpdate = sarathiUsersRepository.findAll().size();
        sarathiUsers.setId(count.incrementAndGet());

        // Create the SarathiUsers
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSarathiUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sarathiUsersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSarathiUsers() throws Exception {
        int databaseSizeBeforeUpdate = sarathiUsersRepository.findAll().size();
        sarathiUsers.setId(count.incrementAndGet());

        // Create the SarathiUsers
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSarathiUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSarathiUsers() throws Exception {
        int databaseSizeBeforeUpdate = sarathiUsersRepository.findAll().size();
        sarathiUsers.setId(count.incrementAndGet());

        // Create the SarathiUsers
        SarathiUsersDTO sarathiUsersDTO = sarathiUsersMapper.toDto(sarathiUsers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSarathiUsersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sarathiUsersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SarathiUsers in the database
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSarathiUsers() throws Exception {
        // Initialize the database
        sarathiUsersRepository.saveAndFlush(sarathiUsers);

        int databaseSizeBeforeDelete = sarathiUsersRepository.findAll().size();

        // Delete the sarathiUsers
        restSarathiUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, sarathiUsers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SarathiUsers> sarathiUsersList = sarathiUsersRepository.findAll();
        assertThat(sarathiUsersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
