package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.Cancelled;
import com.one2n.sarathi.repository.CancelledRepository;
import com.one2n.sarathi.service.dto.CancelledDTO;
import com.one2n.sarathi.service.mapper.CancelledMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cancelled}.
 */
@Service
@Transactional
public class CancelledService {

    private final Logger log = LoggerFactory.getLogger(CancelledService.class);

    private final CancelledRepository cancelledRepository;

    private final CancelledMapper cancelledMapper;

    public CancelledService(CancelledRepository cancelledRepository, CancelledMapper cancelledMapper) {
        this.cancelledRepository = cancelledRepository;
        this.cancelledMapper = cancelledMapper;
    }

    /**
     * Save a cancelled.
     *
     * @param cancelledDTO the entity to save.
     * @return the persisted entity.
     */
    public CancelledDTO save(CancelledDTO cancelledDTO) {
        log.debug("Request to save Cancelled : {}", cancelledDTO);
        Cancelled cancelled = cancelledMapper.toEntity(cancelledDTO);
        cancelled = cancelledRepository.save(cancelled);
        return cancelledMapper.toDto(cancelled);
    }

    /**
     * Update a cancelled.
     *
     * @param cancelledDTO the entity to save.
     * @return the persisted entity.
     */
    public CancelledDTO update(CancelledDTO cancelledDTO) {
        log.debug("Request to update Cancelled : {}", cancelledDTO);
        Cancelled cancelled = cancelledMapper.toEntity(cancelledDTO);
        cancelled = cancelledRepository.save(cancelled);
        return cancelledMapper.toDto(cancelled);
    }

    /**
     * Partially update a cancelled.
     *
     * @param cancelledDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CancelledDTO> partialUpdate(CancelledDTO cancelledDTO) {
        log.debug("Request to partially update Cancelled : {}", cancelledDTO);

        return cancelledRepository
            .findById(cancelledDTO.getId())
            .map(existingCancelled -> {
                cancelledMapper.partialUpdate(existingCancelled, cancelledDTO);

                return existingCancelled;
            })
            .map(cancelledRepository::save)
            .map(cancelledMapper::toDto);
    }

    /**
     * Get all the cancelleds.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CancelledDTO> findAll() {
        log.debug("Request to get all Cancelleds");
        return cancelledRepository.findAll().stream().map(cancelledMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one cancelled by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CancelledDTO> findOne(Long id) {
        log.debug("Request to get Cancelled : {}", id);
        return cancelledRepository.findById(id).map(cancelledMapper::toDto);
    }

    /**
     * Delete the cancelled by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cancelled : {}", id);
        cancelledRepository.deleteById(id);
    }
}
