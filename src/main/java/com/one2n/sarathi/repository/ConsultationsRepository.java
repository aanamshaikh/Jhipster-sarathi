package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.Consultations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Consultations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultationsRepository extends JpaRepository<Consultations, Long> {}
