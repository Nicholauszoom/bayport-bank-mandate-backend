package com.example.Project.Management.bayport.loan.service;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
import com.example.Project.Management.bayport.apis.dto.GenericApiReqDataDto;
import com.example.Project.Management.bayport.apis.dto.GenericApiReqDataDtoWrapper;
import com.example.Project.Management.bayport.balance.entity.Balance;
import com.example.Project.Management.bayport.balance.repository.BalanceRepository;
import com.example.Project.Management.bayport.charge.entity.Charge;
import com.example.Project.Management.bayport.charge.entity.ClientRv;
import com.example.Project.Management.bayport.charge.repository.ChargeRepository;
import com.example.Project.Management.bayport.charge.repository.ClientRvRepository;
import com.example.Project.Management.bayport.common.Constants;
import com.example.Project.Management.bayport.common.DigitalSignature;
import com.example.Project.Management.bayport.common.enumerator.LoanType;
import com.example.Project.Management.bayport.common.enumerator.PaymentStatus;
import com.example.Project.Management.bayport.common.enumerator.RequestAction;
import com.example.Project.Management.bayport.common.enumerator.RequestType;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.repositories.FinancialServiceProviderRepository;
import com.example.Project.Management.bayport.loan.dto.approval.LoanApprovalApiDataDto;
import com.example.Project.Management.bayport.loan.dto.approval.LoanApprovalApiDetailDto;
import com.example.Project.Management.bayport.loan.dto.approval.LoanApprovalApiDto;
import com.example.Project.Management.bayport.loan.dto.cancellation.LoanCancellationApiDto;
import com.example.Project.Management.bayport.loan.dto.liquidization.LoanLiquidizationApiReqDataDto;
import com.example.Project.Management.bayport.loan.dto.liquidization.LoanLiquidizationApiReqDetailDto;
import com.example.Project.Management.bayport.loan.dto.liquidization.portal.LiquidationDto;
import com.example.Project.Management.bayport.loan.dto.payment_acknoledgement.PaymentAcknoledgeApiDataDto;
import com.example.Project.Management.bayport.loan.dto.payment_acknoledgement.PaymentAcknoledgeApiDetailDto;
import com.example.Project.Management.bayport.loan.dto.portal.LoanDto;
import com.example.Project.Management.bayport.loan.dto.registration.LoanRegistrationApiReqDto;
import com.example.Project.Management.bayport.loan.dto.registration.LoanRegistrationReqDetailDto;
import com.example.Project.Management.bayport.loan.dto.restructure.affordability.request.AffordabilityApiDto;
import com.example.Project.Management.bayport.loan.dto.restructure.affordability.request.AffordabilityApiReqDataDto;
import com.example.Project.Management.bayport.loan.dto.restructure.affordability.response.AffordabilityApirRespDataDto;
import com.example.Project.Management.bayport.loan.dto.restructure.balance.request.RestructureBalanceApiReqDataDto;
import com.example.Project.Management.bayport.loan.dto.restructure.balance.request.RestructureBalanceApiReqDto;
import com.example.Project.Management.bayport.loan.dto.restructure.balance.response.RestructureBalanceApiRespDataDto;
import com.example.Project.Management.bayport.loan.dto.restructure.balance.response.RestructureBalanceApiRespDto;
import com.example.Project.Management.bayport.loan.dto.restructure.fsp_initiate_restructure.InitiateRestructureFspApiDataDto;
import com.example.Project.Management.bayport.loan.dto.restructure.rejection.RestructureRejectApiDto;
import com.example.Project.Management.bayport.loan.dto.restructure.rejection.RestructureRejectApiReqDataDto;
import com.example.Project.Management.bayport.loan.dto.restructure.restructure.RestructureApiReqDataDto;
import com.example.Project.Management.bayport.loan.dto.restructure.restructure.RestructureApiReqDto;
import com.example.Project.Management.bayport.loan.dto.takeover.payment_notification.PaymentNotificationApiReqDetailDto;
import com.example.Project.Management.bayport.loan.dto.takeover.payment_notification.PaymentNotificationApiReqDto;
import com.example.Project.Management.bayport.loan.dto.takeover.take_over_balance.TakeOverBalanceApiReqDetailDto;
import com.example.Project.Management.bayport.loan.dto.takeover.take_over_balance.TakeOverBalanceApiReqDto;
import com.example.Project.Management.bayport.loan.dto.takeover.take_over_balance.response.TakeOverBalanceApiRespDataDto;
import com.example.Project.Management.bayport.loan.dto.takeover.take_over_balance.response.TakeOverBalanceApiRespDetailDto;
import com.example.Project.Management.bayport.loan.dto.takeover.take_over_balance.response.TakeOverBalanceApiRespDto;
import com.example.Project.Management.bayport.loan.dto.takeover.take_over_loan.TakeOverLoanApiReqDto;
import com.example.Project.Management.bayport.loan.dto.topup.LoanTopUpApiReqDataDto;
import com.example.Project.Management.bayport.loan.dto.topup.LoanTopUpApiReqDto;
import com.example.Project.Management.bayport.loan.dto.topup.balance.request.LoanTopUpBalancApiReqDataDto;
import com.example.Project.Management.bayport.loan.dto.topup.balance.request.LoanTopUpBalanceApiReqDetailDto;
import com.example.Project.Management.bayport.loan.dto.topup.balance.request.LoanTopUpBalanceApiReqDto;
import com.example.Project.Management.bayport.loan.dto.topup.balance.response.LoanTopUpBalanceApiRespDataDto;
import com.example.Project.Management.bayport.loan.dto.topup.balance.response.LoanTopUpPayOffBalanceApiRespDetailDto;
import com.example.Project.Management.bayport.loan.entity.Loan;
import com.example.Project.Management.bayport.loan.entity.LoanCancellation;
import com.example.Project.Management.bayport.loan.repository.*;
import com.example.Project.Management.bayport.product.entity.Product;
import com.example.Project.Management.bayport.product.repository.ProductRepository;
import com.example.Project.Management.bayport.setup.dto.response.ResponseApiDto;
import com.example.Project.Management.bayport.setup.dto.response.ResponseDetailApiDto;
import com.example.Project.Management.bayport.status.repository.StatusRepository;
import com.example.Project.Management.bayport.takeover.dto.portal.PaymentAcknoledgeDto;
import com.example.Project.Management.bayport.takeover.entity.Takeoverbalance;
import com.example.Project.Management.bayport.takeover.entity.Takeoverpayment;
import com.example.Project.Management.bayport.takeover.repository.TakeoverBalanceRepository;
import com.example.Project.Management.bayport.takeover.repository.TakeoverPaymentRepository;
import com.example.Project.Management.bayport.util.ResponseCodes;
import com.example.Project.Management.bayport.util.StringUtil;
import com.example.Project.Management.bayport.util.Utils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService {

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

    @Autowired
    private Utils utils;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private DigitalSignature digitalSignature;

    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private FinancialServiceProviderRepository financialServiceProviderRepository;

    @Autowired
    private ProductRepository productRepository;

    private final ChargeRepository chargeRepository;

    private final ClientRvRepository clientRvRepository;

    private final BalanceRepository balanceRepository;

    private final TakeoverBalanceRepository  takeoverBalanceRepository;

    private final TakeoverPaymentRepository takeoverPaymentRepository;

//    private final Balance
    
    public LoanServiceImpl(
            Utils utils, DigitalSignature digitalSignature, LoanRepository loanRepository,
            ProductRepository productRepository, FinancialServiceProviderRepository financialServiceProviderRepository, StatusRepository statusRepository, StringUtil stringUtil, ChargeRepository chargeRepository, ClientRvRepository clientRvRepository, BalanceRepository balanceRepository, TakeoverBalanceRepository takeoverBalanceRepository, TakeoverPaymentRepository takeoverPaymentRepository) {
        this.utils = utils;
        this.digitalSignature = digitalSignature;
        this.loanRepository = loanRepository;
        this.productRepository = productRepository;
        this.chargeRepository = chargeRepository;
        this.financialServiceProviderRepository =financialServiceProviderRepository;
        this.statusRepository =statusRepository;
        this.stringUtil = stringUtil;
        this.clientRvRepository = clientRvRepository;
        this.balanceRepository = balanceRepository;
        this.takeoverBalanceRepository = takeoverBalanceRepository;
        this.takeoverPaymentRepository = takeoverPaymentRepository;
    }
    
    @Override
    public ResponseEntity<String> loanRegistration(String message) throws Exception {

        log.info("Received loan application request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanRegistrationApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        
        LoanRegistrationApiReqDto loanRegistrationApiReqDto = (LoanRegistrationApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanRegistrationApiReqDto.getLoanRegistrationData().getRequestHeader().getFspCode();
        String mssId = loanRegistrationApiReqDto.getLoanRegistrationData().getRequestHeader().getMsgId();
        String messageType = loanRegistrationApiReqDto.getLoanRegistrationData().getRequestHeader().getMessageType();
        String checkNumber =loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getCheckNumber();
        try {
            String loanApplicationNumber = loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getApplicationNumber();

            Optional<FinancialServiceProvider> fsp = financialServiceProviderRepository.findByFspCode(fspCode);
            if (fsp.isEmpty()) {
                log.info("invalid fsp code i.e fspcode: {}" , fspCode);
                return createResponseEntity(ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER, fspCode, mssId);
            }

            Optional<Product> product = productRepository.findByProductCode(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getProductCode());
            if (product.isEmpty()) {
                log.info("invalid product code i.e productCode: {}" , loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail().getProductCode());
                return createResponseEntity(ResponseCodes.INVALID_PRODUCT_CODE, fspCode, mssId);
            }
            
            Optional<Loan> loanByApplicationNumber = loanRepository.findFirstByApplicationNumber(loanApplicationNumber);

            if (loanByApplicationNumber.isPresent()) {
                log.info("Duplicate application number: {}", loanApplicationNumber);
                return createResponseEntity(ResponseCodes.DUPLICATE_REQUEST, fspCode, mssId);
            }

            Charge charge = chargeRepository.findLatestChargeByChecknumber(checkNumber);
            if (charge == null) {
                log.info("No charge found with checknumber: {}", checkNumber);
                return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
            }

            LoanType loanType = switch (messageType) {
                case Constants.LOAN_OFFER_REQUEST -> LoanType.NEW;
                case Constants.TOP_UP_OFFER_REQUEST -> LoanType.TOPUP;
                case Constants.LOAN_RESTRUCTURING_NOTIFICATION -> LoanType.RESTRUCTURE;
                default -> null;
            };

            // save
            Loan savedLoan = saveloan(loanRegistrationApiReqDto.getLoanRegistrationData().getLoanRegistrationDetail(), loanType, RequestType.fsp_received, charge);

            if (savedLoan == null) {
                return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
            }

            log.info("loan Saved Successfully");
            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            log.info("Exception occurred while processing loan registration {}", e.getMessage());
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    public Loan saveloan(LoanRegistrationReqDetailDto loanRegistrationReqDetailDto, LoanType loanType, RequestType requestType, Charge charge) {
        try {
            Loan loan = Loan.builder()
                    .firstName(loanRegistrationReqDetailDto.getFirstName())
                    .middleName(loanRegistrationReqDetailDto.getMiddleName())
                    .lastName(loanRegistrationReqDetailDto.getLastName())
                    .sex(loanRegistrationReqDetailDto.getSex())
                    .bankAccountNumber(loanRegistrationReqDetailDto.getBankAccountNumber())
                    .employmentDate(loanRegistrationReqDetailDto.getEmploymentDate())
                    .maritalStatus(loanRegistrationReqDetailDto.getMaritalStatus())
                    .confirmationDate(loanRegistrationReqDetailDto.getConfirmationDate())
                    .totalEmployeeDeduction(loanRegistrationReqDetailDto.getTotalEmployeeDeduction())
                    .nearestBranchName(loanRegistrationReqDetailDto.getNearestBranchName())
                    .nearestBranchCode(loanRegistrationReqDetailDto.getNearestBranchCode())
                    .voteCode(loanRegistrationReqDetailDto.getVoteCode())
                    .voteName(loanRegistrationReqDetailDto.getVoteName())
                    .NIN(loanRegistrationReqDetailDto.getNIN())
                    .designationCode(loanRegistrationReqDetailDto.getDesignationCode())
                    .designationName(loanRegistrationReqDetailDto.getDesignationName())
                    .basicSalary(loanRegistrationReqDetailDto.getBasicSalary())
                    .netSalary(loanRegistrationReqDetailDto.getNetSalary())
                    .oneThirdAmount(loanRegistrationReqDetailDto.getOneThirdAmount())
                    .requestedAmount(loanRegistrationReqDetailDto.getRequestedAmount())
                    .desiredDeductibleAmount(loanRegistrationReqDetailDto.getDesiredDeductibleAmount())
                    .retirementDate(loanRegistrationReqDetailDto.getRetirementDate())
                    .termsOfEmployment(loanRegistrationReqDetailDto.getTermsOfEmployment())
                    .tenure(loanRegistrationReqDetailDto.getTenure())
                    .productCode(loanRegistrationReqDetailDto.getProductCode())
                    .interestRate(loanRegistrationReqDetailDto.getInterestRate())
                    .processingFee(loanRegistrationReqDetailDto.getProcessingFee())
                    .insurance(loanRegistrationReqDetailDto.getInsurance())
                    .physicalAddress(loanRegistrationReqDetailDto.getPhysicalAddress())
                    .emailAddress(loanRegistrationReqDetailDto.getEmailAddress())
                    .mobileNumber(loanRegistrationReqDetailDto.getMobileNumber())
                    .applicationNumber(loanRegistrationReqDetailDto.getApplicationNumber())
                    .loanPurpose(loanRegistrationReqDetailDto.getLoanPurpose())
                    .contractStartDate(loanRegistrationReqDetailDto.getContractStartDate())
                    .contractEndDate(loanRegistrationReqDetailDto.getContractEndDate())
                    .checkNumber(loanRegistrationReqDetailDto.getCheckNumber())
                    .swiftCode(loanRegistrationReqDetailDto.getSwiftCode())
                    .funding(loanRegistrationReqDetailDto.getFunding())
                    .loanType(loanType)
                    .requestType(requestType)
                    .chargeId(charge.getId())
                    .build();

            Loan savedLoan=loanRepository.save(loan);
            return savedLoan;
        } catch (Exception e) {
            log.info("Exception: ", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public List<LoanDto> searchLoans(String term, RequestType requestType) {
        List<Loan> loans = loanRepository.searchLoans(term, requestType);
        return loans.stream()
                .map(this::mapToDto)
                .toList();
    }

    private LoanDto mapToDto(Loan loan) {
        return LoanDto.builder()
                .firstName(loan.getFirstName())
                .middleName(loan.getMiddleName())
                .lastName(loan.getLastName())
                .sex(loan.getSex())
                .bankAccountNumber(loan.getBankAccountNumber())
                .employmentDate(loan.getEmploymentDate())
                .maritalStatus(loan.getMaritalStatus())
                .confirmationDate(loan.getConfirmationDate())
                .totalEmployeeDeduction(loan.getTotalEmployeeDeduction())
                .nearestBranchName(loan.getNearestBranchName())
                .nearestBranchCode(loan.getNearestBranchCode())
                .voteCode(loan.getVoteCode())
                .voteName(loan.getVoteName())
                .NIN(loan.getNIN())
                .designationCode(loan.getDesignationCode())
                .designationName(loan.getDesignationName())
                .basicSalary(loan.getBasicSalary())
                .netSalary(loan.getNetSalary())
                .oneThirdAmount(loan.getOneThirdAmount())
                .requestedAmount(loan.getRequestedAmount())
                .desiredDeductibleAmount(loan.getDesiredDeductibleAmount())
                .retirementDate(loan.getRetirementDate())
                .termsOfEmployment(loan.getTermsOfEmployment())
                .tenure(loan.getTenure())
                .productCode(loan.getProductCode())
                .interestRate(loan.getInterestRate())
                .processingFee(loan.getProcessingFee())
                .insurance(loan.getInsurance())
                .physicalAddress(loan.getPhysicalAddress())
                .emailAddress(loan.getEmailAddress())
                .mobileNumber(loan.getMobileNumber())
                .applicationNumber(loan.getApplicationNumber())
                .loanPurpose(loan.getLoanPurpose())
                .contractStartDate(loan.getContractStartDate())
                .contractEndDate(loan.getContractEndDate())
                .checkNumber(loan.getCheckNumber())
                .swiftCode(loan.getSwiftCode())
                .funding(loan.getFunding())
                .loanType(loan.getLoanType())
                .createdDate(loan.getCreatedDate())
                .build();
    }

    @Override
    public ResponseEntity<String> loanInitialApproveNotification(Long id, RequestAction requestAction) throws Exception {
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            Optional<Loan> loanOption = loanRepository.findById(id);
            if (loanOption.isEmpty()) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan Not Found , loanId: "+ id);
            }
            Loan loan = loanOption.get();

            Optional<Charge> chargeOpt = chargeRepository.findById(loan.getChargeId());
            if (chargeOpt.isEmpty()) {
                log.info("No charge found with checknumber: {}", loanOption.get().getCheckNumber());
                return ResponseEntity.ok("No charge found");
            }
            Charge charge = chargeOpt.get();
            log.info("Charge found with checknumber: {},{}", loan.getCheckNumber(), requestAction);

            LoanApprovalApiDetailDto loanApprovalApiDetailDto = new LoanApprovalApiDetailDto();
            loanApprovalApiDetailDto.setApplicationNumber(loan.getApplicationNumber());
            loanApprovalApiDetailDto.setReason(requestAction.name());
            loanApprovalApiDetailDto.setFspReferenceNumber(String.valueOf(loan.getId()));
            loanApprovalApiDetailDto.setLoanNumber(String.valueOf(loan.getId()));
            loanApprovalApiDetailDto.setTotalAmountToPay(charge.getTotalAmountToPay());
            loanApprovalApiDetailDto.setOtherCharges(charge.getOtherCharges());
            loanApprovalApiDetailDto.setApproval(requestAction.name());

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_INITIAL_APPROVAL_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());

            LoanApprovalApiDataDto loanApprovalApiDataDto = new LoanApprovalApiDataDto();
            loanApprovalApiDataDto.setRequestHeader(apiReqHeaderDto);
            loanApprovalApiDataDto.setMessageDetails(loanApprovalApiDetailDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanApprovalApiDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanApprovalApiDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();

            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            // Update loan
            if (requestAction.equals(RequestAction.APPROVED)) {
                loan.setRequestType(RequestType.fsp_accepted);
            } else {
                loan.setRequestType(RequestType.fsp_rejected);
            }

            log.info("Initial Approve Utumishi Response: " + responseEntity);

            ResponseDetailApiDto responseDetailApiDto = getUtumishiApiResponse(String.valueOf(responseEntity));

            if (responseDetailApiDto.getResponseCode() == "8000") {
                // Update loan
                loan.setInitialApproveDate(String.valueOf(now));
                loanRepository.save(loan);
                return ResponseEntity.status(HttpStatus.OK).body("INITIAL APPROVAL SUCCESSFUL");
            } else {
                return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("INITIAL APPROVAL FAILED, WITH CODE :" + responseDetailApiDto.getResponseCode());
            }

        } catch (Exception e) {
            log.info(" Exception occur when send initial approval:{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INITIAL APPROVAL FAILED");
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
        String applicationNumber = loanCancellationApiDto.getLoanCancellationApiReqData().getMessageDetails().getApplicationNumber();
        log.info("Received loan cancellation request: {}", message);

        try {
            LocalDateTime now = LocalDateTime.now();
            Optional<FinancialServiceProvider> fsp = financialServiceProviderRepository.findByFspCode(fspCode);
            if (fsp.isEmpty()) {
                log.info("invalid fsp code, reason: no such fsp with code: " + fspCode);
                return createResponseEntity(ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER,  fspCode, mssId);
            }

            Optional<Loan> loanOption = loanRepository.findByApplicationNumber(applicationNumber);
            if (loanOption.isEmpty()) {
                log.info("Loan Not Found , applicationNumber: {}", applicationNumber);
                return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
            }
            Loan loan = loanOption.get();
            loan.setEssApprovalReason(loanCancellationApiDto.getLoanCancellationApiReqData().getMessageDetails().getReason());
            loan.setRequestType(RequestType.employee_rejected);
            loan.setEmployeeCancellDate(String.valueOf(now));
            loanRepository.save(loan);

            log.info("Loan Cancellation Success");
            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            log.error("Error processing loan cancellation request", e);
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    @Override
    public ResponseEntity<String> loanFinalApprovalNotification(String message) throws Exception {
        log.info("Received loan Final Approval Notification request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanApprovalApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanApprovalApiDto loanApprovalApiDto = (LoanApprovalApiDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanApprovalApiDto.getLoanApprovalApiDataDto().getRequestHeader().getFspCode();
        String mssId = loanApprovalApiDto.getLoanApprovalApiDataDto().getRequestHeader().getMsgId();
        String applicationNumber = loanApprovalApiDto.getLoanApprovalApiDataDto().getMessageDetails().getApplicationNumber();
        RequestAction requestAction = RequestAction.valueOf(loanApprovalApiDto.getLoanApprovalApiDataDto().getMessageDetails().getApproval());
        try {

            LocalDateTime now = LocalDateTime.now();
            Optional<FinancialServiceProvider> fsp = financialServiceProviderRepository.findByFspCode(fspCode);
            if (fsp.isEmpty()) {
                log.info("invalid fsp code, reason: no such fsp with code: " + fspCode);
                return createResponseEntity(ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER,  fspCode, mssId);
            }

            Optional<Loan> loanOption = loanRepository.findByApplicationNumber(applicationNumber);
            if (loanOption.isEmpty()) {
                log.info("Loan Not Found , applicationNumber: {}", applicationNumber);
                return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
            }
            Loan loan = loanOption.get();
            loan.setEssApprovalReason(loanApprovalApiDto.getLoanApprovalApiDataDto().getMessageDetails().getReason());
            if (requestAction.equals(RequestAction.APPROVED)) {
                loan.setRequestType(RequestType.hro_approved);
            } else {
                loan.setRequestType(RequestType.hro_rejected);
            }

            loan.setHroApproveDate(String.valueOf(now));
            loanRepository.save(loan);
            log.info("Loan Final Approval Success");
            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        }catch (Exception e){
            log.info(e.getMessage());
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR,fspCode, mssId);
        }
    }

    @Override
    public Optional<Loan> findById(long id) {
        return loanRepository.findById(id);
    }

    @Override
    public List<Loan> findLoanByRequestType(RequestType requestType) {
        return loanRepository.findLoanByRequestType(requestType);
    }

    @Override
    public ResponseEntity<?> loanLiquidation(Long id, String remark) {
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            Optional<Loan> loanOption = loanRepository.findById(id);
            if (loanOption.isEmpty()) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan Not Found , loanId: "+ id);
            }
            Loan loan = loanOption.get();

            LoanLiquidizationApiReqDetailDto loanLiquidizationApiReqDetailDto = new LoanLiquidizationApiReqDetailDto();
            loanLiquidizationApiReqDetailDto.setApplicationNumber(loan.getApplicationNumber());
            loanLiquidizationApiReqDetailDto.setLoanNumber(String.valueOf(loan.getId()));
            loanLiquidizationApiReqDetailDto.setRemarks(remark);

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_LIQUIDATION_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());

            LoanLiquidizationApiReqDataDto loanLiquidizationApiReqDataDto = new LoanLiquidizationApiReqDataDto();
            loanLiquidizationApiReqDataDto.setRequestHeader(apiReqHeaderDto);
            loanLiquidizationApiReqDataDto.setLoanLiquidizationApiReqDetailDto(loanLiquidizationApiReqDetailDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanLiquidizationApiReqDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanLiquidizationApiReqDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();

            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            log.info("Liquidation Utumishi Response: " + responseEntity);

            ResponseDetailApiDto responseDetailApiDto = getUtumishiApiResponse(String.valueOf(responseEntity));

            if (responseDetailApiDto.getResponseCode() == "8000") {
                // Update loan
                loan.setRequestType(RequestType.loan_liquidated);
                loan.setLiquidationRemark(remark);
                loan.setLiquidationDate(String.valueOf(now));
                Loan saveResponse = loanRepository.save(loan);
                log.info("loan save resposne : {}", saveResponse);

                return ResponseEntity.status(HttpStatus.OK).body("LIQUIDATION SUCCESSFUL");
            }else {
                return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("LIQUIDATION FAILED, WITH CODE :" + responseDetailApiDto.getResponseCode());
            }
        } catch (Exception e) {
            log.info("Liquidation failed with :{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("LIQUIDATION FAILED");
        }
    }

    @Override
    public ResponseEntity<?> liquidation(LiquidationDto liquidationDto) {
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }
        try {
            LoanLiquidizationApiReqDetailDto loanLiquidizationApiReqDetailDto = new LoanLiquidizationApiReqDetailDto();
            loanLiquidizationApiReqDetailDto.setApplicationNumber(liquidationDto.getApplicationNumber());
            loanLiquidizationApiReqDetailDto.setLoanNumber(liquidationDto.getLoanNumber());
            loanLiquidizationApiReqDetailDto.setRemarks(liquidationDto.getRemarks());

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_LIQUIDATION_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());

            LoanLiquidizationApiReqDataDto loanLiquidizationApiReqDataDto = new LoanLiquidizationApiReqDataDto();
            loanLiquidizationApiReqDataDto.setRequestHeader(apiReqHeaderDto);
            loanLiquidizationApiReqDataDto.setLoanLiquidizationApiReqDetailDto(loanLiquidizationApiReqDetailDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanLiquidizationApiReqDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanLiquidizationApiReqDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();

            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            log.info("Liquidation Utumishi Response: " + responseEntity);

            ResponseDetailApiDto responseDetailApiDto = getUtumishiApiResponse(String.valueOf(responseEntity));

            if (responseDetailApiDto.getResponseCode() == "8000") {

                return ResponseEntity.status(HttpStatus.OK).body("LIQUIDATION SUCCESSFUL");
            }else {
                return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("LIQUIDATION FAILED, WITH CODE :" + responseDetailApiDto.getResponseCode());
            }
        } catch (Exception e) {
            log.info("Liquidation failed with :{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("LIQUIDATION FAILED");
        }
    }


    @Override
    public ResponseEntity<String> loanTopUpPayOffBalance(String message) throws Exception {
        log.info("Received loan TopUp pay off balance request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanTopUpBalanceApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        LoanTopUpBalanceApiReqDto loanTopUpBalanceApiReqDto = (LoanTopUpBalanceApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto().getRequestHeader().getMsgId();
        String loanNumber = loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto().getLoanTopUpBalanceApiReqDetailDto().getLoanNumber();
        String checkNumber = loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto().getLoanTopUpBalanceApiReqDetailDto().getCheckNumber();
        LoanTopUpBalanceApiReqDetailDto loanTopUpBalanceApiReqDetailDto = loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto().getLoanTopUpBalanceApiReqDetailDto();
        try {
            JAXBContext jaxbContextReq = JAXBContext.newInstance(LoanTopUpBalancApiReqDataDto.class);
            Marshaller marshallerRq = jaxbContextReq.createMarshaller();
            marshallerRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterRq = new StringWriter();
            marshallerRq.marshal(loanTopUpBalanceApiReqDto.getLoanTopUpBalanceApiReqDataDto(), stringWriterRq);
            String balanceUtumishiRq = stringWriterRq.toString();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime after7Days = now.plusDays(7);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String after7DaysFormated = after7Days.format(formatter);

            log.info("loan charges response is successfull i.e Product Code: {}", balanceUtumishiRq);

            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);

            if(fspByCode.isEmpty()) {
                return createResponseEntity(ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER, fspCode, mssId);
            }

            ClientRv client = clientRvRepository.findByCheckNumber(checkNumber);
            if(client == null) {
                return createResponseEntity(ResponseCodes.CHECK_NUMBER_NOT_EXIST, fspCode, mssId);
            }

            String lastDeductionDate = client.getLastDeductionDate();
            String loanEndDate =  client.getLoanEndDate();
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            ZonedDateTime zonedLastDeductionDateTime = ZonedDateTime.parse(lastDeductionDate, inputFormatter);
            LocalDateTime formattedlastDeductionDate = zonedLastDeductionDateTime.toLocalDateTime();

            ZonedDateTime zonedLoanEndDateDateTime = ZonedDateTime.parse(loanEndDate, inputFormatter);
            LocalDateTime formattedLoanEndDateDate = zonedLoanEndDateDateTime.toLocalDateTime();

            LoanTopUpPayOffBalanceApiRespDetailDto loanTopUpPayOffBalanceApiRespDetailDto = new LoanTopUpPayOffBalanceApiRespDetailDto();
            loanTopUpPayOffBalanceApiRespDetailDto.setLoanNumber(loanNumber);
            loanTopUpPayOffBalanceApiRespDetailDto.setFSPReferenceNumber(client.getFspReferenceNumber());
            loanTopUpPayOffBalanceApiRespDetailDto.setPaymentReferenceNumber(client.getPaymentReferenceNumber());
            loanTopUpPayOffBalanceApiRespDetailDto.setTotalPayoffAmount(client.getTotalPayoffAmount());
            loanTopUpPayOffBalanceApiRespDetailDto.setOutstandingBalance(client.getTotalOutstandingBalance());
            loanTopUpPayOffBalanceApiRespDetailDto.setFinalPaymentDate(after7DaysFormated);
            loanTopUpPayOffBalanceApiRespDetailDto.setLastDeductionDate(formattedlastDeductionDate.toString());
            loanTopUpPayOffBalanceApiRespDetailDto.setLastPayDate(after7DaysFormated);
            loanTopUpPayOffBalanceApiRespDetailDto.setEndDate(formattedLoanEndDateDate.toString());

            ApiReqHeaderDto apiReqHeaderDtoCC = new ApiReqHeaderDto();
            apiReqHeaderDtoCC.setFspCode(fspByCode.get().getFspCode());
            apiReqHeaderDtoCC.setMsgId(mssId);
            apiReqHeaderDtoCC.setMessageType(Constants.LOAN_TOP_UP_BALANCE_RESPONSE);
            apiReqHeaderDtoCC.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDtoCC.setSender(fspByCode.get().getFspName());

            LoanTopUpBalanceApiRespDataDto loanTopUpBalanceApiRespDataDto = new LoanTopUpBalanceApiRespDataDto();
            loanTopUpBalanceApiRespDataDto.setRequestHeader(apiReqHeaderDtoCC);
            loanTopUpBalanceApiRespDataDto.setLoanTopUpBalanceApiRespDetailDto(loanTopUpPayOffBalanceApiRespDetailDto);
            log.info("balance json payload: {}", loanTopUpPayOffBalanceApiRespDetailDto);

            JAXBContext jaxbContext2CC = JAXBContext.newInstance(LoanTopUpBalanceApiRespDataDto.class);
            Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
            marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterCC = new StringWriter();
            marshallerCC.marshal(loanTopUpBalanceApiRespDataDto, stringWriterCC);
            String xmlPayloadCC = stringWriterCC.toString();
            log.info("balance catalog i.e with fsp code: {}" ,xmlPayloadCC);

            JAXBContext jaxbContextSg = JAXBContext.newInstance(GenericApiReqDataDto.class);
            Unmarshaller unmarshallerSg = jaxbContextSg.createUnmarshaller();
            StringReader stringReaderSg = new StringReader(xmlPayloadCC);
            GenericApiReqDataDto genericApiReqDataDtoSg = (GenericApiReqDataDto) unmarshallerSg.unmarshal(stringReaderSg);
            String content = convertObjectToXml(genericApiReqDataDtoSg);

            String signatureSg = digitalSignature.createSignature(content);
            String finalXml = appendSignatureToXml2(content, signatureSg);

            // save balance
            saveTopupBalance(loanTopUpBalanceApiReqDetailDto, loanTopUpPayOffBalanceApiRespDetailDto);
            log.info("loan top up balance successfull :{}", finalXml);
            return ResponseEntity.ok(finalXml);

        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception occurred while processing loan top up pay off balance request {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    public void saveTopupBalance(LoanTopUpBalanceApiReqDetailDto loanTopUpBalanceApiReqDetailDto, LoanTopUpPayOffBalanceApiRespDetailDto loanTopUpPayOffBalanceApiRespDetailDto) {

        try {

            Balance balance = Balance.builder()
                    .checkNumber(loanTopUpBalanceApiReqDetailDto.getCheckNumber())
                    .loanNumber(loanTopUpBalanceApiReqDetailDto.getLoanNumber())
                    .firstName(loanTopUpBalanceApiReqDetailDto.getFirstName())
                    .lastName(loanTopUpBalanceApiReqDetailDto.getLastName())
                    .middleName(loanTopUpBalanceApiReqDetailDto.getMiddleName())
                    .voteCode(loanTopUpBalanceApiReqDetailDto.getVoteCode())
                    .voteName(loanTopUpBalanceApiReqDetailDto.getVoteName())
                    .deductionAmount(loanTopUpBalanceApiReqDetailDto.getDeductionAmount())
                    .deductionBalance(loanTopUpBalanceApiReqDetailDto.getDeductionBalance())
                    .paymentOption(loanTopUpBalanceApiReqDetailDto.getPaymentOption())
                    .fSPReferenceNumber(loanTopUpPayOffBalanceApiRespDetailDto.getFSPReferenceNumber())
                    .paymentReferenceNumber(loanTopUpPayOffBalanceApiRespDetailDto.getPaymentReferenceNumber())
                    .totalPayoffAmount(loanTopUpPayOffBalanceApiRespDetailDto.getTotalPayoffAmount())
                    .outstandingBalance(loanTopUpPayOffBalanceApiRespDetailDto.getOutstandingBalance())
                    .finalPaymentDate(loanTopUpPayOffBalanceApiRespDetailDto.getFinalPaymentDate())
                    .lastDeductionDate(loanTopUpPayOffBalanceApiRespDetailDto.getLastDeductionDate())
                    .lastPayDate(loanTopUpPayOffBalanceApiRespDetailDto.getLastPayDate())
                    .endDate(loanTopUpPayOffBalanceApiRespDetailDto.getEndDate())
                    .loanType(LoanType.TOPUP)
                    .build();

            balanceRepository.save(balance);

            log.info("balance json payload: {}", balance);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public ResponseDetailApiDto getUtumishiApiResponse(String response) throws Exception {
        log.info("Received Utumishi resposne: {}", response);
        JAXBContext jaxbContext = JAXBContext.newInstance(ResponseApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(response);
        ResponseApiDto responseApiDto = (ResponseApiDto) unmarshaller.unmarshal(stringReader);
        try {
            log.info("ResponseCode: {}", responseApiDto.getResponseDataDto().getResponseDetailDto().getResponseCode());
            return responseApiDto.getResponseDataDto().getResponseDetailDto();
        } catch (Exception e) {
            log.info("Exception caught: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<String> loanTakeOverBalance(String message) throws Exception {
        log.info("Received Take_Over balance request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(TakeOverBalanceApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        TakeOverBalanceApiReqDto takeOverBalanceApiReqDto = (TakeOverBalanceApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = takeOverBalanceApiReqDto.getTakeOverBalanceApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = takeOverBalanceApiReqDto.getTakeOverBalanceApiReqDataDto().getRequestHeader().getMsgId();
        String checkNumber = takeOverBalanceApiReqDto.getTakeOverBalanceApiReqDataDto().getTakeOverBalanceApiReqDetailDto().getCheckNumber();
        String loanNumber = takeOverBalanceApiReqDto.getTakeOverBalanceApiReqDataDto().getTakeOverBalanceApiReqDetailDto().getLoanNumber();
        TakeOverBalanceApiReqDetailDto takeOverBalanceApiReqDetailDto = takeOverBalanceApiReqDto.getTakeOverBalanceApiReqDataDto().getTakeOverBalanceApiReqDetailDto();
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime after7Days = now.plusDays(7);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String after7DaysFormated = after7Days.format(formatter);

            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);

            ClientRv client = clientRvRepository.findByCheckNumber(checkNumber);
            if(client == null) {
                return createResponseEntity(ResponseCodes.CHECK_NUMBER_NOT_EXIST, fspCode, mssId);
            }
            String lastDeductionDate = client.getLastDeductionDate();
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            ZonedDateTime zonedLastDeductionDateTime = ZonedDateTime.parse(lastDeductionDate, inputFormatter);
            LocalDateTime formattedlastDeductionDate = zonedLastDeductionDateTime.toLocalDateTime();

            TakeOverBalanceApiRespDetailDto takeOverBalanceApiRespDetailDto = new TakeOverBalanceApiRespDetailDto();
            takeOverBalanceApiRespDetailDto.setLoanNumber(loanNumber);
            takeOverBalanceApiRespDetailDto.setFspCode(fspCode);
            takeOverBalanceApiRespDetailDto.setFSPReferenceNumber(client.getFspReferenceNumber());
            takeOverBalanceApiRespDetailDto.setPaymentReferenceNumber(client.getPaymentReferenceNumber());
            takeOverBalanceApiRespDetailDto.setTotalPayoffAmount(client.getTotalPayoffAmount());
            takeOverBalanceApiRespDetailDto.setOutstandingBalance(client.getTotalOutstandingBalance());
            takeOverBalanceApiRespDetailDto.setFspBankAccount("BAYPORT ACCOUNT");
            takeOverBalanceApiRespDetailDto.setFspBankAccountName("BAYPORT FIANNACIAL SERVICE");
            takeOverBalanceApiRespDetailDto.setSwiftCode(client.getLoanNumber());
            takeOverBalanceApiRespDetailDto.setMnoChannels("0786329768");
            takeOverBalanceApiRespDetailDto.setFinalPaymentDate(after7DaysFormated);
            takeOverBalanceApiRespDetailDto.setLastDeductionDate(formattedlastDeductionDate.toString());
            takeOverBalanceApiRespDetailDto.setDeductionEndDate(formattedlastDeductionDate.toString());

            ApiReqHeaderDto apiReqHeaderDtoCC = new ApiReqHeaderDto();
            apiReqHeaderDtoCC.setFspCode(fspByCode.get().getFspCode());
            apiReqHeaderDtoCC.setMsgId(mssId);
            apiReqHeaderDtoCC.setMessageType(Constants.LOAN_TAKEOVER_BALANCE_RESPONSE);
            apiReqHeaderDtoCC.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDtoCC.setSender(fspByCode.get().getFspName());

            TakeOverBalanceApiRespDataDto takeOverBalanceApiRespDataDto = new TakeOverBalanceApiRespDataDto();
            takeOverBalanceApiRespDataDto.setRequestHeader(apiReqHeaderDtoCC);
            takeOverBalanceApiRespDataDto.setTakeOverBalanceApiRespDetailDto(takeOverBalanceApiRespDetailDto);
            log.info("takeover balance json payload:{}", takeOverBalanceApiRespDetailDto);

            JAXBContext jaxbContext2CC = JAXBContext.newInstance(TakeOverBalanceApiRespDto.class);
            Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
            marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterCC = new StringWriter();
            marshallerCC.marshal(takeOverBalanceApiRespDataDto, stringWriterCC);
            String xmlPayloadCC = stringWriterCC.toString();
            log.info("takeover balance catalog i.e with fsp code:{}", xmlPayloadCC);

            JAXBContext jaxbContextSg = JAXBContext.newInstance(GenericApiReqDataDto.class);
            Unmarshaller unmarshallerSg = jaxbContextSg.createUnmarshaller();
            StringReader stringReaderSg = new StringReader(xmlPayloadCC);
            GenericApiReqDataDto genericApiReqDataDtoSg = (GenericApiReqDataDto) unmarshallerSg.unmarshal(stringReaderSg);

            String content = convertObjectToXml(genericApiReqDataDtoSg);

            String signatureSg = digitalSignature.createSignature(content);

            String finalXml = appendSignatureToXml2(content, signatureSg);
            log.info("Final Xml Request :{}", finalXml);

            // save balance
            saveTakeOverBalance(takeOverBalanceApiReqDetailDto, takeOverBalanceApiRespDetailDto);
            return ResponseEntity.ok(finalXml);

        } catch (Exception e) {
            log.info("Exception occurred while processing take over balance request :{}", e.getMessage());
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }

    public void saveTakeOverBalance(TakeOverBalanceApiReqDetailDto takeOverBalanceApiReqDetailDto, TakeOverBalanceApiRespDetailDto takeOverBalanceApiRespDetailDto) {
        try {
            Takeoverbalance balance = Takeoverbalance.builder()
                    .checkNumber(takeOverBalanceApiReqDetailDto.getCheckNumber())
                    .loanNumber(takeOverBalanceApiReqDetailDto.getLoanNumber())
                    .fspCode(takeOverBalanceApiRespDetailDto.getFspCode())
                    .firstName(takeOverBalanceApiReqDetailDto.getFirstName())
                    .lastName(takeOverBalanceApiReqDetailDto.getLastName())
                    .middleName(takeOverBalanceApiReqDetailDto.getMiddleName())
                    .voteCode(takeOverBalanceApiReqDetailDto.getVoteCode())
                    .voteName(takeOverBalanceApiReqDetailDto.getVoteName())
                    .deductionAmount(takeOverBalanceApiReqDetailDto.getDeductionAmount())
                    .deductionAmount(takeOverBalanceApiReqDetailDto.getDeductionAmount())
                    .deductionCode(takeOverBalanceApiReqDetailDto.getDeductionCode())
                    .deductionBalance(takeOverBalanceApiReqDetailDto.getDeductionBalance())
                    .paymentOption(takeOverBalanceApiReqDetailDto.getPaymentOption())
                    .fSPReferenceNumber(takeOverBalanceApiRespDetailDto.getFSPReferenceNumber())
                    .paymentReferenceNumber(takeOverBalanceApiRespDetailDto.getPaymentReferenceNumber())
                    .totalPayoffAmount(takeOverBalanceApiRespDetailDto.getTotalPayoffAmount())
                    .outstandingBalance(takeOverBalanceApiRespDetailDto.getOutstandingBalance())
                    .fspBankAccount(takeOverBalanceApiRespDetailDto.getFspBankAccount())
                    .fspBankAccountName(takeOverBalanceApiRespDetailDto.getFspBankAccountName())
                    .swiftCode(takeOverBalanceApiRespDetailDto.getSwiftCode())
                    .mnoChannels(takeOverBalanceApiRespDetailDto.getMnoChannels())
                    .finalPaymentDate(takeOverBalanceApiRespDetailDto.getFinalPaymentDate())
                    .lastDeductionDate(takeOverBalanceApiRespDetailDto.getLastDeductionDate())
                    .deductionEndDate(takeOverBalanceApiRespDetailDto.getDeductionEndDate())
                    .build();

            takeoverBalanceRepository.save(balance);

            log.info("takeover balance json payload: {}", balance);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> takeOverPaymentNotification(String message) throws Exception {
        log.info("Received take over payment Notification request: {}", message);

        JAXBContext jaxbContext = JAXBContext.newInstance(PaymentNotificationApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        PaymentNotificationApiReqDto paymentNotificationApiReqDto = (PaymentNotificationApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = paymentNotificationApiReqDto.getPaymentNotificationApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = paymentNotificationApiReqDto.getPaymentNotificationApiReqDataDto().getRequestHeader().getMsgId();
        String loanNumber = paymentNotificationApiReqDto.getPaymentNotificationApiReqDataDto().getPaymentNotificationApiReqDetailDto().getLoanNumber();
        PaymentNotificationApiReqDetailDto paymentNotificationApiReqDetailDto =  paymentNotificationApiReqDto.getPaymentNotificationApiReqDataDto().getPaymentNotificationApiReqDetailDto();
        try {

            //loan pay off operation
            Optional<FinancialServiceProvider> fsp =
                    financialServiceProviderRepository.findByFspCode(fspCode);

            if (fsp.isEmpty()) {
                log.info("invalid fsp code, reason: no such fsp with code: " + fspCode);
                return createResponseEntity(ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER, fspCode, mssId);
            }

            Takeoverbalance takeoverbalance = takeoverBalanceRepository.findLatestBalanceByLoannumber(loanNumber);
            if (takeoverbalance == null) {
                log.info("No takeover balance found with loannumber: {}", loanNumber);
                return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
            }

            saveTakeoverPayment(paymentNotificationApiReqDetailDto, takeoverbalance);

            log.info("response is successfull");
            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            log.info(e.getMessage());
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR, fspCode, mssId);
        }
    }

    public void saveTakeoverPayment(PaymentNotificationApiReqDetailDto paymentNotificationApiReqDetailDto, Takeoverbalance takeoverbalance) {
        try {
            Takeoverpayment takeoverpayment = Takeoverpayment.builder()
                    .applicationNumber(paymentNotificationApiReqDetailDto.getApplicationNumber())
                    .loanNumber(paymentNotificationApiReqDetailDto.getLoanNumber())
                    .reason(paymentNotificationApiReqDetailDto.getReason())
                    .fspReferenceNumber(paymentNotificationApiReqDetailDto.getFspReferenceNumber())
                    .paymentReferenceNumber(paymentNotificationApiReqDetailDto.getPaymentReferenceNumber())
                    .totalPayoffAmount(paymentNotificationApiReqDetailDto.getTotalPayoffAmount())
                    .paymentDate(paymentNotificationApiReqDetailDto.getPaymentDate())
                    .paymentAdvice(paymentNotificationApiReqDetailDto.getPaymentAdvice())
                    .paymentAdviceAttachment(paymentNotificationApiReqDetailDto.getPaymentAdviceAttachment())
                    .takeoverBalanceId(takeoverbalance.getId())
                    .requestType(RequestType.takeover_payment)
                    .build();

            takeoverPaymentRepository.save(takeoverpayment);

            log.info("takeover payment successful saved: {}", takeoverpayment);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> paymentAcknoledgementNotification(Long paymentId, PaymentAcknoledgeDto paymentAcknoledgeDto) throws Exception {
        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }
        try {
            Optional<Takeoverpayment> takeoverpaymentOpt = takeoverPaymentRepository.findById(paymentId);
            if (takeoverpaymentOpt.isEmpty()) {
                log.info("no takeover payment found with id: {}", paymentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No takeover payment with id: " + paymentId);
            }
            Takeoverpayment takeoverpayment = takeoverpaymentOpt.get();

            PaymentAcknoledgeApiDetailDto paymentAcknoledgeApiDetailDto = new PaymentAcknoledgeApiDetailDto();
            paymentAcknoledgeApiDetailDto.setApplicationNumber(takeoverpayment.getApplicationNumber());
            paymentAcknoledgeApiDetailDto.setRemarks(paymentAcknoledgeDto.getRemarks());
            paymentAcknoledgeApiDetailDto.setLoanNumber(takeoverpayment.getLoanNumber());
            paymentAcknoledgeApiDetailDto.setPaymentStatus(paymentAcknoledgeDto.getPaymentStatus().name());

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.PAYMENT_ACKNOWLEDGMENT_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());

            PaymentAcknoledgeApiDataDto paymentAcknoledgeApiDataDto = new PaymentAcknoledgeApiDataDto();
            paymentAcknoledgeApiDataDto.setRequestHeader(apiReqHeaderDto);
            paymentAcknoledgeApiDataDto.setMessageDetails(paymentAcknoledgeApiDetailDto);

            log.info("Payment Acknoledge notification json in data level payload: " + paymentAcknoledgeApiDataDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(PaymentAcknoledgeApiDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(paymentAcknoledgeApiDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();

            log.info("payment acknoledgement notification xml payload: " + xmlPayload);

            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            log.info("payment acknoledgement notification response from ess" + responseEntity);
            ResponseDetailApiDto responseDetailApiDto = getUtumishiApiResponse(String.valueOf(responseEntity));

            if (responseDetailApiDto.getResponseCode() == "8000") {
                // save payment acknowledgement
                takeoverpayment.setAcknoledgeRemarks(paymentAcknoledgeApiDetailDto.getRemarks());
                takeoverpayment.setPaymentStatus(PaymentStatus.valueOf(paymentAcknoledgeApiDetailDto.getPaymentStatus()));
                takeoverpayment.setRequestType(RequestType.takeover_settlement);
                takeoverPaymentRepository.save(takeoverpayment);
                log.info("takeover payment acknowledgement saved successfully");

                return ResponseEntity.status(HttpStatus.OK).body("PAYMENT ACKNOLEDGEMENT SUCCESSFUL");
            }else {
                return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("PAYMENT ACKNOLEDGEMENT FAILED, WITH CODE :" + responseDetailApiDto.getResponseCode());
            }
        } catch (Exception e) {
            log.info("takeover payment acknowledgement failed with :{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("PAYMENT ACKNOLEDGEMENT FAILED");
        }

    }



























    private ResponseEntity<String> createResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
        String description = ResponseCodes.getDescription(responseCode);
        ResponseEntity<String> responseEntity = utils.messageResponseWrapperToUtumishi(responseCode, description, fspCode, mssId, Constants.UTUMISHI_RESPONSE);
        log.info("Response for FSP Code {}: {}", fspCode, responseEntity.getBody());
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> loanTopUp(String message) throws Exception {

        log.info("Received loan TopUp request: {}", message);
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

            log.info("prepared request sent to the queue: " + loanUtumishiRq);

            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception occurred while processing loan top up {}");
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

        log.info("Final Xml Request {}:", finalXml);

        // Send the final XML to the specified API endpoint
        String response = sendXmlToApi(finalXml);
        log.info("Final CURL xml request sent to utumishi {}:", response);
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

        log.info("Final Xml Request {}:", finalXml);

        // Send the final XML to the specified API endpoint
        String response = sendXmlToApi(finalXml);
        log.info("Final CURL xml request sent to utumishi {}:", response);
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
            log.info("Content to be signed: " + content);

            // Generate the digital signature
            String signature = digitalSignature.createSignature(content);
            log.info("Generated Signature: " + signature);

            // Create the final XML with the signature
            String finalXml = appendSignatureToXml2(content, signature);
            System.out.println("Final XML: " + finalXml);

            // Log the final XML
            log.info("Final Xml Request {}:", finalXml);

            // Send the final XML to the specified API endpoint
            String response = sendXmlToApi(finalXml);
            log.info("Final CURL xml request sent to utumishi {}:", response);

            // Return the API response
            return ResponseEntity.ok(response);

        } catch (JAXBException e) {
            log.error("Error unmarshalling XML message: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid XML format");
        } catch (SignatureException e) {
            log.error("Error generating digital signature: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating signature");
        } catch (IOException e) {
            log.error("Error sending XML to API: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error communicating with API");
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
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

        log.info("Final Xml Request {}:", finalXml);

        // Send the final XML to the specified API endpoint

        log.info("Final CURL SUPPOSED request sent to utumishi***** {}:", finalXml);
        return ResponseEntity.ok(finalXml);

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
        log.info("Final CURL XML request sent to Utumishi: {}", xml);

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
        log.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

//for takeover balance
    private String balanceTakeOverXmlSendToApi(String xml) throws Exception {
        // Log the final XML request
        log.info("Final CURL XML request sent to Utumishi: {}", xml);

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
        log.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    //for loan charge api to portal
    private String cahrgeXmlSendToApi(String xml) throws Exception {
        // Log the final XML request
        log.info("Final CURL XML request sent to Utumishi: {}", xml);

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
        log.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    private String sendXmlToApi(String xml) throws Exception {
        log.info("Final CURL XML request sent to Utumishi: {}", xml);

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
        log.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    private static LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


    public static String cleanMessage(String xmlInput) {
        String decodedXml = StringEscapeUtils.unescapeHtml4(xmlInput);

        String partiallyCleanedXml = decodedXml.replaceAll("&#13;", "");

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
        log.info("loan liquidization notification" + message);

        String mssId = generateMessageId();
        List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
        FinancialServiceProvider fsp = null;
        if (!fspList.isEmpty()) {
            fsp = fspList.get(0);
        }
        try {
            LoanLiquidizationApiReqDetailDto loanLiquidizationApiReqDetailDto = new LoanLiquidizationApiReqDetailDto();
//            loanLiquidizationApiReqDetailDto.setApplicationNumber(liquidizationNotificationDto.getApplicationNumber());
//            loanLiquidizationApiReqDetailDto.setLoanNumber(liquidizationNotificationDto.getLoanrequestid());
//            loanLiquidizationApiReqDetailDto.setRemarks(liquidizationNotificationDto.getReason());

            log.info("loanliquidization json in detail level payload: " + loanLiquidizationApiReqDetailDto);

            ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
            apiReqHeaderDto.setFspCode(fsp.getFspCode());
            apiReqHeaderDto.setMsgId(mssId);
            apiReqHeaderDto.setMessageType(Constants.LOAN_LIQUIDATION_NOTIFICATION);
            apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
            apiReqHeaderDto.setSender(fsp.getFspName());

            LoanLiquidizationApiReqDataDto loanLiquidizationApiReqDataDto = new LoanLiquidizationApiReqDataDto();
            loanLiquidizationApiReqDataDto.setRequestHeader(apiReqHeaderDto);
            loanLiquidizationApiReqDataDto.setLoanLiquidizationApiReqDetailDto(loanLiquidizationApiReqDetailDto);

            log.info("loan liquidization notification json in data level payload: " + loanLiquidizationApiReqDataDto);

            JAXBContext jaxbContext2 = JAXBContext.newInstance(LoanLiquidizationApiReqDataDto.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(loanLiquidizationApiReqDataDto, stringWriter);
            String xmlPayload = stringWriter.toString();
            System.out.println("liquidization xml payload: " + xmlPayload);

            log.info("liquidization xml payload: " + xmlPayload);
            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            log.info("liquidization sent to be signed" + xmlPayload);
            log.info("liquidization response from ess" + responseEntity);

            return responseEntity;

        } catch (Exception e) {
            log.info("fail to send liquidization to utumishi ");
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

        log.info("Received take over loan application request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(TakeOverLoanApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        System.out.println("Phase 0 ***************");
        TakeOverLoanApiReqDto takeOverLoanApiReqDto = (TakeOverLoanApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getRequestHeader().getMsgId();
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
                log.info("invalid product code i.e productCode: " + takeOverLoanApiReqDto.getTakeOverLoanApiReqDataDto().getTakeOverLoanApiReqDetailDto().getProductCode());
                return createResponseEntity(ResponseCodes.INVALID_PRODUCT_CODE, fspCode, mssId);
            }

            System.out.println("Phase 2 ***************");

            System.out.println("Phase 3 ***************");

            Optional<Loan> loanByApplicationNumber = loanRepository.findFirstByApplicationNumber(loanApplicationNumber);

            if (loanByApplicationNumber.isPresent()) {
                log.info("Duplicate application number: " + loanApplicationNumber);
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
            log.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue (use the same Queue with Loan Request)


            log.info("request sent to the queue: " + loanUtumishiRq);

            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception occurred while processing take over loan registration {}");
            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
        }
    }


    @Override
    public ResponseEntity<String> extractTakeoverRequests() throws Exception {
        return null;
    }


    //                       RESTRUCTURE FEATURES
    // RESTRUCTURE BALANCE REQUEST AND RESPONSE
    @Override
    public ResponseEntity<String> loanRestructureBalance(String message) throws Exception {
        log.info("Received Restructure balance request: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(RestructureBalanceApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        RestructureBalanceApiReqDto restructureBalanceApiReqDto = (RestructureBalanceApiReqDto) unmarshaller.unmarshal(stringReader);
//        String fspCode = restructureBalanceApiReqDto.getRestructureBalanceApiReqDataDto().getRequestHeader().getFspCode();
//        String mssId = restructureBalanceApiReqDto.getRestructureBalanceApiReqDataDto().getRequestHeader().getMsgId();
//        String loanNo = restructureBalanceApiReqDto.getRestructureBalanceApiReqDataDto().getRestructureBalanceApiReqDetailDto().getLoanNumber();
        try {
            //convert balance utumishi request data into xml(string)
            JAXBContext jaxbContextReq = JAXBContext.newInstance(RestructureBalanceApiReqDataDto.class);
            Marshaller marshallerRq = jaxbContextReq.createMarshaller();
            marshallerRq.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterRq = new StringWriter();
//            marshallerRq.marshal(restructureBalanceApiReqDto.getRestructureBalanceApiReqDataDto(), stringWriterRq);
            String balanceUtumishiRq = stringWriterRq.toString();
            System.out.println("restructure balance request sent to api: " + balanceUtumishiRq);

            log.info("restructure balance response is successfull i.e Product Code: {}" + balanceUtumishiRq);

            // send and receive balance messages thr: Api (share with top up balance messages)
            String apiResponse = balanceRestructureXmlSendToApi(balanceUtumishiRq);
            log.info("restructure balance response Message from Portal Api: {}", apiResponse);

            JAXBContext jaxbContextCC = JAXBContext.newInstance(RestructureBalanceApiRespDataDto.class);
            Unmarshaller unmarshallerCC = jaxbContextCC.createUnmarshaller();
            StringReader stringReaderCC = new StringReader(apiResponse);
            RestructureBalanceApiRespDataDto balancePortalDto = (RestructureBalanceApiRespDataDto) unmarshallerCC.unmarshal(stringReaderCC);

//            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);
//
//            String sender = fspByCode.get().getFspShortName();

            JAXBContext jaxbContext2CC = JAXBContext.newInstance(RestructureBalanceApiRespDto.class);
            Marshaller marshallerCC = jaxbContext2CC.createMarshaller();
            marshallerCC.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriterCC = new StringWriter();
            marshallerCC.marshal(balancePortalDto, stringWriterCC);
            String xmlPayloadCC = stringWriterCC.toString();
            System.out.println("restructure balance xml payload: " + xmlPayloadCC);

            log.info("restructure balance catalog i.e with fsp code:" + xmlPayloadCC);

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

            log.info("Final Xml Request {}:", finalXml);

            log.info("Final CURL SUPPOSED request sent to utumishi***** {}:", finalXml);

            log.info("restructure balance successfull " + finalXml);

            return ResponseEntity.ok(finalXml);

        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception occurred while processing take over balance request {}");
//            return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, fspCode, mssId);
            return null;
        }
    }

    @Override
    public ResponseEntity<String> loanRestructureAffordability(String message) throws Exception{
        log.info("Received loan charges request from utumishi: {}", message);

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
                log.info("invalid product code i.e Product Code: {}"+ affordabilityApiDto.getAffordabilityApiReqDataDto().getMessageDetails().getProductCode());
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

            log.info("affordability response is successfull i.e Product Code: {}" + chargeUtumishiRq);

            String apiResponse = affordabilityXmlSendToApi(chargeUtumishiRq);
            log.info("Response Message from Portal Api: {}", apiResponse);

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
            log.info("affordability catalog i.e with fsp code:" + xmlPayloadCC);

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

            log.info("Final Xml Request {}:", finalXml);

            // Send the final XML to the specified API endpoint
            log.info("Final CURL SUPPOSED request sent to utumishi***** {}:", finalXml);

            return ResponseEntity.ok(finalXml);
        } catch(Exception e){
            return createResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR,fspCode, mssId);
        }
    }

    //for restructure balance
    private String balanceRestructureXmlSendToApi(String xml) throws Exception {
        // Log the final XML request
        log.info("Final CURL XML request sent to Utumishi: {}", xml);

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
        log.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    //for affordability api to portal
    private String affordabilityXmlSendToApi(String xml) throws Exception {
        // Log the final XML request
        log.info("Final CURL XML request sent to Utumishi: {}", xml);

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
        log.info("Response Code: {}, Response Message: {}", responseCode, responseMessage);

        return responseMessage;
    }

    @Override
    public ResponseEntity<String> loanRestructureRequest(String message) throws Exception {
        log.info("Received restructure application request: {}", message);
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
                log.info("invalid product code i.e productCode: " + restructureApiReqDto.getRestructureApiReqDataDto().getRestructureReqDetailDto().getProductCode());
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

            log.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue


            log.info("request sent to the queue: " + loanUtumishiRq);

            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception occurred while processing loan restructure {}");
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
        log.info("Received loan restructure rejection request: {}", message);

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

            log.info("prepared request sent to the queue: " + loanUtumishiRq);

            // publish to send to the Queue


            log.info("request restructure rejection sent to the queue: " + loanUtumishiRq);


            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

        } catch (Exception e) {
            log.error("Error processing loan restructure request", e);
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

            log.info("restructure xml payload: " + xmlPayload);

            ResponseEntity<String> responseEntity = generateRequestSignature2(xmlPayload);

            log.info("restructure response" + responseEntity);

            return responseEntity;

        } catch (Exception e) {
            log.info("fail to send loan initial notification to utumishi");
            return null;
        }
    }

}