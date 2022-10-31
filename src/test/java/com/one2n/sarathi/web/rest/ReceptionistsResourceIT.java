package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.Receptionists;
import com.one2n.sarathi.repository.ReceptionistsRepository;
import com.one2n.sarathi.service.dto.ReceptionistsDTO;
import com.one2n.sarathi.service.mapper.ReceptionistsMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ReceptionistsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReceptionistsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/receptionists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReceptionistsRepository receptionistsRepository;

    @Autowired
    private ReceptionistsMapper receptionistsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceptionistsMockMvc;

    private Receptionists receptionists;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receptionists createEntity(EntityManager em) {
        Receptionists receptionists = new Receptionists()
            .name(DEFAULT_NAME)
            .gender(DEFAULT_GENDER)
            .dob(DEFAULT_DOB)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .qualification(DEFAULT_QUALIFICATION);
        return receptionists;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receptionists createUpdatedEntity(EntityManager em) {
        Receptionists receptionists = new Receptionists()
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .dob(UPDATED_DOB)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .address(UPDATED_ADDRESS)
            .qualification(UPDATED_QUALIFICATION);
        return receptionists;
    }

    @BeforeEach
    public void initTest() {
        receptionists = createEntity(em);
    }

    @Test
    @Transactional
    void createReceptionists() throws Exception {
        int databaseSizeBeforeCreate = receptionistsRepository.findAll().size();
        // Create the Receptionists
        ReceptionistsDTO receptionistsDTO = receptionistsMapper.toDto(receptionists);
        restReceptionistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionistsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeCreate + 1);
        Receptionists testReceptionists = receptionistsList.get(receptionistsList.size() - 1);
        assertThat(testReceptionists.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReceptionists.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testReceptionists.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testReceptionists.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testReceptionists.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testReceptionists.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
    }

    @Test
    @Transactional
    void createReceptionistsWithExistingId() throws Exception {
        // Create the Receptionists with an existing ID
        receptionists.setId(1L);
        ReceptionistsDTO receptionistsDTO = receptionistsMapper.toDto(receptionists);

        int databaseSizeBeforeCreate = receptionistsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceptionistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionistsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReceptionists() throws Exception {
        // Initialize the database
        receptionistsRepository.saveAndFlush(receptionists);

        // Get all the receptionistsList
        restReceptionistsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receptionists.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION)));
    }

    @Test
    @Transactional
    void getReceptionists() throws Exception {
        // Initialize the database
        receptionistsRepository.saveAndFlush(receptionists);

        // Get the receptionists
        restReceptionistsMockMvc
            .perform(get(ENTITY_API_URL_ID, receptionists.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receptionists.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION));
    }

    @Test
    @Transactional
    void getNonExistingReceptionists() throws Exception {
        // Get the receptionists
        restReceptionistsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReceptionists() throws Exception {
        // Initialize the database
        receptionistsRepository.saveAndFlush(receptionists);

        int databaseSizeBeforeUpdate = receptionistsRepository.findAll().size();

        // Update the receptionists
        Receptionists updatedReceptionists = receptionistsRepository.findById(receptionists.getId()).get();
        // Disconnect from session so that the updates on updatedReceptionists are not directly saved in db
        em.detach(updatedReceptionists);
        updatedReceptionists
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .dob(UPDATED_DOB)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .address(UPDATED_ADDRESS)
            .qualification(UPDATED_QUALIFICATION);
        ReceptionistsDTO receptionistsDTO = receptionistsMapper.toDto(updatedReceptionists);

        restReceptionistsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receptionistsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receptionistsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeUpdate);
        Receptionists testReceptionists = receptionistsList.get(receptionistsList.size() - 1);
        assertThat(testReceptionists.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReceptionists.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testReceptionists.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testReceptionists.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testReceptionists.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testReceptionists.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void putNonExistingReceptionists() throws Exception {
        int databaseSizeBeforeUpdate = receptionistsRepository.findAll().size();
        receptionists.setId(count.incrementAndGet());

        // Create the Receptionists
        ReceptionistsDTO receptionistsDTO = receptionistsMapper.toDto(receptionists);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionistsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receptionistsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receptionistsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceptionists() throws Exception {
        int databaseSizeBeforeUpdate = receptionistsRepository.findAll().size();
        receptionists.setId(count.incrementAndGet());

        // Create the Receptionists
        ReceptionistsDTO receptionistsDTO = receptionistsMapper.toDto(receptionists);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionistsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receptionistsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceptionists() throws Exception {
        int databaseSizeBeforeUpdate = receptionistsRepository.findAll().size();
        receptionists.setId(count.incrementAndGet());

        // Create the Receptionists
        ReceptionistsDTO receptionistsDTO = receptionistsMapper.toDto(receptionists);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionistsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionistsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceptionistsWithPatch() throws Exception {
        // Initialize the database
        receptionistsRepository.saveAndFlush(receptionists);

        int databaseSizeBeforeUpdate = receptionistsRepository.findAll().size();

        // Update the receptionists using partial update
        Receptionists partialUpdatedReceptionists = new Receptionists();
        partialUpdatedReceptionists.setId(receptionists.getId());

        partialUpdatedReceptionists.name(UPDATED_NAME).dob(UPDATED_DOB).qualification(UPDATED_QUALIFICATION);

        restReceptionistsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceptionists.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceptionists))
            )
            .andExpect(status().isOk());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeUpdate);
        Receptionists testReceptionists = receptionistsList.get(receptionistsList.size() - 1);
        assertThat(testReceptionists.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReceptionists.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testReceptionists.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testReceptionists.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testReceptionists.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testReceptionists.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void fullUpdateReceptionistsWithPatch() throws Exception {
        // Initialize the database
        receptionistsRepository.saveAndFlush(receptionists);

        int databaseSizeBeforeUpdate = receptionistsRepository.findAll().size();

        // Update the receptionists using partial update
        Receptionists partialUpdatedReceptionists = new Receptionists();
        partialUpdatedReceptionists.setId(receptionists.getId());

        partialUpdatedReceptionists
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .dob(UPDATED_DOB)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .address(UPDATED_ADDRESS)
            .qualification(UPDATED_QUALIFICATION);

        restReceptionistsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceptionists.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceptionists))
            )
            .andExpect(status().isOk());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeUpdate);
        Receptionists testReceptionists = receptionistsList.get(receptionistsList.size() - 1);
        assertThat(testReceptionists.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReceptionists.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testReceptionists.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testReceptionists.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testReceptionists.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testReceptionists.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void patchNonExistingReceptionists() throws Exception {
        int databaseSizeBeforeUpdate = receptionistsRepository.findAll().size();
        receptionists.setId(count.incrementAndGet());

        // Create the Receptionists
        ReceptionistsDTO receptionistsDTO = receptionistsMapper.toDto(receptionists);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionistsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, receptionistsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receptionistsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceptionists() throws Exception {
        int databaseSizeBeforeUpdate = receptionistsRepository.findAll().size();
        receptionists.setId(count.incrementAndGet());

        // Create the Receptionists
        ReceptionistsDTO receptionistsDTO = receptionistsMapper.toDto(receptionists);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionistsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receptionistsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceptionists() throws Exception {
        int databaseSizeBeforeUpdate = receptionistsRepository.findAll().size();
        receptionists.setId(count.incrementAndGet());

        // Create the Receptionists
        ReceptionistsDTO receptionistsDTO = receptionistsMapper.toDto(receptionists);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionistsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receptionistsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receptionists in the database
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceptionists() throws Exception {
        // Initialize the database
        receptionistsRepository.saveAndFlush(receptionists);

        int databaseSizeBeforeDelete = receptionistsRepository.findAll().size();

        // Delete the receptionists
        restReceptionistsMockMvc
            .perform(delete(ENTITY_API_URL_ID, receptionists.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Receptionists> receptionistsList = receptionistsRepository.findAll();
        assertThat(receptionistsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
