package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.AppointmentStatus;
import com.one2n.sarathi.service.dto.AppointmentStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppointmentStatus} and its DTO {@link AppointmentStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppointmentStatusMapper extends EntityMapper<AppointmentStatusDTO, AppointmentStatus> {}
