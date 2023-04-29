package com.zipcode.panic.repository;

import com.zipcode.panic.domain.Soundtrack;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Soundtrack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoundtrackRepository extends JpaRepository<Soundtrack, Long> {}
