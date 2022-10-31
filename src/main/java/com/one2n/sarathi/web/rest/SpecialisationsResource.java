package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.SpecialisationsRepository;
import com.one2n.sarathi.service.SpecialisationsService;
import com.one2n.sarathi.service.dto.SpecialisationsDTO;
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
 * REST controller for managing {@link com.one2n.sarathi.domain.Specialisations}.
 */
@RestController
@RequestMapping("/api")
public class SpecialisationsResource {

    private final Logger log = LoggerFactory.getLogger(SpecialisationsResource.class);

    private static final String ENTITY_NAME = "specialisations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialisationsService specialisationsService;

    private final SpecialisationsRepository specialisationsRepository;

    public SpecialisationsResource(SpecialisationsService specialisationsService, SpecialisationsRepository specialisationsRepository) {
        this.specialisationsService = specialisationsService;
        this.specialisationsRepository = specialisationsRepository;
    }

    /**
     * {@code POST  /specialisations} : Create a new specialisations.
     *
     * @param specialisationsDTO the specialisationsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialisationsDTO, or with status {@code 400 (Bad Request)} if the specialisations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specialisations")
    public ResponseEntity<SpecialisationsDTO> createSpecialisations(@RequestBody SpecialisationsDTO specialisationsDTO)
        throws URISyntaxException {
        log.debug("REST request to save Specialisations : {}", specialisationsDTO);
        if (specialisationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialisations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialisationsDTO result = specialisationsService.save(specialisationsDTO);
        return ResponseEntity
            .created(new URI("/api/specialisations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specialisations/:id} : Updates an existing specialisations.
     *
     * @param id the id of the specialisationsDTO to save.
     * @param specialisationsDTO the specialisationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialisationsDTO,
     * or with status {@code 400 (Bad Request)} if the specialisationsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialisationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specialisations/{id}")
    public ResponseEntity<SpecialisationsDTO> updateSpecialisations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpecialisationsDTO specialisationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Specialisations : {}, {}", id, specialisationsDTO);
        if (specialisationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialisationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialisationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecialisationsDTO result = specialisationsService.update(specialisationsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialisationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /specialisations/:id} : Partial updates given fields of an existing specialisations, field will ignore if it is null
     *
     * @param id the id of the specialisationsDTO to save.
     * @param specialisationsDTO the specialisationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialisationsDTO,
     * or with status {@code 400 (Bad Request)} if the specialisationsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specialisationsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialisationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/specialisations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialisationsDTO> partialUpdateSpecialisations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpecialisationsDTO specialisationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Specialisations partially : {}, {}", id, specialisationsDTO);
        if (specialisationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialisationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialisationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialisationsDTO> result = specialisationsService.partialUpdate(specialisationsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialisationsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /specialisations} : get all the specialisations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialisations in body.
     */
    @GetMapping("/specialisations")
    public List<SpecialisationsDTO> getAllSpecialisations() {
        log.debug("REST request to get all Specialisations");
        return specialisationsService.findAll();
    }

    /**
     * {@code GET  /specialisations/:id} : get the "id" specialisations.
     *
     * @param id the id of the specialisationsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialisationsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specialisations/{id}")
    public ResponseEntity<SpecialisationsDTO> getSpecialisations(@PathVariable Long id) {
        log.debug("REST request to get Specialisations : {}", id);
        Optional<SpecialisationsDTO> specialisationsDTO = specialisationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialisationsDTO);
    }

    /**
     * {@code DELETE  /specialisations/:id} : delete the "id" specialisations.
     *
     * @param id the id of the specialisationsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specialisations/{id}")
    public ResponseEntity<Void> deleteSpecialisations(@PathVariable Long id) {
        log.debug("REST request to delete Specialisations : {}", id);
        specialisationsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
