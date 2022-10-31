package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.WeekdaysRepository;
import com.one2n.sarathi.service.WeekdaysService;
import com.one2n.sarathi.service.dto.WeekdaysDTO;
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
 * REST controller for managing {@link com.one2n.sarathi.domain.Weekdays}.
 */
@RestController
@RequestMapping("/api")
public class WeekdaysResource {

    private final Logger log = LoggerFactory.getLogger(WeekdaysResource.class);

    private static final String ENTITY_NAME = "weekdays";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeekdaysService weekdaysService;

    private final WeekdaysRepository weekdaysRepository;

    public WeekdaysResource(WeekdaysService weekdaysService, WeekdaysRepository weekdaysRepository) {
        this.weekdaysService = weekdaysService;
        this.weekdaysRepository = weekdaysRepository;
    }

    /**
     * {@code POST  /weekdays} : Create a new weekdays.
     *
     * @param weekdaysDTO the weekdaysDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weekdaysDTO, or with status {@code 400 (Bad Request)} if the weekdays has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weekdays")
    public ResponseEntity<WeekdaysDTO> createWeekdays(@RequestBody WeekdaysDTO weekdaysDTO) throws URISyntaxException {
        log.debug("REST request to save Weekdays : {}", weekdaysDTO);
        if (weekdaysDTO.getId() != null) {
            throw new BadRequestAlertException("A new weekdays cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeekdaysDTO result = weekdaysService.save(weekdaysDTO);
        return ResponseEntity
            .created(new URI("/api/weekdays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weekdays/:id} : Updates an existing weekdays.
     *
     * @param id the id of the weekdaysDTO to save.
     * @param weekdaysDTO the weekdaysDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weekdaysDTO,
     * or with status {@code 400 (Bad Request)} if the weekdaysDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weekdaysDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weekdays/{id}")
    public ResponseEntity<WeekdaysDTO> updateWeekdays(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeekdaysDTO weekdaysDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Weekdays : {}, {}", id, weekdaysDTO);
        if (weekdaysDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weekdaysDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weekdaysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WeekdaysDTO result = weekdaysService.update(weekdaysDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weekdaysDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /weekdays/:id} : Partial updates given fields of an existing weekdays, field will ignore if it is null
     *
     * @param id the id of the weekdaysDTO to save.
     * @param weekdaysDTO the weekdaysDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weekdaysDTO,
     * or with status {@code 400 (Bad Request)} if the weekdaysDTO is not valid,
     * or with status {@code 404 (Not Found)} if the weekdaysDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the weekdaysDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/weekdays/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeekdaysDTO> partialUpdateWeekdays(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeekdaysDTO weekdaysDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Weekdays partially : {}, {}", id, weekdaysDTO);
        if (weekdaysDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weekdaysDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weekdaysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeekdaysDTO> result = weekdaysService.partialUpdate(weekdaysDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weekdaysDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /weekdays} : get all the weekdays.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weekdays in body.
     */
    @GetMapping("/weekdays")
    public List<WeekdaysDTO> getAllWeekdays() {
        log.debug("REST request to get all Weekdays");
        return weekdaysService.findAll();
    }

    /**
     * {@code GET  /weekdays/:id} : get the "id" weekdays.
     *
     * @param id the id of the weekdaysDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weekdaysDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weekdays/{id}")
    public ResponseEntity<WeekdaysDTO> getWeekdays(@PathVariable Long id) {
        log.debug("REST request to get Weekdays : {}", id);
        Optional<WeekdaysDTO> weekdaysDTO = weekdaysService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weekdaysDTO);
    }

    /**
     * {@code DELETE  /weekdays/:id} : delete the "id" weekdays.
     *
     * @param id the id of the weekdaysDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weekdays/{id}")
    public ResponseEntity<Void> deleteWeekdays(@PathVariable Long id) {
        log.debug("REST request to delete Weekdays : {}", id);
        weekdaysService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
