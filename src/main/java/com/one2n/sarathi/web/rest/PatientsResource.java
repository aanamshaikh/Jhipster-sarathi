package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.PatientsRepository;
import com.one2n.sarathi.service.PatientsService;
import com.one2n.sarathi.service.dto.PatientsDTO;
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
 * REST controller for managing {@link com.one2n.sarathi.domain.Patients}.
 */
@RestController
@RequestMapping("/api")
public class PatientsResource {

    private final Logger log = LoggerFactory.getLogger(PatientsResource.class);

    private static final String ENTITY_NAME = "patients";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientsService patientsService;

    private final PatientsRepository patientsRepository;

    public PatientsResource(PatientsService patientsService, PatientsRepository patientsRepository) {
        this.patientsService = patientsService;
        this.patientsRepository = patientsRepository;
    }

    /**
     * {@code POST  /patients} : Create a new patients.
     *
     * @param patientsDTO the patientsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientsDTO, or with status {@code 400 (Bad Request)} if the patients has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patients")
    public ResponseEntity<PatientsDTO> createPatients(@RequestBody PatientsDTO patientsDTO) throws URISyntaxException {
        log.debug("REST request to save Patients : {}", patientsDTO);
        if (patientsDTO.getId() != null) {
            throw new BadRequestAlertException("A new patients cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatientsDTO result = patientsService.save(patientsDTO);
        return ResponseEntity
            .created(new URI("/api/patients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patients/:id} : Updates an existing patients.
     *
     * @param id the id of the patientsDTO to save.
     * @param patientsDTO the patientsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientsDTO,
     * or with status {@code 400 (Bad Request)} if the patientsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientsDTO> updatePatients(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatientsDTO patientsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Patients : {}, {}", id, patientsDTO);
        if (patientsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PatientsDTO result = patientsService.update(patientsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patients/:id} : Partial updates given fields of an existing patients, field will ignore if it is null
     *
     * @param id the id of the patientsDTO to save.
     * @param patientsDTO the patientsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientsDTO,
     * or with status {@code 400 (Bad Request)} if the patientsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the patientsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientsDTO> partialUpdatePatients(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatientsDTO patientsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Patients partially : {}, {}", id, patientsDTO);
        if (patientsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientsDTO> result = patientsService.partialUpdate(patientsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /patients} : get all the patients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patients in body.
     */
    @GetMapping("/patients")
    public List<PatientsDTO> getAllPatients() {
        log.debug("REST request to get all Patients");
        return patientsService.findAll();
    }

    /**
     * {@code GET  /patients/:id} : get the "id" patients.
     *
     * @param id the id of the patientsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientsDTO> getPatients(@PathVariable Long id) {
        log.debug("REST request to get Patients : {}", id);
        Optional<PatientsDTO> patientsDTO = patientsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientsDTO);
    }

    /**
     * {@code DELETE  /patients/:id} : delete the "id" patients.
     *
     * @param id the id of the patientsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatients(@PathVariable Long id) {
        log.debug("REST request to delete Patients : {}", id);
        patientsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
