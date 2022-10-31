package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.Appointments;
import com.one2n.sarathi.domain.Consultations;
import com.one2n.sarathi.service.dto.AppointmentsDTO;
import com.one2n.sarathi.service.dto.ConsultationsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Consultations} and its DTO {@link ConsultationsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsultationsMapper extends EntityMapper<ConsultationsDTO, Consultations> {
    @Mapping(target = "appointment", source = "appointment", qualifiedByName = "appointmentsId")
    ConsultationsDTO toDto(Consultations s);

    @Named("appointmentsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppointmentsDTO toDtoAppointmentsId(Appointments appointments);
}
