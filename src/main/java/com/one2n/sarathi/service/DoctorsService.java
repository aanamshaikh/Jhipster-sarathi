package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.Doctors;
import com.one2n.sarathi.repository.DoctorsRepository;
import com.one2n.sarathi.service.dto.DoctorsDTO;
import com.one2n.sarathi.service.mapper.DoctorsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Doctors}.
 */
@Service
@Transactional
public class DoctorsService {

    private final Logger log = LoggerFactory.getLogger(DoctorsService.class);

    private final DoctorsRepository doctorsRepository;

    private final DoctorsMapper doctorsMapper;

    public DoctorsService(DoctorsRepository doctorsRepository, DoctorsMapper doctorsMapper) {
        this.doctorsRepository = doctorsRepository;
        this.doctorsMapper = doctorsMapper;
    }

    /**
     * Save a doctors.
     *
     * @param doctorsDTO the entity to save.
     * @return the persisted entity.
     */
    public DoctorsDTO save(DoctorsDTO doctorsDTO) {
        log.debug("Request to save Doctors : {}", doctorsDTO);
        Doctors doctors = doctorsMapper.toEntity(doctorsDTO);
        doctors = doctorsRepository.save(doctors);
        return doctorsMapper.toDto(doctors);
    }

    /**
     * Update a doctors.
     *
     * @param doctorsDTO the entity to save.
     * @return the persisted entity.
     */
    public DoctorsDTO update(DoctorsDTO doctorsDTO) {
        log.debug("Request to update Doctors : {}", doctorsDTO);
        Doctors doctors = doctorsMapper.toEntity(doctorsDTO);
        doctors = doctorsRepository.save(doctors);
        return doctorsMapper.toDto(doctors);
    }

    /**
     * Partially update a doctors.
     *
     * @param doctorsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DoctorsDTO> partialUpdate(DoctorsDTO doctorsDTO) {
        log.debug("Request to partially update Doctors : {}", doctorsDTO);

        return doctorsRepository
            .findById(doctorsDTO.getId())
            .map(existingDoctors -> {
                doctorsMapper.partialUpdate(existingDoctors, doctorsDTO);

                return existingDoctors;
            })
            .map(doctorsRepository::save)
            .map(doctorsMapper::toDto);
    }

    /**
     * Get all the doctors.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DoctorsDTO> findAll() {
        log.debug("Request to get all Doctors");
        return doctorsRepository.findAll().stream().map(doctorsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the doctors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DoctorsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return doctorsRepository.findAllWithEagerRelationships(pageable).map(doctorsMapper::toDto);
    }

    /**
     * Get one doctors by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DoctorsDTO> findOne(Long id) {
        log.debug("Request to get Doctors : {}", id);
        return doctorsRepository.findOneWithEagerRelationships(id).map(doctorsMapper::toDto);
    }

    /**
     * Delete the doctors by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Doctors : {}", id);
        doctorsRepository.deleteById(id);
    }
}
