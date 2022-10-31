package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.Receptionists;
import com.one2n.sarathi.service.dto.ReceptionistsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Receptionists} and its DTO {@link ReceptionistsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReceptionistsMapper extends EntityMapper<ReceptionistsDTO, Receptionists> {}
