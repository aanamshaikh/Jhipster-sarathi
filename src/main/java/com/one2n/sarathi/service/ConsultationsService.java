package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.Consultations;
import com.one2n.sarathi.repository.ConsultationsRepository;
import com.one2n.sarathi.service.dto.ConsultationsDTO;
import com.one2n.sarathi.service.mapper.ConsultationsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Consultations}.
 */
@Service
@Transactional
public class ConsultationsService {

    private final Logger log = LoggerFactory.getLogger(ConsultationsService.class);

    private final ConsultationsRepository consultationsRepository;

    private final ConsultationsMapper consultationsMapper;

    public ConsultationsService(ConsultationsRepository consultationsRepository, ConsultationsMapper consultationsMapper) {
        this.consultationsRepository = consultationsRepository;
        this.consultationsMapper = consultationsMapper;
    }

    /**
     * Save a consultations.
     *
     * @param consultationsDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultationsDTO save(ConsultationsDTO consultationsDTO) {
        log.debug("Request to save Consultations : {}", consultationsDTO);
        Consultations consultations = consultationsMapper.toEntity(consultationsDTO);
        consultations = consultationsRepository.save(consultations);
        return consultationsMapper.toDto(consultations);
    }

    /**
     * Update a consultations.
     *
     * @param consultationsDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultationsDTO update(ConsultationsDTO consultationsDTO) {
        log.debug("Request to update Consultations : {}", consultationsDTO);
        Consultations consultations = consultationsMapper.toEntity(consultationsDTO);
        consultations = consultationsRepository.save(consultations);
        return consultationsMapper.toDto(consultations);
    }

    /**
     * Partially update a consultations.
     *
     * @param consultationsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConsultationsDTO> partialUpdate(ConsultationsDTO consultationsDTO) {
        log.debug("Request to partially update Consultations : {}", consultationsDTO);

        return consultationsRepository
            .findById(consultationsDTO.getId())
            .map(existingConsultations -> {
                consultationsMapper.partialUpdate(existingConsultations, consultationsDTO);

                return existingConsultations;
            })
            .map(consultationsRepository::save)
            .map(consultationsMapper::toDto);
    }

    /**
     * Get all the consultations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsultationsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Consultations");
        return consultationsRepository.findAll(pageable).map(consultationsMapper::toDto);
    }

    /**
     * Get one consultations by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConsultationsDTO> findOne(Long id) {
        log.debug("Request to get Consultations : {}", id);
        return consultationsRepository.findById(id).map(consultationsMapper::toDto);
    }

    /**
     * Delete the consultations by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Consultations : {}", id);
        consultationsRepository.deleteById(id);
    }
}
