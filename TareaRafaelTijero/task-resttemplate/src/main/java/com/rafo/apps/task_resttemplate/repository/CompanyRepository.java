package com.rafo.apps.task_resttemplate.repository;

import com.rafo.apps.task_resttemplate.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
}
