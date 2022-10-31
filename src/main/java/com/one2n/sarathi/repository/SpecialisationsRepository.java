package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.Specialisations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Specialisations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialisationsRepository extends JpaRepository<Specialisations, Long> {}
