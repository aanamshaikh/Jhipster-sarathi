package com.one2n.sarathi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.one2n.sarathi.IntegrationTest;
import com.one2n.sarathi.domain.Cancelled;
import com.one2n.sarathi.repository.CancelledRepository;
import com.one2n.sarathi.service.dto.CancelledDTO;
import com.one2n.sarathi.service.mapper.CancelledMapper;
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
 * Integration tests for the {@link CancelledResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CancelledResourceIT {

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cancelleds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CancelledRepository cancelledRepository;

    @Autowired
    private CancelledMapper cancelledMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCancelledMockMvc;

    private Cancelled cancelled;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cancelled createEntity(EntityManager em) {
        Cancelled cancelled = new Cancelled().reason(DEFAULT_REASON);
        return cancelled;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cancelled createUpdatedEntity(EntityManager em) {
        Cancelled cancelled = new Cancelled().reason(UPDATED_REASON);
        return cancelled;
    }

    @BeforeEach
    public void initTest() {
        cancelled = createEntity(em);
    }

    @Test
    @Transactional
    void createCancelled() throws Exception {
        int databaseSizeBeforeCreate = cancelledRepository.findAll().size();
        // Create the Cancelled
        CancelledDTO cancelledDTO = cancelledMapper.toDto(cancelled);
        restCancelledMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cancelledDTO)))
            .andExpect(status().isCreated());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeCreate + 1);
        Cancelled testCancelled = cancelledList.get(cancelledList.size() - 1);
        assertThat(testCancelled.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    void createCancelledWithExistingId() throws Exception {
        // Create the Cancelled with an existing ID
        cancelled.setId(1L);
        CancelledDTO cancelledDTO = cancelledMapper.toDto(cancelled);

        int databaseSizeBeforeCreate = cancelledRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCancelledMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cancelledDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCancelleds() throws Exception {
        // Initialize the database
        cancelledRepository.saveAndFlush(cancelled);

        // Get all the cancelledList
        restCancelledMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cancelled.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @Test
    @Transactional
    void getCancelled() throws Exception {
        // Initialize the database
        cancelledRepository.saveAndFlush(cancelled);

        // Get the cancelled
        restCancelledMockMvc
            .perform(get(ENTITY_API_URL_ID, cancelled.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cancelled.getId().intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingCancelled() throws Exception {
        // Get the cancelled
        restCancelledMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCancelled() throws Exception {
        // Initialize the database
        cancelledRepository.saveAndFlush(cancelled);

        int databaseSizeBeforeUpdate = cancelledRepository.findAll().size();

        // Update the cancelled
        Cancelled updatedCancelled = cancelledRepository.findById(cancelled.getId()).get();
        // Disconnect from session so that the updates on updatedCancelled are not directly saved in db
        em.detach(updatedCancelled);
        updatedCancelled.reason(UPDATED_REASON);
        CancelledDTO cancelledDTO = cancelledMapper.toDto(updatedCancelled);

        restCancelledMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cancelledDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cancelledDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeUpdate);
        Cancelled testCancelled = cancelledList.get(cancelledList.size() - 1);
        assertThat(testCancelled.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void putNonExistingCancelled() throws Exception {
        int databaseSizeBeforeUpdate = cancelledRepository.findAll().size();
        cancelled.setId(count.incrementAndGet());

        // Create the Cancelled
        CancelledDTO cancelledDTO = cancelledMapper.toDto(cancelled);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCancelledMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cancelledDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cancelledDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCancelled() throws Exception {
        int databaseSizeBeforeUpdate = cancelledRepository.findAll().size();
        cancelled.setId(count.incrementAndGet());

        // Create the Cancelled
        CancelledDTO cancelledDTO = cancelledMapper.toDto(cancelled);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCancelledMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cancelledDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCancelled() throws Exception {
        int databaseSizeBeforeUpdate = cancelledRepository.findAll().size();
        cancelled.setId(count.incrementAndGet());

        // Create the Cancelled
        CancelledDTO cancelledDTO = cancelledMapper.toDto(cancelled);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCancelledMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cancelledDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCancelledWithPatch() throws Exception {
        // Initialize the database
        cancelledRepository.saveAndFlush(cancelled);

        int databaseSizeBeforeUpdate = cancelledRepository.findAll().size();

        // Update the cancelled using partial update
        Cancelled partialUpdatedCancelled = new Cancelled();
        partialUpdatedCancelled.setId(cancelled.getId());

        partialUpdatedCancelled.reason(UPDATED_REASON);

        restCancelledMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCancelled.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCancelled))
            )
            .andExpect(status().isOk());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeUpdate);
        Cancelled testCancelled = cancelledList.get(cancelledList.size() - 1);
        assertThat(testCancelled.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void fullUpdateCancelledWithPatch() throws Exception {
        // Initialize the database
        cancelledRepository.saveAndFlush(cancelled);

        int databaseSizeBeforeUpdate = cancelledRepository.findAll().size();

        // Update the cancelled using partial update
        Cancelled partialUpdatedCancelled = new Cancelled();
        partialUpdatedCancelled.setId(cancelled.getId());

        partialUpdatedCancelled.reason(UPDATED_REASON);

        restCancelledMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCancelled.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCancelled))
            )
            .andExpect(status().isOk());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeUpdate);
        Cancelled testCancelled = cancelledList.get(cancelledList.size() - 1);
        assertThat(testCancelled.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void patchNonExistingCancelled() throws Exception {
        int databaseSizeBeforeUpdate = cancelledRepository.findAll().size();
        cancelled.setId(count.incrementAndGet());

        // Create the Cancelled
        CancelledDTO cancelledDTO = cancelledMapper.toDto(cancelled);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCancelledMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cancelledDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cancelledDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCancelled() throws Exception {
        int databaseSizeBeforeUpdate = cancelledRepository.findAll().size();
        cancelled.setId(count.incrementAndGet());

        // Create the Cancelled
        CancelledDTO cancelledDTO = cancelledMapper.toDto(cancelled);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCancelledMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cancelledDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCancelled() throws Exception {
        int databaseSizeBeforeUpdate = cancelledRepository.findAll().size();
        cancelled.setId(count.incrementAndGet());

        // Create the Cancelled
        CancelledDTO cancelledDTO = cancelledMapper.toDto(cancelled);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCancelledMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cancelledDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cancelled in the database
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCancelled() throws Exception {
        // Initialize the database
        cancelledRepository.saveAndFlush(cancelled);

        int databaseSizeBeforeDelete = cancelledRepository.findAll().size();

        // Delete the cancelled
        restCancelledMockMvc
            .perform(delete(ENTITY_API_URL_ID, cancelled.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cancelled> cancelledList = cancelledRepository.findAll();
        assertThat(cancelledList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
