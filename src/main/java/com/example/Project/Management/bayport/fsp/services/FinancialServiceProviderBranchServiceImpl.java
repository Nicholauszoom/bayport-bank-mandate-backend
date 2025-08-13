package com.dflex.emkopo.app.fsp.services;


import com.dflex.emkopo.api.dto.ApiReqHeaderDto;
import com.dflex.emkopo.api.dto.GenericApiReqDataDto;
import com.dflex.emkopo.api.dto.GenericApiReqDataDtoWrapper;
import com.dflex.emkopo.app.fsp.repositories.FinancialServiceProviderRepository;
import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProvider;
import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProviderBranch;
import com.dflex.emkopo.app.fsp.repositories.FinancialServiceProviderBranchRepository;
import com.dflex.emkopo.app.setup.dto.BranchDetailDto;
import com.dflex.emkopo.app.setup.dto.BranchDto;
import com.dflex.emkopo.app.setup.entity.District;
import com.dflex.emkopo.app.setup.repository.DistrictRepository;
import com.dflex.emkopo.commons.utils.Constants;
import com.dflex.emkopo.commons.utils.DigitalSignature;
import com.dflex.emkopo.commons.utils.ResponseCodes;
import com.dflex.emkopo.commons.utils.Utils;
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
