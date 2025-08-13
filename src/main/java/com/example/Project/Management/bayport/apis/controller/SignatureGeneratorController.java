package com.dflex.emkopo.api.controller;

import com.dflex.emkopo.api.dto.GenericApiReqDataDtoWrapper;
import com.dflex.emkopo.app.borrower.service.BorrowerService;
import com.dflex.emkopo.app.loan.service.LoanService;
import com.dflex.emkopo.app.product.service.ProductService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringReader;

@RestController
@RequestMapping("/dflex_generate_signature")
public class SignatureGeneratorController {

    //TODO: THIS ENPOINT FOR ALTERNATIVE SEND CURL FAIL MESSAGES TO UTUMISHI SYSTEM

    @Autowired
    LoanService loanService;

    @Autowired
    ProductService productService;

    @Autowired
    BorrowerService borrowerService;

    public SignatureGeneratorController(LoanService loanService) {
        this.loanService = loanService;
    }


@PostMapping("/message")
public ResponseEntity<String> handleMessage(@RequestBody String message)throws Exception {

    JAXBContext jaxbContext = JAXBContext.newInstance(GenericApiReqDataDtoWrapper.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    StringReader stringReader = new StringReader(message);
    GenericApiReqDataDtoWrapper genericApiReqDataDtoWrapper = (GenericApiReqDataDtoWrapper) unmarshaller.unmarshal(stringReader);
    String messageType = genericApiReqDataDtoWrapper.getRequestData().getRequestHeader().getMessageType();
//        String messageType = genericApiReqDataDto.getRequestHeader().getMessageType();

    switch (messageType) {

        case "REQUEST_DIGITAL_SIGNATURE_CREATE":
            return loanService.generateRequestSignature(message);

        case "PRODUCT_DETAIL":
            return loanService.generateRequestSignature(message);

        case "LOAN_CHARGES_REQUEST":
            return loanService.generateRequestSignature(message);

        case "LOAN_CHARGES_RESPONSE":
            return loanService.generateRequestSignature(message);

        case "PAY_0FF_BALANCE_REQUEST":
            return loanService.generateRequestSignature(message);

        case "TOP_UP_PAY_0FF_BALANCE_REQUEST":
            return loanService.generateRequestSignature(message);

        case "FULL_REPAYMENT_0FF_BALANCE_REQUEST":
            return loanService.generateRequestSignature(message);

        case "LOAN_CANCELLATION_NOTIFICATION":
            return loanService.generateRequestSignature(message);

        case "ACCOUNT_VALIDATION":
            return loanService.generateRequestSignature(message);

        case "LOAN_OFFER_REQUEST":
            return loanService.generateRequestSignature(message);

        case "PRODUCT_DECOMMISSION":
            return loanService.generateRequestSignature(message);

        case "TOP_UP_OFFER_REQUEST":
            return loanService.generateRequestSignature(message);

        case "LOAN_INITIAL_APPROVAL_NOTIFICATION":
            return loanService.generateRequestSignature(message);

        case "TAKEOVER_EMPLOYEE_DETAILS_TO_FSP":
            return loanService.generateRequestSignature(message);

        case "LOAN_FINAL_APPROVAL_NOTIFICATION":
            return loanService.generateRequestSignature(message);

        case "LOAN_DISBURSEMENT_NOTIFICATION":
            return loanService.generateRequestSignature(message);

        case "LOAN_DISBURSEMENT_FAILURE_NOTIFICATION":
            return loanService.generateRequestSignature(message);

        case "LOAN_LIQUIDATION_NOTIFICATION":
            return loanService.generateRequestSignature(message);

        case "FSP_BRANCHES":
            return loanService.generateRequestSignature(message);

        case "LOAN_RESTRUCTURE_REQUEST":
            return  loanService.loanRestructureRequest(message);

        case "LOAN_RESTRUCTURE_REJECTION":
            return  loanService.loanRestructureRejection(message);




        default:
            return ResponseEntity.badRequest().body("Invalid message type");
    }
}

}