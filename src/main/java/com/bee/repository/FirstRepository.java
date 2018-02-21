package com.bee.repository;

import com.bee.domain.First;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the First entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FirstRepository extends JpaRepository<First, Long> {

}
