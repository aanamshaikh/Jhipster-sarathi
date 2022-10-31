package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.Weekdays;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Weekdays entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeekdaysRepository extends JpaRepository<Weekdays, Long> {}
