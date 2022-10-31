package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.Reasons;
import com.one2n.sarathi.repository.ReasonsRepository;
import com.one2n.sarathi.service.dto.ReasonsDTO;
import com.one2n.sarathi.service.mapper.ReasonsMapper;
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
 * Integration tests for the {@link ReasonsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReasonsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReasonsRepository reasonsRepository;

    @Autowired
    private ReasonsMapper reasonsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReasonsMockMvc;

    private Reasons reasons;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reasons createEntity(EntityManager em) {
        Reasons reasons = new Reasons().name(DEFAULT_NAME);
        return reasons;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reasons createUpdatedEntity(EntityManager em) {
        Reasons reasons = new Reasons().name(UPDATED_NAME);
        return reasons;
    }

    @BeforeEach
    public void initTest() {
        reasons = createEntity(em);
    }

    @Test
    @Transactional
    void createReasons() throws Exception {
        int databaseSizeBeforeCreate = reasonsRepository.findAll().size();
        // Create the Reasons
        ReasonsDTO reasonsDTO = reasonsMapper.toDto(reasons);
        restReasonsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reasonsDTO)))
            .andExpect(status().isCreated());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeCreate + 1);
        Reasons testReasons = reasonsList.get(reasonsList.size() - 1);
        assertThat(testReasons.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createReasonsWithExistingId() throws Exception {
        // Create the Reasons with an existing ID
        reasons.setId(1L);
        ReasonsDTO reasonsDTO = reasonsMapper.toDto(reasons);

        int databaseSizeBeforeCreate = reasonsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReasonsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reasonsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReasons() throws Exception {
        // Initialize the database
        reasonsRepository.saveAndFlush(reasons);

        // Get all the reasonsList
        restReasonsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reasons.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getReasons() throws Exception {
        // Initialize the database
        reasonsRepository.saveAndFlush(reasons);

        // Get the reasons
        restReasonsMockMvc
            .perform(get(ENTITY_API_URL_ID, reasons.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reasons.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingReasons() throws Exception {
        // Get the reasons
        restReasonsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReasons() throws Exception {
        // Initialize the database
        reasonsRepository.saveAndFlush(reasons);

        int databaseSizeBeforeUpdate = reasonsRepository.findAll().size();

        // Update the reasons
        Reasons updatedReasons = reasonsRepository.findById(reasons.getId()).get();
        // Disconnect from session so that the updates on updatedReasons are not directly saved in db
        em.detach(updatedReasons);
        updatedReasons.name(UPDATED_NAME);
        ReasonsDTO reasonsDTO = reasonsMapper.toDto(updatedReasons);

        restReasonsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reasonsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeUpdate);
        Reasons testReasons = reasonsList.get(reasonsList.size() - 1);
        assertThat(testReasons.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingReasons() throws Exception {
        int databaseSizeBeforeUpdate = reasonsRepository.findAll().size();
        reasons.setId(count.incrementAndGet());

        // Create the Reasons
        ReasonsDTO reasonsDTO = reasonsMapper.toDto(reasons);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReasonsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reasonsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReasons() throws Exception {
        int databaseSizeBeforeUpdate = reasonsRepository.findAll().size();
        reasons.setId(count.incrementAndGet());

        // Create the Reasons
        ReasonsDTO reasonsDTO = reasonsMapper.toDto(reasons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReasons() throws Exception {
        int databaseSizeBeforeUpdate = reasonsRepository.findAll().size();
        reasons.setId(count.incrementAndGet());

        // Create the Reasons
        ReasonsDTO reasonsDTO = reasonsMapper.toDto(reasons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reasonsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReasonsWithPatch() throws Exception {
        // Initialize the database
        reasonsRepository.saveAndFlush(reasons);

        int databaseSizeBeforeUpdate = reasonsRepository.findAll().size();

        // Update the reasons using partial update
        Reasons partialUpdatedReasons = new Reasons();
        partialUpdatedReasons.setId(reasons.getId());

        partialUpdatedReasons.name(UPDATED_NAME);

        restReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReasons.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReasons))
            )
            .andExpect(status().isOk());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeUpdate);
        Reasons testReasons = reasonsList.get(reasonsList.size() - 1);
        assertThat(testReasons.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateReasonsWithPatch() throws Exception {
        // Initialize the database
        reasonsRepository.saveAndFlush(reasons);

        int databaseSizeBeforeUpdate = reasonsRepository.findAll().size();

        // Update the reasons using partial update
        Reasons partialUpdatedReasons = new Reasons();
        partialUpdatedReasons.setId(reasons.getId());

        partialUpdatedReasons.name(UPDATED_NAME);

        restReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReasons.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReasons))
            )
            .andExpect(status().isOk());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeUpdate);
        Reasons testReasons = reasonsList.get(reasonsList.size() - 1);
        assertThat(testReasons.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingReasons() throws Exception {
        int databaseSizeBeforeUpdate = reasonsRepository.findAll().size();
        reasons.setId(count.incrementAndGet());

        // Create the Reasons
        ReasonsDTO reasonsDTO = reasonsMapper.toDto(reasons);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reasonsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reasonsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReasons() throws Exception {
        int databaseSizeBeforeUpdate = reasonsRepository.findAll().size();
        reasons.setId(count.incrementAndGet());

        // Create the Reasons
        ReasonsDTO reasonsDTO = reasonsMapper.toDto(reasons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reasonsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReasons() throws Exception {
        int databaseSizeBeforeUpdate = reasonsRepository.findAll().size();
        reasons.setId(count.incrementAndGet());

        // Create the Reasons
        ReasonsDTO reasonsDTO = reasonsMapper.toDto(reasons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reasonsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reasons in the database
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReasons() throws Exception {
        // Initialize the database
        reasonsRepository.saveAndFlush(reasons);

        int databaseSizeBeforeDelete = reasonsRepository.findAll().size();

        // Delete the reasons
        restReasonsMockMvc
            .perform(delete(ENTITY_API_URL_ID, reasons.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reasons> reasonsList = reasonsRepository.findAll();
        assertThat(reasonsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
