package com.example.Project.Management.bayport.apis.controller;

import com.example.Project.Management.bayport.apis.dto.GenericApiReqDataDtoWrapper;
import com.example.Project.Management.bayport.charge.service.ChargeService;
import com.example.Project.Management.bayport.common.DigitalSignature;
import com.example.Project.Management.bayport.disbursement.service.DisbursementService;
import com.example.Project.Management.bayport.loan.service.LoanService;
import com.example.Project.Management.bayport.product.service.ProductDecommissionService;
import com.example.Project.Management.bayport.product.service.ProductService;
import com.example.Project.Management.bayport.setup.service.UtumishiResponseService;
import com.example.Project.Management.bayport.util.ResponseCodes;
import com.example.Project.Management.bayport.util.Utils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringReader;
import java.io.StringWriter;

import static com.example.Project.Management.bayport.util.Constant.UTUMISHI_RESPONSE;


@RestController
@RequestMapping("/api/loans/")
@Log4j2
public class UtumishiController {

    //TODO: THIS ENDPOINT FOR COMMUNICATION FROM UTUMISHI SYSTEM

    @Autowired
    LoanService loanService;

    private final ChargeService chargeService;

    @Autowired
    ProductService productService;

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


    public UtumishiController(LoanService loanService, ChargeService chargeService, ProductService productService, ProductDecommissionService productDecommissionService, DigitalSignature digitalSignature, UtumishiResponseService utumishiResponseService) {
        this.loanService = loanService;
        this.chargeService = chargeService;
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
            case "LOAN_CHARGES_REQUEST":
               return chargeService.calculateLoanCharges(message);

            case "TOP_UP_PAY_0FF_BALANCE_REQUEST":
                return loanService.loanTopUpPayOffBalance(message);

            case "LOAN_CANCELLATION_NOTIFICATION":
                return loanService.loanCancellation(message);

            case "LOAN_OFFER_REQUEST":
                return loanService.loanRegistration(message);

            case "TOP_UP_OFFER_REQUEST":
                return loanService.loanRegistration(message);

            case "TAKEOVER_EMPLOYEE_DETAILS_TO_FSP":
                return null;

            case "LOAN_FINAL_APPROVAL_NOTIFICATION":
                return loanService.loanFinalApprovalNotification(message);

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