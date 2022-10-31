package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.Specialisations;
import com.one2n.sarathi.service.dto.SpecialisationsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specialisations} and its DTO {@link SpecialisationsDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialisationsMapper extends EntityMapper<SpecialisationsDTO, Specialisations> {}
