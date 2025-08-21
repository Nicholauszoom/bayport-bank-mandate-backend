package com.example.Project.Management.bayport.fsp.services;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
import com.example.Project.Management.bayport.apis.dto.GenericApiReqDataDto;
import com.example.Project.Management.bayport.apis.dto.GenericApiReqDataDtoWrapper;
import com.example.Project.Management.bayport.common.Constants;
import com.example.Project.Management.bayport.common.DigitalSignature;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProviderBranch;
import com.example.Project.Management.bayport.fsp.repositories.FinancialServiceProviderBranchRepository;
import com.example.Project.Management.bayport.fsp.repositories.FinancialServiceProviderRepository;
import com.example.Project.Management.bayport.setup.dto.BranchDetailDto;
import com.example.Project.Management.bayport.setup.dto.BranchDto;
import com.example.Project.Management.bayport.setup.entity.District;
import com.example.Project.Management.bayport.setup.repository.DistrictRepository;
import com.example.Project.Management.bayport.util.ResponseCodes;
import com.example.Project.Management.bayport.util.Utils;
import jakarta.xml.bind.JAXBContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class FinancialServiceProviderBranchServiceImpl implements FinancialServiceProviderBranchService {



    private Utils utils;

    private DistrictRepository districtRepository;

    private FinancialServiceProviderRepository financialServiceProviderRepository;

    private FinancialServiceProviderBranchRepository financialServiceProviderBranchRepository;

    private DigitalSignature digitalSignature;

    @Autowired
    void setDigitalSignature(DigitalSignature digitalSignature){
        this.digitalSignature = digitalSignature;
    }

    @Autowired
    void setFinancialServiceProviderBranchRepository(FinancialServiceProviderBranchRepository financialServiceProviderBranchRepository){
        this.financialServiceProviderBranchRepository = financialServiceProviderBranchRepository;
    }

    @Autowired
    void setDistrictRepository(DistrictRepository districtRepository){
        this.districtRepository = districtRepository;
    }

    @Autowired
    void setUtils(Utils utils){
        this.utils = utils;
    }

    @Autowired
    void setFinancialServiceProviderRepository(FinancialServiceProviderRepository financialServiceProviderRepository){
        this.financialServiceProviderRepository = financialServiceProviderRepository;
    }

    @Override
    public ResponseEntity<String> getFspBranches(String fspCode) {

        try {
            FinancialServiceProvider financialServiceProvider =
                    financialServiceProviderRepository.findByFspCodeAndRecordStatusId(fspCode, Constants.RECORD_STATUS_ACTIVE);


            if(financialServiceProvider == null || financialServiceProvider.getBranches().isEmpty()){

            }

            String receipt = utils.getReceipt(fspCode);
            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);
            String sender = fspByCode.get().getFspShortName();
            ApiReqHeaderDto apiReqHeaderDto = utils.getApiRequestHeader(fspCode,receipt, Constants.FSP_BRANCHES, sender);
            List<District> districtList = districtRepository.findAll();
            List<BranchDetailDto> branchDetailDtos= new ArrayList<>();


            for (District district : districtList) {
                BranchDetailDto  branchDetails = new BranchDetailDto();
                branchDetails.setDistrictCode(district.getDistrictCode());
                List<FinancialServiceProviderBranch> branches = financialServiceProviderBranchRepository.
                        findFinancialServiceProviderBranchByDistrict_DistrictCode(district.getDistrictCode());

                for (FinancialServiceProviderBranch branch : branches) {
                    branchDetails.getBranches().add(new BranchDto(branch.getBranchCode(), branch.getBranchName()));
                }

                branchDetailDtos.add(branchDetails);
            }


            GenericApiReqDataDto genericApiReqDataDto = new GenericApiReqDataDto();
            genericApiReqDataDto.setMessageDetails(branchDetailDtos);
            genericApiReqDataDto.setRequestHeader(apiReqHeaderDto);


            String stringToBeSigned = utils.convertXmlToString(JAXBContext.newInstance(GenericApiReqDataDto.class), genericApiReqDataDto);
            String signature = digitalSignature.createSignature(stringToBeSigned);

            GenericApiReqDataDtoWrapper genericApiReqDataDtoWrapper = new GenericApiReqDataDtoWrapper();
            genericApiReqDataDtoWrapper.setRequestData(genericApiReqDataDto);
            genericApiReqDataDtoWrapper.setMessageHash(signature);


            String requestToBeSend = utils.convertXmlToString(JAXBContext.newInstance(GenericApiReqDataDtoWrapper.class), genericApiReqDataDtoWrapper);

            return  ResponseEntity.ok(requestToBeSend);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ResponseCodes.getDescription(ResponseCodes.REQUEST_CANNOT_COMPLETED));

        }
    }
}
