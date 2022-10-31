package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.AppointmentStatus;
import com.one2n.sarathi.domain.Appointments;
import com.one2n.sarathi.domain.Cancelled;
import com.one2n.sarathi.domain.Doctors;
import com.one2n.sarathi.domain.Patients;
import com.one2n.sarathi.domain.Reasons;
import com.one2n.sarathi.service.dto.AppointmentStatusDTO;
import com.one2n.sarathi.service.dto.AppointmentsDTO;
import com.one2n.sarathi.service.dto.CancelledDTO;
import com.one2n.sarathi.service.dto.DoctorsDTO;
import com.one2n.sarathi.service.dto.PatientsDTO;
import com.one2n.sarathi.service.dto.ReasonsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointments} and its DTO {@link AppointmentsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppointmentsMapper extends EntityMapper<AppointmentsDTO, Appointments> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientsId")
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "doctorsId")
    @Mapping(target = "reason", source = "reason", qualifiedByName = "reasonsId")
    @Mapping(target = "cancelReason", source = "cancelReason", qualifiedByName = "cancelledId")
    @Mapping(target = "status", source = "status", qualifiedByName = "appointmentStatusId")
    AppointmentsDTO toDto(Appointments s);

    @Named("patientsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientsDTO toDtoPatientsId(Patients patients);

    @Named("doctorsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoctorsDTO toDtoDoctorsId(Doctors doctors);

    @Named("reasonsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReasonsDTO toDtoReasonsId(Reasons reasons);

    @Named("cancelledId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CancelledDTO toDtoCancelledId(Cancelled cancelled);

    @Named("appointmentStatusId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppointmentStatusDTO toDtoAppointmentStatusId(AppointmentStatus appointmentStatus);
}
