package com.example.Project.Management.bayport.fsp.repositories;

import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProviderBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FinancialServiceProviderBranchRepository extends JpaRepository<FinancialServiceProviderBranch, Long> {

    List<FinancialServiceProviderBranch> findFinancialServiceProviderBranchByDistrict_DistrictCode(String districtCode);
}
