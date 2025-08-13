package com.dflex.emkopo.app.fsp.repositories;

import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProviderCategory;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FSPCategoryRepository extends JpaRepository<FinancialServiceProviderCategory, Long> {
    @Query(value = "SELECT c FROM FinancialServiceProviderCategory c WHERE c.deleted = false")
    @NonNull
    List<FinancialServiceProviderCategory> findAll();
}
