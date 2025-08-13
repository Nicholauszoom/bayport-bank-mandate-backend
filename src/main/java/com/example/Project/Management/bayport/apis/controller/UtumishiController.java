package com.dflex.emkopo.api.controller;

import com.dflex.emkopo.api.consumer.ProductRegistrationApiConsumer;
import com.dflex.emkopo.api.dto.GenericApiReqDataDto;
import com.dflex.emkopo.api.dto.GenericApiReqDataDtoWrapper;
import com.dflex.emkopo.app.borrower.service.BorrowerService;
import com.dflex.emkopo.app.ess_accounts.service.AccountsService;
import com.dflex.emkopo.app.loan.dto.registration.LoanRegistrationApiReqDto;
import com.dflex.emkopo.app.loan.service.LoanService;
import com.dflex.emkopo.app.product.service.ProductDecommissionService;
import com.dflex.emkopo.app.product.service.ProductService;
import com.dflex.emkopo.app.setup.service.UtumishiResponseService;
import com.dflex.emkopo.commons.utils.DigitalSignature;
import com.dflex.emkopo.commons.utils.ResponseCodes;
import com.dflex.emkopo.commons.utils.Utils;
import com.dflex.emkopo.disbursement.service.DisbursementService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.StringReader;

import static com.dflex.emkopo.commons.utils.Constants.*;
import java.io.StringWriter;

@RestController
@RequestMapping("/api/loans/")
@Log4j2
public class UtumishiController {

    //TODO: THIS ENDPOINT FOR COMMUNICATION FROM UTUMISHI SYSTEM

    @Autowired
    LoanService loanService;

    @Autowired
    ProductService productService;

    @Autowired
    AccountsService accountsService;

    @Autowired
   ProductDecommissionService productDecommissionService;

    DisbursementService disbursementService;

    @Autowired
    UtumishiResponseService utumishiResponseService;


    @Autowired
    Utils utils;

    String publicKeyPass=null;

    String publicKeyAlias=null;

    @Value("${dflex.public.key.path}")
    private String publicKeyFilePath;

    private static final Logger logger = LogManager.getLogger(UtumishiController.class);

    @Autowired
    private DigitalSignature digitalSignature; // Inject DigitalSignature bean


    public UtumishiController(LoanService loanService, ProductService productService,  ProductDecommissionService productDecommissionService, DigitalSignature digitalSignature, UtumishiResponseService utumishiResponseService) {
        this.loanService = loanService;
        this.productService = productService;
        this.productDecommissionService = productDecommissionService;
        this.digitalSignature = digitalSignature;
        this.utumishiResponseService = utumishiResponseService;
    }

    private ResponseEntity<String> createResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
        String description = ResponseCodes.getDescription(responseCode);

        ResponseEntity<String> responseEntity = utils.messageResponseWrapperToUtumishi(responseCode, description, fspCode,
                mssId, UTUMISHI_RESPONSE);
        logger.info("Response for FSP Code {}: {}", fspCode, responseEntity.getBody());
        return responseEntity;
    }


    @PostMapping("/message")
    public ResponseEntity<String> handleMessage(@RequestBody String message) throws Exception {
        // Extract signature and content from the incoming message
        JAXBContext jaxbContext = JAXBContext.newInstance(GenericApiReqDataDtoWrapper.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        GenericApiReqDataDtoWrapper genericApiReqDataDtoWrapper = (GenericApiReqDataDtoWrapper) unmarshaller.unmarshal(stringReader);



        System.out.println("resposne from utumishi pass here "+ message);
        String messageType = genericApiReqDataDtoWrapper.getRequestData().getRequestHeader().getMessageType();

        switch (messageType) {
            case "PRODUCT_DETAIL":
                return productService.saveProduct(message);

            case "PRODUCT_DECOMMISSION":
                return productDecommissionService.productDecommission(message);

            case "LOAN_CHARGES_REQUEST":
               return loanService.calculateLoanCharges(message);

            case "PAY_0FF_BALANCE_REQUEST":
                return loanService.loanPayOffReq(message);

            case "TOP_UP_PAY_0FF_BALANCE_REQUEST":
                return loanService.loanTopUpPayOffBalance(message);

            case "FULL_REPAYMENT_0FF_BALANCE_REQUEST":
                return loanService.fullPaymentPayOffReq(message);

            case "LOAN_CANCELLATION_NOTIFICATION":
                return loanService.loanCancellation(message);

            case "ACCOUNT_VALIDATION":
                return accountsService.accountValidation(message);

            case "LOAN_OFFER_REQUEST":
                return loanService.loanRegistration(message);

            case "TOP_UP_OFFER_REQUEST":
                return loanService.loanRegistration(message);

            case "TAKEOVER_EMPLOYEE_DETAILS_TO_FSP":
                return loanService.loanTakeOver(message);

            case "LOAN_FINAL_APPROVAL_NOTIFICATION":
                return loanService.loanFinalApprovalNotification(message);

            case "LOAN_DISBURSEMENT_NOTIFICATION":
                return disbursementService.saveLoanDisbursement(message);

            case "LOAN_DISBURSEMENT_FAILURE_NOTIFICATION":
                return disbursementService.saveLoanDisbursementFailure(message);

            case "RESPONSE":
                return utumishiResponseService.saveUtumishiResponse(message);

            case "TAKEOVER_PAY_OFF_BALANCE_REQUEST":
                return loanService.loanTakeOverBalance(message);

            case "LOAN_TAKEOVER_OFFER_REQUEST":
                return loanService.takeOverLoanRegistration(message);

            case "TAKEOVER_PAYMENT_NOTIFICATION":
                return loanService.takeOverPaymentNotification(message);

            case "TAKEOVER_BALANCE_REQUEST_LOG":
                return  loanService.extractTakeoverRequests();


            case "LOAN_RESTRUCTURE_BALANCE_REQUEST":
                return  loanService.loanRestructureBalance(message);

            case "LOAN_RESTRUCTURE_AFFORDABILITY_REQUEST":
                return  loanService.loanRestructureAffordability(message);

            case "LOAN_RESTRUCTURE_REQUEST":
                return  loanService.loanRestructureRequest(message);

            case "LOAN_RESTRUCTURE_REJECTION":
                return  loanService.loanRestructureRejection(message);

            default:
                return ResponseEntity.badRequest().body("Invalid message type");
        }


    }

    private String convertObjectToXml(Object obj) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        // Create JAXBContext with the class type of the content
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        // Configure marshaller to omit the XML declaration
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        // Marshal the object content to XML string
        marshaller.marshal(obj, stringWriter);
        return stringWriter.toString();
    }
}