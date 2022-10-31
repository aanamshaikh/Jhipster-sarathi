package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.Receptionists;
import com.one2n.sarathi.repository.ReceptionistsRepository;
import com.one2n.sarathi.service.dto.ReceptionistsDTO;
import com.one2n.sarathi.service.mapper.ReceptionistsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Receptionists}.
 */
@Service
@Transactional
public class ReceptionistsService {

    private final Logger log = LoggerFactory.getLogger(ReceptionistsService.class);

    private final ReceptionistsRepository receptionistsRepository;

    private final ReceptionistsMapper receptionistsMapper;

    public ReceptionistsService(ReceptionistsRepository receptionistsRepository, ReceptionistsMapper receptionistsMapper) {
        this.receptionistsRepository = receptionistsRepository;
        this.receptionistsMapper = receptionistsMapper;
    }

    /**
     * Save a receptionists.
     *
     * @param receptionistsDTO the entity to save.
     * @return the persisted entity.
     */
    public ReceptionistsDTO save(ReceptionistsDTO receptionistsDTO) {
        log.debug("Request to save Receptionists : {}", receptionistsDTO);
        Receptionists receptionists = receptionistsMapper.toEntity(receptionistsDTO);
        receptionists = receptionistsRepository.save(receptionists);
        return receptionistsMapper.toDto(receptionists);
    }

    /**
     * Update a receptionists.
     *
     * @param receptionistsDTO the entity to save.
     * @return the persisted entity.
     */
    public ReceptionistsDTO update(ReceptionistsDTO receptionistsDTO) {
        log.debug("Request to update Receptionists : {}", receptionistsDTO);
        Receptionists receptionists = receptionistsMapper.toEntity(receptionistsDTO);
        receptionists = receptionistsRepository.save(receptionists);
        return receptionistsMapper.toDto(receptionists);
    }

    /**
     * Partially update a receptionists.
     *
     * @param receptionistsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReceptionistsDTO> partialUpdate(ReceptionistsDTO receptionistsDTO) {
        log.debug("Request to partially update Receptionists : {}", receptionistsDTO);

        return receptionistsRepository
            .findById(receptionistsDTO.getId())
            .map(existingReceptionists -> {
                receptionistsMapper.partialUpdate(existingReceptionists, receptionistsDTO);

                return existingReceptionists;
            })
            .map(receptionistsRepository::save)
            .map(receptionistsMapper::toDto);
    }

    /**
     * Get all the receptionists.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReceptionistsDTO> findAll() {
        log.debug("Request to get all Receptionists");
        return receptionistsRepository.findAll().stream().map(receptionistsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one receptionists by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReceptionistsDTO> findOne(Long id) {
        log.debug("Request to get Receptionists : {}", id);
        return receptionistsRepository.findById(id).map(receptionistsMapper::toDto);
    }

    /**
     * Delete the receptionists by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Receptionists : {}", id);
        receptionistsRepository.deleteById(id);
    }
}
