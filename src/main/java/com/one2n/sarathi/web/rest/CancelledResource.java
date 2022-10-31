package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.CancelledRepository;
import com.one2n.sarathi.service.CancelledService;
import com.one2n.sarathi.service.dto.CancelledDTO;
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
 * REST controller for managing {@link com.one2n.sarathi.domain.Cancelled}.
 */
@RestController
@RequestMapping("/api")
public class CancelledResource {

    private final Logger log = LoggerFactory.getLogger(CancelledResource.class);

    private static final String ENTITY_NAME = "cancelled";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CancelledService cancelledService;

    private final CancelledRepository cancelledRepository;

    public CancelledResource(CancelledService cancelledService, CancelledRepository cancelledRepository) {
        this.cancelledService = cancelledService;
        this.cancelledRepository = cancelledRepository;
    }

    /**
     * {@code POST  /cancelleds} : Create a new cancelled.
     *
     * @param cancelledDTO the cancelledDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cancelledDTO, or with status {@code 400 (Bad Request)} if the cancelled has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cancelleds")
    public ResponseEntity<CancelledDTO> createCancelled(@RequestBody CancelledDTO cancelledDTO) throws URISyntaxException {
        log.debug("REST request to save Cancelled : {}", cancelledDTO);
        if (cancelledDTO.getId() != null) {
            throw new BadRequestAlertException("A new cancelled cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CancelledDTO result = cancelledService.save(cancelledDTO);
        return ResponseEntity
            .created(new URI("/api/cancelleds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cancelleds/:id} : Updates an existing cancelled.
     *
     * @param id the id of the cancelledDTO to save.
     * @param cancelledDTO the cancelledDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cancelledDTO,
     * or with status {@code 400 (Bad Request)} if the cancelledDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cancelledDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cancelleds/{id}")
    public ResponseEntity<CancelledDTO> updateCancelled(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CancelledDTO cancelledDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cancelled : {}, {}", id, cancelledDTO);
        if (cancelledDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cancelledDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cancelledRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CancelledDTO result = cancelledService.update(cancelledDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cancelledDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cancelleds/:id} : Partial updates given fields of an existing cancelled, field will ignore if it is null
     *
     * @param id the id of the cancelledDTO to save.
     * @param cancelledDTO the cancelledDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cancelledDTO,
     * or with status {@code 400 (Bad Request)} if the cancelledDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cancelledDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cancelledDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cancelleds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CancelledDTO> partialUpdateCancelled(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CancelledDTO cancelledDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cancelled partially : {}, {}", id, cancelledDTO);
        if (cancelledDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cancelledDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cancelledRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CancelledDTO> result = cancelledService.partialUpdate(cancelledDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cancelledDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cancelleds} : get all the cancelleds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cancelleds in body.
     */
    @GetMapping("/cancelleds")
    public List<CancelledDTO> getAllCancelleds() {
        log.debug("REST request to get all Cancelleds");
        return cancelledService.findAll();
    }

    /**
     * {@code GET  /cancelleds/:id} : get the "id" cancelled.
     *
     * @param id the id of the cancelledDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cancelledDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cancelleds/{id}")
    public ResponseEntity<CancelledDTO> getCancelled(@PathVariable Long id) {
        log.debug("REST request to get Cancelled : {}", id);
        Optional<CancelledDTO> cancelledDTO = cancelledService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cancelledDTO);
    }

    /**
     * {@code DELETE  /cancelleds/:id} : delete the "id" cancelled.
     *
     * @param id the id of the cancelledDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cancelleds/{id}")
    public ResponseEntity<Void> deleteCancelled(@PathVariable Long id) {
        log.debug("REST request to delete Cancelled : {}", id);
        cancelledService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
