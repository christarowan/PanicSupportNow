package com.zipcode.panic.repository;

import com.zipcode.panic.domain.PhoneLink;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PhoneLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneLinkRepository extends JpaRepository<PhoneLink, Long> {}
