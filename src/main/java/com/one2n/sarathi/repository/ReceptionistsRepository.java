package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.Receptionists;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Receptionists entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceptionistsRepository extends JpaRepository<Receptionists, Long> {}
