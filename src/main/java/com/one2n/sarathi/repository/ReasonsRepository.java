package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.Reasons;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reasons entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReasonsRepository extends JpaRepository<Reasons, Long> {}
