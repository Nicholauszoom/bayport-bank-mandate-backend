package com.example.Project.Management.bayport.loan.service;

import com.dflex.emkopo.api.consumer.portal.dto.*;
import com.dflex.emkopo.api.dto.ApiReqHeaderDto;
import com.dflex.emkopo.api.dto.GenericApiReqDataDto;
import com.dflex.emkopo.api.dto.GenericApiReqDataDtoWrapper;
import com.dflex.emkopo.app.balance.takeover.entity.Takeoverbal;
import com.dflex.emkopo.app.balance.takeover.service.TakeoverBalanceService;
import com.dflex.emkopo.app.borrower.repository.BorrowerRepository;
import com.dflex.emkopo.app.disbursement.repository.DisbursementRepository;
import com.dflex.emkopo.app.ess_accounts.repository.AccountRepository;
import com.dflex.emkopo.app.ess_accounts.service.AccountsService;
import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProvider;
import com.dflex.emkopo.app.fsp.repositories.FinancialServiceProviderRepository;
import com.dflex.emkopo.app.loan.dto.approval.LoanApprovalApiDataDto;
import com.dflex.emkopo.app.loan.dto.approval.LoanApprovalApiDetailDto;
import com.dflex.emkopo.app.loan.dto.approval.loan_utumishi_final_response.LoanUtumishiFinalResponseApiDataDto;
import com.dflex.emkopo.app.loan.dto.approval.loan_utumishi_final_response.LoanUtumishiFinalResponseApiDto;
import com.dflex.emkopo.app.loan.dto.cancellation.LoanCancellationApiDto;
import com.dflex.emkopo.app.loan.dto.cancellation.LoanCancellationApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.charges.*;
import com.dflex.emkopo.app.loan.dto.ess_notification_response.EssNotificationApiRespDataDto;
import com.dflex.emkopo.app.loan.dto.ess_notification_response.EssRequestLoanInitialApprovePortalDto;
import com.dflex.emkopo.app.loan.dto.ess_notification_response.EssResponseLoanInitialApprovePortalDto;
import com.dflex.emkopo.app.loan.dto.ess_notification_response.payment_acknoledgement_notification.EssPaymentNotificationApiRespDataDto;
import com.dflex.emkopo.app.loan.dto.ess_notification_response.payment_acknoledgement_notification.EssRequestPaymentAcknoledgementPortalDto;
import com.dflex.emkopo.app.loan.dto.ess_notification_response.payment_acknoledgement_notification.EssResponsePaymentAcknoledgementPortalDto;
import com.dflex.emkopo.app.loan.dto.liquidization.LoanLiquidizationApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.liquidization.LoanLiquidizationApiReqDetailDto;
import com.dflex.emkopo.app.loan.dto.payment_acknoledgement.PaymentAcknoledgeApiDataDto;
import com.dflex.emkopo.app.loan.dto.payment_acknoledgement.PaymentAcknoledgeApiDetailDto;
import com.dflex.emkopo.app.loan.dto.payoff.LoanPayOffApiReqDto;
import com.dflex.emkopo.app.loan.dto.payoff.partialPaymentReqPayOff.PaymentPayoffReqDto;
import com.dflex.emkopo.app.loan.dto.payoff.partialPaymentResponsePayOff.PaymentPayOffRespDataDto;
import com.dflex.emkopo.app.loan.dto.payoff.partialPaymentResponsePayOff.PaymentPayOffRespDetailDto;
import com.dflex.emkopo.app.loan.dto.payoff.response.LoanPayOffApiResponseDataDto;
import com.dflex.emkopo.app.loan.dto.payoff.response.LoanPayOffApiResponseDto;
import com.dflex.emkopo.app.loan.dto.payoff.response.LoanPayOffResponseDetailDto;
import com.dflex.emkopo.app.loan.dto.registration.LoanRegistrationApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.registration.LoanRegistrationApiReqDto;
import com.dflex.emkopo.app.loan.dto.restructure.affordability.request.AffordabilityApiDto;
import com.dflex.emkopo.app.loan.dto.restructure.affordability.request.AffordabilityApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.restructure.affordability.response.AffordabilityApirRespDataDto;
import com.dflex.emkopo.app.loan.dto.restructure.balance.request.RestructureBalanceApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.restructure.balance.response.RestructureBalanceApiRespDataDto;
import com.dflex.emkopo.app.loan.dto.restructure.fsp_initiate_restructure.InitiateRestructureFspApiDataDto;
import com.dflex.emkopo.app.loan.dto.restructure.rejection.RestructureRejectApiDto;
import com.dflex.emkopo.app.loan.dto.restructure.rejection.RestructureRejectApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.restructure.restructure.RestructureApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.restructure.restructure.RestructureApiReqDto;
import com.dflex.emkopo.app.loan.dto.takeover.LoanTakeOverOfferApiReqDto;
import com.dflex.emkopo.app.loan.dto.takeover.payment_notification.PaymentNotificationApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.takeover.payment_notification.PaymentNotificationApiReqDto;
import com.dflex.emkopo.app.loan.dto.takeover.take_over_balance.TakeOverBalanceApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.takeover.take_over_balance.TakeOverBalanceApiReqDto;
import com.dflex.emkopo.app.loan.dto.takeover.take_over_balance.response.TakeOverBalanceApiRespDataDto;
import com.dflex.emkopo.app.loan.dto.takeover.take_over_balance.response.TakeOverBalanceApiRespDetailDto;
import com.dflex.emkopo.app.loan.dto.takeover.take_over_balance.response.TakeOverBalanceApiRespDto;
import com.dflex.emkopo.app.loan.dto.takeover.take_over_loan.TakeOverLoanApiReqDto;
import com.dflex.emkopo.app.loan.dto.topup.LoanTopUpApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.topup.LoanTopUpApiReqDto;
import com.dflex.emkopo.app.loan.dto.topup.balance.request.LoanTopUpBalancApiReqDataDto;
import com.dflex.emkopo.app.loan.dto.topup.balance.request.LoanTopUpBalanceApiReqDetailDto;
import com.dflex.emkopo.app.loan.dto.topup.balance.request.LoanTopUpBalanceApiReqDto;
import com.dflex.emkopo.app.loan.dto.topup.balance.response.LoanTopUpBalanceApiRespDataDto;
import com.dflex.emkopo.app.loan.dto.topup.balance.response.LoanTopUpPayOffBalanceApiRespDetailDto;
import com.dflex.emkopo.app.loan.entity.Loan;
import com.dflex.emkopo.app.loan.entity.LoanCancellation;
import com.dflex.emkopo.app.loan.entity.Repayment;
import com.dflex.emkopo.app.loan.repository.*;
import com.dflex.emkopo.app.message_req_resp.charges.entity.ChargeLogs;
import com.dflex.emkopo.app.message_req_resp.charges.repository.ChargeLogRepository;
import com.dflex.emkopo.app.message_req_resp.loan.entity.LoanLogs;
import com.dflex.emkopo.app.message_req_resp.loan.repository.LoanLogRepository;
import com.dflex.emkopo.app.product.entity.Product;
import com.dflex.emkopo.app.product.repository.ProductRepository;
import com.dflex.emkopo.app.restructuring.dto.balance.RestructureBalanceApiReqDto;
import com.dflex.emkopo.app.restructuring.dto.balance.response.RestructureBalanceApiRespDto;
import com.dflex.emkopo.app.status.repository.StatusRepository;
import com.dflex.emkopo.app.status.service.StatusService;
import com.dflex.emkopo.commons.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SignatureException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
//import java.util.logging.Logger;

@Service
@Log4j2
public class LoanServiceImpl implements LoanService {
    ObjectMapper objectMapper = new ObjectMapper();

   @Value("${utumishiApiUrl}")
    private String loanResponseUrl;

    @Value("${utumishiApiUrl}")
    private String loanPayOffResponseUrl;

    @Value("${utumishiApiUrl}")
    private String partialPaymentPayOffResponseUrl;

    @Value("${utumishiApiUrl}")
    private String utumishiApiUrl;

    @Value("${charges.url}")
    private String chargesEndpoint;

    @Value("${balance.url}")
    private String balanceEndpoint;

    @Value("${takeover.balance.url}")
    private String balanceTakeOverEndpoint;

    @Value("${restructure.balance.url}")
    private String balanceRestructureEndpoint;

    @Value("${restructure.affordability.url}")
    private String affordabilityRestructureEndpoint;


    @Value("${log.path.files.url}")
    private String pathLogFile;

    @Autowired
    private Utils utils;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private DigitalSignature digitalSignature;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RabbitMqParameters rabbitMqParameters;

    @Autowired
    private LoanFinalUtumishiApprovalRepository loanFinalUtumishiApprovalRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private FinancialServiceProviderRepository financialServiceProviderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TakeoverBalanceService takeoverBalanceService;

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired
    private LoanCancellationRepository loanCancellationRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private StatusService statusService;

    @Autowired
    private RestructuringRepository restructuringRepository;

    @Autowired
    private LoanApprovalRepository loanApprovalRepository;

    @Autowired
    private DisbursementRepository disbursementRepository;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ChargeLogRepository chargeLogRepository;

    @Autowired
    private LoanLogRepository loanLogRepository;


    private static final Logger logger = LogManager.getLogger(LoanServiceImpl.class);

    public LoanServiceImpl(
            ObjectMapper objectMapper, Utils utils, DigitalSignature digitalSignature, LoanRepository loanRepository,
            ProductRepository productRepository, RepaymentRepository repaymentRepository, LoanCancellationRepository loanCancellationRepository, BorrowerRepository borrowerRepository, StatusService statusService, RestructuringRepository restructuringRepository, LoanApprovalRepository loanApprovalRepository, FinancialServiceProviderRepository financialServiceProviderRepository, StatusRepository statusRepository, StringUtil stringUtil, RabbitMqParameters rabbitMqParameters, ChargeLogRepository chargeLogRepository, LoanLogRepository loanLogRepository) {
        this.objectMapper = objectMapper;
        this.utils = utils;
        this.digitalSignature = digitalSignature;
        this.loanRepository = loanRepository;
        this.productRepository = productRepository;
        this.repaymentRepository = repaymentRepository;
        this.loanCancellationRepository = loanCancellationRepository;
        this.borrowerRepository = borrowerRepository;
        this.statusService = statusService;
        this.restructuringRepository = restructuringRepository;
        this.loanApprovalRepository = loanApprovalRepository;
        this.financialServiceProviderRepository =financialServiceProviderRepository;
        this.statusRepository =statusRepository;
        this.stringUtil = stringUtil;
        this.rabbitMqParameters = rabbitMqParameters;
        this.chargeLogRepository = chargeLogRepository;
        this.loanLogRepository = loanLogRepository;
    }



