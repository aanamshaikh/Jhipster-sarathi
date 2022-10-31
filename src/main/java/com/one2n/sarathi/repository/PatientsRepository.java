package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.Patients;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Patients entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientsRepository extends JpaRepository<Patients, Long> {}
