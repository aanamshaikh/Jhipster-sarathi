package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.Appointments;
import com.one2n.sarathi.repository.AppointmentsRepository;
import com.one2n.sarathi.service.dto.AppointmentsDTO;
import com.one2n.sarathi.service.mapper.AppointmentsMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AppointmentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppointmentsResourceIT {

    private static final Instant DEFAULT_SLOT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SLOT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_BOOK_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BOOK_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_CANCELLED = false;
    private static final Boolean UPDATED_IS_CANCELLED = true;

    private static final Instant DEFAULT_REQUESTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUESTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/appointments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private AppointmentsMapper appointmentsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppointmentsMockMvc;

    private Appointments appointments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointments createEntity(EntityManager em) {
        Appointments appointments = new Appointments()
            .slotTime(DEFAULT_SLOT_TIME)
            .bookDay(DEFAULT_BOOK_DAY)
            .isCancelled(DEFAULT_IS_CANCELLED)
            .requestedAt(DEFAULT_REQUESTED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return appointments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointments createUpdatedEntity(EntityManager em) {
        Appointments appointments = new Appointments()
            .slotTime(UPDATED_SLOT_TIME)
            .bookDay(UPDATED_BOOK_DAY)
            .isCancelled(UPDATED_IS_CANCELLED)
            .requestedAt(UPDATED_REQUESTED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return appointments;
    }

    @BeforeEach
    public void initTest() {
        appointments = createEntity(em);
    }

    @Test
    @Transactional
    void createAppointments() throws Exception {
        int databaseSizeBeforeCreate = appointmentsRepository.findAll().size();
        // Create the Appointments
        AppointmentsDTO appointmentsDTO = appointmentsMapper.toDto(appointments);
        restAppointmentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeCreate + 1);
        Appointments testAppointments = appointmentsList.get(appointmentsList.size() - 1);
        assertThat(testAppointments.getSlotTime()).isEqualTo(DEFAULT_SLOT_TIME);
        assertThat(testAppointments.getBookDay()).isEqualTo(DEFAULT_BOOK_DAY);
        assertThat(testAppointments.getIsCancelled()).isEqualTo(DEFAULT_IS_CANCELLED);
        assertThat(testAppointments.getRequestedAt()).isEqualTo(DEFAULT_REQUESTED_AT);
        assertThat(testAppointments.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createAppointmentsWithExistingId() throws Exception {
        // Create the Appointments with an existing ID
        appointments.setId(1L);
        AppointmentsDTO appointmentsDTO = appointmentsMapper.toDto(appointments);

        int databaseSizeBeforeCreate = appointmentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentsRepository.saveAndFlush(appointments);

        // Get all the appointmentsList
        restAppointmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointments.getId().intValue())))
            .andExpect(jsonPath("$.[*].slotTime").value(hasItem(DEFAULT_SLOT_TIME.toString())))
            .andExpect(jsonPath("$.[*].bookDay").value(hasItem(DEFAULT_BOOK_DAY.toString())))
            .andExpect(jsonPath("$.[*].isCancelled").value(hasItem(DEFAULT_IS_CANCELLED.booleanValue())))
            .andExpect(jsonPath("$.[*].requestedAt").value(hasItem(DEFAULT_REQUESTED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getAppointments() throws Exception {
        // Initialize the database
        appointmentsRepository.saveAndFlush(appointments);

        // Get the appointments
        restAppointmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, appointments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointments.getId().intValue()))
            .andExpect(jsonPath("$.slotTime").value(DEFAULT_SLOT_TIME.toString()))
            .andExpect(jsonPath("$.bookDay").value(DEFAULT_BOOK_DAY.toString()))
            .andExpect(jsonPath("$.isCancelled").value(DEFAULT_IS_CANCELLED.booleanValue()))
            .andExpect(jsonPath("$.requestedAt").value(DEFAULT_REQUESTED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAppointments() throws Exception {
        // Get the appointments
        restAppointmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppointments() throws Exception {
        // Initialize the database
        appointmentsRepository.saveAndFlush(appointments);

        int databaseSizeBeforeUpdate = appointmentsRepository.findAll().size();

        // Update the appointments
        Appointments updatedAppointments = appointmentsRepository.findById(appointments.getId()).get();
        // Disconnect from session so that the updates on updatedAppointments are not directly saved in db
        em.detach(updatedAppointments);
        updatedAppointments
            .slotTime(UPDATED_SLOT_TIME)
            .bookDay(UPDATED_BOOK_DAY)
            .isCancelled(UPDATED_IS_CANCELLED)
            .requestedAt(UPDATED_REQUESTED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        AppointmentsDTO appointmentsDTO = appointmentsMapper.toDto(updatedAppointments);

        restAppointmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeUpdate);
        Appointments testAppointments = appointmentsList.get(appointmentsList.size() - 1);
        assertThat(testAppointments.getSlotTime()).isEqualTo(UPDATED_SLOT_TIME);
        assertThat(testAppointments.getBookDay()).isEqualTo(UPDATED_BOOK_DAY);
        assertThat(testAppointments.getIsCancelled()).isEqualTo(UPDATED_IS_CANCELLED);
        assertThat(testAppointments.getRequestedAt()).isEqualTo(UPDATED_REQUESTED_AT);
        assertThat(testAppointments.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAppointments() throws Exception {
        int databaseSizeBeforeUpdate = appointmentsRepository.findAll().size();
        appointments.setId(count.incrementAndGet());

        // Create the Appointments
        AppointmentsDTO appointmentsDTO = appointmentsMapper.toDto(appointments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppointments() throws Exception {
        int databaseSizeBeforeUpdate = appointmentsRepository.findAll().size();
        appointments.setId(count.incrementAndGet());

        // Create the Appointments
        AppointmentsDTO appointmentsDTO = appointmentsMapper.toDto(appointments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppointments() throws Exception {
        int databaseSizeBeforeUpdate = appointmentsRepository.findAll().size();
        appointments.setId(count.incrementAndGet());

        // Create the Appointments
        AppointmentsDTO appointmentsDTO = appointmentsMapper.toDto(appointments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppointmentsWithPatch() throws Exception {
        // Initialize the database
        appointmentsRepository.saveAndFlush(appointments);

        int databaseSizeBeforeUpdate = appointmentsRepository.findAll().size();

        // Update the appointments using partial update
        Appointments partialUpdatedAppointments = new Appointments();
        partialUpdatedAppointments.setId(appointments.getId());

        partialUpdatedAppointments.requestedAt(UPDATED_REQUESTED_AT);

        restAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointments))
            )
            .andExpect(status().isOk());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeUpdate);
        Appointments testAppointments = appointmentsList.get(appointmentsList.size() - 1);
        assertThat(testAppointments.getSlotTime()).isEqualTo(DEFAULT_SLOT_TIME);
        assertThat(testAppointments.getBookDay()).isEqualTo(DEFAULT_BOOK_DAY);
        assertThat(testAppointments.getIsCancelled()).isEqualTo(DEFAULT_IS_CANCELLED);
        assertThat(testAppointments.getRequestedAt()).isEqualTo(UPDATED_REQUESTED_AT);
        assertThat(testAppointments.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAppointmentsWithPatch() throws Exception {
        // Initialize the database
        appointmentsRepository.saveAndFlush(appointments);

        int databaseSizeBeforeUpdate = appointmentsRepository.findAll().size();

        // Update the appointments using partial update
        Appointments partialUpdatedAppointments = new Appointments();
        partialUpdatedAppointments.setId(appointments.getId());

        partialUpdatedAppointments
            .slotTime(UPDATED_SLOT_TIME)
            .bookDay(UPDATED_BOOK_DAY)
            .isCancelled(UPDATED_IS_CANCELLED)
            .requestedAt(UPDATED_REQUESTED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointments))
            )
            .andExpect(status().isOk());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeUpdate);
        Appointments testAppointments = appointmentsList.get(appointmentsList.size() - 1);
        assertThat(testAppointments.getSlotTime()).isEqualTo(UPDATED_SLOT_TIME);
        assertThat(testAppointments.getBookDay()).isEqualTo(UPDATED_BOOK_DAY);
        assertThat(testAppointments.getIsCancelled()).isEqualTo(UPDATED_IS_CANCELLED);
        assertThat(testAppointments.getRequestedAt()).isEqualTo(UPDATED_REQUESTED_AT);
        assertThat(testAppointments.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAppointments() throws Exception {
        int databaseSizeBeforeUpdate = appointmentsRepository.findAll().size();
        appointments.setId(count.incrementAndGet());

        // Create the Appointments
        AppointmentsDTO appointmentsDTO = appointmentsMapper.toDto(appointments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppointments() throws Exception {
        int databaseSizeBeforeUpdate = appointmentsRepository.findAll().size();
        appointments.setId(count.incrementAndGet());

        // Create the Appointments
        AppointmentsDTO appointmentsDTO = appointmentsMapper.toDto(appointments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppointments() throws Exception {
        int databaseSizeBeforeUpdate = appointmentsRepository.findAll().size();
        appointments.setId(count.incrementAndGet());

        // Create the Appointments
        AppointmentsDTO appointmentsDTO = appointmentsMapper.toDto(appointments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointments in the database
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppointments() throws Exception {
        // Initialize the database
        appointmentsRepository.saveAndFlush(appointments);

        int databaseSizeBeforeDelete = appointmentsRepository.findAll().size();

        // Delete the appointments
        restAppointmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appointments> appointmentsList = appointmentsRepository.findAll();
        assertThat(appointmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
