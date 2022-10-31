package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.Specialisations;
import com.one2n.sarathi.repository.SpecialisationsRepository;
import com.one2n.sarathi.service.dto.SpecialisationsDTO;
import com.one2n.sarathi.service.mapper.SpecialisationsMapper;
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
 * Integration tests for the {@link SpecialisationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialisationsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specialisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialisationsRepository specialisationsRepository;

    @Autowired
    private SpecialisationsMapper specialisationsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialisationsMockMvc;

    private Specialisations specialisations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialisations createEntity(EntityManager em) {
        Specialisations specialisations = new Specialisations().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return specialisations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialisations createUpdatedEntity(EntityManager em) {
        Specialisations specialisations = new Specialisations().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return specialisations;
    }

    @BeforeEach
    public void initTest() {
        specialisations = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialisations() throws Exception {
        int databaseSizeBeforeCreate = specialisationsRepository.findAll().size();
        // Create the Specialisations
        SpecialisationsDTO specialisationsDTO = specialisationsMapper.toDto(specialisations);
        restSpecialisationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialisationsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeCreate + 1);
        Specialisations testSpecialisations = specialisationsList.get(specialisationsList.size() - 1);
        assertThat(testSpecialisations.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpecialisations.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createSpecialisationsWithExistingId() throws Exception {
        // Create the Specialisations with an existing ID
        specialisations.setId(1L);
        SpecialisationsDTO specialisationsDTO = specialisationsMapper.toDto(specialisations);

        int databaseSizeBeforeCreate = specialisationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialisationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialisationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpecialisations() throws Exception {
        // Initialize the database
        specialisationsRepository.saveAndFlush(specialisations);

        // Get all the specialisationsList
        restSpecialisationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialisations.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSpecialisations() throws Exception {
        // Initialize the database
        specialisationsRepository.saveAndFlush(specialisations);

        // Get the specialisations
        restSpecialisationsMockMvc
            .perform(get(ENTITY_API_URL_ID, specialisations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialisations.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSpecialisations() throws Exception {
        // Get the specialisations
        restSpecialisationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialisations() throws Exception {
        // Initialize the database
        specialisationsRepository.saveAndFlush(specialisations);

        int databaseSizeBeforeUpdate = specialisationsRepository.findAll().size();

        // Update the specialisations
        Specialisations updatedSpecialisations = specialisationsRepository.findById(specialisations.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialisations are not directly saved in db
        em.detach(updatedSpecialisations);
        updatedSpecialisations.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        SpecialisationsDTO specialisationsDTO = specialisationsMapper.toDto(updatedSpecialisations);

        restSpecialisationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialisationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialisationsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeUpdate);
        Specialisations testSpecialisations = specialisationsList.get(specialisationsList.size() - 1);
        assertThat(testSpecialisations.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecialisations.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingSpecialisations() throws Exception {
        int databaseSizeBeforeUpdate = specialisationsRepository.findAll().size();
        specialisations.setId(count.incrementAndGet());

        // Create the Specialisations
        SpecialisationsDTO specialisationsDTO = specialisationsMapper.toDto(specialisations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialisationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialisationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialisationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialisations() throws Exception {
        int databaseSizeBeforeUpdate = specialisationsRepository.findAll().size();
        specialisations.setId(count.incrementAndGet());

        // Create the Specialisations
        SpecialisationsDTO specialisationsDTO = specialisationsMapper.toDto(specialisations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialisationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialisationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialisations() throws Exception {
        int databaseSizeBeforeUpdate = specialisationsRepository.findAll().size();
        specialisations.setId(count.incrementAndGet());

        // Create the Specialisations
        SpecialisationsDTO specialisationsDTO = specialisationsMapper.toDto(specialisations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialisationsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialisationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialisationsWithPatch() throws Exception {
        // Initialize the database
        specialisationsRepository.saveAndFlush(specialisations);

        int databaseSizeBeforeUpdate = specialisationsRepository.findAll().size();

        // Update the specialisations using partial update
        Specialisations partialUpdatedSpecialisations = new Specialisations();
        partialUpdatedSpecialisations.setId(specialisations.getId());

        restSpecialisationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialisations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialisations))
            )
            .andExpect(status().isOk());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeUpdate);
        Specialisations testSpecialisations = specialisationsList.get(specialisationsList.size() - 1);
        assertThat(testSpecialisations.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpecialisations.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateSpecialisationsWithPatch() throws Exception {
        // Initialize the database
        specialisationsRepository.saveAndFlush(specialisations);

        int databaseSizeBeforeUpdate = specialisationsRepository.findAll().size();

        // Update the specialisations using partial update
        Specialisations partialUpdatedSpecialisations = new Specialisations();
        partialUpdatedSpecialisations.setId(specialisations.getId());

        partialUpdatedSpecialisations.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSpecialisationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialisations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialisations))
            )
            .andExpect(status().isOk());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeUpdate);
        Specialisations testSpecialisations = specialisationsList.get(specialisationsList.size() - 1);
        assertThat(testSpecialisations.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecialisations.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialisations() throws Exception {
        int databaseSizeBeforeUpdate = specialisationsRepository.findAll().size();
        specialisations.setId(count.incrementAndGet());

        // Create the Specialisations
        SpecialisationsDTO specialisationsDTO = specialisationsMapper.toDto(specialisations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialisationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialisationsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialisationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialisations() throws Exception {
        int databaseSizeBeforeUpdate = specialisationsRepository.findAll().size();
        specialisations.setId(count.incrementAndGet());

        // Create the Specialisations
        SpecialisationsDTO specialisationsDTO = specialisationsMapper.toDto(specialisations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialisationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialisationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialisations() throws Exception {
        int databaseSizeBeforeUpdate = specialisationsRepository.findAll().size();
        specialisations.setId(count.incrementAndGet());

        // Create the Specialisations
        SpecialisationsDTO specialisationsDTO = specialisationsMapper.toDto(specialisations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialisationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialisationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialisations in the database
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialisations() throws Exception {
        // Initialize the database
        specialisationsRepository.saveAndFlush(specialisations);

        int databaseSizeBeforeDelete = specialisationsRepository.findAll().size();

        // Delete the specialisations
        restSpecialisationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialisations.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specialisations> specialisationsList = specialisationsRepository.findAll();
        assertThat(specialisationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
