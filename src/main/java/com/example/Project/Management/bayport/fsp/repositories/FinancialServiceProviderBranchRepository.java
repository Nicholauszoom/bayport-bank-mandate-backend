package com.dflex.emkopo.app.fsp.repositories;

import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProviderBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FinancialServiceProviderBranchRepository extends JpaRepository<FinancialServiceProviderBranch, Long> {


    List<FinancialServiceProviderBranch> findFinancialServiceProviderBranchByDistrict_DistrictCode(String districtCode);
}
