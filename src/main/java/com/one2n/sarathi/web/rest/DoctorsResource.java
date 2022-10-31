package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.DoctorsRepository;
import com.one2n.sarathi.service.DoctorsService;
import com.one2n.sarathi.service.dto.DoctorsDTO;
import com.one2n.sarathi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.one2n.sarathi.domain.Doctors}.
 */
@RestController
@RequestMapping("/api")
public class DoctorsResource {

    private final Logger log = LoggerFactory.getLogger(DoctorsResource.class);

    private static final String ENTITY_NAME = "doctors";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctorsService doctorsService;

    private final DoctorsRepository doctorsRepository;

    public DoctorsResource(DoctorsService doctorsService, DoctorsRepository doctorsRepository) {
        this.doctorsService = doctorsService;
        this.doctorsRepository = doctorsRepository;
    }

    /**
     * {@code POST  /doctors} : Create a new doctors.
     *
     * @param doctorsDTO the doctorsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctorsDTO, or with status {@code 400 (Bad Request)} if the doctors has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doctors")
    public ResponseEntity<DoctorsDTO> createDoctors(@Valid @RequestBody DoctorsDTO doctorsDTO) throws URISyntaxException {
        log.debug("REST request to save Doctors : {}", doctorsDTO);
        if (doctorsDTO.getId() != null) {
            throw new BadRequestAlertException("A new doctors cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorsDTO result = doctorsService.save(doctorsDTO);
        return ResponseEntity
            .created(new URI("/api/doctors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doctors/:id} : Updates an existing doctors.
     *
     * @param id the id of the doctorsDTO to save.
     * @param doctorsDTO the doctorsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorsDTO,
     * or with status {@code 400 (Bad Request)} if the doctorsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doctorsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doctors/{id}")
    public ResponseEntity<DoctorsDTO> updateDoctors(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DoctorsDTO doctorsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Doctors : {}, {}", id, doctorsDTO);
        if (doctorsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doctorsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doctorsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DoctorsDTO result = doctorsService.update(doctorsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doctors/:id} : Partial updates given fields of an existing doctors, field will ignore if it is null
     *
     * @param id the id of the doctorsDTO to save.
     * @param doctorsDTO the doctorsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorsDTO,
     * or with status {@code 400 (Bad Request)} if the doctorsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the doctorsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the doctorsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doctors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DoctorsDTO> partialUpdateDoctors(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DoctorsDTO doctorsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Doctors partially : {}, {}", id, doctorsDTO);
        if (doctorsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doctorsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doctorsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DoctorsDTO> result = doctorsService.partialUpdate(doctorsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /doctors} : get all the doctors.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctors in body.
     */
    @GetMapping("/doctors")
    public List<DoctorsDTO> getAllDoctors(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Doctors");
        return doctorsService.findAll();
    }

    /**
     * {@code GET  /doctors/:id} : get the "id" doctors.
     *
     * @param id the id of the doctorsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorsDTO> getDoctors(@PathVariable Long id) {
        log.debug("REST request to get Doctors : {}", id);
        Optional<DoctorsDTO> doctorsDTO = doctorsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorsDTO);
    }

    /**
     * {@code DELETE  /doctors/:id} : delete the "id" doctors.
     *
     * @param id the id of the doctorsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctors(@PathVariable Long id) {
        log.debug("REST request to delete Doctors : {}", id);
        doctorsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
