package com.dflex.emkopo.app.setup.dto;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.List;


@XmlType(propOrder = {"districtCode", "branches"})
public class BranchDetailDto {
    private String districtCode;
    private List<BranchDto> branches;

    @XmlElement(name = "DistrictCode")
    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    @XmlElement(name = "Branch")
    public List<BranchDto> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDto> branches) {
        this.branches = branches;
    }
}