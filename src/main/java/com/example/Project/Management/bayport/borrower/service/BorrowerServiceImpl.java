package com.example.Project.Management.bayport.borrower.service;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
import com.example.Project.Management.bayport.borrower.dto.AccountValidation.AccountValidationApiReqDto;
import com.example.Project.Management.bayport.borrower.dto.AccountValidationResp.AccountValidationApiRespDataDto;
import com.example.Project.Management.bayport.borrower.dto.AccountValidationResp.AccountValidationApiRespDetailDto;
import com.example.Project.Management.bayport.borrower.dto.AccountValidationResp.AccountValidationApiRespDto;
import com.example.Project.Management.bayport.borrower.entity.Borrower;
import com.example.Project.Management.bayport.borrower.repository.BorrowerRepository;
import com.example.Project.Management.bayport.common.Constants;
import com.example.Project.Management.bayport.common.DigitalSignature;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.repositories.FinancialServiceProviderRepository;
import com.example.Project.Management.bayport.util.ResponseCodes;
import com.example.Project.Management.bayport.util.Utils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowerServiceImpl implements BorrowerService{

    static {
        IOUtils.setByteArrayMaxOverride(150_000_000);
    }

    @Autowired
    BorrowerRepository borrowerRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private DigitalSignature digitalSignature;


    @Autowired
    private FinancialServiceProviderRepository financialServiceProviderRepository;

    private static final Logger logger = LogManager.getLogger(BorrowerServiceImpl.class);

    public BorrowerServiceImpl(BorrowerRepository borrowerRepository, Utils utils, DigitalSignature digitalSignature, FinancialServiceProviderRepository financialServiceProviderRepository) {
        this.borrowerRepository = borrowerRepository;
        this.utils = utils;
        this.digitalSignature = digitalSignature;
        this.financialServiceProviderRepository = financialServiceProviderRepository;
    }

    @Override
    public Borrower findByNIN(String NIN) {
        return borrowerRepository.findByNIN(NIN);
    }

    @Override
    public List<Borrower> getAllBorrower() {
        return borrowerRepository.findAll();
    }

    @Override
    public Optional<Borrower> findById(Long id) {
        return borrowerRepository.findById(id);
    }

    @Override
    public ResponseEntity<String> accountValidation(String message)  throws Exception {
        logger.info("Received account validation request: {}", message);

        JAXBContext jaxbContext = JAXBContext.newInstance(AccountValidationApiReqDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        AccountValidationApiReqDto accountValidationApiReqDto = (AccountValidationApiReqDto) unmarshaller.unmarshal(stringReader);
        String fspCode = accountValidationApiReqDto.getAccountValidationApiReqDataDto().getRequestHeader().getFspCode();
        String mssId = accountValidationApiReqDto.getAccountValidationApiReqDataDto().getRequestHeader().getMsgId();
        Boolean valid = null;
        System.out.println("fsp code**************"+ fspCode);
        try {
            Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);
            if (fspByCode.isEmpty()) {
                valid = false;
                return createResponseEntity(valid, ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER, fspCode, mssId);
            }

            if (borrowerRepository.findByBankAccountNumber(accountValidationApiReqDto.getAccountValidationApiReqDataDto().getAccountValidationApiReqDetailDto().getAccountNumber()) == null) {
                valid = false;
                return createResponseEntity(valid , ResponseCodes.INVALID_BANK_ACCOUNT, fspCode, mssId);
            }

            /*
             * Response
             * */
            AccountValidationApiRespDetailDto accountValidationApiRespDetailDto = new AccountValidationApiRespDetailDto();
            accountValidationApiRespDetailDto.setValid(true);
            accountValidationApiRespDetailDto.setReason("Account number is Valid");

            Optional<FinancialServiceProvider> fsp = financialServiceProviderRepository.findByFspCode(fspCode);

            String sender = fsp.get().getFspShortName();

            ApiReqHeaderDto apiReqHeaderDto =  utils.getApiRequestHeader(fspCode, accountValidationApiReqDto.getAccountValidationApiReqDataDto().getRequestHeader().getMsgId(), Constants.ACCOUNT_VALIDATION_RESPONSE, sender);


            AccountValidationApiRespDataDto genericApiReqDataDto = new AccountValidationApiRespDataDto();
            genericApiReqDataDto.setRequestHeader(apiReqHeaderDto);
            genericApiReqDataDto.setAccountValidationApiRespDetailDto(accountValidationApiRespDetailDto);


            String stringToBeSigned = utils.convertXmlToString(JAXBContext.newInstance(AccountValidationApiRespDetailDto.class), genericApiReqDataDto);
            String signature = digitalSignature.createSignature(stringToBeSigned);

            AccountValidationApiRespDto genericApiReqDataDtoWrapper = new AccountValidationApiRespDto();
            genericApiReqDataDtoWrapper.setAccountValidationApiRespDataDto(genericApiReqDataDto);
            genericApiReqDataDtoWrapper.setMessageHash(signature);


            String requestToBeSend = utils.convertXmlToString(JAXBContext.newInstance(AccountValidationApiRespDto.class), genericApiReqDataDtoWrapper);

            logger.info("Account validation response is successfull i.e Account Number: {} "+ accountValidationApiReqDto.getAccountValidationApiReqDataDto().getAccountValidationApiReqDetailDto().getAccountNumber());
//            logger.info(" accont validation logs {}" + requestToBeSend);
            return ResponseEntity.ok(requestToBeSend);
//            return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode);

        }  catch (Exception e) {
            logger.info("Exception occurred while create Account validation response {}");
            System.out.println("fsp code 500 :**************"+ fspCode);
            valid =false;
            return createResponseEntity(valid,ResponseCodes.INTERNAL_SERVER_ERROR, fspCode, mssId);
        }
    }

    @Override
    public void saveBorrowers(MultipartFile file) throws Exception {
        List<Borrower> borrowers = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowIndex = 0;

            System.out.println("process pass here************");


            for (Row row : sheet) {
                if (rowIndex == 0 || rowIndex == 1) { // Skip the first two rows
                    rowIndex++;
                    continue;
                }


                System.out.println("process pass in step 2************");

                Borrower borrower = new Borrower();
                for (int cellIndex = 1; cellIndex < row.getLastCellNum(); cellIndex++) { // Skip the first column
                    Cell cell = row.getCell(cellIndex);
                    if (cell == null) continue;

                    System.out.println("process pass in step 3************");
                    switch (cellIndex) {
                        case 1:
                            borrower.setCheckNumber(getStringValueFromCell(cell));
                            break;
                        case 2:
                            borrower.setFullName(cell.getStringCellValue());
                            break;
                        case 3:
                            borrower.setBankAccountNumber(getStringValueFromCell(cell));
                            break;
                        case 4:
                            borrower.setNearestBranchName(cell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                }
//                borrowerRepository.save(borrower);
                borrowers.add(borrower);
                rowIndex++;
            }

            borrowerRepository.saveAll(borrowers); // Save all borrowers in batch
            logger.info("Successfully processed and saved borrowers' data from the Excel file");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Failed to process the borrowers' data from the Excel file: " + e);
            throw new Exception("The file is not a valid Excel file", e);
        }
    }

    @Override
    public List<Borrower> findByFspCode(String fspCode) {
        return borrowerRepository.findByFspCode(fspCode);
    }

    private String getStringValueFromCell(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            default -> "";
        };
    }

    private ResponseEntity<String> createResponseEntity(Boolean valid, int responseCode, String fspCode, String mssId) throws Exception {
        String reason = ResponseCodes.getDescription(responseCode);
        System.out.println("fsp code************** 200"+ fspCode);
        ResponseEntity<String> responseEntity = utils.messageAccountValidationResponseWrapperToUtumishi(valid, reason, fspCode, mssId, Constants.ACCOUNT_VALIDATION_RESPONSE);
        logger.info("Response for FSP Code {}: {}", fspCode, responseEntity.getBody());
        return responseEntity;
    }


}
