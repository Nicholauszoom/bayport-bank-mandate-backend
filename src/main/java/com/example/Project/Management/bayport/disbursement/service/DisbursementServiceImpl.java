package com.example.Project.Management.bayport.disbursement.service;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
import com.example.Project.Management.bayport.common.Constants;
import com.example.Project.Management.bayport.disbursement.dto.LoanDisbursement.LoanDisburseNotApiDataDto;
import com.example.Project.Management.bayport.disbursement.dto.LoanDisbursement.LoanDisburseNotApiDetailDto;
import com.example.Project.Management.bayport.disbursement.dto.LoanDisbursement.LoanDisburseNotApiDto;
import com.example.Project.Management.bayport.disbursement.dto.portal.DisbursementDto;
import com.example.Project.Management.bayport.disbursement.entity.Disbursement;
import com.example.Project.Management.bayport.disbursement.repository.DisbursementRepository;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.repositories.FinancialServiceProviderRepository;
import com.example.Project.Management.bayport.fsp.services.FinancialServiceProviderService;
import com.example.Project.Management.bayport.loan.entity.Loan;
import com.example.Project.Management.bayport.loan.repository.LoanRepository;
import com.example.Project.Management.bayport.loan.service.LoanService;
import com.example.Project.Management.bayport.util.ResponseCodes;
import com.example.Project.Management.bayport.util.Utils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class DisbursementServiceImpl implements DisbursementService {

    @Autowired
    private DisbursementRepository disbursementRepository;

    @Autowired
    private FinancialServiceProviderRepository financialServiceProviderRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private Utils utils;
    
    public DisbursementServiceImpl(DisbursementRepository disbursementRepository, FinancialServiceProviderService financialServiceProviderService, FinancialServiceProviderRepository financialServiceProviderRepository, LoanService loanService) {
        this.disbursementRepository = disbursementRepository;
        this.financialServiceProviderRepository = financialServiceProviderRepository;
        this.loanService = loanService;
    }

    //UTUMISHI DISBURSEMENT
    @Override
    public ResponseEntity<?> saveLoanDisbursement(DisbursementDto disbursementDto) throws Exception {
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }

        try {

            LoanDisburseNotApiDetailDto loanDisburseNotApiDetailDto = new LoanDisburseNotApiDetailDto();
            loanDisburseNotApiDetailDto.setApplicationNumber(disbursementDto.getApplicationNumber());
            loanDisburseNotApiDetailDto.setReason(disbursementDto.getReason());
            loanDisburseNotApiDetailDto.setFspReferenceNumber(disbursementDto.getFspReferenceNumber());
            loanDisburseNotApiDetailDto.setLoanNumber(disbursementDto.getLoanNumber());
            String totaltopay = String.valueOf(disbursementDto.getTotalAmountToPay());
            if (totaltopay != null) {
                // Replace comma with a period
                String totaltopays = totaltopay.replace(",", "");
                loanDisburseNotApiDetailDto.setTotalAmountToPay(new BigDecimal(totaltopays).setScale(2, RoundingMode.HALF_UP));
            }
            loanDisburseNotApiDetailDto.setDisbursementDate(disbursementDto.getDisbursementDate());

            log.info("loan disbursement notification json in detail level payload: {}" , loanDisburseNotApiDetailDto);

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_DISBURSEMENT_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());

            LoanDisburseNotApiDataDto loanDisburseNotApiDataDto = new LoanDisburseNotApiDataDto();
            loanDisburseNotApiDataDto.setRequestHeader(apiReqHeaderDto);
            loanDisburseNotApiDataDto.setMessageLoanDisburseDetails(loanDisburseNotApiDetailDto);
            log.info("loan disbursement json in data level payload: " + loanDisburseNotApiDataDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanDisburseNotApiDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanDisburseNotApiDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();

            log.info(" loan disbursement xml payload: {} ", xmlPayload);

            ResponseEntity<String> responseEntityDisburse = loanService.generateRequestSignature2(xmlPayload);

//            log.info("loan disbursement sent to be signed" + xmlPayload);

            log.info("utumishi response message for disbursement notification sent to the queue: {}", responseEntityDisburse);

            return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");

        } catch (Exception e) {
            log.info("fail to send disbursement to utumishi");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL");
        }
    }

    @Override
    public ResponseEntity<?> saveLoanDisbursementFailure(DisbursementDto disbursementDto) throws Exception {
       String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }

        try {
            LoanDisburseNotApiDetailDto loanDisburseNotApiDetailDto = new LoanDisburseNotApiDetailDto();
            loanDisburseNotApiDetailDto.setApplicationNumber(disbursementDto.getApplicationNumber());
            loanDisburseNotApiDetailDto.setReason(disbursementDto.getReason());

            log.info("loan disbursement failure notification json in detail level payload: ", loanDisburseNotApiDetailDto);

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_DISBURSEMENT_FAILURE_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());

            LoanDisburseNotApiDataDto loanDisburseNotApiDataDto = new LoanDisburseNotApiDataDto();
            loanDisburseNotApiDataDto.setRequestHeader(apiReqHeaderDto);
            loanDisburseNotApiDataDto.setMessageLoanDisburseDetails(loanDisburseNotApiDetailDto);

            log.info("loan disbursement json in data level payload: {}", loanDisburseNotApiDataDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanDisburseNotApiDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanDisburseNotApiDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();

            log.info(" loan disbursement failure xml payload: {}", xmlPayload);

            loanService.generateRequestSignature2(xmlPayload);

            log.info("loan disbursement failure sent tobe signed" + xmlPayload);
            return null;

        } catch (Exception e) {
            log.info("fail to send disbursement failure to utumishi");
            return null;
        }
    }

    //BAYPORT DISBURSEMENT
    @Override
    public ResponseEntity<String> saveBayportLoanDisbursement(String message) throws Exception {
        log.info("Received loan disbursement request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanDisburseNotApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanDisburseNotApiDto loanDisburseNotApiDto = (LoanDisburseNotApiDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanDisburseNotApiDto.getLoanDisburseNotApiData().getRequestHeader().getFspCode();
        String mssId = loanDisburseNotApiDto.getLoanDisburseNotApiData().getRequestHeader().getMsgId();

        try {
            Optional<Loan> loan = loanRepository.findByApplicationNumber(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getApplicationNumber());

            // Check for loan existence
            if (loan.isEmpty()) {
                log.info("loan application not match for any loan" + loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getApplicationNumber());
                return createJsonResponseEntity(ResponseCodes.INVALID_LOAN_CODE, fspCode, mssId);
            }

            // Save loan disbursement
            Disbursement disbursement = new Disbursement();

            disbursement.setReason(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getReason());
            disbursement.setLoan(loan.get());
            disbursement.setApplicationNumber(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getApplicationNumber());
            disbursement.setDisbursementAmount(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getTotalAmountToPay());
            disbursement.setDisbursementStatus(Constants.SUCCESS_DISBURSMENT_ACTION);
            disbursementRepository.save(disbursement);

            //Update loan by loan id
//            loan.get().setLoanStatus(Constants.LOAN_DISBURSEMENT_ACTION);
            loanRepository.save(loan.get());

//            log.info("loan disbursement successfull i.e loan with Loan Number:" + loan.get().getLoanNumber());
            return createJsonResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception occurred while processing loan disbursement {}");
            return createJsonResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> saveBayportLoanDisbursementFailure(String message) throws Exception {
        log.info("Received loan disbursement request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanDisburseNotApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanDisburseNotApiDto loanDisburseNotApiDto = (LoanDisburseNotApiDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanDisburseNotApiDto.getLoanDisburseNotApiData().getRequestHeader().getFspCode();
        String mssId = loanDisburseNotApiDto.getLoanDisburseNotApiData().getRequestHeader().getMsgId();

        try {
            Optional<Loan> loan = loanRepository.findByApplicationNumber(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getApplicationNumber());

            // Check for loan existence
            if (loan.isEmpty()) {
                log.info("loan application not match for any loan {}" + loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getApplicationNumber());
                return createJsonResponseEntity(ResponseCodes.INVALID_LOAN_CODE, fspCode, mssId);
            }

            // Save loan disbursement
            Disbursement disbursement = new Disbursement();
            disbursement.setReason(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getReason());
            disbursement.setLoan(loan.get());
            disbursement.setApplicationNumber(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getApplicationNumber());
//            disbursement.setDisbursementDate(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getDisbursementDate());
            disbursement.setDisbursementAmount(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getTotalAmountToPay());
            disbursement.setDisbursementStatus(Constants.FAIL_DISBURSMENT_ACTION);
            disbursement.setReason(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getReason());
            disbursementRepository.save(disbursement);

            //Update loan by loan id
//            loan.get().setLoanStatus(Constants.LOAN_DISBURSEMENT_FAILURE_NOTIFICATION);
            loanRepository.save(loan.get());

//            log.info("loan disbursement failure notification successfull i.e loan with Loan Number:" + loan.get().getLoanNumber());
            return createJsonResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception occurred while processing loan disbursement {}");
            return createJsonResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    /* BAYPORT JSON RESPONSE
     * */
    public ResponseEntity<String> createJsonResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
        String description = ResponseCodes.getDescription(responseCode);

        ResponseEntity<String> responseEntityJson = utils.messageResponseWrapperToPayPort(responseCode, description, fspCode,
                mssId, Constants.LOAN_DISBURSEMENT_DETAIL);
        log.info("Response for FSP Code {}: {}", fspCode, responseEntityJson.getBody());

        return responseEntityJson;
    }

    /*  XML RESPONSE
     * */
    private ResponseEntity<String> createResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
        String description = ResponseCodes.getDescription(responseCode);
        ResponseEntity<String> responseEntity = utils.messageResponseWrapperToUtumishi(responseCode, description, fspCode, mssId, Constants.UTUMISHI_RESPONSE);
        log.info("Response for FSP Code {}: {}", fspCode, responseEntity.getBody());
        return responseEntity;
    }

    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }

    //TAKE OVER DISBURSEMENT NOTIFICATION
    @Override
    public ResponseEntity<?> takeOverLoanDisbursement(DisbursementDto disbursementDto) throws Exception {
       String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }

            try {
                log.info("Unmarshed loan disbursement request: {}", disbursementDto);

                LoanDisburseNotApiDetailDto loanDisburseNotApiDetailDto = new LoanDisburseNotApiDetailDto();
                loanDisburseNotApiDetailDto.setApplicationNumber(disbursementDto.getApplicationNumber());
                loanDisburseNotApiDetailDto.setReason(disbursementDto.getReason());
                loanDisburseNotApiDetailDto.setFspReferenceNumber(disbursementDto.getFspReferenceNumber());
                loanDisburseNotApiDetailDto.setLoanNumber(disbursementDto.getLoanNumber());
                String totaltopay = String.valueOf(disbursementDto.getTotalAmountToPay());
                if (totaltopay != null) {
                    // Replace comma with a period
                    String totaltopays = totaltopay.replace(",", ".");
                    loanDisburseNotApiDetailDto.setTotalAmountToPay(new BigDecimal(totaltopays).setScale(2, RoundingMode.HALF_UP));
                }

                loanDisburseNotApiDetailDto.setDisbursementDate(disbursementDto.getDisbursementDate());

                String cleanedPaymentAdvice = disbursementDto.getPaymentAdvice().replaceAll("\\s+", "");

                loanDisburseNotApiDetailDto.setPaymentAdviceDto(cleanedPaymentAdvice);

                loanDisburseNotApiDetailDto.setPaymentAdviceAttachment(disbursementDto.getPaymentAdviceAttachment());

                log.info("take over disbursement notification json in detail level payload: {}" , loanDisburseNotApiDetailDto);

                ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
                apiReqHeaderDto.setFspCode(fsp.getFspCode());
                apiReqHeaderDto.setMsgId(mssId);
                apiReqHeaderDto.setMessageType(Constants.TAKEOVER_DISBURSEMENT_NOTIFICATION);
                apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
                apiReqHeaderDto.setSender(fsp.getFspName());

                LoanDisburseNotApiDataDto loanDisburseNotApiDataDto = new LoanDisburseNotApiDataDto();
                loanDisburseNotApiDataDto.setRequestHeader(apiReqHeaderDto);
                loanDisburseNotApiDataDto.setMessageLoanDisburseDetails(loanDisburseNotApiDetailDto);
                log.info("take over disbursement json in data level payload: {}" , loanDisburseNotApiDataDto);

                JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanDisburseNotApiDataDto.class);
                Marshaller marshaller = jaxbContext2.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(loanDisburseNotApiDataDto, stringWriter);
                String xmlPayload = stringWriter.toString();
                if (xmlPayload != null) {
                    // Replace comma with a period
                    String xmlPayloadFirstClean = xmlPayload.replace("&lt;", "<");
                    String xmlPayloadSecondClean = xmlPayloadFirstClean.replace("&gt;&#13;", ">");
                     xmlPayload = xmlPayloadSecondClean.replace("&gt;", ">");
                }

                log.info("take over disbursement xml payload:{}" , xmlPayload);

                ResponseEntity<String> responseEntityDisburse = loanService.generateRequestSignature2(xmlPayload);


                log.info("utumishi response message for take over disbursement notification sent to the queue: {}", responseEntityDisburse);

                return responseEntityDisburse;

            } catch (Exception e) {
                log.info("fail to send take over disbursement to utumishi ");
                return null;
            }
        }

    // Convert Node to String (Preserve XML Format)
    private static String nodeToString(Node node) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        return writer.toString();
    }
}


