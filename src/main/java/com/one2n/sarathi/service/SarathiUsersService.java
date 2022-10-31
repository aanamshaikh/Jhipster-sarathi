package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.SarathiUsers;
import com.one2n.sarathi.repository.SarathiUsersRepository;
import com.one2n.sarathi.service.dto.SarathiUsersDTO;
import com.one2n.sarathi.service.mapper.SarathiUsersMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SarathiUsers}.
 */
@Service
@Transactional
public class SarathiUsersService {

    private final Logger log = LoggerFactory.getLogger(SarathiUsersService.class);

    private final SarathiUsersRepository sarathiUsersRepository;

    private final SarathiUsersMapper sarathiUsersMapper;

    public SarathiUsersService(SarathiUsersRepository sarathiUsersRepository, SarathiUsersMapper sarathiUsersMapper) {
        this.sarathiUsersRepository = sarathiUsersRepository;
        this.sarathiUsersMapper = sarathiUsersMapper;
    }

    /**
     * Save a sarathiUsers.
     *
     * @param sarathiUsersDTO the entity to save.
     * @return the persisted entity.
     */
    public SarathiUsersDTO save(SarathiUsersDTO sarathiUsersDTO) {
        log.debug("Request to save SarathiUsers : {}", sarathiUsersDTO);
        SarathiUsers sarathiUsers = sarathiUsersMapper.toEntity(sarathiUsersDTO);
        sarathiUsers = sarathiUsersRepository.save(sarathiUsers);
        return sarathiUsersMapper.toDto(sarathiUsers);
    }

    /**
     * Update a sarathiUsers.
     *
     * @param sarathiUsersDTO the entity to save.
     * @return the persisted entity.
     */
    public SarathiUsersDTO update(SarathiUsersDTO sarathiUsersDTO) {
        log.debug("Request to update SarathiUsers : {}", sarathiUsersDTO);
        SarathiUsers sarathiUsers = sarathiUsersMapper.toEntity(sarathiUsersDTO);
        sarathiUsers = sarathiUsersRepository.save(sarathiUsers);
        return sarathiUsersMapper.toDto(sarathiUsers);
    }

    /**
     * Partially update a sarathiUsers.
     *
     * @param sarathiUsersDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SarathiUsersDTO> partialUpdate(SarathiUsersDTO sarathiUsersDTO) {
        log.debug("Request to partially update SarathiUsers : {}", sarathiUsersDTO);

        return sarathiUsersRepository
            .findById(sarathiUsersDTO.getId())
            .map(existingSarathiUsers -> {
                sarathiUsersMapper.partialUpdate(existingSarathiUsers, sarathiUsersDTO);

                return existingSarathiUsers;
            })
            .map(sarathiUsersRepository::save)
            .map(sarathiUsersMapper::toDto);
    }

    /**
     * Get all the sarathiUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SarathiUsersDTO> findAll() {
        log.debug("Request to get all SarathiUsers");
        return sarathiUsersRepository.findAll().stream().map(sarathiUsersMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one sarathiUsers by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SarathiUsersDTO> findOne(Long id) {
        log.debug("Request to get SarathiUsers : {}", id);
        return sarathiUsersRepository.findById(id).map(sarathiUsersMapper::toDto);
    }

    /**
     * Delete the sarathiUsers by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SarathiUsers : {}", id);
        sarathiUsersRepository.deleteById(id);
    }
}
