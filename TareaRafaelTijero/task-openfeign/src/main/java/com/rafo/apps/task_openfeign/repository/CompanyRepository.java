package com.rafo.apps.task_openfeign.repository;

import com.rafo.apps.task_openfeign.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
}
