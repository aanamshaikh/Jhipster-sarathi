package com.one2n.sarathi.service.mapper;

import com.one2n.sarathi.domain.SarathiUsers;
import com.one2n.sarathi.service.dto.SarathiUsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SarathiUsers} and its DTO {@link SarathiUsersDTO}.
 */
@Mapper(componentModel = "spring")
public interface SarathiUsersMapper extends EntityMapper<SarathiUsersDTO, SarathiUsers> {}
