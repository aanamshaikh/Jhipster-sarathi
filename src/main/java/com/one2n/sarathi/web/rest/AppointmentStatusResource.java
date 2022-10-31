package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.AppointmentStatusRepository;
import com.one2n.sarathi.service.AppointmentStatusService;
import com.one2n.sarathi.service.dto.AppointmentStatusDTO;
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
 * REST controller for managing {@link com.one2n.sarathi.domain.AppointmentStatus}.
 */
@RestController
@RequestMapping("/api")
public class AppointmentStatusResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentStatusResource.class);

    private static final String ENTITY_NAME = "appointmentStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppointmentStatusService appointmentStatusService;

    private final AppointmentStatusRepository appointmentStatusRepository;

    public AppointmentStatusResource(
        AppointmentStatusService appointmentStatusService,
        AppointmentStatusRepository appointmentStatusRepository
    ) {
        this.appointmentStatusService = appointmentStatusService;
        this.appointmentStatusRepository = appointmentStatusRepository;
    }

    /**
     * {@code POST  /appointment-statuses} : Create a new appointmentStatus.
     *
     * @param appointmentStatusDTO the appointmentStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appointmentStatusDTO, or with status {@code 400 (Bad Request)} if the appointmentStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appointment-statuses")
    public ResponseEntity<AppointmentStatusDTO> createAppointmentStatus(@RequestBody AppointmentStatusDTO appointmentStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save AppointmentStatus : {}", appointmentStatusDTO);
        if (appointmentStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new appointmentStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppointmentStatusDTO result = appointmentStatusService.save(appointmentStatusDTO);
        return ResponseEntity
            .created(new URI("/api/appointment-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appointment-statuses/:id} : Updates an existing appointmentStatus.
     *
     * @param id the id of the appointmentStatusDTO to save.
     * @param appointmentStatusDTO the appointmentStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentStatusDTO,
     * or with status {@code 400 (Bad Request)} if the appointmentStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appointmentStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appointment-statuses/{id}")
    public ResponseEntity<AppointmentStatusDTO> updateAppointmentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppointmentStatusDTO appointmentStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppointmentStatus : {}, {}", id, appointmentStatusDTO);
        if (appointmentStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppointmentStatusDTO result = appointmentStatusService.update(appointmentStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appointment-statuses/:id} : Partial updates given fields of an existing appointmentStatus, field will ignore if it is null
     *
     * @param id the id of the appointmentStatusDTO to save.
     * @param appointmentStatusDTO the appointmentStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentStatusDTO,
     * or with status {@code 400 (Bad Request)} if the appointmentStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appointmentStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appointmentStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appointment-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppointmentStatusDTO> partialUpdateAppointmentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppointmentStatusDTO appointmentStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppointmentStatus partially : {}, {}", id, appointmentStatusDTO);
        if (appointmentStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppointmentStatusDTO> result = appointmentStatusService.partialUpdate(appointmentStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /appointment-statuses} : get all the appointmentStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appointmentStatuses in body.
     */
    @GetMapping("/appointment-statuses")
    public List<AppointmentStatusDTO> getAllAppointmentStatuses() {
        log.debug("REST request to get all AppointmentStatuses");
        return appointmentStatusService.findAll();
    }

    /**
     * {@code GET  /appointment-statuses/:id} : get the "id" appointmentStatus.
     *
     * @param id the id of the appointmentStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appointmentStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appointment-statuses/{id}")
    public ResponseEntity<AppointmentStatusDTO> getAppointmentStatus(@PathVariable Long id) {
        log.debug("REST request to get AppointmentStatus : {}", id);
        Optional<AppointmentStatusDTO> appointmentStatusDTO = appointmentStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appointmentStatusDTO);
    }

    /**
     * {@code DELETE  /appointment-statuses/:id} : delete the "id" appointmentStatus.
     *
     * @param id the id of the appointmentStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appointment-statuses/{id}")
    public ResponseEntity<Void> deleteAppointmentStatus(@PathVariable Long id) {
        log.debug("REST request to delete AppointmentStatus : {}", id);
        appointmentStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
