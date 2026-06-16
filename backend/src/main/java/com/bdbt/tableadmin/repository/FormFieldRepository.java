package com.bdbt.tableadmin.repository;

import com.bdbt.tableadmin.domain.FormField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormFieldRepository extends JpaRepository<FormField, Long> {
}
