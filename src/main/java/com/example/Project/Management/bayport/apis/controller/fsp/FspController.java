package com.example.Project.Management.bayport.apis.controller.fsp;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
import com.example.Project.Management.bayport.disbursement.dto.LoanDisbursement.LoanDisburseNotApiDataDto;
import com.example.Project.Management.bayport.disbursement.dto.LoanDisbursement.LoanDisburseNotApiDetailDto;
import com.example.Project.Management.bayport.disbursement.dto.LoanDisbursement.LoanDisburseNotApiDto;
import com.example.Project.Management.bayport.disbursement.service.DisbursementService;
import com.example.Project.Management.bayport.loan.service.LoanService;
import com.example.Project.Management.bayport.product.dto.bayport.BayPortProductApiReqDetailDto;
import com.example.Project.Management.bayport.product.dto.bayport.BayportProductApiReqDataDto;
import com.example.Project.Management.bayport.product.dto.bayport.BayportproductApiReqDto;
import com.example.Project.Management.bayport.product.dto.decommission.ProductDecommisionApiReqDataDto;
import com.example.Project.Management.bayport.product.dto.decommission.ProductDecommissionApiReqDto;
import com.example.Project.Management.bayport.product.dto.decommission.ProductDecommissionsApiReqDetailDto;
import com.example.Project.Management.bayport.product.service.ProductDecommissionService;
import com.example.Project.Management.bayport.product.service.ProductService;
import com.example.Project.Management.bayport.product.service.ProductServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/fsp/bayport/")
public class FspController {

    //TODO: this end-point is used for communication with DGBAY BAYPORT system

    @Autowired
    private LoanService loanService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDecommissionService productDecommissionService;

