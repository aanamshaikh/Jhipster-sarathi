package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.AppointmentStatus;
import com.one2n.sarathi.repository.AppointmentStatusRepository;
import com.one2n.sarathi.service.dto.AppointmentStatusDTO;
import com.one2n.sarathi.service.mapper.AppointmentStatusMapper;
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
 * Integration tests for the {@link AppointmentStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppointmentStatusResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/appointment-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppointmentStatusRepository appointmentStatusRepository;

    @Autowired
    private AppointmentStatusMapper appointmentStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppointmentStatusMockMvc;

    private AppointmentStatus appointmentStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentStatus createEntity(EntityManager em) {
        AppointmentStatus appointmentStatus = new AppointmentStatus().type(DEFAULT_TYPE);
        return appointmentStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentStatus createUpdatedEntity(EntityManager em) {
        AppointmentStatus appointmentStatus = new AppointmentStatus().type(UPDATED_TYPE);
        return appointmentStatus;
    }

    @BeforeEach
    public void initTest() {
        appointmentStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createAppointmentStatus() throws Exception {
        int databaseSizeBeforeCreate = appointmentStatusRepository.findAll().size();
        // Create the AppointmentStatus
        AppointmentStatusDTO appointmentStatusDTO = appointmentStatusMapper.toDto(appointmentStatus);
        restAppointmentStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeCreate + 1);
        AppointmentStatus testAppointmentStatus = appointmentStatusList.get(appointmentStatusList.size() - 1);
        assertThat(testAppointmentStatus.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createAppointmentStatusWithExistingId() throws Exception {
        // Create the AppointmentStatus with an existing ID
        appointmentStatus.setId(1L);
        AppointmentStatusDTO appointmentStatusDTO = appointmentStatusMapper.toDto(appointmentStatus);

        int databaseSizeBeforeCreate = appointmentStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppointmentStatuses() throws Exception {
        // Initialize the database
        appointmentStatusRepository.saveAndFlush(appointmentStatus);

        // Get all the appointmentStatusList
        restAppointmentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointmentStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getAppointmentStatus() throws Exception {
        // Initialize the database
        appointmentStatusRepository.saveAndFlush(appointmentStatus);

        // Get the appointmentStatus
        restAppointmentStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, appointmentStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointmentStatus.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingAppointmentStatus() throws Exception {
        // Get the appointmentStatus
        restAppointmentStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppointmentStatus() throws Exception {
        // Initialize the database
        appointmentStatusRepository.saveAndFlush(appointmentStatus);

        int databaseSizeBeforeUpdate = appointmentStatusRepository.findAll().size();

        // Update the appointmentStatus
        AppointmentStatus updatedAppointmentStatus = appointmentStatusRepository.findById(appointmentStatus.getId()).get();
        // Disconnect from session so that the updates on updatedAppointmentStatus are not directly saved in db
        em.detach(updatedAppointmentStatus);
        updatedAppointmentStatus.type(UPDATED_TYPE);
        AppointmentStatusDTO appointmentStatusDTO = appointmentStatusMapper.toDto(updatedAppointmentStatus);

        restAppointmentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeUpdate);
        AppointmentStatus testAppointmentStatus = appointmentStatusList.get(appointmentStatusList.size() - 1);
        assertThat(testAppointmentStatus.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAppointmentStatus() throws Exception {
        int databaseSizeBeforeUpdate = appointmentStatusRepository.findAll().size();
        appointmentStatus.setId(count.incrementAndGet());

        // Create the AppointmentStatus
        AppointmentStatusDTO appointmentStatusDTO = appointmentStatusMapper.toDto(appointmentStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppointmentStatus() throws Exception {
        int databaseSizeBeforeUpdate = appointmentStatusRepository.findAll().size();
        appointmentStatus.setId(count.incrementAndGet());

        // Create the AppointmentStatus
        AppointmentStatusDTO appointmentStatusDTO = appointmentStatusMapper.toDto(appointmentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppointmentStatus() throws Exception {
        int databaseSizeBeforeUpdate = appointmentStatusRepository.findAll().size();
        appointmentStatus.setId(count.incrementAndGet());

        // Create the AppointmentStatus
        AppointmentStatusDTO appointmentStatusDTO = appointmentStatusMapper.toDto(appointmentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppointmentStatusWithPatch() throws Exception {
        // Initialize the database
        appointmentStatusRepository.saveAndFlush(appointmentStatus);

        int databaseSizeBeforeUpdate = appointmentStatusRepository.findAll().size();

        // Update the appointmentStatus using partial update
        AppointmentStatus partialUpdatedAppointmentStatus = new AppointmentStatus();
        partialUpdatedAppointmentStatus.setId(appointmentStatus.getId());

        partialUpdatedAppointmentStatus.type(UPDATED_TYPE);

        restAppointmentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentStatus))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeUpdate);
        AppointmentStatus testAppointmentStatus = appointmentStatusList.get(appointmentStatusList.size() - 1);
        assertThat(testAppointmentStatus.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAppointmentStatusWithPatch() throws Exception {
        // Initialize the database
        appointmentStatusRepository.saveAndFlush(appointmentStatus);

        int databaseSizeBeforeUpdate = appointmentStatusRepository.findAll().size();

        // Update the appointmentStatus using partial update
        AppointmentStatus partialUpdatedAppointmentStatus = new AppointmentStatus();
        partialUpdatedAppointmentStatus.setId(appointmentStatus.getId());

        partialUpdatedAppointmentStatus.type(UPDATED_TYPE);

        restAppointmentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentStatus))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeUpdate);
        AppointmentStatus testAppointmentStatus = appointmentStatusList.get(appointmentStatusList.size() - 1);
        assertThat(testAppointmentStatus.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAppointmentStatus() throws Exception {
        int databaseSizeBeforeUpdate = appointmentStatusRepository.findAll().size();
        appointmentStatus.setId(count.incrementAndGet());

        // Create the AppointmentStatus
        AppointmentStatusDTO appointmentStatusDTO = appointmentStatusMapper.toDto(appointmentStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppointmentStatus() throws Exception {
        int databaseSizeBeforeUpdate = appointmentStatusRepository.findAll().size();
        appointmentStatus.setId(count.incrementAndGet());

        // Create the AppointmentStatus
        AppointmentStatusDTO appointmentStatusDTO = appointmentStatusMapper.toDto(appointmentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppointmentStatus() throws Exception {
        int databaseSizeBeforeUpdate = appointmentStatusRepository.findAll().size();
        appointmentStatus.setId(count.incrementAndGet());

        // Create the AppointmentStatus
        AppointmentStatusDTO appointmentStatusDTO = appointmentStatusMapper.toDto(appointmentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentStatus in the database
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppointmentStatus() throws Exception {
        // Initialize the database
        appointmentStatusRepository.saveAndFlush(appointmentStatus);

        int databaseSizeBeforeDelete = appointmentStatusRepository.findAll().size();

        // Delete the appointmentStatus
        restAppointmentStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointmentStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppointmentStatus> appointmentStatusList = appointmentStatusRepository.findAll();
        assertThat(appointmentStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
