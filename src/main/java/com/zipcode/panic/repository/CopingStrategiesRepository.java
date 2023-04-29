package com.zipcode.panic.repository;

import com.zipcode.panic.domain.CopingStrategies;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CopingStrategies entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CopingStrategiesRepository extends JpaRepository<CopingStrategies, Long> {}
