package com.zipcode.panic.repository;

import com.zipcode.panic.domain.ActionPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ActionPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionPlanRepository extends JpaRepository<ActionPlan, Long> {}
