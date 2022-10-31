package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.Patients;
import com.one2n.sarathi.repository.PatientsRepository;
import com.one2n.sarathi.service.dto.PatientsDTO;
import com.one2n.sarathi.service.mapper.PatientsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Patients}.
 */
@Service
@Transactional
public class PatientsService {

    private final Logger log = LoggerFactory.getLogger(PatientsService.class);

    private final PatientsRepository patientsRepository;

    private final PatientsMapper patientsMapper;

    public PatientsService(PatientsRepository patientsRepository, PatientsMapper patientsMapper) {
        this.patientsRepository = patientsRepository;
        this.patientsMapper = patientsMapper;
    }

    /**
     * Save a patients.
     *
     * @param patientsDTO the entity to save.
     * @return the persisted entity.
     */
    public PatientsDTO save(PatientsDTO patientsDTO) {
        log.debug("Request to save Patients : {}", patientsDTO);
        Patients patients = patientsMapper.toEntity(patientsDTO);
        patients = patientsRepository.save(patients);
        return patientsMapper.toDto(patients);
    }

    /**
     * Update a patients.
     *
     * @param patientsDTO the entity to save.
     * @return the persisted entity.
     */
    public PatientsDTO update(PatientsDTO patientsDTO) {
        log.debug("Request to update Patients : {}", patientsDTO);
        Patients patients = patientsMapper.toEntity(patientsDTO);
        patients = patientsRepository.save(patients);
        return patientsMapper.toDto(patients);
    }

    /**
     * Partially update a patients.
     *
     * @param patientsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PatientsDTO> partialUpdate(PatientsDTO patientsDTO) {
        log.debug("Request to partially update Patients : {}", patientsDTO);

        return patientsRepository
            .findById(patientsDTO.getId())
            .map(existingPatients -> {
                patientsMapper.partialUpdate(existingPatients, patientsDTO);

                return existingPatients;
            })
            .map(patientsRepository::save)
            .map(patientsMapper::toDto);
    }

    /**
     * Get all the patients.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PatientsDTO> findAll() {
        log.debug("Request to get all Patients");
        return patientsRepository.findAll().stream().map(patientsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one patients by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatientsDTO> findOne(Long id) {
        log.debug("Request to get Patients : {}", id);
        return patientsRepository.findById(id).map(patientsMapper::toDto);
    }

    /**
     * Delete the patients by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Patients : {}", id);
        patientsRepository.deleteById(id);
    }
}
