package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.Doctors;
import com.one2n.sarathi.domain.Specialisations;
import com.one2n.sarathi.service.dto.DoctorsDTO;
import com.one2n.sarathi.service.dto.SpecialisationsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Doctors} and its DTO {@link DoctorsDTO}.
 */
@Mapper(componentModel = "spring")
public interface DoctorsMapper extends EntityMapper<DoctorsDTO, Doctors> {
    @Mapping(target = "specialisation", source = "specialisation", qualifiedByName = "specialisationsName")
    DoctorsDTO toDto(Doctors s);

    @Named("specialisationsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SpecialisationsDTO toDtoSpecialisationsName(Specialisations specialisations);
}
