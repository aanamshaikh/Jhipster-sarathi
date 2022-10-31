package com.one2n.sarathi.repository;

import com.one2n.sarathi.domain.Doctors;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Doctors entity.
 */
@Repository
public interface DoctorsRepository extends JpaRepository<Doctors, Long> {
    default Optional<Doctors> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Doctors> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Doctors> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct doctors from Doctors doctors left join fetch doctors.specialisation",
        countQuery = "select count(distinct doctors) from Doctors doctors"
    )
    Page<Doctors> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct doctors from Doctors doctors left join fetch doctors.specialisation")
    List<Doctors> findAllWithToOneRelationships();

    @Query("select doctors from Doctors doctors left join fetch doctors.specialisation where doctors.id =:id")
    Optional<Doctors> findOneWithToOneRelationships(@Param("id") Long id);
}
