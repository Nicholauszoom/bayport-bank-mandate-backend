package com.example.Project.Management.bayport.charge.service;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
import com.example.Project.Management.bayport.apis.dto.GenericApiReqDataDto;
import com.example.Project.Management.bayport.calculator.Calculator;
import com.example.Project.Management.bayport.charge.dto.charges.*;
import com.example.Project.Management.bayport.charge.dto.portal.ChargeDto;
import com.example.Project.Management.bayport.charge.entity.Charge;
import com.example.Project.Management.bayport.charge.entity.ClientRv;
import com.example.Project.Management.bayport.charge.repository.ChargeRepository;
import com.example.Project.Management.bayport.common.Constants;
import com.example.Project.Management.bayport.common.DigitalSignature;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.repositories.FinancialServiceProviderRepository;
import com.example.Project.Management.bayport.product.entity.Product;
import com.example.Project.Management.bayport.product.repository.ProductRepository;
import com.example.Project.Management.bayport.util.ResponseCodes;
import com.example.Project.Management.bayport.util.Utils;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChargeServiceImpl implements ChargeService {

    private final ProductRepository productRepository;

    private final FinancialServiceProviderRepository financialServiceProviderRepository;

    private final ChargeRepository chargeRepository;

    private final Utils utils;

    private final DigitalSignature digitalSignature;

    private final Calculator calculator;

    private final PriceClientRvService priceClientRvService;

    public ChargeServiceImpl(ProductRepository productRepository, FinancialServiceProviderRepository financialServiceProviderRepository, ChargeRepository chargeRepository, Utils utils, DigitalSignature digitalSignature, Calculator calculator, PriceClientRvService priceClientRvService) {
        this.productRepository = productRepository;
        this.financialServiceProviderRepository = financialServiceProviderRepository;
        this.chargeRepository = chargeRepository;
        this.utils = utils;
        this.calculator = calculator;
        this.digitalSignature = digitalSignature;
        this.priceClientRvService = priceClientRvService;
    }

    @Transactional
    @Override
    public ResponseEntity<String> calculateLoanCharges(String message) throws Exception{
        log.info("Received loan charges request from utumishi: {}", message);

        JAXBContext jaxbContext = JAXBContext.newInstance(LoanChargesApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanChargesApiDto loanChargesApiDto = (LoanChargesApiDto) unmarshaller.unmarshal(stringReader);
        BigDecimal RV = BigDecimal.ZERO;
        BigDecimal takeHomeAmount = BigDecimal.ZERO;
        String mssId = loanChargesApiDto.getLoanChargesApiReqData().getRequestHeader().getMsgId();
        String fspCode = loanChargesApiDto.getLoanChargesApiReqData().getRequestHeader().getFspCode();
        String checkNumber = loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getCheckNumber();
        LoanChargesApiReqDataDto loanChargeDataLevel= loanChargesApiDto.getLoanChargesApiReqData();
        try {

            Optional<Product> productDetail = productRepository.findByProductCode(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getProductCode());

            if(productDetail.isEmpty()){
                log.info("invalid product code i.e Product Code: {}"+ loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getProductCode());
                return createResponseEntity(ResponseCodes.INVALID_PRODUCT_CODE ,loanChargesApiDto.getLoanChargesApiReqData().getRequestHeader().getFspCode(),mssId);

            }
            Optional<FinancialServiceProvider> fsp = financialServiceProviderRepository.findByFspCode(fspCode);

            //calculate
            LoanChargesApiRespDetailDto chargeResponse = calculator.calculateCharges(loanChargeDataLevel.getMessageDetails());
            log.info("Charge Response: {}", chargeResponse);

            //if is refinanced or new i.e calculate take home amount
            ClientRv client = priceClientRvService.findByCheckNumber(checkNumber);
            if (client != null){
                RV = client.getTotalPayoffAmount();
            }
            takeHomeAmount = calculator.getTakeHomeAmount(RV, chargeResponse.getOtherCharges(),
                    chargeResponse.getNetLoanAmount());

            ApiReqHeaderDto apiReqHeaderDtoCC = new ApiReqHeaderDto();
            apiReqHeaderDtoCC.setFspCode(fsp.get().getFspCode());
            apiReqHeaderDtoCC.setMsgId(mssId);
            apiReqHeaderDtoCC.setMessageType(Constants.LOAN_CHARGES_RESPONSE);
            apiReqHeaderDtoCC.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDtoCC.setSender(fsp.get().getFspName());

            LoanChargesApirRespDataDto chargesApirRespDataDto = new LoanChargesApirRespDataDto();
            chargesApirRespDataDto.setRequestHeader(apiReqHeaderDtoCC);

            BigDecimal totalInsurance =chargeResponse.getTotalInsurance();
            BigDecimal totalProcessingFee = chargeResponse.getTotalProcessingFee();
            BigDecimal totalEssInsuranceAmount = calculator.getEssTotalInsurance(chargeResponse.getTotalInsurance(), chargeResponse.getTotalProcessingFee());
            chargeResponse.setTotalInsurance(totalEssInsuranceAmount);
            chargeResponse.setTotalProcessingFee(BigDecimal.ZERO);
            chargesApirRespDataDto.setLoanChargesApiRespMessageDetail(chargeResponse);

            JAXBContext jaxbContext2CC = JAXBContext.newInstance(LoanChargesApirRespDataDto.class);
            Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
            marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterCC = new StringWriter();
            marshallerCC.marshal(chargesApirRespDataDto, stringWriterCC);
            String xmlPayloadCC = stringWriterCC.toString();
            log.info("charges catalog i.e with fsp code:" + xmlPayloadCC);

            JAXBContext jaxbContextSg = JAXBContext.newInstance(GenericApiReqDataDto.class);
            Unmarshaller unmarshallerSg = jaxbContextSg.createUnmarshaller();
            StringReader stringReaderSg = new StringReader(xmlPayloadCC);
            GenericApiReqDataDto genericApiReqDataDtoSg = (GenericApiReqDataDto) unmarshallerSg.unmarshal(stringReaderSg);

            // Extract and convert the content to be signed
            String content = convertObjectToXml(genericApiReqDataDtoSg);

            String signatureSg = digitalSignature.createSignature(content);
            log.info("Generated Signature: ", signatureSg);

            // Create the final XML with the signature
            String finalXml = appendSignatureToXml2(content, signatureSg);
            log.info("Final Xml Request {}:", finalXml);

            //save
            saveLoanChargeRequestResponse(loanChargeDataLevel.getMessageDetails(), chargeResponse, takeHomeAmount, totalInsurance, totalProcessingFee);

            return ResponseEntity.ok(finalXml);
        } catch(Exception e){
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR,fspCode, mssId);
        }
    }

    @Override
    public List<Charge> findAll() {
        return chargeRepository.findAllOrderByCreatedDateDesc();
    }

    @Override
    public List<ChargeDto> searchCharges(String term) {
        List<Charge> charges = chargeRepository.searchCharges(term);
        return charges.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public Optional<Charge> findById(long id) {
        return chargeRepository.findById(id);
    }

    @Override
    public void deleteCharge(long id) {
        chargeRepository.deleteById(id);
    }

    @Override
    public Charge findLatestChargeByChecknumber(String checkNumber) {
        return chargeRepository.findLatestChargeByChecknumber(checkNumber);
    }

    private ChargeDto mapToDto(Charge charge) {
        return ChargeDto.builder()
                .checkNumber(charge.getCheckNumber())
                .designationCode(charge.getDesignationCode())
                .designationName(charge.getDesignationName())
                .basicSalary(charge.getBasicSalary())
                .netSalary(charge.getNetSalary())
                .oneThirdAmount(charge.getOneThirdAmount())
                .requestedAmount(charge.getRequestedAmount())
                .deductibleAmount(charge.getDeductibleAmount())
                .desiredDeductibleAmount(charge.getDesiredDeductibleAmount())
                .retirementDate(charge.getRetirementDate())
                .termsOfEmployment(charge.getTermsOfEmployment())
                .tenure(charge.getTenure())
                .productCode(charge.getProductCode())
                .voteCode(charge.getVoteCode())
                .totalEmployeeDeduction(charge.getTotalEmployeeDeduction())
                .jobClassCode(charge.getJobClassCode())
                .createdDate(charge.getCreatedDate())
                .build();
    }

    private ResponseEntity<String> createResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
        String description = ResponseCodes.getDescription(responseCode);
        ResponseEntity<String> responseEntity = utils.messageResponseWrapperToUtumishi(responseCode, description, fspCode, mssId, Constants.UTUMISHI_RESPONSE);
        log.info("Response for FSP Code {}: {}", fspCode, responseEntity.getBody());
        return responseEntity;
    }

    public void saveLoanChargeRequestResponse(LoanChargesApiReqDetailDto loanChargesApiReqDetailDto, LoanChargesApiRespDetailDto chargeResponse, BigDecimal takeHomeAmount, BigDecimal totalInsurance, BigDecimal totalProcessingFee) {
        try {
            Charge charge = Charge.builder()
                    // charge request
                    .checkNumber(loanChargesApiReqDetailDto.getCheckNumber())
                    .designationCode(loanChargesApiReqDetailDto.getDesignationCode())
                    .designationName(loanChargesApiReqDetailDto.getDesignationName())
                    .basicSalary(loanChargesApiReqDetailDto.getBasicSalary())
                    .netSalary(loanChargesApiReqDetailDto.getNetSalary())
                    .oneThirdAmount(loanChargesApiReqDetailDto.getOneThirdAmount())
                    .requestedAmount(loanChargesApiReqDetailDto.getRequestedAmount())
                    .deductibleAmount(loanChargesApiReqDetailDto.getDeductibleAmount())
                    .desiredDeductibleAmount(loanChargesApiReqDetailDto.getDesiredDeductibleAmount())
                    .retirementDate(loanChargesApiReqDetailDto.getRetirementDate())
                    .termsOfEmployment(loanChargesApiReqDetailDto.getTermsOfEmployment())
                    .tenure(loanChargesApiReqDetailDto.getTenure())
                    .productCode(loanChargesApiReqDetailDto.getProductCode())
                    .voteCode(loanChargesApiReqDetailDto.getVoteCode())
                    .totalEmployeeDeduction(loanChargesApiReqDetailDto.getTotalEmployeeDeduction())
                    .jobClassCode(loanChargesApiReqDetailDto.getJobClassCode())
                    // charge response
                    .totalInsurance(totalInsurance)
                    .totalProcessingFee(totalProcessingFee)
                    .totalInterestRateAmount(chargeResponse.getTotalInterestRateAmount())
                    .otherCharges(chargeResponse.getOtherCharges())
                    .netLoanAmount(chargeResponse.getNetLoanAmount())
                    .totalAmountToPay(chargeResponse.getTotalAmountToPay())
                    .eligibleAmount(chargeResponse.getEligibleAmount())
                    .monthlyReturnAmount(chargeResponse.getMonthlyReturnAmount())
                    .responseTenure(chargeResponse.getTenure())
                    .takeHomeAmount(takeHomeAmount)
                    .insuranceProcessFee(chargeResponse.getTotalInsurance())
                    .build();

            chargeRepository.save(charge);
            log.info("Charge Saved Successfully");
        } catch (Exception e) {
          log.info("Exception: ", e.getMessage());
        }
    }

    private String convertObjectToXml(Object obj) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        // Create JAXBContext with the class type of the content
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        // Configure marshaller to omit the XML declaration
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        // Configure marshaller to not format the XML (prevent spaces between tags)
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        // Marshal the object content to XML string
        marshaller.marshal(obj, stringWriter);
        String xml = stringWriter.toString();

        xml = xml.replaceAll(">\\s+<", "><");

        return xml;
    }

    private String appendSignatureToXml2(String originalXml, String signature) {
        int index = originalXml.indexOf("</Data>");
        if (index != -1) {
            return "<Document>" + originalXml + "<Signature>" + signature + "</Signature></Document>";
        }
        return "<Document>" + originalXml + "</Document>";
    }

}