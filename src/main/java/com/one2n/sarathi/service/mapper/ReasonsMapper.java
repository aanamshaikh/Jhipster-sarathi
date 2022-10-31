package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.Reasons;
import com.one2n.sarathi.service.dto.ReasonsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reasons} and its DTO {@link ReasonsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReasonsMapper extends EntityMapper<ReasonsDTO, Reasons> {}
