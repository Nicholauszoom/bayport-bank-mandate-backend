package com.example.Project.Management.bayport.setup.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@XmlType(propOrder = {"branchCode", "branchName"})
public
class BranchDto {
    private String branchCode;
    private String branchName;

    @XmlElement(name = "BranchCode")
    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    @XmlElement(name = "BranchName")
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}