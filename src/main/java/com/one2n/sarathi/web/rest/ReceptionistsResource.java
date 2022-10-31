package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.ReceptionistsRepository;
import com.one2n.sarathi.service.ReceptionistsService;
import com.one2n.sarathi.service.dto.ReceptionistsDTO;
import com.one2n.sarathi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.one2n.sarathi.domain.Receptionists}.
 */
@RestController
@RequestMapping("/api")
public class ReceptionistsResource {

    private final Logger log = LoggerFactory.getLogger(ReceptionistsResource.class);

    private static final String ENTITY_NAME = "receptionists";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceptionistsService receptionistsService;

    private final ReceptionistsRepository receptionistsRepository;

    public ReceptionistsResource(ReceptionistsService receptionistsService, ReceptionistsRepository receptionistsRepository) {
        this.receptionistsService = receptionistsService;
        this.receptionistsRepository = receptionistsRepository;
    }

    /**
     * {@code POST  /receptionists} : Create a new receptionists.
     *
     * @param receptionistsDTO the receptionistsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receptionistsDTO, or with status {@code 400 (Bad Request)} if the receptionists has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/receptionists")
    public ResponseEntity<ReceptionistsDTO> createReceptionists(@RequestBody ReceptionistsDTO receptionistsDTO) throws URISyntaxException {
        log.debug("REST request to save Receptionists : {}", receptionistsDTO);
        if (receptionistsDTO.getId() != null) {
            throw new BadRequestAlertException("A new receptionists cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceptionistsDTO result = receptionistsService.save(receptionistsDTO);
        return ResponseEntity
            .created(new URI("/api/receptionists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /receptionists/:id} : Updates an existing receptionists.
     *
     * @param id the id of the receptionistsDTO to save.
     * @param receptionistsDTO the receptionistsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receptionistsDTO,
     * or with status {@code 400 (Bad Request)} if the receptionistsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the receptionistsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/receptionists/{id}")
    public ResponseEntity<ReceptionistsDTO> updateReceptionists(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceptionistsDTO receptionistsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Receptionists : {}, {}", id, receptionistsDTO);
        if (receptionistsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receptionistsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receptionistsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReceptionistsDTO result = receptionistsService.update(receptionistsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receptionistsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /receptionists/:id} : Partial updates given fields of an existing receptionists, field will ignore if it is null
     *
     * @param id the id of the receptionistsDTO to save.
     * @param receptionistsDTO the receptionistsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receptionistsDTO,
     * or with status {@code 400 (Bad Request)} if the receptionistsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the receptionistsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the receptionistsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/receptionists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReceptionistsDTO> partialUpdateReceptionists(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceptionistsDTO receptionistsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Receptionists partially : {}, {}", id, receptionistsDTO);
        if (receptionistsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receptionistsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receptionistsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReceptionistsDTO> result = receptionistsService.partialUpdate(receptionistsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receptionistsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /receptionists} : get all the receptionists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receptionists in body.
     */
    @GetMapping("/receptionists")
    public List<ReceptionistsDTO> getAllReceptionists() {
        log.debug("REST request to get all Receptionists");
        return receptionistsService.findAll();
    }

    /**
     * {@code GET  /receptionists/:id} : get the "id" receptionists.
     *
     * @param id the id of the receptionistsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receptionistsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/receptionists/{id}")
    public ResponseEntity<ReceptionistsDTO> getReceptionists(@PathVariable Long id) {
        log.debug("REST request to get Receptionists : {}", id);
        Optional<ReceptionistsDTO> receptionistsDTO = receptionistsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(receptionistsDTO);
    }

    /**
     * {@code DELETE  /receptionists/:id} : delete the "id" receptionists.
     *
     * @param id the id of the receptionistsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/receptionists/{id}")
    public ResponseEntity<Void> deleteReceptionists(@PathVariable Long id) {
        log.debug("REST request to delete Receptionists : {}", id);
        receptionistsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
