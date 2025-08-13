package com.dflex.emkopo.app.setup.dto;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.List;

@XmlRootElement(name = "MessageDetails")
@XmlType(propOrder = {"branchDetails"})
public class MessageDetailsDto {
    private List<BranchDetailDto> branchDetails;

    @XmlElement(name = "BranchDetail")
    public List<BranchDetailDto> getBranchDetails() {
        return branchDetails;
    }

    public void setBranchDetails(List<BranchDetailDto> branchDetails) {
        this.branchDetails = branchDetails;
    }
}

