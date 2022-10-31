package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.Doctors;
import com.one2n.sarathi.repository.DoctorsRepository;
import com.one2n.sarathi.service.DoctorsService;
import com.one2n.sarathi.service.dto.DoctorsDTO;
import com.one2n.sarathi.service.mapper.DoctorsMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DoctorsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DoctorsResourceIT {

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

    private static final Integer DEFAULT_EXPERIENCE = 50;
    private static final Integer UPDATED_EXPERIENCE = 49;

    private static final String ENTITY_API_URL = "/api/doctors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DoctorsRepository doctorsRepository;

    @Mock
    private DoctorsRepository doctorsRepositoryMock;

    @Autowired
    private DoctorsMapper doctorsMapper;

    @Mock
    private DoctorsService doctorsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctorsMockMvc;

    private Doctors doctors;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctors createEntity(EntityManager em) {
        Doctors doctors = new Doctors()
            .name(DEFAULT_NAME)
            .gender(DEFAULT_GENDER)
            .dob(DEFAULT_DOB)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .qualification(DEFAULT_QUALIFICATION)
            .experience(DEFAULT_EXPERIENCE);
        return doctors;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctors createUpdatedEntity(EntityManager em) {
        Doctors doctors = new Doctors()
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .dob(UPDATED_DOB)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .address(UPDATED_ADDRESS)
            .qualification(UPDATED_QUALIFICATION)
            .experience(UPDATED_EXPERIENCE);
        return doctors;
    }

    @BeforeEach
    public void initTest() {
        doctors = createEntity(em);
    }

    @Test
    @Transactional
    void createDoctors() throws Exception {
        int databaseSizeBeforeCreate = doctorsRepository.findAll().size();
        // Create the Doctors
        DoctorsDTO doctorsDTO = doctorsMapper.toDto(doctors);
        restDoctorsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorsDTO)))
            .andExpect(status().isCreated());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeCreate + 1);
        Doctors testDoctors = doctorsList.get(doctorsList.size() - 1);
        assertThat(testDoctors.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDoctors.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testDoctors.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testDoctors.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testDoctors.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testDoctors.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
        assertThat(testDoctors.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
    }

    @Test
    @Transactional
    void createDoctorsWithExistingId() throws Exception {
        // Create the Doctors with an existing ID
        doctors.setId(1L);
        DoctorsDTO doctorsDTO = doctorsMapper.toDto(doctors);

        int databaseSizeBeforeCreate = doctorsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDoctors() throws Exception {
        // Initialize the database
        doctorsRepository.saveAndFlush(doctors);

        // Get all the doctorsList
        restDoctorsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctors.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION)))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoctorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(doctorsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoctorsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(doctorsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoctorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(doctorsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoctorsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(doctorsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDoctors() throws Exception {
        // Initialize the database
        doctorsRepository.saveAndFlush(doctors);

        // Get the doctors
        restDoctorsMockMvc
            .perform(get(ENTITY_API_URL_ID, doctors.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctors.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION))
            .andExpect(jsonPath("$.experience").value(DEFAULT_EXPERIENCE));
    }

    @Test
    @Transactional
    void getNonExistingDoctors() throws Exception {
        // Get the doctors
        restDoctorsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDoctors() throws Exception {
        // Initialize the database
        doctorsRepository.saveAndFlush(doctors);

        int databaseSizeBeforeUpdate = doctorsRepository.findAll().size();

        // Update the doctors
        Doctors updatedDoctors = doctorsRepository.findById(doctors.getId()).get();
        // Disconnect from session so that the updates on updatedDoctors are not directly saved in db
        em.detach(updatedDoctors);
        updatedDoctors
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .dob(UPDATED_DOB)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .address(UPDATED_ADDRESS)
            .qualification(UPDATED_QUALIFICATION)
            .experience(UPDATED_EXPERIENCE);
        DoctorsDTO doctorsDTO = doctorsMapper.toDto(updatedDoctors);

        restDoctorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctorsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeUpdate);
        Doctors testDoctors = doctorsList.get(doctorsList.size() - 1);
        assertThat(testDoctors.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDoctors.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testDoctors.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testDoctors.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testDoctors.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDoctors.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
        assertThat(testDoctors.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
    }

    @Test
    @Transactional
    void putNonExistingDoctors() throws Exception {
        int databaseSizeBeforeUpdate = doctorsRepository.findAll().size();
        doctors.setId(count.incrementAndGet());

        // Create the Doctors
        DoctorsDTO doctorsDTO = doctorsMapper.toDto(doctors);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoctors() throws Exception {
        int databaseSizeBeforeUpdate = doctorsRepository.findAll().size();
        doctors.setId(count.incrementAndGet());

        // Create the Doctors
        DoctorsDTO doctorsDTO = doctorsMapper.toDto(doctors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoctors() throws Exception {
        int databaseSizeBeforeUpdate = doctorsRepository.findAll().size();
        doctors.setId(count.incrementAndGet());

        // Create the Doctors
        DoctorsDTO doctorsDTO = doctorsMapper.toDto(doctors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoctorsWithPatch() throws Exception {
        // Initialize the database
        doctorsRepository.saveAndFlush(doctors);

        int databaseSizeBeforeUpdate = doctorsRepository.findAll().size();

        // Update the doctors using partial update
        Doctors partialUpdatedDoctors = new Doctors();
        partialUpdatedDoctors.setId(doctors.getId());

        partialUpdatedDoctors.gender(UPDATED_GENDER).dob(UPDATED_DOB).mobileNumber(UPDATED_MOBILE_NUMBER).address(UPDATED_ADDRESS);

        restDoctorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctors.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoctors))
            )
            .andExpect(status().isOk());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeUpdate);
        Doctors testDoctors = doctorsList.get(doctorsList.size() - 1);
        assertThat(testDoctors.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDoctors.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testDoctors.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testDoctors.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testDoctors.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDoctors.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
        assertThat(testDoctors.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
    }

    @Test
    @Transactional
    void fullUpdateDoctorsWithPatch() throws Exception {
        // Initialize the database
        doctorsRepository.saveAndFlush(doctors);

        int databaseSizeBeforeUpdate = doctorsRepository.findAll().size();

        // Update the doctors using partial update
        Doctors partialUpdatedDoctors = new Doctors();
        partialUpdatedDoctors.setId(doctors.getId());

        partialUpdatedDoctors
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .dob(UPDATED_DOB)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .address(UPDATED_ADDRESS)
            .qualification(UPDATED_QUALIFICATION)
            .experience(UPDATED_EXPERIENCE);

        restDoctorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctors.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoctors))
            )
            .andExpect(status().isOk());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeUpdate);
        Doctors testDoctors = doctorsList.get(doctorsList.size() - 1);
        assertThat(testDoctors.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDoctors.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testDoctors.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testDoctors.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testDoctors.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDoctors.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
        assertThat(testDoctors.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
    }

    @Test
    @Transactional
    void patchNonExistingDoctors() throws Exception {
        int databaseSizeBeforeUpdate = doctorsRepository.findAll().size();
        doctors.setId(count.incrementAndGet());

        // Create the Doctors
        DoctorsDTO doctorsDTO = doctorsMapper.toDto(doctors);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doctorsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doctorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoctors() throws Exception {
        int databaseSizeBeforeUpdate = doctorsRepository.findAll().size();
        doctors.setId(count.incrementAndGet());

        // Create the Doctors
        DoctorsDTO doctorsDTO = doctorsMapper.toDto(doctors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doctorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoctors() throws Exception {
        int databaseSizeBeforeUpdate = doctorsRepository.findAll().size();
        doctors.setId(count.incrementAndGet());

        // Create the Doctors
        DoctorsDTO doctorsDTO = doctorsMapper.toDto(doctors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(doctorsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctors in the database
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoctors() throws Exception {
        // Initialize the database
        doctorsRepository.saveAndFlush(doctors);

        int databaseSizeBeforeDelete = doctorsRepository.findAll().size();

        // Delete the doctors
        restDoctorsMockMvc
            .perform(delete(ENTITY_API_URL_ID, doctors.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doctors> doctorsList = doctorsRepository.findAll();
        assertThat(doctorsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