    @Override
    public ResponseEntity<String> calculateLoanCharges(String message) throws Exception{
        logger.info("Received loan charges request from utumishi: {}", message);

        JAXBContext jaxbContext = JAXBContext.newInstance(LoanChargesApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanChargesApiDto loanChargesApiDto = (LoanChargesApiDto) unmarshaller.unmarshal(stringReader);
        String mssId = loanChargesApiDto.getLoanChargesApiReqData().getRequestHeader().getMsgId();
        String fspCode = loanChargesApiDto.getLoanChargesApiReqData().getRequestHeader().getFspCode();
        LoanChargesApiReqDataDto loanChargeDataLevel= loanChargesApiDto.getLoanChargesApiReqData();
        try {
            BigDecimal   desiredDeductibleAmount;
            BigDecimal    totalInsurance ;
            BigDecimal     totalProcessingFee ;
            BigDecimal    totalInterestRateAmount ;
            BigDecimal    otherCharges ;
            BigDecimal    netLoanAmount ;
            BigDecimal   totalAmountToPay ;
            BigDecimal    tenure ;
            BigDecimal    eligibleAmount ;
            BigDecimal    monthlyReturnAmount = new BigDecimal("0.00");


            // Access the values from the converted objects
            Optional<Product> productDetail = productRepository.findByProductCode(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getProductCode());

            if(productDetail.isEmpty()){
                logger.info("invalid product code i.e Product Code: {}"+ loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getProductCode());
                return createResponseEntity(ResponseCodes.INVALID_PRODUCT_CODE ,loanChargesApiDto.getLoanChargesApiReqData().getRequestHeader().getFspCode(),mssId);

            }


            BigDecimal requestedAmount = loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getRequestedAmount();

            BigDecimal tenureTime = BigDecimal.valueOf(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getTenure());
            BigDecimal deductibleAmount = loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getDesiredDeductibleAmount();


            if (requestedAmount.compareTo(BigDecimal.ZERO) == 0){
                requestedAmount = deductibleAmount.multiply(tenureTime);
            }

            System.out.println("***********requested amount" + requestedAmount);

            /*
             * Response
             * */
            LoanChargesApiRespDetailDto loanChargesApiRespDetailDto = new LoanChargesApiRespDetailDto();

            Optional<FinancialServiceProvider> fsp = financialServiceProviderRepository.findByFspCode(fspCode);

            String sender = fsp.get().getFspShortName();

            ApiReqHeaderDto apiReqHeaderDto = utils.getApiRequestHeader(productDetail.get().getFspCode(), loanChargesApiDto.getLoanChargesApiReqData().getRequestHeader().getMsgId(), Constants.LOAN_CHARGES_RESPONSE, sender);

            LoanChargesApirRespDataDto genericApiReqDataDto = new LoanChargesApirRespDataDto();
            genericApiReqDataDto.setRequestHeader(apiReqHeaderDto);
            genericApiReqDataDto.setLoanChargesApiRespMessageDetail(loanChargesApiRespDetailDto);


            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanChargesApirRespDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(genericApiReqDataDto, stringWriter);
            String stringToBeSigned = stringWriter.toString();
            System.out.println("xml payload: " + stringToBeSigned);

            //convert charge utumishi request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(LoanChargesApiReqDataDto.class);
            Marshaller marshallerRq = jaxbContextReq.createMarshaller();
            marshallerRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterRq = new StringWriter();
            marshallerRq.marshal(loanChargeDataLevel, stringWriterRq);
            String chargeUtumishiRq = stringWriterRq.toString();
            System.out.println("loan charge sent to api: " + chargeUtumishiRq);

            logger.info("loan charges response is successfull i.e Product Code: {}" + chargeUtumishiRq);

            String apiResponse = cahrgeXmlSendToApi(chargeUtumishiRq);
            logger.info("Response Message from Portal Api: {}", apiResponse);

           // Remove the <row> tags and their corresponding closing tags
            String cleanedApiResponse = apiResponse.replaceAll("<row[^>]*>", "").replaceAll("</row>", "");

            logger.info("Cleaned Response Message: {}", cleanedApiResponse);

            JAXBContext jaxbContextCC = JAXBContext.newInstance(ChargePortalDto.class);
            Unmarshaller unmarshallerCC = jaxbContextCC.createUnmarshaller();
            StringReader stringReaderCC = new StringReader(cleanedApiResponse);
            ChargePortalDto chargePortalDto = (ChargePortalDto) unmarshallerCC.unmarshal(stringReaderCC);


            LoanChargesApiRespDetailDto loanChargesApiRespDetailDtoCC = new LoanChargesApiRespDetailDto();
            BigDecimal DesiredDeductibleAmount = chargePortalDto.getDesiredDeductibleAmount().setScale(2, RoundingMode.HALF_UP);
            loanChargesApiRespDetailDtoCC.setDesiredDeductibleAmount(DesiredDeductibleAmount);

            BigDecimal TotalInsurance = chargePortalDto.getTotalInsurance().setScale(2, RoundingMode.HALF_UP);
            loanChargesApiRespDetailDtoCC.setTotalInsurance(TotalInsurance);

            BigDecimal TotalProcessingFee = chargePortalDto.getTotalProcessingFee().setScale(2, RoundingMode.HALF_UP);
            loanChargesApiRespDetailDtoCC.setTotalProcessingFee(TotalProcessingFee);

            BigDecimal TotalInterestRateAmount = chargePortalDto.getTotalInterestRateAmount().setScale(2, RoundingMode.HALF_UP);
            loanChargesApiRespDetailDtoCC.setTotalInterestRateAmount(TotalInterestRateAmount);

            BigDecimal OtherCharges = chargePortalDto.getOtherCharges().setScale(2, RoundingMode.HALF_UP);
            loanChargesApiRespDetailDtoCC.setOtherCharges(OtherCharges);

            BigDecimal NetLoanAmount = chargePortalDto.getNetLoanAmount().setScale(2, RoundingMode.HALF_UP);
            loanChargesApiRespDetailDtoCC.setNetLoanAmount(NetLoanAmount);

            BigDecimal TotalAmountToPay = chargePortalDto.getTotalAmountToPay().setScale(2, RoundingMode.HALF_UP);
            loanChargesApiRespDetailDtoCC.setTotalAmountToPay(TotalAmountToPay);

            loanChargesApiRespDetailDtoCC.setTenure(chargePortalDto.getTenure());

            BigDecimal EligibleAmount = chargePortalDto.getEligibleAmount().setScale(2, RoundingMode.HALF_UP);
            loanChargesApiRespDetailDtoCC.setEligibleAmount(EligibleAmount);

            BigDecimal MonthlyReturnAmount = chargePortalDto.getMonthlyReturnAmount().setScale(2, RoundingMode.HALF_UP);
            loanChargesApiRespDetailDtoCC.setMonthlyReturnAmount(MonthlyReturnAmount);
            logger.info("charge message details payload:" + loanChargesApiRespDetailDtoCC);
            System.out.println("phase 1 *************: ");

            ApiReqHeaderDto apiReqHeaderDtoCC = new ApiReqHeaderDto();
            apiReqHeaderDtoCC.setFspCode(fsp.get().getFspCode());
            apiReqHeaderDtoCC.setMsgId(mssId);
            apiReqHeaderDtoCC.setMessageType(Constants.LOAN_CHARGES_RESPONSE);
            apiReqHeaderDtoCC.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDtoCC.setSender(fsp.get().getFspName());

            LoanChargesApirRespDataDto chargesApirRespDataDto = new LoanChargesApirRespDataDto();
            chargesApirRespDataDto.setRequestHeader(apiReqHeaderDtoCC);
            chargesApirRespDataDto.setLoanChargesApiRespMessageDetail(loanChargesApiRespDetailDtoCC);
            logger.info("charge json payload:" + loanChargesApiRespDetailDtoCC);

            JAXBContext jaxbContext2CC = JAXBContext.newInstance(LoanChargesApirRespDataDto.class);
            Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
            marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterCC = new StringWriter();
            marshallerCC.marshal(chargesApirRespDataDto, stringWriterCC);
            String xmlPayloadCC = stringWriterCC.toString();
            System.out.println("charge xml payload: " + xmlPayloadCC);
            logger.info("charges catalog i.e with fsp code:" + xmlPayloadCC);

            JAXBContext jaxbContextSg = JAXBContext.newInstance(GenericApiReqDataDto.class);
            Unmarshaller unmarshallerSg = jaxbContextSg.createUnmarshaller();
            StringReader stringReaderSg = new StringReader(xmlPayloadCC);
            GenericApiReqDataDto genericApiReqDataDtoSg = (GenericApiReqDataDto) unmarshallerSg.unmarshal(stringReaderSg);

            // Extract and convert the content to be signed
            String content = convertObjectToXml(genericApiReqDataDtoSg);
            System.out.println("Content to be signed: " + content);

            String signatureSg = digitalSignature.createSignature(content);
            System.out.println("Generated Signature: " + signatureSg);

            // Create the final XML with the signature
            String finalXml = appendSignatureToXml2(content, signatureSg);
            System.out.println("Final XML: " + finalXml);

            logger.info("Final Xml Request {}:", finalXml);

            // Send the final XML to the specified API endpoint
            logger.info("Final CURL SUPPOSED request sent to utumishi***** {}:", finalXml);

            //save charges logs in loan charges log table
            ChargeLogs chargeLogs = new ChargeLogs();
            chargeLogs.setCheckNumber(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getCheckNumber());
            chargeLogs.setDesignationCode(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getDesignationCode());
            chargeLogs.setDesignatonName(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getDesignationName());
            chargeLogs.setBasicSalary(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getBasicSalary());
            chargeLogs.setNetSalary(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getNetSalary());
            chargeLogs.setOneThirdAmount(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getOneThirdAmount());
            chargeLogs.setRequestedAmount(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getRequestedAmount());
            chargeLogs.setDeductibleAmount(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getDeductibleAmount());
            chargeLogs.setDesiredDeductibleAmount(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getDesiredDeductibleAmount());
            chargeLogs.setRetirementDate(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getRetirementDate());
            chargeLogs.setTermsOfEmployment(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getTermsOfEmployment());
            chargeLogs.setTenure(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getTenure());
            chargeLogs.setProductCode(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getProductCode());
            chargeLogs.setVoteCode(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getVoteCode());
            chargeLogs.setTotalEmployeeDeduction(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getTotalEmployeeDeduction());
            chargeLogs.setJobClassCode(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getJobClassCode());
            chargeLogs.setTotalInsurance(loanChargesApiRespDetailDtoCC.getTotalInsurance());
            chargeLogs.setTotalProcessingFees(loanChargesApiRespDetailDtoCC.getTotalProcessingFee());
            chargeLogs.setTotalInterestRateAmount(loanChargesApiRespDetailDtoCC.getTotalInterestRateAmount());
            chargeLogs.setOtherCharges(loanChargesApiRespDetailDtoCC.getOtherCharges());
            chargeLogs.setNetLoanAmount(loanChargesApiRespDetailDtoCC.getNetLoanAmount());
            chargeLogs.setTotalAmountToPay(loanChargesApiRespDetailDtoCC.getTotalAmountToPay());
            chargeLogs.setEligibleAmount(loanChargesApiRespDetailDtoCC.getEligibleAmount());
            chargeLogs.setMonthlyReturnAmount(loanChargesApiRespDetailDtoCC.getMonthlyReturnAmount());
            chargeLogRepository.save(chargeLogs);

            return ResponseEntity.ok(finalXml);
        } catch(Exception e){
           return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR,fspCode, mssId);
        }
    }


