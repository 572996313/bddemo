package com.bdbt.tableadmin.repository;

import com.bdbt.tableadmin.domain.QueryCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryConditionRepository extends JpaRepository<QueryCondition, Long> {
}
