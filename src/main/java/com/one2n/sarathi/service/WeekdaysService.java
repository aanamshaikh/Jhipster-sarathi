package com.one2n.sarathi.service;

import com.one2n.sarathi.domain.Weekdays;
import com.one2n.sarathi.repository.WeekdaysRepository;
import com.one2n.sarathi.service.dto.WeekdaysDTO;
import com.one2n.sarathi.service.mapper.WeekdaysMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Weekdays}.
 */
@Service
@Transactional
public class WeekdaysService {

    private final Logger log = LoggerFactory.getLogger(WeekdaysService.class);

    private final WeekdaysRepository weekdaysRepository;

    private final WeekdaysMapper weekdaysMapper;

    public WeekdaysService(WeekdaysRepository weekdaysRepository, WeekdaysMapper weekdaysMapper) {
        this.weekdaysRepository = weekdaysRepository;
        this.weekdaysMapper = weekdaysMapper;
    }

    /**
     * Save a weekdays.
     *
     * @param weekdaysDTO the entity to save.
     * @return the persisted entity.
     */
    public WeekdaysDTO save(WeekdaysDTO weekdaysDTO) {
        log.debug("Request to save Weekdays : {}", weekdaysDTO);
        Weekdays weekdays = weekdaysMapper.toEntity(weekdaysDTO);
        weekdays = weekdaysRepository.save(weekdays);
        return weekdaysMapper.toDto(weekdays);
    }

    /**
     * Update a weekdays.
     *
     * @param weekdaysDTO the entity to save.
     * @return the persisted entity.
     */
    public WeekdaysDTO update(WeekdaysDTO weekdaysDTO) {
        log.debug("Request to update Weekdays : {}", weekdaysDTO);
        Weekdays weekdays = weekdaysMapper.toEntity(weekdaysDTO);
        weekdays = weekdaysRepository.save(weekdays);
        return weekdaysMapper.toDto(weekdays);
    }

    /**
     * Partially update a weekdays.
     *
     * @param weekdaysDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WeekdaysDTO> partialUpdate(WeekdaysDTO weekdaysDTO) {
        log.debug("Request to partially update Weekdays : {}", weekdaysDTO);

        return weekdaysRepository
            .findById(weekdaysDTO.getId())
            .map(existingWeekdays -> {
                weekdaysMapper.partialUpdate(existingWeekdays, weekdaysDTO);

                return existingWeekdays;
            })
            .map(weekdaysRepository::save)
            .map(weekdaysMapper::toDto);
    }

    /**
     * Get all the weekdays.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WeekdaysDTO> findAll() {
        log.debug("Request to get all Weekdays");
        return weekdaysRepository.findAll().stream().map(weekdaysMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one weekdays by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WeekdaysDTO> findOne(Long id) {
        log.debug("Request to get Weekdays : {}", id);
        return weekdaysRepository.findById(id).map(weekdaysMapper::toDto);
    }

    /**
     * Delete the weekdays by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Weekdays : {}", id);
        weekdaysRepository.deleteById(id);
    }
}
