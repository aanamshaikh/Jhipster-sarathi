package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.AppointmentStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppointmentStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Long> {}
