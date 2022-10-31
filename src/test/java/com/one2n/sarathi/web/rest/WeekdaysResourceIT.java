package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.Weekdays;
import com.one2n.sarathi.repository.WeekdaysRepository;
import com.one2n.sarathi.service.dto.WeekdaysDTO;
import com.one2n.sarathi.service.mapper.WeekdaysMapper;
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
 * Integration tests for the {@link WeekdaysResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeekdaysResourceIT {

    private static final String DEFAULT_DAY = "AAAAAAAAAA";
    private static final String UPDATED_DAY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/weekdays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeekdaysRepository weekdaysRepository;

    @Autowired
    private WeekdaysMapper weekdaysMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeekdaysMockMvc;

    private Weekdays weekdays;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weekdays createEntity(EntityManager em) {
        Weekdays weekdays = new Weekdays().day(DEFAULT_DAY);
        return weekdays;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weekdays createUpdatedEntity(EntityManager em) {
        Weekdays weekdays = new Weekdays().day(UPDATED_DAY);
        return weekdays;
    }

    @BeforeEach
    public void initTest() {
        weekdays = createEntity(em);
    }

    @Test
    @Transactional
    void createWeekdays() throws Exception {
        int databaseSizeBeforeCreate = weekdaysRepository.findAll().size();
        // Create the Weekdays
        WeekdaysDTO weekdaysDTO = weekdaysMapper.toDto(weekdays);
        restWeekdaysMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weekdaysDTO)))
            .andExpect(status().isCreated());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeCreate + 1);
        Weekdays testWeekdays = weekdaysList.get(weekdaysList.size() - 1);
        assertThat(testWeekdays.getDay()).isEqualTo(DEFAULT_DAY);
    }

    @Test
    @Transactional
    void createWeekdaysWithExistingId() throws Exception {
        // Create the Weekdays with an existing ID
        weekdays.setId(1L);
        WeekdaysDTO weekdaysDTO = weekdaysMapper.toDto(weekdays);

        int databaseSizeBeforeCreate = weekdaysRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeekdaysMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weekdaysDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWeekdays() throws Exception {
        // Initialize the database
        weekdaysRepository.saveAndFlush(weekdays);

        // Get all the weekdaysList
        restWeekdaysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weekdays.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)));
    }

    @Test
    @Transactional
    void getWeekdays() throws Exception {
        // Initialize the database
        weekdaysRepository.saveAndFlush(weekdays);

        // Get the weekdays
        restWeekdaysMockMvc
            .perform(get(ENTITY_API_URL_ID, weekdays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weekdays.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY));
    }

    @Test
    @Transactional
    void getNonExistingWeekdays() throws Exception {
        // Get the weekdays
        restWeekdaysMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWeekdays() throws Exception {
        // Initialize the database
        weekdaysRepository.saveAndFlush(weekdays);

        int databaseSizeBeforeUpdate = weekdaysRepository.findAll().size();

        // Update the weekdays
        Weekdays updatedWeekdays = weekdaysRepository.findById(weekdays.getId()).get();
        // Disconnect from session so that the updates on updatedWeekdays are not directly saved in db
        em.detach(updatedWeekdays);
        updatedWeekdays.day(UPDATED_DAY);
        WeekdaysDTO weekdaysDTO = weekdaysMapper.toDto(updatedWeekdays);

        restWeekdaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weekdaysDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weekdaysDTO))
            )
            .andExpect(status().isOk());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeUpdate);
        Weekdays testWeekdays = weekdaysList.get(weekdaysList.size() - 1);
        assertThat(testWeekdays.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    void putNonExistingWeekdays() throws Exception {
        int databaseSizeBeforeUpdate = weekdaysRepository.findAll().size();
        weekdays.setId(count.incrementAndGet());

        // Create the Weekdays
        WeekdaysDTO weekdaysDTO = weekdaysMapper.toDto(weekdays);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeekdaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weekdaysDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weekdaysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeekdays() throws Exception {
        int databaseSizeBeforeUpdate = weekdaysRepository.findAll().size();
        weekdays.setId(count.incrementAndGet());

        // Create the Weekdays
        WeekdaysDTO weekdaysDTO = weekdaysMapper.toDto(weekdays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeekdaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weekdaysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeekdays() throws Exception {
        int databaseSizeBeforeUpdate = weekdaysRepository.findAll().size();
        weekdays.setId(count.incrementAndGet());

        // Create the Weekdays
        WeekdaysDTO weekdaysDTO = weekdaysMapper.toDto(weekdays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeekdaysMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weekdaysDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeekdaysWithPatch() throws Exception {
        // Initialize the database
        weekdaysRepository.saveAndFlush(weekdays);

        int databaseSizeBeforeUpdate = weekdaysRepository.findAll().size();

        // Update the weekdays using partial update
        Weekdays partialUpdatedWeekdays = new Weekdays();
        partialUpdatedWeekdays.setId(weekdays.getId());

        restWeekdaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeekdays.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeekdays))
            )
            .andExpect(status().isOk());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeUpdate);
        Weekdays testWeekdays = weekdaysList.get(weekdaysList.size() - 1);
        assertThat(testWeekdays.getDay()).isEqualTo(DEFAULT_DAY);
    }

    @Test
    @Transactional
    void fullUpdateWeekdaysWithPatch() throws Exception {
        // Initialize the database
        weekdaysRepository.saveAndFlush(weekdays);

        int databaseSizeBeforeUpdate = weekdaysRepository.findAll().size();

        // Update the weekdays using partial update
        Weekdays partialUpdatedWeekdays = new Weekdays();
        partialUpdatedWeekdays.setId(weekdays.getId());

        partialUpdatedWeekdays.day(UPDATED_DAY);

        restWeekdaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeekdays.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeekdays))
            )
            .andExpect(status().isOk());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeUpdate);
        Weekdays testWeekdays = weekdaysList.get(weekdaysList.size() - 1);
        assertThat(testWeekdays.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    void patchNonExistingWeekdays() throws Exception {
        int databaseSizeBeforeUpdate = weekdaysRepository.findAll().size();
        weekdays.setId(count.incrementAndGet());

        // Create the Weekdays
        WeekdaysDTO weekdaysDTO = weekdaysMapper.toDto(weekdays);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeekdaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weekdaysDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weekdaysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeekdays() throws Exception {
        int databaseSizeBeforeUpdate = weekdaysRepository.findAll().size();
        weekdays.setId(count.incrementAndGet());

        // Create the Weekdays
        WeekdaysDTO weekdaysDTO = weekdaysMapper.toDto(weekdays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeekdaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weekdaysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeekdays() throws Exception {
        int databaseSizeBeforeUpdate = weekdaysRepository.findAll().size();
        weekdays.setId(count.incrementAndGet());

        // Create the Weekdays
        WeekdaysDTO weekdaysDTO = weekdaysMapper.toDto(weekdays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeekdaysMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(weekdaysDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weekdays in the database
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeekdays() throws Exception {
        // Initialize the database
        weekdaysRepository.saveAndFlush(weekdays);

        int databaseSizeBeforeDelete = weekdaysRepository.findAll().size();

        // Delete the weekdays
        restWeekdaysMockMvc
            .perform(delete(ENTITY_API_URL_ID, weekdays.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Weekdays> weekdaysList = weekdaysRepository.findAll();
        assertThat(weekdaysList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
