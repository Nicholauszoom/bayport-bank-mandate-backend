package com.example.Project.Management.bayport.fsp.repositories;

import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProviderCategory;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FSPCategoryRepository extends JpaRepository<FinancialServiceProviderCategory, Long> {
    List<FinancialServiceProviderCategory> findAll();
}
