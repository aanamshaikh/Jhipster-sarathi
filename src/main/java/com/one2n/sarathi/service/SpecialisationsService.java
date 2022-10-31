package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.Specialisations;
import com.one2n.sarathi.repository.SpecialisationsRepository;
import com.one2n.sarathi.service.dto.SpecialisationsDTO;
import com.one2n.sarathi.service.mapper.SpecialisationsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Specialisations}.
 */
@Service
@Transactional
public class SpecialisationsService {

    private final Logger log = LoggerFactory.getLogger(SpecialisationsService.class);

    private final SpecialisationsRepository specialisationsRepository;

    private final SpecialisationsMapper specialisationsMapper;

    public SpecialisationsService(SpecialisationsRepository specialisationsRepository, SpecialisationsMapper specialisationsMapper) {
        this.specialisationsRepository = specialisationsRepository;
        this.specialisationsMapper = specialisationsMapper;
    }

    /**
     * Save a specialisations.
     *
     * @param specialisationsDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecialisationsDTO save(SpecialisationsDTO specialisationsDTO) {
        log.debug("Request to save Specialisations : {}", specialisationsDTO);
        Specialisations specialisations = specialisationsMapper.toEntity(specialisationsDTO);
        specialisations = specialisationsRepository.save(specialisations);
        return specialisationsMapper.toDto(specialisations);
    }

    /**
     * Update a specialisations.
     *
     * @param specialisationsDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecialisationsDTO update(SpecialisationsDTO specialisationsDTO) {
        log.debug("Request to update Specialisations : {}", specialisationsDTO);
        Specialisations specialisations = specialisationsMapper.toEntity(specialisationsDTO);
        specialisations = specialisationsRepository.save(specialisations);
        return specialisationsMapper.toDto(specialisations);
    }

    /**
     * Partially update a specialisations.
     *
     * @param specialisationsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SpecialisationsDTO> partialUpdate(SpecialisationsDTO specialisationsDTO) {
        log.debug("Request to partially update Specialisations : {}", specialisationsDTO);

        return specialisationsRepository
            .findById(specialisationsDTO.getId())
            .map(existingSpecialisations -> {
                specialisationsMapper.partialUpdate(existingSpecialisations, specialisationsDTO);

                return existingSpecialisations;
            })
            .map(specialisationsRepository::save)
            .map(specialisationsMapper::toDto);
    }

    /**
     * Get all the specialisations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SpecialisationsDTO> findAll() {
        log.debug("Request to get all Specialisations");
        return specialisationsRepository
            .findAll()
            .stream()
            .map(specialisationsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one specialisations by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpecialisationsDTO> findOne(Long id) {
        log.debug("Request to get Specialisations : {}", id);
        return specialisationsRepository.findById(id).map(specialisationsMapper::toDto);
    }

    /**
     * Delete the specialisations by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Specialisations : {}", id);
        specialisationsRepository.deleteById(id);
    }
}
