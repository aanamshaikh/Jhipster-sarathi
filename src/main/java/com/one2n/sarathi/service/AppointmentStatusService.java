package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.AppointmentStatus;
import com.one2n.sarathi.repository.AppointmentStatusRepository;
import com.one2n.sarathi.service.dto.AppointmentStatusDTO;
import com.one2n.sarathi.service.mapper.AppointmentStatusMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AppointmentStatus}.
 */
@Service
@Transactional
public class AppointmentStatusService {

    private final Logger log = LoggerFactory.getLogger(AppointmentStatusService.class);

    private final AppointmentStatusRepository appointmentStatusRepository;

    private final AppointmentStatusMapper appointmentStatusMapper;

    public AppointmentStatusService(
        AppointmentStatusRepository appointmentStatusRepository,
        AppointmentStatusMapper appointmentStatusMapper
    ) {
        this.appointmentStatusRepository = appointmentStatusRepository;
        this.appointmentStatusMapper = appointmentStatusMapper;
    }

    /**
     * Save a appointmentStatus.
     *
     * @param appointmentStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public AppointmentStatusDTO save(AppointmentStatusDTO appointmentStatusDTO) {
        log.debug("Request to save AppointmentStatus : {}", appointmentStatusDTO);
        AppointmentStatus appointmentStatus = appointmentStatusMapper.toEntity(appointmentStatusDTO);
        appointmentStatus = appointmentStatusRepository.save(appointmentStatus);
        return appointmentStatusMapper.toDto(appointmentStatus);
    }

    /**
     * Update a appointmentStatus.
     *
     * @param appointmentStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public AppointmentStatusDTO update(AppointmentStatusDTO appointmentStatusDTO) {
        log.debug("Request to update AppointmentStatus : {}", appointmentStatusDTO);
        AppointmentStatus appointmentStatus = appointmentStatusMapper.toEntity(appointmentStatusDTO);
        appointmentStatus = appointmentStatusRepository.save(appointmentStatus);
        return appointmentStatusMapper.toDto(appointmentStatus);
    }

    /**
     * Partially update a appointmentStatus.
     *
     * @param appointmentStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppointmentStatusDTO> partialUpdate(AppointmentStatusDTO appointmentStatusDTO) {
        log.debug("Request to partially update AppointmentStatus : {}", appointmentStatusDTO);

        return appointmentStatusRepository
            .findById(appointmentStatusDTO.getId())
            .map(existingAppointmentStatus -> {
                appointmentStatusMapper.partialUpdate(existingAppointmentStatus, appointmentStatusDTO);

                return existingAppointmentStatus;
            })
            .map(appointmentStatusRepository::save)
            .map(appointmentStatusMapper::toDto);
    }

    /**
     * Get all the appointmentStatuses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppointmentStatusDTO> findAll() {
        log.debug("Request to get all AppointmentStatuses");
        return appointmentStatusRepository
            .findAll()
            .stream()
            .map(appointmentStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one appointmentStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppointmentStatusDTO> findOne(Long id) {
        log.debug("Request to get AppointmentStatus : {}", id);
        return appointmentStatusRepository.findById(id).map(appointmentStatusMapper::toDto);
    }

    /**
     * Delete the appointmentStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppointmentStatus : {}", id);
        appointmentStatusRepository.deleteById(id);
    }
}
