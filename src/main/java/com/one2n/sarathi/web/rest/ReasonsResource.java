package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.ReasonsRepository;
import com.one2n.sarathi.service.ReasonsService;
import com.one2n.sarathi.service.dto.ReasonsDTO;
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
 * REST controller for managing {@link com.one2n.sarathi.domain.Reasons}.
 */
@RestController
@RequestMapping("/api")
public class ReasonsResource {

    private final Logger log = LoggerFactory.getLogger(ReasonsResource.class);

    private static final String ENTITY_NAME = "reasons";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReasonsService reasonsService;

    private final ReasonsRepository reasonsRepository;

    public ReasonsResource(ReasonsService reasonsService, ReasonsRepository reasonsRepository) {
        this.reasonsService = reasonsService;
        this.reasonsRepository = reasonsRepository;
    }

    /**
     * {@code POST  /reasons} : Create a new reasons.
     *
     * @param reasonsDTO the reasonsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reasonsDTO, or with status {@code 400 (Bad Request)} if the reasons has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reasons")
    public ResponseEntity<ReasonsDTO> createReasons(@RequestBody ReasonsDTO reasonsDTO) throws URISyntaxException {
        log.debug("REST request to save Reasons : {}", reasonsDTO);
        if (reasonsDTO.getId() != null) {
            throw new BadRequestAlertException("A new reasons cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReasonsDTO result = reasonsService.save(reasonsDTO);
        return ResponseEntity
            .created(new URI("/api/reasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reasons/:id} : Updates an existing reasons.
     *
     * @param id the id of the reasonsDTO to save.
     * @param reasonsDTO the reasonsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reasonsDTO,
     * or with status {@code 400 (Bad Request)} if the reasonsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reasonsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reasons/{id}")
    public ResponseEntity<ReasonsDTO> updateReasons(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReasonsDTO reasonsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reasons : {}, {}", id, reasonsDTO);
        if (reasonsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reasonsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reasonsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReasonsDTO result = reasonsService.update(reasonsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reasonsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reasons/:id} : Partial updates given fields of an existing reasons, field will ignore if it is null
     *
     * @param id the id of the reasonsDTO to save.
     * @param reasonsDTO the reasonsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reasonsDTO,
     * or with status {@code 400 (Bad Request)} if the reasonsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reasonsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reasonsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reasons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReasonsDTO> partialUpdateReasons(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReasonsDTO reasonsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reasons partially : {}, {}", id, reasonsDTO);
        if (reasonsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reasonsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reasonsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReasonsDTO> result = reasonsService.partialUpdate(reasonsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reasonsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reasons} : get all the reasons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reasons in body.
     */
    @GetMapping("/reasons")
    public List<ReasonsDTO> getAllReasons() {
        log.debug("REST request to get all Reasons");
        return reasonsService.findAll();
    }

    /**
     * {@code GET  /reasons/:id} : get the "id" reasons.
     *
     * @param id the id of the reasonsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reasonsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reasons/{id}")
    public ResponseEntity<ReasonsDTO> getReasons(@PathVariable Long id) {
        log.debug("REST request to get Reasons : {}", id);
        Optional<ReasonsDTO> reasonsDTO = reasonsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reasonsDTO);
    }

    /**
     * {@code DELETE  /reasons/:id} : delete the "id" reasons.
     *
     * @param id the id of the reasonsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reasons/{id}")
    public ResponseEntity<Void> deleteReasons(@PathVariable Long id) {
        log.debug("REST request to delete Reasons : {}", id);
        reasonsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
