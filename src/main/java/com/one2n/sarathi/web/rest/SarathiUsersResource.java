package com.one2n.sarathi.web.rest;

import com.one2n.sarathi.repository.SarathiUsersRepository;
import com.one2n.sarathi.service.SarathiUsersService;
import com.one2n.sarathi.service.dto.SarathiUsersDTO;
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
 * REST controller for managing {@link com.one2n.sarathi.domain.SarathiUsers}.
 */
@RestController
@RequestMapping("/api")
public class SarathiUsersResource {

    private final Logger log = LoggerFactory.getLogger(SarathiUsersResource.class);

    private static final String ENTITY_NAME = "sarathiUsers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SarathiUsersService sarathiUsersService;

    private final SarathiUsersRepository sarathiUsersRepository;

    public SarathiUsersResource(SarathiUsersService sarathiUsersService, SarathiUsersRepository sarathiUsersRepository) {
        this.sarathiUsersService = sarathiUsersService;
        this.sarathiUsersRepository = sarathiUsersRepository;
    }

    /**
     * {@code POST  /sarathi-users} : Create a new sarathiUsers.
     *
     * @param sarathiUsersDTO the sarathiUsersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sarathiUsersDTO, or with status {@code 400 (Bad Request)} if the sarathiUsers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sarathi-users")
    public ResponseEntity<SarathiUsersDTO> createSarathiUsers(@Valid @RequestBody SarathiUsersDTO sarathiUsersDTO)
        throws URISyntaxException {
        log.debug("REST request to save SarathiUsers : {}", sarathiUsersDTO);
        if (sarathiUsersDTO.getId() != null) {
            throw new BadRequestAlertException("A new sarathiUsers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SarathiUsersDTO result = sarathiUsersService.save(sarathiUsersDTO);
        return ResponseEntity
            .created(new URI("/api/sarathi-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sarathi-users/:id} : Updates an existing sarathiUsers.
     *
     * @param id the id of the sarathiUsersDTO to save.
     * @param sarathiUsersDTO the sarathiUsersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sarathiUsersDTO,
     * or with status {@code 400 (Bad Request)} if the sarathiUsersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sarathiUsersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sarathi-users/{id}")
    public ResponseEntity<SarathiUsersDTO> updateSarathiUsers(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SarathiUsersDTO sarathiUsersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SarathiUsers : {}, {}", id, sarathiUsersDTO);
        if (sarathiUsersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sarathiUsersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sarathiUsersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SarathiUsersDTO result = sarathiUsersService.update(sarathiUsersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sarathiUsersDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sarathi-users/:id} : Partial updates given fields of an existing sarathiUsers, field will ignore if it is null
     *
     * @param id the id of the sarathiUsersDTO to save.
     * @param sarathiUsersDTO the sarathiUsersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sarathiUsersDTO,
     * or with status {@code 400 (Bad Request)} if the sarathiUsersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sarathiUsersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sarathiUsersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sarathi-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SarathiUsersDTO> partialUpdateSarathiUsers(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SarathiUsersDTO sarathiUsersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SarathiUsers partially : {}, {}", id, sarathiUsersDTO);
        if (sarathiUsersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sarathiUsersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sarathiUsersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SarathiUsersDTO> result = sarathiUsersService.partialUpdate(sarathiUsersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sarathiUsersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sarathi-users} : get all the sarathiUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sarathiUsers in body.
     */
    @GetMapping("/sarathi-users")
    public List<SarathiUsersDTO> getAllSarathiUsers() {
        log.debug("REST request to get all SarathiUsers");
        return sarathiUsersService.findAll();
    }

    /**
     * {@code GET  /sarathi-users/:id} : get the "id" sarathiUsers.
     *
     * @param id the id of the sarathiUsersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sarathiUsersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sarathi-users/{id}")
    public ResponseEntity<SarathiUsersDTO> getSarathiUsers(@PathVariable Long id) {
        log.debug("REST request to get SarathiUsers : {}", id);
        Optional<SarathiUsersDTO> sarathiUsersDTO = sarathiUsersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sarathiUsersDTO);
    }

    /**
     * {@code DELETE  /sarathi-users/:id} : delete the "id" sarathiUsers.
     *
     * @param id the id of the sarathiUsersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sarathi-users/{id}")
    public ResponseEntity<Void> deleteSarathiUsers(@PathVariable Long id) {
        log.debug("REST request to delete SarathiUsers : {}", id);
        sarathiUsersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
