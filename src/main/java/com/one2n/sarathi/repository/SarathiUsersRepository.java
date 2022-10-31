package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.SarathiUsers;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SarathiUsers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SarathiUsersRepository extends JpaRepository<SarathiUsers, Long> {}
