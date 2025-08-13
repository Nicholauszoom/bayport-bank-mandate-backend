package com.dflex.emkopo.app.disbursement.service;

import com.dflex.emkopo.api.consumer.portal.dto.DisbursementPortalDto;
import com.dflex.emkopo.api.consumer.portal.dto.LoanInitialApprovePortalDto;
import com.dflex.emkopo.api.dto.ApiReqHeaderDto;
import com.dflex.emkopo.app.disbursement.dto.LoanDisbursement.LoanDisburseNotApiDataDto;
import com.dflex.emkopo.app.disbursement.dto.LoanDisbursement.LoanDisburseNotApiDetailDto;
import com.dflex.emkopo.app.disbursement.dto.LoanDisbursement.PaymentAdviceDto;
import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProvider;
import com.dflex.emkopo.app.fsp.repositories.FinancialServiceProviderRepository;
import com.dflex.emkopo.app.fsp.services.FinancialServiceProviderService;
import com.dflex.emkopo.app.loan.dto.approval.LoanApprovalApiDataDto;
import com.dflex.emkopo.app.loan.dto.approval.LoanApprovalApiDetailDto;
import com.dflex.emkopo.app.loan.service.LoanService;
import com.dflex.emkopo.app.message_req_resp.loan.entity.LoanLogs;
import com.dflex.emkopo.app.message_req_resp.loan.repository.LoanLogRepository;
import com.dflex.emkopo.app.product.entity.Product;
import com.dflex.emkopo.commons.utils.Constants;
import com.dflex.emkopo.commons.utils.RabbitMqParameters;
import com.dflex.emkopo.commons.utils.ResponseCodes;
import com.dflex.emkopo.commons.utils.Utils;
import com.dflex.emkopo.app.disbursement.dto.LoanDisbursement.LoanDisburseNotApiDto;
import com.dflex.emkopo.app.disbursement.entity.Disbursement;
import com.dflex.emkopo.app.disbursement.repository.DisbursementRepository;
import com.dflex.emkopo.app.loan.entity.Loan;
import com.dflex.emkopo.app.loan.repository.LoanRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private LoanLogRepository loanLogRepository;

    @Autowired
    private RabbitMqParameters rabbitMqParameters;

    private static final Logger logger = LogManager.getLogger(DisbursementServiceImpl.class);

    public DisbursementServiceImpl(DisbursementRepository disbursementRepository, FinancialServiceProviderService financialServiceProviderService, FinancialServiceProviderRepository financialServiceProviderRepository, LoanService loanService, LoanLogRepository loanLogRepository) {
        this.disbursementRepository = disbursementRepository;
        this.financialServiceProviderRepository = financialServiceProviderRepository;
        this.loanService = loanService;
        this.loanLogRepository = loanLogRepository;
    }

    //UTUMISHI DISBURSEMENT
    @Override
    public ResponseEntity<String> saveLoanDisbursement(String message) throws Exception {
        logger.info("Received loan disbursement request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(DisbursementPortalDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        DisbursementPortalDto disbursementPortalDto = (DisbursementPortalDto) unmarshaller.unmarshal(stringReader);
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }

        try {

            LoanDisburseNotApiDetailDto loanDisburseNotApiDetailDto = new LoanDisburseNotApiDetailDto();
            loanDisburseNotApiDetailDto.setApplicationNumber(disbursementPortalDto.getApplicationnumber());
            loanDisburseNotApiDetailDto.setReason(disbursementPortalDto.getReason());
            loanDisburseNotApiDetailDto.setFspReferenceNumber(disbursementPortalDto.getFspreferencenumber());
            loanDisburseNotApiDetailDto.setLoanNumber(disbursementPortalDto.getLoannumber());
            String totaltopay = disbursementPortalDto.getTotalamounttpay();
            if (totaltopay != null) {
                // Replace comma with a period
                String totaltopays = totaltopay.replace(",", "");
                loanDisburseNotApiDetailDto.setTotalAmountToPay(new BigDecimal(totaltopays).setScale(2, RoundingMode.HALF_UP));
            }
            loanDisburseNotApiDetailDto.setDisbursementDate(disbursementPortalDto.getDisbursementdate());

            logger.info("loan disbursement notification json in detail level payload: " + loanDisburseNotApiDetailDto);

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_DISBURSEMENT_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());

            LoanDisburseNotApiDataDto loanDisburseNotApiDataDto = new LoanDisburseNotApiDataDto();
            loanDisburseNotApiDataDto.setRequestHeader(apiReqHeaderDto);
            loanDisburseNotApiDataDto.setMessageLoanDisburseDetails(loanDisburseNotApiDetailDto);
            logger.info("loan disbursement json in data level payload: " + loanDisburseNotApiDataDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanDisburseNotApiDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanDisburseNotApiDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();
            System.out.println("disbursement xml payload: " + xmlPayload);

            logger.info(" loan disbursement xml payload: " + xmlPayload);

            ResponseEntity<String> responseEntityDisburse = loanService.generateRequestSignature2(xmlPayload);

            logger.info("loan disbursement sent to be signed" + xmlPayload);

//            LoanLogs loanLogs = loanLogRepository.findByApplicationNumber(disbursementPortalDto.getApplicationnumber());
//            if(loanLogs != null){
//                loanLogs.setLoanStatus("Loan Disbursed");
//                loanLogs.setDisbursementDate(disbursementPortalDto.getDisbursementdate());
//                loanLogRepository.save(loanLogs);
//            }
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getApprovalAndDisbursRoutingKey(),
                    String.valueOf(responseEntityDisburse));

            logger.info("utumishi response message for disbursement notification sent to the queue: {}", responseEntityDisburse);

            return responseEntityDisburse;

        } catch (Exception e) {
            logger.info("fail to send disbursement to utumishi ");
            return null;
        }
    }

    @Override
    public ResponseEntity<String> saveLoanDisbursementFailure(String message) throws Exception {
        logger.info("Received loan disbursement failure request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(DisbursementPortalDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        DisbursementPortalDto disbursementPortalDto = (DisbursementPortalDto) unmarshaller.unmarshal(stringReader);
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }

        try {
            LoanDisburseNotApiDetailDto loanDisburseNotApiDetailDto = new LoanDisburseNotApiDetailDto();
            loanDisburseNotApiDetailDto.setApplicationNumber(disbursementPortalDto.getApplicationnumber());
            loanDisburseNotApiDetailDto.setReason(disbursementPortalDto.getReason());

            logger.info("loan disbursement failure notification json in detail level payload: " + loanDisburseNotApiDetailDto);

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_DISBURSEMENT_FAILURE_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());

            LoanDisburseNotApiDataDto loanDisburseNotApiDataDto = new LoanDisburseNotApiDataDto();
            loanDisburseNotApiDataDto.setRequestHeader(apiReqHeaderDto);
            loanDisburseNotApiDataDto.setMessageLoanDisburseDetails(loanDisburseNotApiDetailDto);

            logger.info("loan disbursement json in data level payload: " + loanDisburseNotApiDataDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanDisburseNotApiDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanDisburseNotApiDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();
            System.out.println("disbursement failure xml payload: " + xmlPayload);

            logger.info(" loan disbursement failure xml payload: " + xmlPayload);

            loanService.generateRequestSignature2(xmlPayload);

            logger.info("loan disbursement failure sent tobe signed" + xmlPayload);
            return null;

        } catch (Exception e) {
            logger.info("fail to send disbursement failure to utumishi ");
            return null;
        }
    }

    //BAYPORT DISBURSEMENT
    @Override
    public ResponseEntity<String> saveBayportLoanDisbursement(String message) throws Exception {
        logger.info("Received loan disbursement request: {}", message);
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
                logger.info("loan application not match for any loan" + loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getApplicationNumber());
                return createJsonResponseEntity(ResponseCodes.INVALID_LOAN_CODE, fspCode, mssId);
            }

            // Save loan disbursement
            Disbursement disbursement = new Disbursement();

            disbursement.setReason(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getReason());
            disbursement.setLoan(loan.get());
            disbursement.setApplicationNumber(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getApplicationNumber());