    @Autowired
    private DisbursementService disbursementService;


    Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());


    public FspController(LoanService loanService, ProductService productService, DisbursementService disbursementService) {
        this.loanService = loanService;
        this.productService = productService;
        this.disbursementService = disbursementService;
    }

    @PostMapping("/message")
    public ResponseEntity<String> handleMessage(@RequestBody String message,@RequestHeader("msgId") String msgId,
                                                @RequestHeader("sender") String sender,
                                                @RequestHeader("receiver") String receiver,
                                                @RequestHeader("fspCode") String fspCode,
                                                @RequestHeader("messageType") String messageType) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        switch (messageType) {
            case "PRODUCT_DETAIL":
                logger.info("successful pass into PRODUCT_DETAIL i.e with fsp code: " + fspCode);
                BayportProductApiReqDataDto requestData = new BayportProductApiReqDataDto();
                requestData.setRequestHeader(
                        new ApiReqHeaderDto(
                                sender,
                                receiver,
                                fspCode,
                                msgId,
                                messageType
                        )
                );
                List<BayPortProductApiReqDetailDto> productDetailList = objectMapper.readValue(message, new TypeReference<List<BayPortProductApiReqDetailDto>>() {
                });
                requestData.setProductDetailList(productDetailList);

                BayportproductApiReqDto requestDto = new BayportproductApiReqDto();
                requestDto.setProductRegistrationData(requestData);
                requestDto.setMessageHash("XYZ");

                JAXBContext jaxbContext = JAXBContext.newInstance(BayportproductApiReqDto.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(requestDto, stringWriter);
                String xmlPayload = stringWriter.toString();

                logger.info("successful pass into product service i.e with fsp code: " + fspCode);

//                return productService.registerProductFromBayPort(xmlPayload);

                return null;

            case "PRODUCT_DECOMMISSION":
                ProductDecommisionApiReqDataDto decommissionData = new ProductDecommisionApiReqDataDto();
                decommissionData.setRequestHeader(
                        new ApiReqHeaderDto(
                                sender,
                                receiver,
                                fspCode,
                                msgId,
                                messageType
                        )
                );
                List<ProductDecommissionsApiReqDetailDto> decommissionDetailList = objectMapper.readValue(message, new TypeReference<List<ProductDecommissionsApiReqDetailDto>>() {
                });
                decommissionData.setProductDecommissionDetailList(decommissionDetailList);

                ProductDecommissionApiReqDto decommissionDto = new ProductDecommissionApiReqDto();
                decommissionDto.setProductDecommisionApiReqDataDto(decommissionData);
                decommissionDto.setMessageHash("XYZ");

                JAXBContext jaxbDecommissionContext = JAXBContext.newInstance(ProductDecommissionApiReqDto.class);
                Marshaller marshallerDecommission = jaxbDecommissionContext.createMarshaller();
                marshallerDecommission.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                StringWriter stringWriterDecommission = new StringWriter();
                marshallerDecommission.marshal(decommissionDto, stringWriterDecommission);
                String xmlPayloadDecommission = stringWriterDecommission.toString();

//                return productDecommissionService.productDecommission(xmlPayloadDecommission);
                return null;

            case "LOAN_DISBURSEMENT_NOTIFICATION":
                LoanDisburseNotApiDataDto disbursementData = new LoanDisburseNotApiDataDto();
                disbursementData.setRequestHeader(
                        new ApiReqHeaderDto(
                                sender,
                                receiver,
                                fspCode,
                                msgId,
                                messageType
                        )
                );
                //---------complete-------------
                LoanDisburseNotApiDetailDto disbursementDetail = objectMapper.readValue(message, new TypeReference<LoanDisburseNotApiDetailDto>() {
                });
                disbursementData.setMessageLoanDisburseDetails(disbursementDetail);

                LoanDisburseNotApiDto disbursementDto = new LoanDisburseNotApiDto();
                disbursementDto.setLoanDisburseNotApiData(disbursementData);
                disbursementDto.setMessageHash("XYZ");

                JAXBContext jaxbDisbursementContext = JAXBContext.newInstance(LoanDisburseNotApiDto.class);
                Marshaller marshallerDisbursement = jaxbDisbursementContext.createMarshaller();
                marshallerDisbursement.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                StringWriter stringWriterDisbursement = new StringWriter();
                marshallerDisbursement.marshal(disbursementDto, stringWriterDisbursement);
                String xmlPayloadDisbursement = stringWriterDisbursement.toString();

                return disbursementService.saveBayportLoanDisbursement(xmlPayloadDisbursement);



            case "LOAN_DISBURSEMENT_FAILURE_NOTIFICATION":
                LoanDisburseNotApiDataDto disbursementFailData = new LoanDisburseNotApiDataDto();
                disbursementFailData.setRequestHeader(
                        new ApiReqHeaderDto(
                                sender,
                                receiver,
                                fspCode,
                                msgId,
                                messageType
                        )
                );

                LoanDisburseNotApiDetailDto disbursementDetailFail = objectMapper.readValue(message, new TypeReference<LoanDisburseNotApiDetailDto>() {});
                disbursementFailData.setMessageLoanDisburseDetails(disbursementDetailFail);

                LoanDisburseNotApiDto disbursementDtoFail = new LoanDisburseNotApiDto();
                disbursementDtoFail.setLoanDisburseNotApiData(disbursementFailData);
                disbursementDtoFail.setMessageHash("XYZ");

                JAXBContext jaxbDisbursementFailContext = JAXBContext.newInstance(LoanDisburseNotApiDto.class);
                Marshaller marshallerDisbursementFail = jaxbDisbursementFailContext.createMarshaller();
                marshallerDisbursementFail.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                StringWriter stringWriterDisbursementFail = new StringWriter();
                marshallerDisbursementFail.marshal(disbursementDtoFail, stringWriterDisbursementFail);
                String xmlPayloadDisbursementFail = stringWriterDisbursementFail.toString();

                return disbursementService.saveBayportLoanDisbursementFailure(xmlPayloadDisbursementFail);

            default:
                return ResponseEntity.badRequest().body("Invalid message type");
        }
    }
}