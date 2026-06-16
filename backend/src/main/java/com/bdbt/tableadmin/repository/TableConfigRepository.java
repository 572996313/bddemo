package com.bdbt.tableadmin.repository;

import com.bdbt.tableadmin.domain.TableConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableConfigRepository extends JpaRepository<TableConfig, Long> {

    boolean existsByTableCode(String tableCode);

    Optional<TableConfig> findByTableCode(String tableCode);
}