//            disbursement.setDisbursementDate(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getDisbursementDate());
            disbursement.setDisbursementAmount(loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getTotalAmountToPay());
            disbursement.setDisbursementStatus(Constants.SUCCESS_DISBURSMENT_ACTION);
            disbursementRepository.save(disbursement);

            //Update loan by loan id
            loan.get().setLoanStatus(Constants.LOAN_DISBURSEMENT_ACTION);
            loanRepository.save(loan.get());

            logger.info("loan disbursement successfull i.e loan with Loan Number:" + loan.get().getLoanNumber());
            return createJsonResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing loan disbursement {}");
            return createJsonResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> saveBayportLoanDisbursementFailure(String message) throws Exception {
        logger.info("Received loan disbursement request: {}", message);
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
                logger.info("loan application not match for any loan {}" + loanDisburseNotApiDto.getLoanDisburseNotApiData().getMessageLoanDisburseDetails().getApplicationNumber());
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
            loan.get().setLoanStatus(Constants.LOAN_DISBURSEMENT_FAILURE_NOTIFICATION);
            loanRepository.save(loan.get());

            logger.info("loan disbursement failure notification successfull i.e loan with Loan Number:" + loan.get().getLoanNumber());
            return createJsonResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing loan disbursement {}");
            return createJsonResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    /* BAYPORT JSON RESPONSE
     * */
    public ResponseEntity<String> createJsonResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
        String description = ResponseCodes.getDescription(responseCode);

        ResponseEntity<String> responseEntityJson = utils.messageResponseWrapperToPayPort(responseCode, description, fspCode,
                mssId, Constants.LOAN_DISBURSEMENT_DETAIL);
        logger.info("Response for FSP Code {}: {}", fspCode, responseEntityJson.getBody());

        return responseEntityJson;
    }

    /*  XML RESPONSE
     * */
    private ResponseEntity<String> createResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
        String description = ResponseCodes.getDescription(responseCode);
        ResponseEntity<String> responseEntity = utils.messageResponseWrapperToUtumishi(responseCode, description, fspCode, mssId, Constants.UTUMISHI_RESPONSE);
        logger.info("Response for FSP Code {}: {}", fspCode, responseEntity.getBody());
        return responseEntity;
    }

    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }

    //TAKE OVER DISBURSEMENT NOTIFICATION
    @Override
    public ResponseEntity<String> takeOverLoanDisbursement(String message) throws Exception {
        logger.info("Received take over loan disbursement request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(DisbursementPortalDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        DisbursementPortalDto disbursementPortalDto = (DisbursementPortalDto) unmarshaller.unmarshal(stringReader);
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }

            try {
                logger.info("Unmarshed loan disbursement request: {}", disbursementPortalDto);

                LoanDisburseNotApiDetailDto loanDisburseNotApiDetailDto = new LoanDisburseNotApiDetailDto();
                loanDisburseNotApiDetailDto.setApplicationNumber(disbursementPortalDto.getApplicationnumber());
                loanDisburseNotApiDetailDto.setReason(disbursementPortalDto.getReason());
                loanDisburseNotApiDetailDto.setFspReferenceNumber(disbursementPortalDto.getFspreferencenumber());
                loanDisburseNotApiDetailDto.setLoanNumber(disbursementPortalDto.getLoannumber());
                String totaltopay = disbursementPortalDto.getTotalamounttpay();
                if (totaltopay != null) {
                    // Replace comma with a period
                    String totaltopays = totaltopay.replace(",", ".");
                    loanDisburseNotApiDetailDto.setTotalAmountToPay(new BigDecimal(totaltopays).setScale(2, RoundingMode.HALF_UP));
                }

                loanDisburseNotApiDetailDto.setDisbursementDate(disbursementPortalDto.getDisbursementdate());

                String cleanedPaymentAdvice = disbursementPortalDto.getPaymentAdvice().replaceAll("\\s+", "");

                loanDisburseNotApiDetailDto.setPaymentAdviceDto(cleanedPaymentAdvice);

                loanDisburseNotApiDetailDto.setPaymentAdviceAttachment(disbursementPortalDto.getPaymentadviceattachment());

                logger.info("take over disbursement notification json in detail level payload: " + loanDisburseNotApiDetailDto);

                ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
                apiReqHeaderDto.setFspCode(fsp.getFspCode());
                apiReqHeaderDto.setMsgId(mssId);
                apiReqHeaderDto.setMessageType(Constants.TAKEOVER_DISBURSEMENT_NOTIFICATION);
                apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
                apiReqHeaderDto.setSender(fsp.getFspName());

                LoanDisburseNotApiDataDto loanDisburseNotApiDataDto = new LoanDisburseNotApiDataDto();
                loanDisburseNotApiDataDto.setRequestHeader(apiReqHeaderDto);
                loanDisburseNotApiDataDto.setMessageLoanDisburseDetails(loanDisburseNotApiDetailDto);
                logger.info("take over disbursement json in data level payload: " + loanDisburseNotApiDataDto);

                JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanDisburseNotApiDataDto.class);
                Marshaller marshaller = jaxbContext2.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(loanDisburseNotApiDataDto, stringWriter);
                String xmlPayload = stringWriter.toString();
                System.out.println("take over disbursement xml payload: " + xmlPayload);
                if (xmlPayload != null) {
                    // Replace comma with a period
                    String xmlPayloadFirstClean = xmlPayload.replace("&lt;", "<");
                    String xmlPayloadSecondClean = xmlPayloadFirstClean.replace("&gt;&#13;", ">");
                     xmlPayload = xmlPayloadSecondClean.replace("&gt;", ">");
                }

                logger.info("take over disbursement xml payload:" + xmlPayload);

                ResponseEntity<String> responseEntityDisburse = loanService.generateRequestSignature2(xmlPayload);

                logger.info("take over disbursement notification utumishi response" + responseEntityDisburse);

                utils.sendMessage(
                        rabbitMqParameters.getExchange(),
                        rabbitMqParameters.getApprovalAndDisbursRoutingKey(),
                        String.valueOf(responseEntityDisburse));

                logger.info("utumishi response message for take over disbursement notification sent to the queue: {}", responseEntityDisburse);

                return responseEntityDisburse;

            } catch (Exception e) {
                logger.info("fail to send take over disbursement to utumishi ");
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