    //FAKE TOP UP LOAN BALANCE REQUEST
    public ResponseEntity<String> fakeLoanBalanceFromChargeRequest(String message) throws Exception{
        logger.info("Received fake loan charges request from utumishi: {}", message);

        JAXBContext jaxbContext = JAXBContext.newInstance(LoanChargesApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanChargesApiDto loanChargesApiDto = (LoanChargesApiDto) unmarshaller.unmarshal(stringReader);
        String mssId = loanChargesApiDto.getLoanChargesApiReqData().getRequestHeader().getMsgId();
        String fspCode = loanChargesApiDto.getLoanChargesApiReqData().getRequestHeader().getFspCode();
     try{
         LoanTopUpBalanceApiReqDetailDto loanTopUpBalanceApiReqDetailDto = new LoanTopUpBalanceApiReqDetailDto();
         loanTopUpBalanceApiReqDetailDto.setCheckNumber(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getCheckNumber());
         loanTopUpBalanceApiReqDetailDto.setLoanNumber("TESTNUMBER");
         loanTopUpBalanceApiReqDetailDto.setFirstName("TESTFIRST");
         loanTopUpBalanceApiReqDetailDto.setMiddleName("TESTMIDDLE");
         loanTopUpBalanceApiReqDetailDto.setLastName("TESTLOANNUMBER");
         loanTopUpBalanceApiReqDetailDto.setVoteCode(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getVoteCode());
         loanTopUpBalanceApiReqDetailDto.setVoteName("TESTVOTE");
         loanTopUpBalanceApiReqDetailDto.setDeductionAmount(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getDeductibleAmount());
         loanTopUpBalanceApiReqDetailDto.setDeductionCode("TESTDEDUCTIONCODE");
         loanTopUpBalanceApiReqDetailDto.setDeductionName("DEDUCTIONNAME");
         loanTopUpBalanceApiReqDetailDto.setDeductionBalance(loanChargesApiDto.getLoanChargesApiReqData().getMessageDetails().getDeductibleAmount());
         loanTopUpBalanceApiReqDetailDto.setPaymentOption("FLT");

         ApiReqHeaderDto apiReqHeaderDtoCC = new ApiReqHeaderDto();
         apiReqHeaderDtoCC.setFspCode(fspCode);
         apiReqHeaderDtoCC.setMsgId(mssId);
         apiReqHeaderDtoCC.setMessageType(Constants.TOP_UP_PAY_0FF_BALANCE_REQUEST);
         apiReqHeaderDtoCC.setReceiver(Constants.UTUMISHI_RECEIVER);
         apiReqHeaderDtoCC.setSender("FSP");


         LoanTopUpBalancApiReqDataDto loanTopUpBalancApiReqDataDto = new LoanTopUpBalancApiReqDataDto();
         loanTopUpBalancApiReqDataDto.setRequestHeader(apiReqHeaderDtoCC);
         loanTopUpBalancApiReqDataDto.setLoanTopUpBalanceApiReqDetailDto(loanTopUpBalanceApiReqDetailDto);
         logger.info("balance json payload:" + loanTopUpBalanceApiReqDetailDto);

         JAXBContext jaxbContext2CC = JAXBContext.newInstance(LoanTopUpBalancApiReqDataDto.class);
         Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
         marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
         StringWriter stringWriterCC = new StringWriter();
         marshallerCC.marshal(loanTopUpBalancApiReqDataDto, stringWriterCC);
         String xmlPayloadCC = stringWriterCC.toString();
         System.out.println("balance xml payload: " + xmlPayloadCC);


         logger.info("balance catalog i.e with fsp code:" + xmlPayloadCC);

         JAXBContext jaxbContextSg = JAXBContext.newInstance(GenericApiReqDataDto.class);
         Unmarshaller unmarshallerSg = jaxbContextSg.createUnmarshaller();
         StringReader stringReaderSg = new StringReader(xmlPayloadCC);
         GenericApiReqDataDto genericApiReqDataDtoSg = (GenericApiReqDataDto) unmarshallerSg.unmarshal(stringReaderSg);

         // Extract and convert the content to be signed
         String content = convertObjectToXml(genericApiReqDataDtoSg);

         String signatureSg = digitalSignature.createSignature(content);
         System.out.println("Generated Signature: " + signatureSg);

         // Create the final XML with the signature
         String finalXml = appendSignatureToXml2(content, signatureSg);

         ResponseEntity<String> balanceResponse = loanTopUpPayOffBalance(finalXml);

    return balanceResponse;

}catch(Exception e){
         logger.info("failed fake balance response");
    return null;
        }

    }

    @Override
    public ResponseEntity<String> loanPayOffReq(String message) throws Exception{

        logger.info("Received loan PayOff request: {}", message);

        JAXBContext jaxbContext = JAXBContext.newInstance(LoanPayOffApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanPayOffApiReqDto loanPayOffApiReqDto = (LoanPayOffApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanPayOffApiReqDto.getLoanPayOffApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = loanPayOffApiReqDto.getLoanPayOffApiReqDataDto().getRequestHeader().getMsgId();

        try {
            //Access values
            String loanNumber = loanPayOffApiReqDto.getLoanPayOffApiReqDataDto().getLoanPayOffReqDetailDto().getLoanNumber();
            BigDecimal deductionAmount = BigDecimal.valueOf(Double.parseDouble(String.valueOf(loanPayOffApiReqDto.getLoanPayOffApiReqDataDto().getLoanPayOffReqDetailDto().getDeductionAmount())));

            //loan pay off operation
            Loan loanId = loanRepository.findLoanByLoanNumber(loanNumber);
            Optional<FinancialServiceProvider> fsp =
                    financialServiceProviderRepository.findByFspCode(fspCode);

            String fspReferenceNumber = null;
            if (fsp.isPresent()) {
                FinancialServiceProvider financialServiceProvider = fsp.get();
                fspReferenceNumber = financialServiceProvider.getFspReferenceNumber();

                logger.info("successful get Reference Number with fsp code: " + fspCode);

            } else {
                logger.info("fail to get the Reference Number, reason: no such fsp with code: " + fspCode);
                return createResponseEntity(ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER,  fspCode, mssId);
            }

            //payment reference number
            String paymentReferenceNumber = loanId.getPaymentReferenceNumber();

            //out standing balance
            BigDecimal loanAmount = loanId.getLoanAmount();
            //BigDecimal outstandingBalance = loanAmount - deductionAmount;

            BigDecimal outstandingBalance = loanAmount
                    .min(deductionAmount);

            //total Payoff Amount
            BigDecimal interest = loanId.getInterest();
            BigDecimal totalPayoffAmount = outstandingBalance.add(interest).min(deductionAmount);

            //loan pay off response
            LoanPayOffResponseDetailDto loanPayOffResponseDetailDto = new LoanPayOffResponseDetailDto();

            loanPayOffResponseDetailDto.setLoanNumber(loanNumber);
            loanPayOffResponseDetailDto.setFspReferenceNumber(fspReferenceNumber);
            loanPayOffResponseDetailDto.setPaymentReferenceNumber(paymentReferenceNumber);
            loanPayOffResponseDetailDto.setTotalPayoffAmount(totalPayoffAmount);
            loanPayOffResponseDetailDto.setOutstandingBalance(outstandingBalance);
            loanPayOffResponseDetailDto.setFinalPaymentDate(loanId.getLoanDate());
            //loanPayOffResponseDetailDto.setLastDeductionDate(loanLastDeduction.getRepaymentDate());
            loanPayOffResponseDetailDto.setLastPayDate(loanId.getLoanDate());
            loanPayOffResponseDetailDto.setEndDate(loanId.getLoanDate());

            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);

            String sender = fspByCode.get().getFspShortName();

            ApiReqHeaderDto apiReqHeaderDto = utils.getApiRequestHeader(fspCode, utils.getReceipt(fspCode), Constants.LOAN_PAY_0FF_BALANCE_RESPONSE, sender);

            LoanPayOffApiResponseDataDto loanPayOffApiResponseDataDto = new LoanPayOffApiResponseDataDto();
            loanPayOffApiResponseDataDto.setRequestHeader(apiReqHeaderDto);
            loanPayOffApiResponseDataDto.setLoanPayOffResponseDetailDto(loanPayOffResponseDetailDto);

            String stringToBeSigned = utils.convertXmlToString(JAXBContext.newInstance(LoanPayOffApiResponseDataDto.class), loanPayOffApiResponseDataDto);
            String signature = digitalSignature.createSignature(stringToBeSigned);

            LoanPayOffApiResponseDto loanPayOffApiResponseDto = new LoanPayOffApiResponseDto();
            loanPayOffApiResponseDto.setLoanPayOffApiResponseDataDto(loanPayOffApiResponseDataDto);
            loanPayOffApiResponseDto.setMessageHash(signature);

            String requestToBeSend = utils.convertXmlToString(JAXBContext.newInstance(LoanPayOffApiResponseDto.class), loanPayOffApiResponseDto);

            logger.info("loan payoff response is successfull i.e Loan Number:" + loanNumber);
            return ResponseEntity.ok(requestToBeSend);

        }catch (Exception e){
            logger.info(e.getMessage());
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR,fspCode, mssId);

        }

    }

    @Override
    public ResponseEntity<String> fullPaymentPayOffReq(String message) throws Exception {
        logger.info("Received loan Full Payment Pay Off request: {}", message);

        String fspCodeString = null;
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanPayOffApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanPayOffApiReqDto loanPayOffApiReqDto = (LoanPayOffApiReqDto) unmarshaller.unmarshal(stringReader);
        String mssId = loanPayOffApiReqDto.getLoanPayOffApiReqDataDto().getRequestHeader().getMsgId();
        try{

            //Access values
            String loanNumber = loanPayOffApiReqDto.getLoanPayOffApiReqDataDto().getLoanPayOffReqDetailDto().getLoanNumber();
            BigDecimal deductionAmount = BigDecimal.valueOf(Double.parseDouble(String.valueOf(loanPayOffApiReqDto.getLoanPayOffApiReqDataDto().getLoanPayOffReqDetailDto().getDeductionAmount())));
            String fspCode = loanPayOffApiReqDto.getLoanPayOffApiReqDataDto().getRequestHeader().getFspCode();
            fspCodeString = fspCode;
            //loan pay off operation
            Loan loanId = loanRepository.findLoanByLoanNumber(loanNumber);

            //fsp reference number
            String fspReferenceNumber= loanId.getFspReferenceNumber();

            //payment reference number
            String paymentReferenceNumber = loanId.getPaymentReferenceNumber();

            //out standing balance
            BigDecimal loanAmount = loanId.getLoanAmount();
            //BigDecimal outstandingBalance = loanAmount - deductionAmount;

            BigDecimal outstandingBalance = loanAmount
                    .min(deductionAmount);

            //total Payoff Amount
            BigDecimal interest = loanId.getInterest();
            BigDecimal totalPayoffAmount = outstandingBalance.add(interest).min(deductionAmount);

            //loan details
            Loan loanDetail= loanRepository.findByLoanNumberAndFspCode(loanNumber, fspCode);

            //loan pay off response
            LoanPayOffResponseDetailDto loanPayOffResponseDetailDto = new LoanPayOffResponseDetailDto();

            loanPayOffResponseDetailDto.setLoanNumber(loanNumber);
            loanPayOffResponseDetailDto.setFspReferenceNumber(fspReferenceNumber);
            loanPayOffResponseDetailDto.setPaymentReferenceNumber(paymentReferenceNumber);
            loanPayOffResponseDetailDto.setTotalPayoffAmount(totalPayoffAmount);
            loanPayOffResponseDetailDto.setOutstandingBalance(outstandingBalance);
            loanPayOffResponseDetailDto.setFinalPaymentDate(loanDetail.getLoanDate());
            loanPayOffResponseDetailDto.setLastPayDate(loanDetail.getLoanDate());
            loanPayOffResponseDetailDto.setEndDate(loanId.getLoanDate());

            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);

            String sender = fspByCode.get().getFspShortName();

            ApiReqHeaderDto apiReqHeaderDto =  utils.getApiRequestHeader(fspCode, utils.getReceipt(fspCode), Constants.FULL_REPAYMENT_0FF_BALANCE_RESPONSE, sender);

            GenericApiReqDataDto genericApiReqDataDto = new GenericApiReqDataDto();
            genericApiReqDataDto.setRequestHeader(apiReqHeaderDto);
            genericApiReqDataDto.setMessageDetails(loanPayOffResponseDetailDto);

            String stringToBeSigned = utils.convertXmlToString(JAXBContext.newInstance(GenericApiReqDataDto.class), genericApiReqDataDto);
            String signature = digitalSignature.createSignature(stringToBeSigned);

            GenericApiReqDataDtoWrapper genericApiReqDataDtoWrapper = new GenericApiReqDataDtoWrapper();
            genericApiReqDataDtoWrapper.setRequestData(genericApiReqDataDto);
            genericApiReqDataDtoWrapper.setMessageHash(signature);

            String requestToBeSend = utils.convertXmlToString(JAXBContext.newInstance(GenericApiReqDataDtoWrapper.class), genericApiReqDataDtoWrapper);

            logger.info("loan full payment payoff response successfull i.e Loan Number:" + loanNumber);
            return ResponseEntity.ok(requestToBeSend);

        }catch (Exception e){
            logger.info(e.getMessage());
            logger.info("Exception occurred while create loan full payment response {}");
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR, fspCodeString, mssId);
        }
    }

    @Override
    public ResponseEntity<String> partialPaymentPayOffReq(String message) throws Exception {

        logger.info("Received loan Partial Pay Off request: {}", message);

        String fspCodeString = null;
        JAXBContext jaxbContext = JAXBContext.newInstance(PaymentPayoffReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        PaymentPayoffReqDto paymentPayoffReqDto = (PaymentPayoffReqDto) unmarshaller.unmarshal(stringReader);

        String mssId = paymentPayoffReqDto.getPaymentPayoffReqDataDto().getRequestHeader().getMsgId();
        try{

            //Access values
            String loanNumber = paymentPayoffReqDto.getPaymentPayoffReqDataDto().getPaymentPayoffReqDetailDto().getLoanNumber();
            String fspCode = paymentPayoffReqDto.getPaymentPayoffReqDataDto().getPaymentPayoffReqDetailDto().getFspCode();
            fspCodeString = fspCode;

            //loan details
            Loan loanDetail= loanRepository.findByLoanNumberAndFspCode(loanNumber, fspCode);

            //partial payment pay off response
            PaymentPayOffRespDetailDto paymentPayOffRespDetailDto = new PaymentPayOffRespDetailDto();

            //last deduction date
            List<Repayment> repaymentList= repaymentRepository.findByLoanId(loanDetail);
            Repayment latestRepayment = null;
            LocalDate latestDate = LocalDate.MIN;

            for (Repayment repayment : repaymentList) {
                LocalDate repaymentDate = repayment.getRepaymentDate();
                if (repaymentDate.compareTo(latestDate) > 0) {
                    latestDate = repaymentDate;
                    latestRepayment = repayment;
                }
            }

            paymentPayOffRespDetailDto.setLoanNumber(loanNumber);
            paymentPayOffRespDetailDto.setTotalPayOffAmount(loanDetail.getDeductionBalance());
            paymentPayOffRespDetailDto.setLastDeductionDate(latestRepayment.getRepaymentDate());
            paymentPayOffRespDetailDto.setFspBankAccountName(loanDetail.getBankAccountName());
            paymentPayOffRespDetailDto.setSwiftCode(loanDetail.getSwiftCode());
            paymentPayOffRespDetailDto.setMnoChannels(loanDetail.getMnoChannels());
            paymentPayOffRespDetailDto.setPaymentReferenceNumber(loanDetail.getPaymentReferenceNumber());
            paymentPayOffRespDetailDto.setFinalPaymentDate(loanDetail.getLoanDate());
            paymentPayOffRespDetailDto.setEndDate(loanDetail.getLoanDate());

            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);

            String sender = fspByCode.get().getFspShortName();
            ApiReqHeaderDto apiReqHeaderDto =  utils.getApiRequestHeader(fspCode, utils.getReceipt(fspCode), Constants.PARTIAL_REPAYMENT_0FF_BALANCE_RESPONSE, sender);

            GenericApiReqDataDto genericApiReqDataDto = new GenericApiReqDataDto();
            genericApiReqDataDto.setRequestHeader(apiReqHeaderDto);
            genericApiReqDataDto.setMessageDetails(paymentPayOffRespDetailDto);

            String stringToBeSigned = utils.convertXmlToString(JAXBContext.newInstance(PaymentPayOffRespDataDto.class), genericApiReqDataDto);
            String signature = digitalSignature.createSignature(stringToBeSigned);

            GenericApiReqDataDtoWrapper genericApiReqDataDtoWrapper = new GenericApiReqDataDtoWrapper();
            genericApiReqDataDtoWrapper.setRequestData(genericApiReqDataDto);
            genericApiReqDataDtoWrapper.setMessageHash(signature);

            String requestToBeSend = utils.convertXmlToString(JAXBContext.newInstance(GenericApiReqDataDtoWrapper.class), genericApiReqDataDtoWrapper);

            logger.info("partial payment response successfull i.e with Loan Number:" + loanNumber);
            return ResponseEntity.ok(requestToBeSend);

        }catch (Exception e){
            logger.info(e.getMessage());
            logger.info("Exception occurred while create partial payment response {}");
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR, fspCodeString, mssId);
        }
    }

    @Override
    public ResponseEntity<String> loanCancellation(String message) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanCancellationApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanCancellationApiDto loanCancellationApiDto = (LoanCancellationApiDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanCancellationApiDto.getLoanCancellationApiReqData().getRequestHeader().getFspCode();
        String mssId = loanCancellationApiDto.getLoanCancellationApiReqData().getRequestHeader().getMsgId();
        logger.info("Received loan cancellation request: {}", message);

        try {
            LoanCancellation loanCancellation = new LoanCancellation();
            loanCancellation.setReason(loanCancellationApiDto.getLoanCancellationApiReqData().getMessageDetails().getReason());


            // convert l application request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(LoanCancellationApiReqDataDto.class);
            Marshaller marshallerLRq = jaxbContextReq.createMarshaller();
            marshallerLRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterLRq = new StringWriter();
            marshallerLRq.marshal(loanCancellationApiDto.getLoanCancellationApiReqData(), stringWriterLRq);
            String loanUtumishiRq = stringWriterLRq.toString();

            logger.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getFinalNotifRoutingKey(),
                    loanUtumishiRq);

            logger.info("request calcellation sent to the queue: " + loanUtumishiRq);

                return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            logger.error("Error processing loan cancellation request", e);
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }



    private ResponseEntity<String> createResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
        String description = ResponseCodes.getDescription(responseCode);
        ResponseEntity<String> responseEntity = utils.messageResponseWrapperToUtumishi(responseCode, description, fspCode, mssId, Constants.UTUMISHI_RESPONSE);
        logger.info("Response for FSP Code {}: {}", fspCode, responseEntity.getBody());
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> loanTopUp(String message) throws Exception {

        logger.info("Received loan TopUp request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanTopUpApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanTopUpApiReqDto loanTopUpApiReqDto = (LoanTopUpApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanTopUpApiReqDto.getLoanTopUpApiReqData().getRequestHeader().getFspCode();
        String mssId = loanTopUpApiReqDto.getLoanTopUpApiReqData().getRequestHeader().getMsgId();
        try {

            // convert l application request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(LoanTopUpApiReqDataDto.class);
            Marshaller marshallerLRq = jaxbContextReq.createMarshaller();
            marshallerLRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterLRq = new StringWriter();
            marshallerLRq.marshal(loanTopUpApiReqDto.getLoanTopUpApiReqData(), stringWriterLRq);
            String loanUtumishiRq = stringWriterLRq.toString();

            logger.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getTopUPRoutingKey(),
                    loanUtumishiRq);

            //save loan TOP UP logs
            LoanLogs loanLogs = new LoanLogs();
            loanLogs.setCheckNumber(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getCheckNumber());
            loanLogs.setFirstName(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getFirstName());
            loanLogs.setMiddleName(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getMiddleName());
            loanLogs.setLastName(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getLastName());
            loanLogs.setSex(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getSex());
            loanLogs.setBankAccountNumber(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getBankAccountNumber());
            loanLogs.setEmploymentDate(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getEmploymentDate());
            loanLogs.setMaritalStatus(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getMaritalStatus());
            loanLogs.setConfirmationDate(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getConfirmationDate());
            loanLogs.setTotalEmployeeDeduction(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getTotalEmployeeDeduction());
            loanLogs.setNearestBranchCode(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getNearestBranchCode());
            loanLogs.setNearestBranchName(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getNearestBranchName());
            loanLogs.setVoteCode(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getVoteCode());
            loanLogs.setVoteName(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getVoteName());
            loanLogs.setNin(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getNIN());
            loanLogs.setDesignationCode(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getDesignationCode());
            loanLogs.setDesignationName(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getDesignationName());
            loanLogs.setBasicSalary(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getBasicSalary());
            loanLogs.setNetSalary(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getNetSalary());
            loanLogs.setOneThirdAmount(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getOneThirdAmount());
            loanLogs.setRequestedAmount(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getRequestedAmount());
            loanLogs.setDesiredDeductibleAmount(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getDesiredDeductibleAmount());
            loanLogs.setRetirementDate(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getRetirementDate());
            loanLogs.setTermsOfEmployment(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getTermsOfEmployment());
            loanLogs.setTenure(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getTenure());
            loanLogs.setProductCode(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getProductCode());
            loanLogs.setTermsOfEmployment(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getTermsOfEmployment());
            loanLogs.setInterestRate(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getInterestRate());
            loanLogs.setProcessingFee(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getProcessingFee());
            loanLogs.setInsurance(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getInsurance());
            loanLogs.setPhysicalAddress(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getPhysicalAddress());
            loanLogs.setEmailAddress(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getEmailAddress());
            loanLogs.setMobileNumber(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getMobileNumber());
            loanLogs.setApplicationNumber(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getApplicationNumber());
            loanLogs.setLoanPurpose(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getLoanPurpose());
            loanLogs.setContractStartDate(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getContractStartDate());
            loanLogs.setContractEndDate(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getContractEndDate());
            loanLogs.setSwiftCode(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getSwiftCode());
            loanLogs.setFunding(loanTopUpApiReqDto.getLoanTopUpApiReqData().getLoanTopUpReqMessageDetail().getFunding());
            loanLogs.setLoanStatus("REFINANCE LOAN APPLICATION");
            loanLogRepository.save(loanLogs);

            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing loan top up {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }
    @Override
    public ResponseEntity<String> loanTopUpPayOffBalance(String message) throws Exception {
        logger.info("Received loan TopUp pay off balance request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanTopUpBalanceApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanTopUpBalanceApiReqDto loanTopUpBalanceApiReqDto = (LoanTopUpBalanceApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto().getRequestHeader().getMsgId();
        String loanNo = loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto().getLoanTopUpBalanceApiReqDetailDto().getLoanNumber();
        try {
            //convert balance utumishi request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(LoanTopUpBalancApiReqDataDto.class);
            Marshaller marshallerRq = jaxbContextReq.createMarshaller();
            marshallerRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterRq = new StringWriter();
            marshallerRq.marshal(loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto(), stringWriterRq);
            String balanceUtumishiRq = stringWriterRq.toString();
            System.out.println("loan charge sent to api: " + balanceUtumishiRq);

            logger.info("loan charges response is successfull i.e Product Code: {}" + balanceUtumishiRq);

            String apiResponse = balanceXmlSendToApi(balanceUtumishiRq);
            logger.info("balance response Message from Portal Api: {}", apiResponse);

            JAXBContext jaxbContextCC = JAXBContext.newInstance(BalancePortalDto.class);
            Unmarshaller unmarshallerCC = jaxbContextCC.createUnmarshaller();
            StringReader stringReaderCC = new StringReader(apiResponse);
            BalancePortalDto balancePortalDto = (BalancePortalDto) unmarshallerCC.unmarshal(stringReaderCC);

            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);

            String sender = fspByCode.get().getFspShortName();

            LoanTopUpPayOffBalanceApiRespDetailDto loanTopUpPayOffBalanceApiRespDetailDto = new LoanTopUpPayOffBalanceApiRespDetailDto();

            loanTopUpPayOffBalanceApiRespDetailDto.setLoanNumber(balancePortalDto.getLoanNumber());
            loanTopUpPayOffBalanceApiRespDetailDto.setFSPReferenceNumber(balancePortalDto.getFSPReferenceNumber());
            loanTopUpPayOffBalanceApiRespDetailDto.setPaymentReferenceNumber(balancePortalDto.getPaymentReferenceNumber());
            loanTopUpPayOffBalanceApiRespDetailDto.setTotalPayoffAmount(balancePortalDto.getTotalPayoffAmount());
            loanTopUpPayOffBalanceApiRespDetailDto.setOutstandingBalance(balancePortalDto.getOutstandingBalance());
            loanTopUpPayOffBalanceApiRespDetailDto.setFinalPaymentDate(balancePortalDto.getFinalPaymentDate());
            loanTopUpPayOffBalanceApiRespDetailDto.setLastDeductionDate(balancePortalDto.getLastDeductionDate());
            loanTopUpPayOffBalanceApiRespDetailDto.setLastPayDate(balancePortalDto.getLastPayDate());
            loanTopUpPayOffBalanceApiRespDetailDto.setEndDate(balancePortalDto.getEndDate());

            ApiReqHeaderDto apiReqHeaderDtoCC = new ApiReqHeaderDto();
            apiReqHeaderDtoCC.setFspCode(fspByCode.get().getFspCode());
            apiReqHeaderDtoCC.setMsgId(mssId);
            apiReqHeaderDtoCC.setMessageType(Constants.LOAN_TOP_UP_BALANCE_RESPONSE);
            apiReqHeaderDtoCC.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDtoCC.setSender(fspByCode.get().getFspName());


            LoanTopUpBalanceApiRespDataDto loanTopUpBalanceApiRespDataDto = new LoanTopUpBalanceApiRespDataDto();
            loanTopUpBalanceApiRespDataDto.setRequestHeader(apiReqHeaderDtoCC);
            loanTopUpBalanceApiRespDataDto.setLoanTopUpBalanceApiRespDetailDto(loanTopUpPayOffBalanceApiRespDetailDto);
            logger.info("balance json payload:" + loanTopUpPayOffBalanceApiRespDetailDto);

            JAXBContext jaxbContext2CC = JAXBContext.newInstance(LoanTopUpBalanceApiRespDataDto.class);
            Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
            marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterCC = new StringWriter();
            marshallerCC.marshal(loanTopUpBalanceApiRespDataDto, stringWriterCC);
            String xmlPayloadCC = stringWriterCC.toString();
            System.out.println("balance xml payload: " + xmlPayloadCC);


            logger.info("balance catalog i.e with fsp code:" + xmlPayloadCC);

            JAXBContext jaxbContextSg = JAXBContext.newInstance(GenericApiReqDataDto.class);
            Unmarshaller unmarshallerSg = jaxbContextSg.createUnmarshaller();
            StringReader stringReaderSg = new StringReader(xmlPayloadCC);
            GenericApiReqDataDto genericApiReqDataDtoSg = (GenericApiReqDataDto) unmarshallerSg.unmarshal(stringReaderSg);

            // Extract and convert the content to be signed
            String content = convertObjectToXml(genericApiReqDataDtoSg);
            System.out.println("Content to be signed: " + content);

            String signatureSg = digitalSignature.createSignature(content);
            System.out.println("Generated Signature: " + signatureSg);

            // Create the final XML with the signature
            String finalXml = appendSignatureToXml2(content, signatureSg);
            System.out.println("Final XML: " + finalXml);

            logger.info("Final Xml Request {}:", finalXml);

            logger.info("Final CURL SUPPOSED request sent to utumishi***** {}:", finalXml);
//        return ResponseEntity.ok(finalXml);

            logger.info("loan top up balance successfull " + finalXml);

            return ResponseEntity.ok(finalXml);

        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing loan top up pay off balance request {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> loanTakeOver(String message) throws Exception {
        logger.info("Received loan take over request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanTakeOverOfferApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanTakeOverOfferApiReqDto loanTakeOverApiReqDto = (LoanTakeOverOfferApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getRequestHeader().getMsgId();

        try {

//            String fspCode = loanTakeOverApiReqDto.getLoanTakeOverApiReqDataDto().getRequestHeader().getFspCode();

//             Save loan takeover
            Loan loan = new Loan();

            //loan
            Loan loanId = loanRepository.findByLoanNumberAndFspCode(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOneLoanNumber(), loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOneCode());
            BigDecimal requestedAmount = loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getRequestedAmount();
            BigDecimal totalDeductibleAmount = loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getDeductibleAmount();
            BigDecimal totalAmountToPay;
            totalAmountToPay = requestedAmount.add(totalDeductibleAmount);
            loan.setLoanNumber(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOneLoanNumber());
            loan.setFspCode(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOneCode());
            loan.setProductCode(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getProductCode());
            loan.setLoanDate(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOneEndDate());
            loan.setBankAccountNumber(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOneBankAccount());
            loan.setBankAccountName(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOneBankAccountName());
            loan.setLoanAmount(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getRequestedAmount());
            loan.setLoanPurpose(loanId.getLoanPurpose());
            loan.setLoanDate(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOneEndDate());
            loan.setLoanType(loanId.getLoanType());
            loan.setMinTenure(loanId.getMinTenure());
            loan.setMaxTenure(loanId.getMaxTenure());
            //loan.setInterest(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getInterestRate());
            loan.setProcessingFee(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getProcessingFee());
            loan.setFspReferenceNumber(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOnePaymentReferenceNumber());
            loan.setTenure(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getTenure());
            loan.setDeductionCode(loanId.getDeductionCode());
            loan.setDeductionName(loanId.getDeductionName());
            loan.setDeductibleAmount(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getDeductibleAmount());
            loan.setDesiredDeductibleAmount(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getDesiredDeductibleAmount());
            loan.setRequestedAmount(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getRequestedAmount());
            loan.setPaymentReferenceNumber(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getFspOnePaymentReferenceNumber());
            loan.setTotalAmountToPay(totalAmountToPay);
            loan.setSettlementAmount(loanId.getSettlementAmount());
            loan.setApplicationNumber(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getApplicationNumber());
            loan.setInsurance(loanTakeOverApiReqDto.getLoanTakeOverOfferApiReqDataDto().getLoanTakeOverOfferApiReqDetailDto().getInsurance());
            //loan.setFsp2Code();
            loan.setStatus(loanId.getStatus());
            loan.setProduct(loanId.getProduct());
            loan.setBorrower(loanId.getBorrower());

            loanRepository.save(loan);

            logger.info("loan take over sucessfull i.e loan with Loan Number:" + loan.getLoanNumber());
            return ResponseEntity.ok("success takeover");

        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing loan take over {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> loanRegistration(String message) throws Exception {

        logger.info("Received loan application request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanRegistrationApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        System.out.println("Phase 0 ***************");
        LoanRegistrationApiReqDto loanRegistrationApiReqDto = (LoanRegistrationApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanRegistrationApiReqDto.getLoanRegistrationData().getRequestHeader().getFspCode();
        String mssId = loanRegistrationApiReqDto.getLoanRegistrationData().getRequestHeader().getMsgId();
//        String messageType = loanRegistrationApiReqDto.getLoanRegistrationData().getRequestHeader().getMessageType();
        try {
            System.out.println("Phase 1 ***************");
            String firstName= loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getFirstName();
            String middleName= loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getMiddleName();
            String lastName = loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getLastName();
            String fullName = String.format("%s %s %s", firstName, middleName, lastName).trim().replaceAll(" +", " ");
            String fullNameOption = String.format("%s %s %s", middleName, firstName,lastName).trim().replaceAll(" +", " ");
            String loanApplicationNumber = loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getApplicationNumber();

            String accountNo = loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getBankAccountNumber();
            String ninId=loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getNIN();

            System.out.println("Phase 1.1.1 ***************");
           //save logs request


            // Check if the product exists
            Optional<Product> product = productRepository.findByProductCode(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getProductCode());
            if (product.isEmpty()) {
                logger.info("invalid product code i.e productCode: " + loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getProductCode());
                return createResponseEntity(ResponseCodes.INVALID_PRODUCT_CODE, fspCode, mssId);
            }
            System.out.println("Phase 2 ***************");


            System.out.println("Phase 3 ***************");

            Optional<Loan> loanByApplicationNumber = loanRepository.findFirstByApplicationNumber(loanApplicationNumber);

            if (loanByApplicationNumber.isPresent()) {
                logger.info("Duplicate application number: " + loanApplicationNumber);
                return createResponseEntity(ResponseCodes.DUPLICATE_REQUEST, fspCode, mssId);
            }

            // Remove spaces from fullName
            String cleanedFullName = stringUtil.removeSpaces(fullName);
            System.out.println("Contatenate fullname from request*************" + cleanedFullName);


            // convert l application request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(LoanRegistrationApiReqDataDto.class);
            Marshaller marshallerLRq = jaxbContextReq.createMarshaller();
            marshallerLRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterLRq = new StringWriter();
            marshallerLRq.marshal(loanRegistrationApiReqDto.getLoanRegistrationData(), stringWriterLRq);
            String loanUtumishiRq = stringWriterLRq.toString();

            logger.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getLoanRoutingKey(),
                    loanUtumishiRq);

            logger.info("request sent to the queue: " + loanUtumishiRq);

            //save loan logs
            LoanLogs loanLogs = new LoanLogs();
            loanLogs.setCheckNumber(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getCheckNumber());
            loanLogs.setFirstName(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getFirstName());
            loanLogs.setMiddleName(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getMiddleName());
            loanLogs.setLastName(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getLastName());
            loanLogs.setSex(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getSex());
            loanLogs.setBankAccountNumber(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getBankAccountNumber());
            loanLogs.setEmploymentDate(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getEmploymentDate());
            loanLogs.setMaritalStatus(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getMaritalStatus());
            loanLogs.setConfirmationDate(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getConfirmationDate());
            loanLogs.setTotalEmployeeDeduction(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getTotalEmployeeDeduction());
            loanLogs.setNearestBranchCode(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getNearestBranchCode());
            loanLogs.setNearestBranchName(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getNearestBranchName());
            loanLogs.setVoteCode(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getVoteCode());
            loanLogs.setVoteName(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getVoteName());
            loanLogs.setNin(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getNIN());
            loanLogs.setDesignationCode(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getDesignationCode());
            loanLogs.setDesignationName(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getDesignationName());
            loanLogs.setBasicSalary(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getBasicSalary());
            loanLogs.setNetSalary(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getNetSalary());
            loanLogs.setOneThirdAmount(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getOneThirdAmount());
            loanLogs.setRequestedAmount(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getRequestedAmount());
            loanLogs.setDesiredDeductibleAmount(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getDesiredDeductibleAmount());
            loanLogs.setRetirementDate(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getRetirementDate());
            loanLogs.setTermsOfEmployment(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getTermsOfEmployment());
            loanLogs.setTenure(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getTenure());
            loanLogs.setProductCode(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getProductCode());
            loanLogs.setTermsOfEmployment(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getTermsOfEmployment());
            loanLogs.setInterestRate(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getInterestRate());
            loanLogs.setProcessingFee(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getProcessingFee());
            loanLogs.setInsurance(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getInsurance());
            loanLogs.setPhysicalAddress(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getPhysicalAddress());
            loanLogs.setEmailAddress(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getEmailAddress());
            loanLogs.setMobileNumber(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getMobileNumber());
            loanLogs.setApplicationNumber(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getApplicationNumber());
            loanLogs.setLoanPurpose(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getLoanPurpose());
            loanLogs.setContractStartDate(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getContractStartDate());
            loanLogs.setContractEndDate(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getContractEndDate());
            loanLogs.setSwiftCode(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getSwiftCode());
            loanLogs.setFunding(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getFunding());
//            loanLogs.setFlag(0);
            loanLogs.setLoanTypeStatus(loanRegistrationApiReqDto.getLoanRegistrationData().getRequestHeader().getMessageType());

            loanLogs.setLoanStatus("APPLICATION");
            loanLogRepository.save(loanLogs);

            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);


        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing loan registration {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> generateRequestSignature(@RequestBody String message) throws Exception {
        // Unmarshal the incoming XML message
        JAXBContext jaxbContext = JAXBContext.newInstance(GenericApiReqDataDtoWrapper.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        GenericApiReqDataDtoWrapper genericApiReqDataDtoWrapper = (GenericApiReqDataDtoWrapper) unmarshaller.unmarshal(stringReader);

        // Extract and convert the content to be signed
        String content = convertObjectToXml(genericApiReqDataDtoWrapper.getRequestData());
        System.out.println("Content to be signed: " + content);

        String signature = digitalSignature.createSignature(content);
        System.out.println("Generated Signature: " + signature);

        // Create the final XML with the signature
        String finalXml = appendSignatureToXml(message, signature);
        System.out.println("Final XML: " + finalXml);

        logger.info("Final Xml Request {}:", finalXml);

        // Send the final XML to the specified API endpoint
        String response = sendXmlToApi(finalXml);
        logger.info("Final CURL xml request sent to utumishi {}:", response);
        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<String> generateRequestSignature2(@RequestBody String message) throws Exception {
        // Unmarshal the incoming XML message
        JAXBContext jaxbContext = JAXBContext.newInstance(GenericApiReqDataDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        GenericApiReqDataDto genericApiReqDataDto = (GenericApiReqDataDto) unmarshaller.unmarshal(stringReader);

        // Extract and convert the content to be signed
        String content = convertObjectToXml(genericApiReqDataDto);
        System.out.println("Content to be signed: " + content);

        String signature = digitalSignature.createSignature(content);
        System.out.println("Generated Signature: " + signature);
        // Create the final XML with the signature
        String finalXml = appendSignatureToXml2(content, signature);
        System.out.println("Final XML: " + finalXml);

        logger.info("Final Xml Request {}:", finalXml);

        // Send the final XML to the specified API endpoint
        String response = sendXmlToApi(finalXml);
        logger.info("Final CURL xml request sent to utumishi {}:", response);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<String> generateRequestSignatureCheck(@RequestBody String message) {
        try {
            // Unmarshal the incoming XML message
            JAXBContext jaxbContext = JAXBContext.newInstance(GenericApiReqDataDto.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader stringReader = new StringReader(message);
            GenericApiReqDataDto genericApiReqDataDto = (GenericApiReqDataDto) unmarshaller.unmarshal(stringReader);

            // Extract and convert the content to be signed
            String content = convertObjectToXml(genericApiReqDataDto);
            logger.info("Content to be signed: " + content);

            // Generate the digital signature
            String signature = digitalSignature.createSignature(content);
            logger.info("Generated Signature: " + signature);

            // Create the final XML with the signature
            String finalXml = appendSignatureToXml2(content, signature);
            System.out.println("Final XML: " + finalXml);

            // Log the final XML
            logger.info("Final Xml Request {}:", finalXml);

            // Send the final XML to the specified API endpoint
            String response = sendXmlToApi(finalXml);
            logger.info("Final CURL xml request sent to utumishi {}:", response);

            // Return the API response
            return ResponseEntity.ok(response);

        } catch (JAXBException e) {
            logger.error("Error unmarshalling XML message: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid XML format");
        } catch (SignatureException e) {
            logger.error("Error generating digital signature: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating signature");
        } catch (IOException e) {
            logger.error("Error sending XML to API: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error communicating with API");
        } catch (Exception e) {
            logger.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }



    @Override
    public ResponseEntity<String> generateRequestSignature3(@RequestBody String message) throws Exception {
        // Unmarshal the incoming XML message
        JAXBContext jaxbContext = JAXBContext.newInstance(GenericApiReqDataDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        GenericApiReqDataDto genericApiReqDataDto = (GenericApiReqDataDto) unmarshaller.unmarshal(stringReader);

        // Extract and convert the content to be signed
        String content = convertObjectToXml(genericApiReqDataDto);
        System.out.println("Content to be signed: " + content);

        String signature = digitalSignature.createSignature(content);
        System.out.println("Generated Signature: " + signature);

        // Create the final XML with the signature
        String finalXml = appendSignatureToXml2(content, signature);
        System.out.println("Final XML: " + finalXml);

        logger.info("Final Xml Request {}:", finalXml);

        // Send the final XML to the specified API endpoint

        logger.info("Final CURL SUPPOSED request sent to utumishi***** {}:", finalXml);
        return ResponseEntity.ok(finalXml);

    }


    @Override
    public ResponseEntity<String> loanFinalApprovalNotification(String message) throws Exception {

        logger.info("Received loan Final Approval Notification request: {}", message);

        JAXBContext jaxbContext = JAXBContext.newInstance(LoanUtumishiFinalResponseApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanUtumishiFinalResponseApiDto loanUtumishiFinalResponseApiDto = (LoanUtumishiFinalResponseApiDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanUtumishiFinalResponseApiDto.getLoanUtumishiFinalResponseApiDataDto().getRequestHeader().getFspCode();
        String mssId = loanUtumishiFinalResponseApiDto.getLoanUtumishiFinalResponseApiDataDto().getRequestHeader().getMsgId();

        try {
            //Access values
            String loanNumber = loanUtumishiFinalResponseApiDto.getLoanUtumishiFinalResponseApiDataDto().getLoanUtumishiFinalResponseApiDetailDto().getLoanNumber();

            //loan pay off operation
            Optional<FinancialServiceProvider> fsp =
                    financialServiceProviderRepository.findByFspCode(fspCode);

            if (fsp.isEmpty()) {
                logger.info("invalid fsp code, reason: no such fsp with code: " + fspCode);
                return createResponseEntity(ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER,  fspCode, mssId);
            }

            // convert l application request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(LoanUtumishiFinalResponseApiDataDto.class);
            Marshaller marshallerLRq = jaxbContextReq.createMarshaller();
            marshallerLRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterLRq = new StringWriter();
            marshallerLRq.marshal(loanUtumishiFinalResponseApiDto.getLoanUtumishiFinalResponseApiDataDto(), stringWriterLRq);
            String loanUtumishiRq = stringWriterLRq.toString();

            logger.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getFinalNotifRoutingKey(),
                    loanUtumishiRq);

            logger.info("request sent to the queue: " + loanUtumishiRq);

            logger.info("loan final approve response is successfull i.e Loan Number:" + loanNumber);
            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        }catch (Exception e){
            logger.info(e.getMessage());
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR,fspCode, mssId);

        }
    }

    @Override
    public List<Loan> findByFspCode(String fspByCode) {
        return findByFspCode(fspByCode);
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

        // Remove all unnecessary whitespace between tags
        xml = xml.replaceAll(">\\s+<", "><");

        return xml;
    }
    private String appendSignatureToXml(String originalXml, String signature) {
        int index = originalXml.indexOf("</Data>");
        if (index != -1) {
            return originalXml.substring(0, index + 7) + "<Signature>" + signature + "</Signature>" + originalXml.substring(index + 7);
        }
        return originalXml;
    }
    private String appendSignatureToXml2(String originalXml, String signature) {
        int index = originalXml.indexOf("</Data>");
        if (index != -1) {
            return "<Document>" + originalXml + "<Signature>" + signature + "</Signature></Document>";
        }
        return "<Document>" + originalXml + "</Document>";
    }


    private String balanceXmlSendToApi(String xml) throws Exception {
        // Log the final XML request
        logger.info("Final CURL XML request sent to Utumishi: {}", xml);

        URL url = new URL(balanceEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.getOutputStream().write(xml.getBytes(StandardCharsets.UTF_8));

        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        }

        String responseMessage = response.toString();
        logger.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

//for takeover balance
    private String balanceTakeOverXmlSendToApi(String xml) throws Exception {
        // Log the final XML request
        logger.info("Final CURL XML request sent to Utumishi: {}", xml);

        URL url = new URL(balanceTakeOverEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.getOutputStream().write(xml.getBytes(StandardCharsets.UTF_8));

        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        }

        String responseMessage = response.toString();
        logger.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    //for loan charge api to portal
    private String cahrgeXmlSendToApi(String xml) throws Exception {
        // Log the final XML request
        logger.info("Final CURL XML request sent to Utumishi: {}", xml);

        URL url = new URL(chargesEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.getOutputStream().write(xml.getBytes(StandardCharsets.UTF_8));

        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        }

        String responseMessage = response.toString();
        logger.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    private String sendXmlToApi(String xml) throws Exception {
        // Log the final XML request
        logger.info("Final CURL XML request sent to Utumishi: {}", xml);

        URL url = new URL(utumishiApiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.getOutputStream().write(xml.getBytes(StandardCharsets.UTF_8));

        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        }

        String responseMessage = response.toString();
        logger.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    private static LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

@Override
public ResponseEntity<String> consumeCalculatedLoanCharges(String message) throws Exception {
        log.info("consumed calculated charges {}", message);
    JAXBContext jaxbContext = JAXBContext.newInstance(ChargePortalDto.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    StringReader stringReader = new StringReader(message);
    ChargePortalDto chargePortalDto = (ChargePortalDto) unmarshaller.unmarshal(stringReader);
    String mssId = chargePortalDto.getMsgId();
    List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
    FinancialServiceProvider fsp = null;
    if (!fspList.isEmpty()) {
        fsp = fspList.get(0);
    }
    try {

        System.out.println("phase 0 *************: ");

        LoanChargesApiRespDetailDto loanChargesApiRespDetailDto = new LoanChargesApiRespDetailDto();
        loanChargesApiRespDetailDto.setDesiredDeductibleAmount(chargePortalDto.getDesiredDeductibleAmount());
        loanChargesApiRespDetailDto.setTotalInsurance(chargePortalDto.getTotalInsurance());
        loanChargesApiRespDetailDto.setTotalProcessingFee(chargePortalDto.getTotalProcessingFee());
        loanChargesApiRespDetailDto.setTotalInterestRateAmount(chargePortalDto.getTotalInterestRateAmount());
        loanChargesApiRespDetailDto.setOtherCharges(chargePortalDto.getOtherCharges());
        loanChargesApiRespDetailDto.setNetLoanAmount(chargePortalDto.getNetLoanAmount());
        loanChargesApiRespDetailDto.setTotalAmountToPay(chargePortalDto.getTotalAmountToPay());
        loanChargesApiRespDetailDto.setTenure(chargePortalDto.getTenure());
        loanChargesApiRespDetailDto.setEligibleAmount(chargePortalDto.getEligibleAmount());
        loanChargesApiRespDetailDto.setMonthlyReturnAmount(chargePortalDto.getMonthlyReturnAmount());

        System.out.println("phase 1 *************: " );

        ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
        apiReqHeaderDto.setFspCode(fsp.getFspCode());
        apiReqHeaderDto.setMsgId(mssId);
        apiReqHeaderDto.setMessageType(Constants.LOAN_CHARGES_RESPONSE);
        apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
        apiReqHeaderDto.setSender(fsp.getFspName());

        LoanChargesApirRespDataDto chargesApirRespDataDto = new LoanChargesApirRespDataDto();
        chargesApirRespDataDto.setRequestHeader(apiReqHeaderDto);
        chargesApirRespDataDto.setLoanChargesApiRespMessageDetail(loanChargesApiRespDetailDto);

        System.out.println("charge json payload: " + loanChargesApiRespDetailDto);

        JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanChargesApirRespDataDto.class);
        Marshaller marshaller = jaxbContext2.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(chargesApirRespDataDto, stringWriter);
        String xmlPayload = stringWriter.toString();
        System.out.println("charge xml payload: " + xmlPayload);

        System.out.println("charge xml payload Data level to be sent to utumishi" + xmlPayload);

        logger.info("charges catalog sent successfull i.e with fsp code:" + apiReqHeaderDto.getFspCode());

        return generateRequestSignature3(xmlPayload);

    } catch (Exception e) {
        logger.info("fail to send charges to utumishi ");

        return null;
    }

}

    @Override
    public ResponseEntity<String> loanInitialApproveNotification(String message) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanInitialApprovePortalDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanInitialApprovePortalDto loanInitialApprovePortalDto = (LoanInitialApprovePortalDto) unmarshaller.unmarshal(stringReader);
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }

        try {

            logger.info("loan initial approve reach here: ********");

            LoanApprovalApiDetailDto loanApprovalApiDetailDto = new LoanApprovalApiDetailDto();
            loanApprovalApiDetailDto.setApplicationNumber(loanInitialApprovePortalDto.getApplicationNumber());
            loanApprovalApiDetailDto.setReason(loanInitialApprovePortalDto.getReason());
            loanApprovalApiDetailDto.setFspReferenceNumber(fsp.getFspReferenceNumber());
            loanApprovalApiDetailDto.setLoanNumber(loanInitialApprovePortalDto.getLoanrequestid());
            String totaltopay = loanInitialApprovePortalDto.getTotalAmountToPay();
            if (totaltopay!=null) {
                // Remove comma
                String cleantotaltopay = totaltopay.replace(",", "");
                loanApprovalApiDetailDto.setTotalAmountToPay( new BigDecimal(cleantotaltopay).setScale(2, RoundingMode.HALF_UP));
            }

//            loanApprovalApiDetailDto.setTotalAmountToPay(new BigDecimal(totaltopay));

            String otherCharges = loanInitialApprovePortalDto.getOtherCharges();
            if (otherCharges!=null) {
                // Remove comma
                String cleanOtherCharges = otherCharges.replace(",", "");
                loanApprovalApiDetailDto.setOtherCharges( new BigDecimal(cleanOtherCharges).setScale(2, RoundingMode.HALF_UP));
            }

            logger.info("loan initial approve reach here:" + loanApprovalApiDetailDto);
//            loanApprovalApiDetailDto.setOtherCharges(new BigDecimal(loanInitialApprovePortalDto.getOtherCharges()));

            if(loanInitialApprovePortalDto.getAction().equals("REJECT")){
                loanApprovalApiDetailDto.setApproval("REJECTED");
            }
            if(loanInitialApprovePortalDto.getAction().equals("ACCEPT")){
                loanApprovalApiDetailDto.setApproval("APPROVED");
            }
            if(loanInitialApprovePortalDto.getAction().equals("CLOSE")){

                return loanLiquidizationNotification(message);
            }


            logger.info("loan initial approve json in detail level payload: " + loanApprovalApiDetailDto);

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_INITIAL_APPROVAL_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());


            LoanApprovalApiDataDto loanApprovalApiDataDto = new LoanApprovalApiDataDto();
            loanApprovalApiDataDto.setRequestHeader(apiReqHeaderDto);
            loanApprovalApiDataDto.setMessageDetails(loanApprovalApiDetailDto);

            logger.info("loan initial approve json in data level payload: " + loanApprovalApiDataDto);


            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanApprovalApiDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanApprovalApiDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();
            System.out.println("charge xml payload: " + xmlPayload);

            logger.info("charge xml payload: " + xmlPayload);

//            generateRequestSignature2(xmlPayload);

            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            logger.info("loan initial notification response" + responseEntity);

            //save logs into database i.e tab_loan_logs

            LoanLogs loanLogs = loanLogRepository.findByApplicationNumber(loanInitialApprovePortalDto.getApplicationNumber());
            if(loanLogs != null){
                loanLogs.setLoanStatus("Bayport Approved");
                loanLogs.setOtherCharges(new BigDecimal(loanInitialApprovePortalDto.getOtherCharges()));
                loanLogs.setTotalAmountToPay(new BigDecimal(totaltopay));
                loanLogs.setInitialApproveReason(loanInitialApprovePortalDto.getReason());

                String netloan = loanInitialApprovePortalDto.getNetLoanAmount();
                if (netloan!=null) {
                    // Remove comma
                    String cleanNetloan = netloan.replace(",", "");
                    loanLogs.setNetLoanAmount( new BigDecimal(cleanNetloan).setScale(2, RoundingMode.HALF_UP));
                }

                String eligibleAmount = loanInitialApprovePortalDto.getEligibleAmount();
                if (eligibleAmount!=null) {
                    // Remove comma
                    String cleaneligibleAmount = eligibleAmount.replace(",", "");
                    loanLogs.setEligibleAmount( new BigDecimal(cleaneligibleAmount).setScale(2, RoundingMode.HALF_UP));
                }

                String monthlyreturnamount = loanInitialApprovePortalDto.getMonthlyReturnAmount();
                if (monthlyreturnamount!=null) {
                    // Remove comma
                    String cleanmonthlyreturnamount = monthlyreturnamount.replace(",", "");
                    loanLogs.setMonthlyReturnAmount( new BigDecimal(cleanmonthlyreturnamount).setScale(2, RoundingMode.HALF_UP));
                }

                loanLogRepository.save(loanLogs);
            }

            logger.info("prepared utumishi response message sent to the queue:");

            //Create a payload for request and response
            //set request
            EssRequestLoanInitialApprovePortalDto essRequestLoanInitialApprovePortalDto = new EssRequestLoanInitialApprovePortalDto();
            essRequestLoanInitialApprovePortalDto.setApplicationNumber(loanInitialApprovePortalDto.getApplicationNumber());
            essRequestLoanInitialApprovePortalDto.setReason(loanInitialApprovePortalDto.getApplicationNumber());
            essRequestLoanInitialApprovePortalDto.setFspReferenceNumber(loanInitialApprovePortalDto.getApplicationNumber());
            essRequestLoanInitialApprovePortalDto.setLoanNumber(loanInitialApprovePortalDto.getLoanrequestid());
            essRequestLoanInitialApprovePortalDto.setTotalAmountToPay(loanInitialApprovePortalDto.getTotalAmountToPay());
            essRequestLoanInitialApprovePortalDto.setOtherCharges(loanInitialApprovePortalDto.getOtherCharges());
            essRequestLoanInitialApprovePortalDto.setApproval(loanInitialApprovePortalDto.getAction());


            //set response
            EssResponseLoanInitialApprovePortalDto essResponseLoanInitialApprovePortalDto = new EssResponseLoanInitialApprovePortalDto();
            essResponseLoanInitialApprovePortalDto.setResponseDescription(String.valueOf(responseEntity));

            //combine request and response
            EssNotificationApiRespDataDto essNotificationApiRespDataDto= new EssNotificationApiRespDataDto();
            essNotificationApiRespDataDto.setMessageRequest(essRequestLoanInitialApprovePortalDto);
            essNotificationApiRespDataDto.setMessageResponse(essResponseLoanInitialApprovePortalDto);



            JAXBContext jaxbContext6 = JAXBContext.newInstance(EssNotificationApiRespDataDto.class);
            Marshaller marshaller6 = jaxbContext6.createMarshaller();
            marshaller6.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter6 = new StringWriter();
            marshaller6.marshal(essNotificationApiRespDataDto, stringWriter6);
            String xmlPayload6 = stringWriter6.toString();
            System.out.println("loan initial approval xml payload: " + xmlPayload6);
            logger.info("Combined initial loan approval and ess response" + xmlPayload6);
            String cleanedXml6 = cleanMessage(xmlPayload6);

            System.out.println("Cleaned loan initial approval xml payload: " + cleanedXml6);
            logger.info("Cleaned Combined initial loan approval and ess response" + cleanedXml6);

            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getApprovalAndDisbursRoutingKey(),
                    String.valueOf(cleanedXml6));

            logger.info("utumishi response message sent to the queue:{}", responseEntity);

            return responseEntity;

        } catch (Exception e) {
            logger.info("fail to send loan initial notification to utumishi");
            return null;
        }



    }

    public static String cleanMessage(String xmlInput) {
        // Decode HTML entities (e.g., &lt; to <, &gt; to >)
        String decodedXml = StringEscapeUtils.unescapeHtml4(xmlInput);

        // Remove unwanted characters (e.g., &#13;)
        String partiallyCleanedXml = decodedXml.replaceAll("&#13;", "");

        // Remove tags containing unwanted data (e.g., < and > if they appear as entities)
        String fullyCleanedXml = partiallyCleanedXml.replaceAll("&lt;", "")
                .replaceAll("&gt;", "")
                .replaceAll("<requestDescription>", "")
                .replaceAll("</requestDescription>", "")
                .replaceAll("<loanresponse>", "")
                .replaceAll("</loanresponse>", "")
                .replaceAll("<Document>", "")
                .replaceAll("</Document>", "")
                .replaceAll("<Data>", "")
                .replaceAll("</Data>", "")
                .replaceAll("<!\\[CDATA\\[", "")
                .replaceAll("<200 OK OK,", "")
                .replaceAll("]]>", "")
                .replaceAll("<responseDescription>", "")
                .replaceAll("</responseDescription>", "")
                .replaceAll(",\\[\\]", "")
                .replaceAll("<Signature>.*?</Signature>", "");

        return fullyCleanedXml.trim();
    }


    public ResponseEntity<String> loanLiquidizationNotification(String message) throws Exception {
        logger.info("loan liquidization notification" + message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanInitialApprovePortalDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanInitialApprovePortalDto liquidizationNotificationDto = (LoanInitialApprovePortalDto) unmarshaller.unmarshal(stringReader);
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }
        try {
            LoanLiquidizationApiReqDetailDto loanLiquidizationApiReqDetailDto = new LoanLiquidizationApiReqDetailDto();
            loanLiquidizationApiReqDetailDto.setApplicationNumber(liquidizationNotificationDto.getApplicationNumber());
            loanLiquidizationApiReqDetailDto.setLoanNumber(liquidizationNotificationDto.getLoanrequestid());
            loanLiquidizationApiReqDetailDto.setRemarks(liquidizationNotificationDto.getReason());


            logger.info("loanliquidization json in detail level payload: " + loanLiquidizationApiReqDetailDto);


            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_LIQUIDATION_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());


            LoanLiquidizationApiReqDataDto loanLiquidizationApiReqDataDto = new LoanLiquidizationApiReqDataDto();
            loanLiquidizationApiReqDataDto.setRequestHeader(apiReqHeaderDto);
            loanLiquidizationApiReqDataDto.setLoanLiquidizationApiReqDetailDto(loanLiquidizationApiReqDetailDto);

            logger.info("loan liquidization notification json in data level payload: " + loanLiquidizationApiReqDataDto);


            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanLiquidizationApiReqDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanLiquidizationApiReqDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();
            System.out.println("liquidization xml payload: " + xmlPayload);

            logger.info("liquidization xml payload: " + xmlPayload);

            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            logger.info("liquidization sent to be signed" + xmlPayload);
            logger.info("liquidization response from ess" + responseEntity);
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getApprovalAndDisbursRoutingKey(),
                    String.valueOf(responseEntity));

            return responseEntity;

        } catch (Exception e) {
            logger.info("fail to send liquidization to utumishi ");
            return null;
        }

    }


    /*
     *Generate msg Id
     **/
    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }



    //                       TAKEOVER FEATURES
    // TAKEOVER BALANCE REQUEST AND RESPONSE
    @Override
    public ResponseEntity<String> loanTakeOverBalance(String message) throws Exception {
        logger.info("Received Take_Over balance request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(TakeOverBalanceApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        TakeOverBalanceApiReqDto takeOverBalanceApiReqDto = (TakeOverBalanceApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = takeOverBalanceApiReqDto.getTakeOverBalanceApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = takeOverBalanceApiReqDto.getTakeOverBalanceApiReqDataDto().getRequestHeader().getMsgId();
        String loanNo = takeOverBalanceApiReqDto.getTakeOverBalanceApiReqDataDto().getTakeOverBalanceApiReqDetailDto().getLoanNumber();
        try {
            //convert balance utumishi request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(TakeOverBalanceApiReqDataDto.class);
            Marshaller marshallerRq = jaxbContextReq.createMarshaller();
            marshallerRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterRq = new StringWriter();
            marshallerRq.marshal(takeOverBalanceApiReqDto.getTakeOverBalanceApiReqDataDto(), stringWriterRq);
            String balanceUtumishiRq = stringWriterRq.toString();
            System.out.println("take over balance request sent to api: " + balanceUtumishiRq);

            logger.info("takeover balance response is successfull i.e Product Code: {}" + balanceUtumishiRq);

            // send and receive balance messages thr: Api (share with top up balance messages)
            String apiResponse = balanceTakeOverXmlSendToApi(balanceUtumishiRq);
            logger.info("takeover balance response Message from Portal Api: {}", apiResponse);

            JAXBContext jaxbContextCC = JAXBContext.newInstance(BalancePortalDto.class);
            Unmarshaller unmarshallerCC = jaxbContextCC.createUnmarshaller();
            StringReader stringReaderCC = new StringReader(apiResponse);
            BalancePortalDto balancePortalDto = (BalancePortalDto) unmarshallerCC.unmarshal(stringReaderCC);

            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);

            String sender = fspByCode.get().getFspShortName();

            TakeOverBalanceApiRespDetailDto takeOverBalanceApiRespDetailDto = new TakeOverBalanceApiRespDetailDto();

            takeOverBalanceApiRespDetailDto.setLoanNumber(balancePortalDto.getLoanNumber());
            takeOverBalanceApiRespDetailDto.setFspCode(fspCode);
            takeOverBalanceApiRespDetailDto.setFSPReferenceNumber(balancePortalDto.getFSPReferenceNumber());
            takeOverBalanceApiRespDetailDto.setPaymentReferenceNumber(balancePortalDto.getPaymentReferenceNumber());

            BigDecimal TotalPayOffAmount = balancePortalDto.getTotalPayoffAmount().setScale(2, RoundingMode.HALF_UP);
            takeOverBalanceApiRespDetailDto.setTotalPayoffAmount(TotalPayOffAmount);

            BigDecimal OutstandingBalance = balancePortalDto.getOutstandingBalance().setScale(2, RoundingMode.HALF_UP);
            takeOverBalanceApiRespDetailDto.setOutstandingBalance(OutstandingBalance);
            takeOverBalanceApiRespDetailDto.setFspBankAccount(Constants.FSP_BANK_ACCOUNT);
            takeOverBalanceApiRespDetailDto.setFspBankAccountName(Constants.FSP_BANK_ACCOUNT_NAME);
            takeOverBalanceApiRespDetailDto.setSwiftCode(Constants.SWIFT_CODE);
            takeOverBalanceApiRespDetailDto.setMnoChannels(balancePortalDto.getMnoChannels());

            //calculate payment date
            LocalDate calculatedFinalPaymentDate = calculateFinalPaymentDate();
            // Convert LocalDate to Date
            Date finalPaymentDate = Date.from(calculatedFinalPaymentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Convert Date to LocalDateTime
            LocalDateTime localPaymentDateTime = finalPaymentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            // Format LocalDateTime to "yyyy-MM-dd'T'HH:mm:ss"
            String formattedFinalPaymentDate = localPaymentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));


            takeOverBalanceApiRespDetailDto.setFinalPaymentDate(formattedFinalPaymentDate);
            takeOverBalanceApiRespDetailDto.setLastDeductionDate(balancePortalDto.getLastDeductionDate());
            takeOverBalanceApiRespDetailDto.setDeductionEndDate(balancePortalDto.getDeductionEndDate());


            ApiReqHeaderDto apiReqHeaderDtoCC = new ApiReqHeaderDto();
            apiReqHeaderDtoCC.setFspCode(fspByCode.get().getFspCode());
            apiReqHeaderDtoCC.setMsgId(mssId);
            apiReqHeaderDtoCC.setMessageType(Constants.LOAN_TAKEOVER_BALANCE_RESPONSE);
            apiReqHeaderDtoCC.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDtoCC.setSender(fspByCode.get().getFspName());


            TakeOverBalanceApiRespDataDto takeOverBalanceApiRespDataDto = new TakeOverBalanceApiRespDataDto();
            takeOverBalanceApiRespDataDto.setRequestHeader(apiReqHeaderDtoCC);
            takeOverBalanceApiRespDataDto.setTakeOverBalanceApiRespDetailDto(takeOverBalanceApiRespDetailDto);
            logger.info("takeover balance json payload:" + takeOverBalanceApiRespDetailDto);

            JAXBContext jaxbContext2CC = JAXBContext.newInstance(TakeOverBalanceApiRespDto.class);
            Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
            marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterCC = new StringWriter();
            marshallerCC.marshal(takeOverBalanceApiRespDataDto, stringWriterCC);
            String xmlPayloadCC = stringWriterCC.toString();
            System.out.println("takeover balance xml payload: " + xmlPayloadCC);

            logger.info("takeover balance catalog i.e with fsp code:" + xmlPayloadCC);

            JAXBContext jaxbContextSg = JAXBContext.newInstance(GenericApiReqDataDto.class);
            Unmarshaller unmarshallerSg = jaxbContextSg.createUnmarshaller();
            StringReader stringReaderSg = new StringReader(xmlPayloadCC);
            GenericApiReqDataDto genericApiReqDataDtoSg = (GenericApiReqDataDto) unmarshallerSg.unmarshal(stringReaderSg);

            // Extract and convert the content to be signed
            String content = convertObjectToXml(genericApiReqDataDtoSg);
            System.out.println("Content to be signed: " + content);

            String signatureSg = digitalSignature.createSignature(content);
            System.out.println("Generated Signature: " + signatureSg);

            // Create the final XML with the signature
            String finalXml = appendSignatureToXml2(content, signatureSg);
            System.out.println("Final XML: " + finalXml);

            logger.info("Final Xml Request {}:", finalXml);

            logger.info("Final CURL SUPPOSED request sent to utumishi***** {}:", finalXml);

            logger.info("takeover balance successfull " + finalXml);

            return ResponseEntity.ok(finalXml);

        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing take over balance request {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    //calculate final payment date
    public LocalDate calculateFinalPaymentDate() {
        int daysToAdd = 7;
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Add days to the current date
        LocalDate finalPaymentDate = currentDate.plusDays(daysToAdd);

        return finalPaymentDate;
    }



    // TAKEOVER LOAN REQUEST AND RESPONSE
    @Override
    public ResponseEntity<String> takeOverLoanRegistration(String message) throws Exception {

        logger.info("Received take over loan application request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(TakeOverLoanApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        System.out.println("Phase 0 ***************");
        TakeOverLoanApiReqDto takeOverLoanApiReqDto = (TakeOverLoanApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getRequestHeader().getMsgId();
//        String messageType = loanRegistrationApiReqDto.getLoanRegistrationData().getRequestHeader().getMessageType();
        try {
            System.out.println("Phase 1 ***************");
            String firstName= takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getTakeOverLoanApiReqDetailDto().getFirstName();
            String middleName= takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getTakeOverLoanApiReqDetailDto().getMiddleName();
            String lastName = takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getTakeOverLoanApiReqDetailDto().getLastName();
            String fullName = String.format("%s %s %s", firstName, middleName, lastName).trim().replaceAll(" +", " ");
            String fullNameOption = String.format("%s %s %s", middleName, firstName,lastName).trim().replaceAll(" +", " ");
            String loanApplicationNumber = takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getTakeOverLoanApiReqDetailDto().getApplicationNumber();

            String accountNo =takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getTakeOverLoanApiReqDetailDto().getBankAccountNumber();
            String ninId=takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getTakeOverLoanApiReqDetailDto().getNIN();

            System.out.println("Phase 1.1.1 ***************");
            //save logs request

            // Check if the product exists
            Optional<Product> product = productRepository.findByProductCode(takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getTakeOverLoanApiReqDetailDto().getProductCode());
            if (product.isEmpty()) {
                logger.info("invalid product code i.e productCode: " + takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getTakeOverLoanApiReqDetailDto().getProductCode());
                return createResponseEntity(ResponseCodes.INVALID_PRODUCT_CODE, fspCode, mssId);
            }

            System.out.println("Phase 2 ***************");

            System.out.println("Phase 3 ***************");

            Optional<Loan> loanByApplicationNumber = loanRepository.findFirstByApplicationNumber(loanApplicationNumber);

            if (loanByApplicationNumber.isPresent()) {
                logger.info("Duplicate application number: " + loanApplicationNumber);
                return createResponseEntity(ResponseCodes.DUPLICATE_REQUEST, fspCode, mssId);
            }

            // Remove spaces from fullName
            String cleanedFullName = stringUtil.removeSpaces(fullName);
            System.out.println("Contatenate fullname from request*************" + cleanedFullName);

            // convert l application request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(TakeOverLoanApiReqDto.class);
            Marshaller marshallerLRq = jaxbContextReq.createMarshaller();
            marshallerLRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterLRq = new StringWriter();
            marshallerLRq.marshal(takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto(), stringWriterLRq);
            String loanUtumishiRq = stringWriterLRq.toString();
            logger.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue (use the same Queue with Loan Request)
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getLoanTakeoverRoutingKey(),
                    loanUtumishiRq);

            logger.info("request sent to the queue: " + loanUtumishiRq);

            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing take over loan registration {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> takeOverPaymentNotification(String message) throws Exception {
        logger.info("Received take over payment Notification request: {}", message);

        JAXBContext jaxbContext = JAXBContext.newInstance(PaymentNotificationApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        PaymentNotificationApiReqDto paymentNotificationApiReqDto = (PaymentNotificationApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = paymentNotificationApiReqDto.getPaymentNotificationApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = paymentNotificationApiReqDto.getPaymentNotificationApiReqDataDto().getRequestHeader().getMsgId();

        try {

            //loan pay off operation
            Optional<FinancialServiceProvider> fsp =
                    financialServiceProviderRepository.findByFspCode(fspCode);

            if (fsp.isEmpty()) {
                logger.info("invalid fsp code, reason: no such fsp with code: " + fspCode);
                return createResponseEntity(ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER, fspCode, mssId);
            }

            // convert l application request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(PaymentNotificationApiReqDataDto.class);
            Marshaller marshallerLRq = jaxbContextReq.createMarshaller();
            marshallerLRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterLRq = new StringWriter();
            marshallerLRq.marshal(paymentNotificationApiReqDto.getPaymentNotificationApiReqDataDto(), stringWriterLRq);
            String loanUtumishiRq = stringWriterLRq.toString();

            logger.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getTakeOverPaymentNotificationRoutingKey(),
                    loanUtumishiRq);

            logger.info("request sent to the queue: " + loanUtumishiRq);


            logger.info("response is successfull ");
            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            logger.info(e.getMessage());
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR, fspCode, mssId);

        }

    }

    @Override
    public ResponseEntity<String> paymentAcknoledgementNotification(String message) throws Exception {
        logger.info("Payment acknoledgement notification message" + message);
        JAXBContext jaxbContext = JAXBContext.newInstance(PaymentAcknoledgementNotificationPortalDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        PaymentAcknoledgementNotificationPortalDto paymentAcknoledgementNotificationPortalDto = (PaymentAcknoledgementNotificationPortalDto) unmarshaller.unmarshal(stringReader);
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }
        try {
            PaymentAcknoledgeApiDetailDto paymentAcknoledgeApiDetailDto = new PaymentAcknoledgeApiDetailDto();
            paymentAcknoledgeApiDetailDto.setApplicationNumber(paymentAcknoledgementNotificationPortalDto.getApplicatioNumber());
            paymentAcknoledgeApiDetailDto.setLoanNumber(paymentAcknoledgementNotificationPortalDto.getLoanNumber());
            paymentAcknoledgeApiDetailDto.setRemarks(paymentAcknoledgementNotificationPortalDto.getRemarks());
            paymentAcknoledgeApiDetailDto.setFspReferenceNumber(paymentAcknoledgementNotificationPortalDto.getFspReferenceNumber());
            paymentAcknoledgeApiDetailDto.setPaymentStatus(paymentAcknoledgementNotificationPortalDto.getPaymentStatus());

            logger.info("repayment notification json in detail payload: " + paymentAcknoledgeApiDetailDto);

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.PAYMENT_ACKNOWLEDGMENT_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());


            PaymentAcknoledgeApiDataDto paymentAcknoledgeApiDataDto = new PaymentAcknoledgeApiDataDto();
            paymentAcknoledgeApiDataDto.setRequestHeader(apiReqHeaderDto);
            paymentAcknoledgeApiDataDto.setMessageDetails(paymentAcknoledgeApiDetailDto);

            logger.info("Payment Acknoledge notification json in data level payload: " + paymentAcknoledgeApiDataDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(PaymentAcknoledgeApiDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(paymentAcknoledgeApiDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();
            System.out.println("Payment acknoledgement notification xml payload: " + xmlPayload);

            logger.info("payment acknoledgement notification xml payload: " + xmlPayload);

            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            logger.info("payment acknoledgement notification sent to be signed" + xmlPayload);

            logger.info("payment acknoledgement notification response from ess" + responseEntity);
            //Create a payload for request and response
            //set request
            EssRequestPaymentAcknoledgementPortalDto essRequestPaymentAcknoledgementPortalDto = new EssRequestPaymentAcknoledgementPortalDto();
            essRequestPaymentAcknoledgementPortalDto.setApplicatioNumber(paymentAcknoledgementNotificationPortalDto.getApplicatioNumber());
            essRequestPaymentAcknoledgementPortalDto.setRemarks(paymentAcknoledgementNotificationPortalDto.getRemarks());
            essRequestPaymentAcknoledgementPortalDto.setFspReferenceNumber(paymentAcknoledgementNotificationPortalDto.getFspReferenceNumber());
            essRequestPaymentAcknoledgementPortalDto.setLoanNumber(paymentAcknoledgementNotificationPortalDto.getLoanNumber());
            essRequestPaymentAcknoledgementPortalDto.setPaymentStatus(paymentAcknoledgementNotificationPortalDto.getPaymentStatus());

            //set response
            EssResponsePaymentAcknoledgementPortalDto essResponsePaymentAcknoledgementPortalDto = new EssResponsePaymentAcknoledgementPortalDto();
            essResponsePaymentAcknoledgementPortalDto.setResponseDescription(String.valueOf(responseEntity));

            //combine request and response
            EssPaymentNotificationApiRespDataDto essPaymentNotificationApiRespDataDto= new EssPaymentNotificationApiRespDataDto();
            essPaymentNotificationApiRespDataDto.setMessageRequest(essRequestPaymentAcknoledgementPortalDto);
            essPaymentNotificationApiRespDataDto.setMessageResponse(essResponsePaymentAcknoledgementPortalDto);




            JAXBContext jaxbContext08 = JAXBContext.newInstance(EssPaymentNotificationApiRespDataDto.class);
            Marshaller marshaller08 = jaxbContext08.createMarshaller();
            marshaller08.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter08 = new StringWriter();
            marshaller08.marshal(essPaymentNotificationApiRespDataDto, stringWriter08);
            String xmlPayload08 = stringWriter08.toString();
            System.out.println("payment acknoledgement notification approval xml payload: " + xmlPayload08);
            logger.info("Combined payment acknoledgement notification and ess response" + xmlPayload08);
            String cleanedXml08 = cleanMessage(xmlPayload08);

            System.out.println("Cleaned payment acknoledgement notification xml payload: " + cleanedXml08);
            logger.info("Cleaned Combined payment acknoledgement notification and ess response" + cleanedXml08);

            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getApprovalAndDisbursRoutingKey(),
                    String.valueOf(cleanedXml08));

            return responseEntity;
        } catch (Exception e) {
            logger.info("fail to send payment acknoledgement notification to utumishi ");
            return null;
        }

    }


    // EXTRACT TAKEOVER BALANCE REQUEST MESSAGE FROM LOGS
    @Override
    public ResponseEntity<String> extractTakeoverRequests() throws Exception {

        System.out.println("*************pass here");
        List<String> extractedMessages = new ArrayList<>();
        StringBuilder xmlBuffer = new StringBuilder();
        boolean insideDataBlock = false;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(pathLogFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<Data>")) {
                    insideDataBlock = true;
                    xmlBuffer.setLength(0); // reset buffer
                }

                if (insideDataBlock) {
                    xmlBuffer.append(line).append(System.lineSeparator());
                }

                if (line.contains("</Data>") && insideDataBlock) {
                    insideDataBlock = false;
                    String fullXml = xmlBuffer.toString();
                    if (fullXml.contains("<MessageType>LOAN_TAKEOVER_BALANCE_RESPONSE</MessageType>")) {
                        extractedMessages.add(fullXml);
                    }
                }
            }

            // Log and send messages
            for (int i = 0; i < extractedMessages.size(); i++) {
                String message = extractedMessages.get(i);
//                System.out.println("Message " + (i + 1) + ":");
//                System.out.println(message);
//                System.out.println("--------------------------------------------------");

                logger.info("Prepared takeover balance request log sent to the queue: " + message);

                Takeoverbal takeoverBalance = new Takeoverbal();

                if (message.startsWith("<Document>")) {
                    // unmarshaller the takeover balance response
                    JAXBContext jaxbContext = JAXBContext.newInstance(TakeOverBalanceApiRespDto.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    StringReader stringReader = new StringReader(message);
                    TakeOverBalanceApiRespDto takeOverBalanceApiResponse = (TakeOverBalanceApiRespDto) unmarshaller.unmarshal(stringReader);


                    takeoverBalance.setLoanNumber(takeOverBalanceApiResponse.getTakeOverBalanceApiRespDataDto().getTakeOverBalanceApiRespDetailDto().getLoanNumber());
                    takeoverBalance.setTotalPayOffAmount(String.valueOf(takeOverBalanceApiResponse.getTakeOverBalanceApiRespDataDto().getTakeOverBalanceApiRespDetailDto().getTotalPayoffAmount()));
                    takeoverBalance.setFinalPaymentDate(takeOverBalanceApiResponse.getTakeOverBalanceApiRespDataDto().getTakeOverBalanceApiRespDetailDto().getFinalPaymentDate());
//              takeoverBalance.setRequestDate();
                    takeoverBalanceService.save(takeoverBalance);
                }

                if(message.startsWith("<Data>")) {
                    // unmarshaller the takeover balance response
                    JAXBContext jaxbContext = JAXBContext.newInstance(TakeOverBalanceApiRespDataDto.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    StringReader stringReader = new StringReader(message);
                    TakeOverBalanceApiRespDataDto takeOverBalanceApiResponse = (TakeOverBalanceApiRespDataDto) unmarshaller.unmarshal(stringReader);


                    takeoverBalance.setLoanNumber(takeOverBalanceApiResponse.getTakeOverBalanceApiRespDetailDto().getLoanNumber());
                    takeoverBalance.setTotalPayOffAmount(String.valueOf(takeOverBalanceApiResponse.getTakeOverBalanceApiRespDetailDto().getTotalPayoffAmount()));
                    takeoverBalance.setFinalPaymentDate(takeOverBalanceApiResponse.getTakeOverBalanceApiRespDetailDto().getFinalPaymentDate());
//              takeoverBalance.setRequestDate();
                    takeoverBalanceService.save(takeoverBalance);
                }

            }

            String successMsg = "Successfully processed " + extractedMessages.size() + " TAKEOVER_PAY_OFF_BALANCE_REQUEST message(s).";
            return ResponseEntity.ok(successMsg);

        } catch (IOException e) {
            logger.error("Failed to extract takeover requests from log file: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process log file: " + e.getMessage());
        }
    }


    //                       RESTRUCTURE FEATURES
    // RESTRUCTURE BALANCE REQUEST AND RESPONSE
    @Override
    public ResponseEntity<String> loanRestructureBalance(String message) throws Exception {
        logger.info("Received Restructure balance request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(RestructureBalanceApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        RestructureBalanceApiReqDto restructureBalanceApiReqDto = (RestructureBalanceApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = restructureBalanceApiReqDto.getRestructureBalanceApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = restructureBalanceApiReqDto.getRestructureBalanceApiReqDataDto().getRequestHeader().getMsgId();
        String loanNo = restructureBalanceApiReqDto.getRestructureBalanceApiReqDataDto().getRestructureBalanceApiReqDetailDto().getLoanNumber();
        try {
            //convert balance utumishi request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(RestructureBalanceApiReqDataDto.class);
            Marshaller marshallerRq = jaxbContextReq.createMarshaller();
            marshallerRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterRq = new StringWriter();
            marshallerRq.marshal(restructureBalanceApiReqDto.getRestructureBalanceApiReqDataDto(), stringWriterRq);
            String balanceUtumishiRq = stringWriterRq.toString();
            System.out.println("restructure balance request sent to api: " + balanceUtumishiRq);

            logger.info("restructure balance response is successfull i.e Product Code: {}" + balanceUtumishiRq);

            // send and receive balance messages thr: Api (share with top up balance messages)
            String apiResponse = balanceRestructureXmlSendToApi(balanceUtumishiRq);
            logger.info("restructure balance response Message from Portal Api: {}", apiResponse);

            JAXBContext jaxbContextCC = JAXBContext.newInstance(RestructureBalanceApiRespDataDto.class);
            Unmarshaller unmarshallerCC = jaxbContextCC.createUnmarshaller();
            StringReader stringReaderCC = new StringReader(apiResponse);
            RestructureBalanceApiRespDataDto balancePortalDto = (RestructureBalanceApiRespDataDto) unmarshallerCC.unmarshal(stringReaderCC);

            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);

            String sender = fspByCode.get().getFspShortName();

            JAXBContext jaxbContext2CC = JAXBContext.newInstance(RestructureBalanceApiRespDto.class);
            Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
            marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterCC = new StringWriter();
            marshallerCC.marshal(balancePortalDto, stringWriterCC);
            String xmlPayloadCC = stringWriterCC.toString();
            System.out.println("restructure balance xml payload: " + xmlPayloadCC);

            logger.info("restructure balance catalog i.e with fsp code:" + xmlPayloadCC);

            JAXBContext jaxbContextSg = JAXBContext.newInstance(GenericApiReqDataDto.class);
            Unmarshaller unmarshallerSg = jaxbContextSg.createUnmarshaller();
            StringReader stringReaderSg = new StringReader(xmlPayloadCC);
            GenericApiReqDataDto genericApiReqDataDtoSg = (GenericApiReqDataDto) unmarshallerSg.unmarshal(stringReaderSg);

            // Extract and convert the content to be signed
            String content = convertObjectToXml(genericApiReqDataDtoSg);
            System.out.println("Content to be signed: " + content);

            String signatureSg = digitalSignature.createSignature(content);
            System.out.println("Generated Signature: " + signatureSg);

            // Create the final XML with the signature
            String finalXml = appendSignatureToXml2(content, signatureSg);
            System.out.println("Final XML: " + finalXml);

            logger.info("Final Xml Request {}:", finalXml);

            logger.info("Final CURL SUPPOSED request sent to utumishi***** {}:", finalXml);

            logger.info("restructure balance successfull " + finalXml);

            return ResponseEntity.ok(finalXml);

        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing take over balance request {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> loanRestructureAffordability(String message) throws Exception{
        logger.info("Received loan charges request from utumishi: {}", message);

        JAXBContext jaxbContext = JAXBContext.newInstance(AffordabilityApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        AffordabilityApiDto affordabilityApiDto = (AffordabilityApiDto) unmarshaller.unmarshal(stringReader);
        String mssId = affordabilityApiDto.getAffordabilityApiReqDataDto().getRequestHeader().getMsgId();
        String fspCode = affordabilityApiDto.getAffordabilityApiReqDataDto().getRequestHeader().getFspCode();
        AffordabilityApiReqDataDto loanChargeDataLevel= affordabilityApiDto.getAffordabilityApiReqDataDto();
        try {


            // Access the values from the converted objects
            Optional<Product> productDetail = productRepository.findByProductCode(affordabilityApiDto.getAffordabilityApiReqDataDto().getMessageDetails().getProductCode());

            if(productDetail.isEmpty()){
                logger.info("invalid product code i.e Product Code: {}"+ affordabilityApiDto.getAffordabilityApiReqDataDto().getMessageDetails().getProductCode());
                return createResponseEntity(ResponseCodes.INVALID_PRODUCT_CODE ,affordabilityApiDto.getAffordabilityApiReqDataDto().getRequestHeader().getFspCode(),mssId);

            }
            Optional<FinancialServiceProvider> fsp = financialServiceProviderRepository.findByFspCode(fspCode);

            //convert charge utumishi request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(AffordabilityApiReqDataDto.class);
            Marshaller marshallerRq = jaxbContextReq.createMarshaller();
            marshallerRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterRq = new StringWriter();
            marshallerRq.marshal(loanChargeDataLevel, stringWriterRq);
            String chargeUtumishiRq = stringWriterRq.toString();

            logger.info("affordability response is successfull i.e Product Code: {}" + chargeUtumishiRq);

            String apiResponse = affordabilityXmlSendToApi(chargeUtumishiRq);
            logger.info("Response Message from Portal Api: {}", apiResponse);

            JAXBContext jaxbContextCC = JAXBContext.newInstance(AffordabilityApirRespDataDto.class);
            Unmarshaller unmarshallerCC = jaxbContextCC.createUnmarshaller();
            StringReader stringReaderCC = new StringReader(apiResponse);
            AffordabilityApirRespDataDto chargePortalDto = (AffordabilityApirRespDataDto) unmarshallerCC.unmarshal(stringReaderCC);

            JAXBContext jaxbContext2CC = JAXBContext.newInstance(AffordabilityApirRespDataDto.class);
            Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
            marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterCC = new StringWriter();
            marshallerCC.marshal(chargePortalDto, stringWriterCC);
            String xmlPayloadCC = stringWriterCC.toString();
            logger.info("affordability catalog i.e with fsp code:" + xmlPayloadCC);

            JAXBContext jaxbContextSg = JAXBContext.newInstance(GenericApiReqDataDto.class);
            Unmarshaller unmarshallerSg = jaxbContextSg.createUnmarshaller();
            StringReader stringReaderSg = new StringReader(xmlPayloadCC);
            GenericApiReqDataDto genericApiReqDataDtoSg = (GenericApiReqDataDto) unmarshallerSg.unmarshal(stringReaderSg);

            // Extract and convert the content to be signed
            String content = convertObjectToXml(genericApiReqDataDtoSg);
            System.out.println("Content to be signed: " + content);

            String signatureSg = digitalSignature.createSignature(content);
            System.out.println("Generated Signature: " + signatureSg);

            // Create the final XML with the signature
            String finalXml = appendSignatureToXml2(content, signatureSg);
            System.out.println("Final XML: " + finalXml);

            logger.info("Final Xml Request {}:", finalXml);

            // Send the final XML to the specified API endpoint
            logger.info("Final CURL SUPPOSED request sent to utumishi***** {}:", finalXml);

            return ResponseEntity.ok(finalXml);
        } catch(Exception e){
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR,fspCode, mssId);
        }
    }

    //for restructure balance
    private String balanceRestructureXmlSendToApi(String xml) throws Exception {
        // Log the final XML request
        logger.info("Final CURL XML request sent to Utumishi: {}", xml);

        URL url = new URL(balanceRestructureEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.getOutputStream().write(xml.getBytes(StandardCharsets.UTF_8));

        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        }

        String responseMessage = response.toString();
        logger.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    //for affordability api to portal
    private String affordabilityXmlSendToApi(String xml) throws Exception {
        // Log the final XML request
        logger.info("Final CURL XML request sent to Utumishi: {}", xml);

        URL url = new URL(affordabilityRestructureEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.getOutputStream().write(xml.getBytes(StandardCharsets.UTF_8));

        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        }

        String responseMessage = response.toString();
        logger.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    @Override
    public ResponseEntity<String> loanRestructureRequest(String message) throws Exception {
        logger.info("Received restructure application request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(RestructureApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);

        RestructureApiReqDto restructureApiReqDto = (RestructureApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = restructureApiReqDto.getRestructureApiReqDataDto().getRequestHeader().getFspCode();
        String mssId =restructureApiReqDto.getRestructureApiReqDataDto().getRequestHeader().getMsgId();
//        String messageType = loanRegistrationApiReqDto.getLoanRegistrationData().getRequestHeader().getMessageType();
        try {
            String firstName= restructureApiReqDto.getRestructureApiReqDataDto().getRestructureReqDetailDto().getFirstName();
            String middleName= restructureApiReqDto.getRestructureApiReqDataDto().getRestructureReqDetailDto().getMiddleName();
            String lastName = restructureApiReqDto.getRestructureApiReqDataDto().getRestructureReqDetailDto().getLastName();
            String fullName = String.format("%s %s %s", firstName, middleName, lastName).trim().replaceAll(" +", " ");
            String fullNameOption = String.format("%s %s %s", middleName, firstName,lastName).trim().replaceAll(" +", " ");
            String loanApplicationNumber = restructureApiReqDto.getRestructureApiReqDataDto().getRestructureReqDetailDto().getApplicationNumber();
            String ninId=restructureApiReqDto.getRestructureApiReqDataDto().getRestructureReqDetailDto().getNIN();

            // Check if the product exists
            Optional<Product> product = productRepository.findByProductCode(restructureApiReqDto.getRestructureApiReqDataDto().getRestructureReqDetailDto().getProductCode());
            if (product.isEmpty()) {
                logger.info("invalid product code i.e productCode: " + restructureApiReqDto.getRestructureApiReqDataDto().getRestructureReqDetailDto().getProductCode());
                return createResponseEntity(ResponseCodes.INVALID_PRODUCT_CODE, fspCode, mssId);
            }


            // Remove spaces from fullName
            String cleanedFullName = stringUtil.removeSpaces(fullName);
            System.out.println("Contatenate fullname from request*************" + cleanedFullName);

            // convert l application request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(RestructureApiReqDataDto.class);
            Marshaller marshallerLRq = jaxbContextReq.createMarshaller();
            marshallerLRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterLRq = new StringWriter();
            marshallerLRq.marshal(restructureApiReqDto.getRestructureApiReqDataDto(), stringWriterLRq);
            String loanUtumishiRq = stringWriterLRq.toString();

            logger.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getLoanRestructureRqRoutingKey(),
                    loanUtumishiRq);

            logger.info("request sent to the queue: " + loanUtumishiRq);

            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("Exception occurred while processing loan restructure {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> loanRestructureRejection(String message) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(RestructureRejectApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        RestructureRejectApiDto restructureRejectApiDto = (RestructureRejectApiDto) unmarshaller.unmarshal(stringReader);
        String fspCode = restructureRejectApiDto.getRestructureRejectApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = restructureRejectApiDto.getRestructureRejectApiReqDataDto().getRequestHeader().getMsgId();
        logger.info("Received loan restructure rejection request: {}", message);

        try {
            LoanCancellation loanCancellation = new LoanCancellation();
            loanCancellation.setReason(restructureRejectApiDto.getRestructureRejectApiReqDataDto().getMessageDetails().getReason());

            // convert l application request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(RestructureRejectApiReqDataDto.class);
            Marshaller marshallerLRq = jaxbContextReq.createMarshaller();
            marshallerLRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterLRq = new StringWriter();
            marshallerLRq.marshal(restructureRejectApiDto.getRestructureRejectApiReqDataDto(), stringWriterLRq);
            String loanUtumishiRq = stringWriterLRq.toString();

            logger.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue
            utils.sendMessage(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getLoanRejectRestructureRqRoutingKey(),
                    loanUtumishiRq);

            logger.info("request restructure rejection sent to the queue: " + loanUtumishiRq);


            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            logger.error("Error processing loan restructure request", e);
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> restructureInitiatedFromFsp(String message) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(InitiateRestructureFspApiDataDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        InitiateRestructureFspApiDataDto initiateRestructureFspApiDataDto = (InitiateRestructureFspApiDataDto) unmarshaller.unmarshal(stringReader);
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }

        try {
            JAXBContext jaxbContext2 = JAXBContext.newInstance(InitiateRestructureFspApiDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(initiateRestructureFspApiDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();
            System.out.println("restructure xml payload: " + xmlPayload);

            logger.info("restructure xml payload: " + xmlPayload);

            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            logger.info("restructure response" + responseEntity);

            return responseEntity;

        } catch (Exception e) {
            logger.info("fail to send loan initial notification to utumishi");
            return null;
        }



    }



}




