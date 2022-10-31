package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.Weekdays;
import com.one2n.sarathi.service.dto.WeekdaysDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Weekdays} and its DTO {@link WeekdaysDTO}.
 */
@Mapper(componentModel = "spring")
public interface WeekdaysMapper extends EntityMapper<WeekdaysDTO, Weekdays> {}
