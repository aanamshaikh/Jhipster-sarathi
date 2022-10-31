package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.Cancelled;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cancelled entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CancelledRepository extends JpaRepository<Cancelled, Long> {}
