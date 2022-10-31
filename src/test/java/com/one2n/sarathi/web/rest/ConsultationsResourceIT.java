package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.Consultations;
import com.one2n.sarathi.repository.ConsultationsRepository;
import com.one2n.sarathi.service.dto.ConsultationsDTO;
import com.one2n.sarathi.service.mapper.ConsultationsMapper;
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
 * Integration tests for the {@link ConsultationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsultationsResourceIT {

    private static final Instant DEFAULT_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/consultations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsultationsRepository consultationsRepository;

    @Autowired
    private ConsultationsMapper consultationsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultationsMockMvc;

    private Consultations consultations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultations createEntity(EntityManager em) {
        Consultations consultations = new Consultations().inTime(DEFAULT_IN_TIME).outTime(DEFAULT_OUT_TIME);
        return consultations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultations createUpdatedEntity(EntityManager em) {
        Consultations consultations = new Consultations().inTime(UPDATED_IN_TIME).outTime(UPDATED_OUT_TIME);
        return consultations;
    }

    @BeforeEach
    public void initTest() {
        consultations = createEntity(em);
    }

    @Test
    @Transactional
    void createConsultations() throws Exception {
        int databaseSizeBeforeCreate = consultationsRepository.findAll().size();
        // Create the Consultations
        ConsultationsDTO consultationsDTO = consultationsMapper.toDto(consultations);
        restConsultationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeCreate + 1);
        Consultations testConsultations = consultationsList.get(consultationsList.size() - 1);
        assertThat(testConsultations.getInTime()).isEqualTo(DEFAULT_IN_TIME);
        assertThat(testConsultations.getOutTime()).isEqualTo(DEFAULT_OUT_TIME);
    }

    @Test
    @Transactional
    void createConsultationsWithExistingId() throws Exception {
        // Create the Consultations with an existing ID
        consultations.setId(1L);
        ConsultationsDTO consultationsDTO = consultationsMapper.toDto(consultations);

        int databaseSizeBeforeCreate = consultationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsultations() throws Exception {
        // Initialize the database
        consultationsRepository.saveAndFlush(consultations);

        // Get all the consultationsList
        restConsultationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultations.getId().intValue())))
            .andExpect(jsonPath("$.[*].inTime").value(hasItem(DEFAULT_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].outTime").value(hasItem(DEFAULT_OUT_TIME.toString())));
    }

    @Test
    @Transactional
    void getConsultations() throws Exception {
        // Initialize the database
        consultationsRepository.saveAndFlush(consultations);

        // Get the consultations
        restConsultationsMockMvc
            .perform(get(ENTITY_API_URL_ID, consultations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultations.getId().intValue()))
            .andExpect(jsonPath("$.inTime").value(DEFAULT_IN_TIME.toString()))
            .andExpect(jsonPath("$.outTime").value(DEFAULT_OUT_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingConsultations() throws Exception {
        // Get the consultations
        restConsultationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsultations() throws Exception {
        // Initialize the database
        consultationsRepository.saveAndFlush(consultations);

        int databaseSizeBeforeUpdate = consultationsRepository.findAll().size();

        // Update the consultations
        Consultations updatedConsultations = consultationsRepository.findById(consultations.getId()).get();
        // Disconnect from session so that the updates on updatedConsultations are not directly saved in db
        em.detach(updatedConsultations);
        updatedConsultations.inTime(UPDATED_IN_TIME).outTime(UPDATED_OUT_TIME);
        ConsultationsDTO consultationsDTO = consultationsMapper.toDto(updatedConsultations);

        restConsultationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultationsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeUpdate);
        Consultations testConsultations = consultationsList.get(consultationsList.size() - 1);
        assertThat(testConsultations.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testConsultations.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
    }

    @Test
    @Transactional
    void putNonExistingConsultations() throws Exception {
        int databaseSizeBeforeUpdate = consultationsRepository.findAll().size();
        consultations.setId(count.incrementAndGet());

        // Create the Consultations
        ConsultationsDTO consultationsDTO = consultationsMapper.toDto(consultations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsultations() throws Exception {
        int databaseSizeBeforeUpdate = consultationsRepository.findAll().size();
        consultations.setId(count.incrementAndGet());

        // Create the Consultations
        ConsultationsDTO consultationsDTO = consultationsMapper.toDto(consultations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsultations() throws Exception {
        int databaseSizeBeforeUpdate = consultationsRepository.findAll().size();
        consultations.setId(count.incrementAndGet());

        // Create the Consultations
        ConsultationsDTO consultationsDTO = consultationsMapper.toDto(consultations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultationsWithPatch() throws Exception {
        // Initialize the database
        consultationsRepository.saveAndFlush(consultations);

        int databaseSizeBeforeUpdate = consultationsRepository.findAll().size();

        // Update the consultations using partial update
        Consultations partialUpdatedConsultations = new Consultations();
        partialUpdatedConsultations.setId(consultations.getId());

        partialUpdatedConsultations.inTime(UPDATED_IN_TIME).outTime(UPDATED_OUT_TIME);

        restConsultationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultations))
            )
            .andExpect(status().isOk());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeUpdate);
        Consultations testConsultations = consultationsList.get(consultationsList.size() - 1);
        assertThat(testConsultations.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testConsultations.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
    }

    @Test
    @Transactional
    void fullUpdateConsultationsWithPatch() throws Exception {
        // Initialize the database
        consultationsRepository.saveAndFlush(consultations);

        int databaseSizeBeforeUpdate = consultationsRepository.findAll().size();

        // Update the consultations using partial update
        Consultations partialUpdatedConsultations = new Consultations();
        partialUpdatedConsultations.setId(consultations.getId());

        partialUpdatedConsultations.inTime(UPDATED_IN_TIME).outTime(UPDATED_OUT_TIME);

        restConsultationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultations))
            )
            .andExpect(status().isOk());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeUpdate);
        Consultations testConsultations = consultationsList.get(consultationsList.size() - 1);
        assertThat(testConsultations.getInTime()).isEqualTo(UPDATED_IN_TIME);
        assertThat(testConsultations.getOutTime()).isEqualTo(UPDATED_OUT_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingConsultations() throws Exception {
        int databaseSizeBeforeUpdate = consultationsRepository.findAll().size();
        consultations.setId(count.incrementAndGet());

        // Create the Consultations
        ConsultationsDTO consultationsDTO = consultationsMapper.toDto(consultations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultationsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsultations() throws Exception {
        int databaseSizeBeforeUpdate = consultationsRepository.findAll().size();
        consultations.setId(count.incrementAndGet());

        // Create the Consultations
        ConsultationsDTO consultationsDTO = consultationsMapper.toDto(consultations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsultations() throws Exception {
        int databaseSizeBeforeUpdate = consultationsRepository.findAll().size();
        consultations.setId(count.incrementAndGet());

        // Create the Consultations
        ConsultationsDTO consultationsDTO = consultationsMapper.toDto(consultations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultations in the database
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsultations() throws Exception {
        // Initialize the database
        consultationsRepository.saveAndFlush(consultations);

        int databaseSizeBeforeDelete = consultationsRepository.findAll().size();

        // Delete the consultations
        restConsultationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, consultations.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consultations> consultationsList = consultationsRepository.findAll();
        assertThat(consultationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
