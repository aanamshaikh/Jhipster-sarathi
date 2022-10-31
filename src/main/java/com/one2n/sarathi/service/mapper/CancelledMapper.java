package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.Cancelled;
import com.one2n.sarathi.service.dto.CancelledDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cancelled} and its DTO {@link CancelledDTO}.
 */
@Mapper(componentModel = "spring")
public interface CancelledMapper extends EntityMapper<CancelledDTO, Cancelled> {}
