package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.Reasons;
import com.one2n.sarathi.repository.ReasonsRepository;
import com.one2n.sarathi.service.dto.ReasonsDTO;
import com.one2n.sarathi.service.mapper.ReasonsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reasons}.
 */
@Service
@Transactional
public class ReasonsService {

    private final Logger log = LoggerFactory.getLogger(ReasonsService.class);

    private final ReasonsRepository reasonsRepository;

    private final ReasonsMapper reasonsMapper;

    public ReasonsService(ReasonsRepository reasonsRepository, ReasonsMapper reasonsMapper) {
        this.reasonsRepository = reasonsRepository;
        this.reasonsMapper = reasonsMapper;
    }

    /**
     * Save a reasons.
     *
     * @param reasonsDTO the entity to save.
     * @return the persisted entity.
     */
    public ReasonsDTO save(ReasonsDTO reasonsDTO) {
        log.debug("Request to save Reasons : {}", reasonsDTO);
        Reasons reasons = reasonsMapper.toEntity(reasonsDTO);
        reasons = reasonsRepository.save(reasons);
        return reasonsMapper.toDto(reasons);
    }

    /**
     * Update a reasons.
     *
     * @param reasonsDTO the entity to save.
     * @return the persisted entity.
     */
    public ReasonsDTO update(ReasonsDTO reasonsDTO) {
        log.debug("Request to update Reasons : {}", reasonsDTO);
        Reasons reasons = reasonsMapper.toEntity(reasonsDTO);
        reasons = reasonsRepository.save(reasons);
        return reasonsMapper.toDto(reasons);
    }

    /**
     * Partially update a reasons.
     *
     * @param reasonsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReasonsDTO> partialUpdate(ReasonsDTO reasonsDTO) {
        log.debug("Request to partially update Reasons : {}", reasonsDTO);

        return reasonsRepository
            .findById(reasonsDTO.getId())
            .map(existingReasons -> {
                reasonsMapper.partialUpdate(existingReasons, reasonsDTO);

                return existingReasons;
            })
            .map(reasonsRepository::save)
            .map(reasonsMapper::toDto);
    }

    /**
     * Get all the reasons.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReasonsDTO> findAll() {
        log.debug("Request to get all Reasons");
        return reasonsRepository.findAll().stream().map(reasonsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reasons by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReasonsDTO> findOne(Long id) {
        log.debug("Request to get Reasons : {}", id);
        return reasonsRepository.findById(id).map(reasonsMapper::toDto);
    }

    /**
     * Delete the reasons by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reasons : {}", id);
        reasonsRepository.deleteById(id);
    }
}
