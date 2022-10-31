package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.Patients;
import com.one2n.sarathi.repository.PatientsRepository;
import com.one2n.sarathi.service.dto.PatientsDTO;
import com.one2n.sarathi.service.mapper.PatientsMapper;
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
 * Integration tests for the {@link PatientsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientsResourceIT {

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

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientsRepository patientsRepository;

    @Autowired
    private PatientsMapper patientsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientsMockMvc;

    private Patients patients;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patients createEntity(EntityManager em) {
        Patients patients = new Patients()
            .name(DEFAULT_NAME)
            .gender(DEFAULT_GENDER)
            .dob(DEFAULT_DOB)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .address(DEFAULT_ADDRESS);
        return patients;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patients createUpdatedEntity(EntityManager em) {
        Patients patients = new Patients()
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .dob(UPDATED_DOB)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .address(UPDATED_ADDRESS);
        return patients;
    }

    @BeforeEach
    public void initTest() {
        patients = createEntity(em);
    }

    @Test
    @Transactional
    void createPatients() throws Exception {
        int databaseSizeBeforeCreate = patientsRepository.findAll().size();
        // Create the Patients
        PatientsDTO patientsDTO = patientsMapper.toDto(patients);
        restPatientsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientsDTO)))
            .andExpect(status().isCreated());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeCreate + 1);
        Patients testPatients = patientsList.get(patientsList.size() - 1);
        assertThat(testPatients.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatients.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPatients.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testPatients.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testPatients.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createPatientsWithExistingId() throws Exception {
        // Create the Patients with an existing ID
        patients.setId(1L);
        PatientsDTO patientsDTO = patientsMapper.toDto(patients);

        int databaseSizeBeforeCreate = patientsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatients() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);

        // Get all the patientsList
        restPatientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patients.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getPatients() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);

        // Get the patients
        restPatientsMockMvc
            .perform(get(ENTITY_API_URL_ID, patients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patients.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingPatients() throws Exception {
        // Get the patients
        restPatientsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatients() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);

        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();

        // Update the patients
        Patients updatedPatients = patientsRepository.findById(patients.getId()).get();
        // Disconnect from session so that the updates on updatedPatients are not directly saved in db
        em.detach(updatedPatients);
        updatedPatients
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .dob(UPDATED_DOB)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .address(UPDATED_ADDRESS);
        PatientsDTO patientsDTO = patientsMapper.toDto(updatedPatients);

        restPatientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeUpdate);
        Patients testPatients = patientsList.get(patientsList.size() - 1);
        assertThat(testPatients.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatients.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatients.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testPatients.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testPatients.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingPatients() throws Exception {
        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();
        patients.setId(count.incrementAndGet());

        // Create the Patients
        PatientsDTO patientsDTO = patientsMapper.toDto(patients);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatients() throws Exception {
        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();
        patients.setId(count.incrementAndGet());

        // Create the Patients
        PatientsDTO patientsDTO = patientsMapper.toDto(patients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatients() throws Exception {
        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();
        patients.setId(count.incrementAndGet());

        // Create the Patients
        PatientsDTO patientsDTO = patientsMapper.toDto(patients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientsWithPatch() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);

        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();

        // Update the patients using partial update
        Patients partialUpdatedPatients = new Patients();
        partialUpdatedPatients.setId(patients.getId());

        partialUpdatedPatients.gender(UPDATED_GENDER);

        restPatientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatients))
            )
            .andExpect(status().isOk());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeUpdate);
        Patients testPatients = patientsList.get(patientsList.size() - 1);
        assertThat(testPatients.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatients.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatients.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testPatients.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testPatients.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdatePatientsWithPatch() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);

        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();

        // Update the patients using partial update
        Patients partialUpdatedPatients = new Patients();
        partialUpdatedPatients.setId(patients.getId());

        partialUpdatedPatients
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .dob(UPDATED_DOB)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .address(UPDATED_ADDRESS);

        restPatientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatients))
            )
            .andExpect(status().isOk());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeUpdate);
        Patients testPatients = patientsList.get(patientsList.size() - 1);
        assertThat(testPatients.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatients.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatients.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testPatients.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testPatients.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingPatients() throws Exception {
        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();
        patients.setId(count.incrementAndGet());

        // Create the Patients
        PatientsDTO patientsDTO = patientsMapper.toDto(patients);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatients() throws Exception {
        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();
        patients.setId(count.incrementAndGet());

        // Create the Patients
        PatientsDTO patientsDTO = patientsMapper.toDto(patients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatients() throws Exception {
        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();
        patients.setId(count.incrementAndGet());

        // Create the Patients
        PatientsDTO patientsDTO = patientsMapper.toDto(patients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patientsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patients in the database
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatients() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);

        int databaseSizeBeforeDelete = patientsRepository.findAll().size();

        // Delete the patients
        restPatientsMockMvc
            .perform(delete(ENTITY_API_URL_ID, patients.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patients> patientsList = patientsRepository.findAll();
        assertThat(patientsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
