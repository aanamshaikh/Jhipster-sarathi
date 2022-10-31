package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.ConsultationsRepository;
import com.one2n.sarathi.service.ConsultationsService;
import com.one2n.sarathi.service.dto.ConsultationsDTO;
import com.one2n.sarathi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.one2n.sarathi.domain.Consultations}.
 */
@RestController
@RequestMapping("/api")
public class ConsultationsResource {

    private final Logger log = LoggerFactory.getLogger(ConsultationsResource.class);

    private static final String ENTITY_NAME = "consultations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultationsService consultationsService;

    private final ConsultationsRepository consultationsRepository;

    public ConsultationsResource(ConsultationsService consultationsService, ConsultationsRepository consultationsRepository) {
        this.consultationsService = consultationsService;
        this.consultationsRepository = consultationsRepository;
    }

    /**
     * {@code POST  /consultations} : Create a new consultations.
     *
     * @param consultationsDTO the consultationsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultationsDTO, or with status {@code 400 (Bad Request)} if the consultations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consultations")
    public ResponseEntity<ConsultationsDTO> createConsultations(@RequestBody ConsultationsDTO consultationsDTO) throws URISyntaxException {
        log.debug("REST request to save Consultations : {}", consultationsDTO);
        if (consultationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new consultations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConsultationsDTO result = consultationsService.save(consultationsDTO);
        return ResponseEntity
            .created(new URI("/api/consultations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consultations/:id} : Updates an existing consultations.
     *
     * @param id the id of the consultationsDTO to save.
     * @param consultationsDTO the consultationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultationsDTO,
     * or with status {@code 400 (Bad Request)} if the consultationsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consultations/{id}")
    public ResponseEntity<ConsultationsDTO> updateConsultations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsultationsDTO consultationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Consultations : {}, {}", id, consultationsDTO);
        if (consultationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConsultationsDTO result = consultationsService.update(consultationsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consultations/:id} : Partial updates given fields of an existing consultations, field will ignore if it is null
     *
     * @param id the id of the consultationsDTO to save.
     * @param consultationsDTO the consultationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultationsDTO,
     * or with status {@code 400 (Bad Request)} if the consultationsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the consultationsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the consultationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/consultations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsultationsDTO> partialUpdateConsultations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsultationsDTO consultationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Consultations partially : {}, {}", id, consultationsDTO);
        if (consultationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsultationsDTO> result = consultationsService.partialUpdate(consultationsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultationsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /consultations} : get all the consultations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consultations in body.
     */
    @GetMapping("/consultations")
    public ResponseEntity<List<ConsultationsDTO>> getAllConsultations(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Consultations");
        Page<ConsultationsDTO> page = consultationsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consultations/:id} : get the "id" consultations.
     *
     * @param id the id of the consultationsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultationsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consultations/{id}")
    public ResponseEntity<ConsultationsDTO> getConsultations(@PathVariable Long id) {
        log.debug("REST request to get Consultations : {}", id);
        Optional<ConsultationsDTO> consultationsDTO = consultationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultationsDTO);
    }

    /**
     * {@code DELETE  /consultations/:id} : delete the "id" consultations.
     *
     * @param id the id of the consultationsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/consultations/{id}")
    public ResponseEntity<Void> deleteConsultations(@PathVariable Long id) {
        log.debug("REST request to delete Consultations : {}", id);
        consultationsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
