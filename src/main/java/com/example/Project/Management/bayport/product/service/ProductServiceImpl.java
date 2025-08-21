package com.example.Project.Management.bayport.product.service;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
import com.example.Project.Management.bayport.common.Constants;
import com.example.Project.Management.bayport.common.DigitalSignature;
import com.example.Project.Management.bayport.common.entity.Status;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.repositories.FinancialServiceProviderRepository;
import com.example.Project.Management.bayport.fsp.services.FinancialServiceProviderService;
import com.example.Project.Management.bayport.loan.service.LoanService;
import com.example.Project.Management.bayport.product.dto.*;
import com.example.Project.Management.bayport.product.dto.portal.ProductDto;
import com.example.Project.Management.bayport.product.entity.Product;
import com.example.Project.Management.bayport.product.entity.ProductCondition;
import com.example.Project.Management.bayport.product.repository.ProductConditionRepository;
import com.example.Project.Management.bayport.product.repository.ProductRepository;
import com.example.Project.Management.bayport.util.ResponseCodes;
import com.example.Project.Management.bayport.util.Utils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Augustino Mwageni
 *
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Value("${utumishiApiUrl}")
	private String utumishiApiUrl;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private FinancialServiceProviderRepository financialServiceProviderRepository;

	@Autowired
	private LoanService loanService;


	private Utils utils;

	@Autowired
	private ProductConditionService productConditionService;
	@Autowired
	private ProductConditionRepository productConditionRepository;


	@Autowired
	FinancialServiceProviderService financialServiceProviderService;

	@Autowired
	private DigitalSignature digitalSignature;


	public ProductServiceImpl(ProductConditionRepository productConditionRepository, FinancialServiceProviderService financialServiceProviderService, DigitalSignature digitalSignature) {
		this.financialServiceProviderService = financialServiceProviderService;
        this.digitalSignature = digitalSignature;
		this.productConditionRepository = productConditionRepository;
    }

	@Autowired
	void setUtils(Utils utils) {
		this.utils = utils;
	}

	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public Product findByProductCode(String productCode) {
		Optional<Product> product= productRepository.findByProductCode(productCode);
		if (product.isEmpty()){
			return (Product) ResponseEntity.notFound();
		}
		return product.get();
	}

	@Override
	public List<Product> findByFspCode(String fspCode) {
		return productRepository.findByFspCode(fspCode);
	}

	@Override
	public Product findByProductCodeAndFspCodeAndRecordStatusId(String productCode, String fspCode,
																Long recordStatusId) {
		return productRepository.findByProductCodeAndFspCodeAndRecordStatusId(productCode,fspCode,
				recordStatusId);
	}

	@Override
	public List<Product> findByFspCodeAndRecordStatusId(String fspCode, Long recordStatusId) {
		return productRepository.findByFspCodeAndRecordStatusId(fspCode,recordStatusId);
	}

	@Override
	public ResponseEntity<?> registerProductFromPortal(ProductDto productDto) throws Exception {

		String mssId = generateMessageId();
		List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
		FinancialServiceProvider fsp = null;
		if (!fspList.isEmpty()) {
			fsp = fspList.get(0);
		}
		List<ProductRegistrationApiReqDetailDto> productDetailList = new ArrayList<>();
		try {

			Optional<Product> productExist=productRepository.findByProductCode(productDto.getProductCode());
			if (productExist.isPresent()) {
				log.info("Product alredy exists i.e with Product Code: "+ productExist.get().getProductCode());
				return createResponseEntity(ResponseCodes.DUPLICATE_REQUEST, fsp.getFspCode(), mssId);
			}

			Date currentTime = new Date();

			Product product = new Product();
			utils.copyNonNullProperties(productDto, product);
			product.setFspCode(fsp.getFspCode());
			product.setFspName(fsp.getFspName());
			product.setDeductionCode(productDto.getProductCode());
			product.setProductCode(productDto.getProductCode());
			product.setProductName(productDto.getProductName());
			product.setProductDescription(productDto.getProductDescription());
			product.setMinimumTenure(productDto.getMinimumTenure());
			product.setMaximumTenure(productDto.getMaximumTenure());
			product.setInterestRate(productDto.getInterestRate());
			product.setProcessingFee(productDto.getProcessFee());
			product.setInsurance(productDto.getInsurance());
			product.setMaxLoanAmount(productDto.getMaxLoanAmount());
			product.setMinLoanAmount(productDto.getMinLoanAmount());
			product.setOtherCharges(productDto.getOtherCharges());
			product.setRepaymentType(productDto.getRepaymentType());
			product.setIsExecutive(Boolean.valueOf(productDto.getForExecutive()));
			product.setRegistrationDate(currentTime);
			product.setDeregistrationDate(currentTime);
			product.setCurrencyName(productDto.getCurrency().name());
			product.setFinancialServiceProvider(fsp);
			product.setStatus(Status.REGISTERED);
		    productRepository.save(product);

			System.out.println("phase 0 *************: ");
			List<ProductCondition> productConditionList = productConditionRepository.findAll();

			List<ProductConditionApiReqDetailDto> productConditionApiReqDetailDtoList = new ArrayList<>();

			for (ProductCondition productCondition: productConditionList){
				ProductConditionApiReqDetailDto productConditionApiReqDetailDto = new ProductConditionApiReqDetailDto();
				productConditionApiReqDetailDto.setTermsConditionNumber(productCondition.getConditionNumber());
				productConditionApiReqDetailDto.setDescription(productCondition.getConditionDescription());
				productConditionApiReqDetailDto.setTCEffectiveDate(formatter.format(productCondition.getEffectiveDate()));
				productConditionApiReqDetailDtoList.add(productConditionApiReqDetailDto);
			}
			ProductRegistrationApiReqDetailDto productRegistrationApiReqDetailDto = new ProductRegistrationApiReqDetailDto();
			productRegistrationApiReqDetailDto.setDeductionCode(productDto.getProductCode());
			productRegistrationApiReqDetailDto.setProductCode(productDto.getProductCode());
			productRegistrationApiReqDetailDto.setProductName(productDto.getProductName());
			productRegistrationApiReqDetailDto.setProductDescription(productDto.getProductDescription());
			if(productDto.getForExecutive().equals("N")){
				productRegistrationApiReqDetailDto.setForExecutive("false");
			}else{
				productRegistrationApiReqDetailDto.setForExecutive("true");
			}
			productRegistrationApiReqDetailDto.setMinimumTenure(productDto.getMinimumTenure());
			productRegistrationApiReqDetailDto.setMaximumTenure(productDto.getMaximumTenure());
			productRegistrationApiReqDetailDto.setInterestRate(productDto.getInterestRate());
			productRegistrationApiReqDetailDto.setInsurance(productDto.getInsurance());
			productRegistrationApiReqDetailDto.setProcessFee(productDto.getProcessFee());
			productRegistrationApiReqDetailDto.setRepaymentType(productDto.getRepaymentType());
			productRegistrationApiReqDetailDto.setCurrency(productDto.getCurrency().name());
			productRegistrationApiReqDetailDto.setMaxAmount(productDto.getMaxLoanAmount());
			productRegistrationApiReqDetailDto.setMinAmount(productDto.getMinLoanAmount());
			productRegistrationApiReqDetailDto.setInsuranceType("DISTRIBUTED");

			productRegistrationApiReqDetailDto.setProductConditionList(productConditionApiReqDetailDtoList);
			productDetailList.add(productRegistrationApiReqDetailDto);

			ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
			apiReqHeaderDto.setFspCode(product.getFspCode());
			apiReqHeaderDto.setMsgId(mssId);
			apiReqHeaderDto.setMessageType(Constants.PRODUCT_REGISTRATION_DETAIL);
			apiReqHeaderDto.setReceiver(Constants.UTUMISHI_RECEIVER);
			apiReqHeaderDto.setSender(fsp.getFspName());

			ProductRegistrationApiReqDataDto productRegistrationApiReqDataDto = new ProductRegistrationApiReqDataDto();
			productRegistrationApiReqDataDto.setRequestHeader(apiReqHeaderDto);
			productRegistrationApiReqDataDto.setProductDetailList(productDetailList);

			System.out.println("json payload: " + productRegistrationApiReqDataDto);

			JAXBContext jaxbContext2 = JAXBContext.newInstance(ProductRegistrationApiReqDataDto.class);
			Marshaller marshaller = jaxbContext2.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(productRegistrationApiReqDataDto, stringWriter);
			String xmlPayload = stringWriter.toString();
			System.out.println("xml payload: " + xmlPayload);

			ResponseEntity<String> resposneEntity = loanService.generateRequestSignatureCheck(xmlPayload);

		return resposneEntity;

		} catch (Exception e) {
			log.info("fail to save product i.e with product code: " + e.getMessage());
			return null;
		}

	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	private String sendXmlToApi(String xml) throws Exception {
		// Log the final XML request
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

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
	/*
	 * XML RESPONSE
	 * */
	private ResponseEntity<String> createResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
		String description = ResponseCodes.getDescription(responseCode);

		ResponseEntity<String> responseEntity = utils.messageResponseWrapperToUtumishi(responseCode, description, fspCode,
				mssId, Constants.PRODUCT_RESPONSE);
		log.info("Response for FSP Code {}: {}", fspCode, responseEntity.getBody());
		return responseEntity;
	}

	/*
	*Generate msg Id
	**/
	public static String generateMessageId() {
		return UUID.randomUUID().toString();
	}


}