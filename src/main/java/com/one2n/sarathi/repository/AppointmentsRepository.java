package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.Appointments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Appointments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {}
