package com.rafo.apps.task_retrofit.repository;

import com.rafo.apps.task_retrofit.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
}
