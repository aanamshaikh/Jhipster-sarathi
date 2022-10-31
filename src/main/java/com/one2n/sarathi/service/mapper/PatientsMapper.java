package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.Patients;
import com.one2n.sarathi.domain.SarathiUsers;
import com.one2n.sarathi.service.dto.PatientsDTO;
import com.one2n.sarathi.service.dto.SarathiUsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Patients} and its DTO {@link PatientsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientsMapper extends EntityMapper<PatientsDTO, Patients> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "sarathiUsersId")
    PatientsDTO toDto(Patients s);

    @Named("sarathiUsersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SarathiUsersDTO toDtoSarathiUsersId(SarathiUsers sarathiUsers);
}
