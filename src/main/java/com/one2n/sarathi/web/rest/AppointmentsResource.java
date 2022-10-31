package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.AppointmentsRepository;
import com.one2n.sarathi.service.AppointmentsService;
import com.one2n.sarathi.service.dto.AppointmentsDTO;
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
 * REST controller for managing {@link com.one2n.sarathi.domain.Appointments}.
 */
@RestController
@RequestMapping("/api")
public class AppointmentsResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentsResource.class);

    private static final String ENTITY_NAME = "appointments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppointmentsService appointmentsService;

    private final AppointmentsRepository appointmentsRepository;

    public AppointmentsResource(AppointmentsService appointmentsService, AppointmentsRepository appointmentsRepository) {
        this.appointmentsService = appointmentsService;
        this.appointmentsRepository = appointmentsRepository;
    }

    /**
     * {@code POST  /appointments} : Create a new appointments.
     *
     * @param appointmentsDTO the appointmentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appointmentsDTO, or with status {@code 400 (Bad Request)} if the appointments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appointments")
    public ResponseEntity<AppointmentsDTO> createAppointments(@RequestBody AppointmentsDTO appointmentsDTO) throws URISyntaxException {
        log.debug("REST request to save Appointments : {}", appointmentsDTO);
        if (appointmentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new appointments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppointmentsDTO result = appointmentsService.save(appointmentsDTO);
        return ResponseEntity
            .created(new URI("/api/appointments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appointments/:id} : Updates an existing appointments.
     *
     * @param id the id of the appointmentsDTO to save.
     * @param appointmentsDTO the appointmentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentsDTO,
     * or with status {@code 400 (Bad Request)} if the appointmentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appointmentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appointments/{id}")
    public ResponseEntity<AppointmentsDTO> updateAppointments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppointmentsDTO appointmentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Appointments : {}, {}", id, appointmentsDTO);
        if (appointmentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppointmentsDTO result = appointmentsService.update(appointmentsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appointments/:id} : Partial updates given fields of an existing appointments, field will ignore if it is null
     *
     * @param id the id of the appointmentsDTO to save.
     * @param appointmentsDTO the appointmentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentsDTO,
     * or with status {@code 400 (Bad Request)} if the appointmentsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appointmentsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appointmentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appointments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppointmentsDTO> partialUpdateAppointments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppointmentsDTO appointmentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Appointments partially : {}, {}", id, appointmentsDTO);
        if (appointmentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppointmentsDTO> result = appointmentsService.partialUpdate(appointmentsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /appointments} : get all the appointments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appointments in body.
     */
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentsDTO>> getAllAppointments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Appointments");
        Page<AppointmentsDTO> page = appointmentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appointments/:id} : get the "id" appointments.
     *
     * @param id the id of the appointmentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appointmentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appointments/{id}")
    public ResponseEntity<AppointmentsDTO> getAppointments(@PathVariable Long id) {
        log.debug("REST request to get Appointments : {}", id);
        Optional<AppointmentsDTO> appointmentsDTO = appointmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appointmentsDTO);
    }

    /**
     * {@code DELETE  /appointments/:id} : delete the "id" appointments.
     *
     * @param id the id of the appointmentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointments(@PathVariable Long id) {
        log.debug("REST request to delete Appointments : {}", id);
        appointmentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
