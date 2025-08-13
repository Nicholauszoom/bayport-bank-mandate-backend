package com.example.Project.Management.bayport.product.service;


import com.dflex.emkopo.api.consumer.portal.dto.ProductPortalApiDto;
import com.dflex.emkopo.api.dto.ApiReqHeaderDto;
import com.dflex.emkopo.app.auth.services.AuthService;
import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProvider;
import com.dflex.emkopo.app.fsp.repositories.FinancialServiceProviderRepository;
import com.dflex.emkopo.app.fsp.services.FinancialServiceProviderService;
import com.dflex.emkopo.app.loan.service.LoanService;
import com.dflex.emkopo.app.product.dto.ProductConditionApiReqDetailDto;
import com.dflex.emkopo.app.product.dto.ProductRegistrationApiReqDataDto;
import com.dflex.emkopo.app.product.dto.ProductRegistrationApiReqDetailDto;
import com.dflex.emkopo.app.product.dto.ProductRegistrationApiReqDto;
import com.dflex.emkopo.app.product.dto.bayport.BayPortProductApiReqDetailDto;
import com.dflex.emkopo.app.product.dto.bayport.BayportproductApiReqDto;
import com.dflex.emkopo.app.product.entity.Product;
import com.dflex.emkopo.app.product.entity.ProductCondition;
import com.dflex.emkopo.app.product.repository.ProductConditionRepository;
import com.dflex.emkopo.app.product.repository.ProductRepository;
import com.dflex.emkopo.app.setup.service.CurrencyService;
import com.dflex.emkopo.app.setup.service.UtumishiResponseService;
import com.dflex.emkopo.commons.utils.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
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
public class ProductServiceImpl implements ProductService {

	private AuthService authService;

	@Value("${utumishiApiUrl}")
	private String utumishiApiUrl;
	private RabbitMqParameters rabbitMqParameters;


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
	private CurrencyService currencyService;

	@Autowired
	private UtumishiResponseService utumishiResponseService;

	@Autowired
	FinancialServiceProviderService financialServiceProviderService;

//	@Autowired
//	private ProductPublisher productPublisher;

	@Autowired
	private DigitalSignature digitalSignature;


	public ProductServiceImpl(CurrencyService currencyService, ProductConditionRepository productConditionRepository, FinancialServiceProviderService financialServiceProviderService, DigitalSignature digitalSignature) {

        this.currencyService = currencyService;
		this.financialServiceProviderService = financialServiceProviderService;
//        this.productPublisher = productPublisher;
        this.digitalSignature = digitalSignature;
		this.productConditionRepository = productConditionRepository;
    }

	@Autowired
	void setUtils(Utils utils) {
		this.utils = utils;
	}

	@Autowired
	void setRabbitMqParameters(RabbitMqParameters rabbitMqParameters) {
		this.rabbitMqParameters = rabbitMqParameters;
	}

//	Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());
   private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);


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
	public ResponseEntity<String> saveProductPortalQueue(String message) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(ProductPortalApiDto.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader stringReader = new StringReader(message);
		ProductPortalApiDto productPortalApiDto = (ProductPortalApiDto) unmarshaller.unmarshal(stringReader);
		String mssId = generateMessageId();
		List<FinancialServiceProvider> fspList = financialServiceProviderRepository.findAll();
		FinancialServiceProvider fsp = null;
		if (!fspList.isEmpty()) {
			fsp = fspList.get(0);
		}
		List<ProductRegistrationApiReqDetailDto> productDetailList = new ArrayList<>();

		try {


			Optional<Product> productExist=productRepository.findByProductCode(productPortalApiDto.getProductCode());
			if (productExist.isPresent()) {
				logger.info("Product alredy exists i.e with Product Code: "+ productExist.get().getProductCode());
				return createResponseEntity(ResponseCodes.DUPLICATE_REQUEST, fsp.getFspCode(), mssId);
			}



			Date currentTime = new Date();

			Product product = new Product();
			utils.copyNonNullProperties(productPortalApiDto, product);
			product.setFspCode(fsp.getFspCode());
			product.setFspName(fsp.getFspName());
			product.setDeductionCode(productPortalApiDto.getProductCode());
			product.setProductCode(productPortalApiDto.getProductCode());
			product.setProductName(productPortalApiDto.getProductName());
			product.setProductDescription(productPortalApiDto.getProductDescription());
			product.setMinimumTenure(productPortalApiDto.getMinimumTenure());
			product.setMaximumTenure(productPortalApiDto.getMaximumTenure());
			product.setInterestRate(productPortalApiDto.getInterestRate());
			product.setProcessingFee(productPortalApiDto.getProcessFee());
			product.setInsurance(productPortalApiDto.getInsurance());
			product.setMaxLoanAmount(productPortalApiDto.getMaxAmount());
			product.setMinLoanAmount(productPortalApiDto.getMinAmount());
			product.setOtherCharges(productPortalApiDto.getOtherCharges());
			product.setRepaymentType(productPortalApiDto.getRepaymentType());
			product.setIsExecutive(Boolean.valueOf(productPortalApiDto.getForExecutive()));
			product.setRegistrationDate(currentTime);
			product.setDeregistrationDate(currentTime);
			product.setCurrencyName(productPortalApiDto.getCurrency());
			product.setFinancialServiceProvider(fsp);
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
			productRegistrationApiReqDetailDto.setDeductionCode(productPortalApiDto.getProductCode());
			productRegistrationApiReqDetailDto.setProductCode(productPortalApiDto.getProductCode());
			productRegistrationApiReqDetailDto.setProductName(productPortalApiDto.getProductName());
			productRegistrationApiReqDetailDto.setProductDescription(productPortalApiDto.getProductDescription());
			if(productPortalApiDto.getForExecutive().equals("N")){
				productRegistrationApiReqDetailDto.setForExecutive("false");
			}else{
				productRegistrationApiReqDetailDto.setForExecutive("true");
			}
			productRegistrationApiReqDetailDto.setMinimumTenure(productPortalApiDto.getMinimumTenure());
			productRegistrationApiReqDetailDto.setMaximumTenure(productPortalApiDto.getMaximumTenure());
			productRegistrationApiReqDetailDto.setInterestRate(productPortalApiDto.getInterestRate());
			productRegistrationApiReqDetailDto.setInsurance(productPortalApiDto.getInsurance());
			productRegistrationApiReqDetailDto.setProcessFee(productPortalApiDto.getProcessFee());
			productRegistrationApiReqDetailDto.setRepaymentType(productPortalApiDto.getRepaymentType());
			productRegistrationApiReqDetailDto.setCurrency(productPortalApiDto.getCurrency());
			productRegistrationApiReqDetailDto.setMaxAmount(productPortalApiDto.getMaxAmount());
			productRegistrationApiReqDetailDto.setMinAmount(productPortalApiDto.getMinAmount());
			productRegistrationApiReqDetailDto.setInsuranceType("DISTRIBUTED");

			productRegistrationApiReqDetailDto.setProductConditionList(productConditionApiReqDetailDtoList);
			productDetailList.add(productRegistrationApiReqDetailDto);

			System.out.println("phase 1 *************: " );

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

//			loanService.generateRequestSignature2(xmlPayload);

			ResponseEntity<String> resposneEntity = loanService.generateRequestSignatureCheck(xmlPayload);

		return resposneEntity;


		} catch (Exception e) {
			logger.info("fail to save product i.e with product code: " + e.getMessage());

			return null;

		}

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

	public ResponseEntity<String> registerProductFromBayPort(String message) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(BayportproductApiReqDto.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader stringReader = new StringReader(message);
		BayportproductApiReqDto bayportproductApiReqDto = (BayportproductApiReqDto) unmarshaller.unmarshal(stringReader);
		String fspCode = bayportproductApiReqDto.getProductRegistrationData().getRequestHeader().getFspCode();
		String mssId =  bayportproductApiReqDto.getProductRegistrationData().getRequestHeader().getMsgId();

		try {
			logger.info("Received product from bayport request: {}", message);

			// Fetch product list from the API request
			List<BayPortProductApiReqDetailDto> productDetailList = bayportproductApiReqDto.getProductRegistrationData().getProductDetailList();

			//check if fsp code exists
			if( financialServiceProviderService.findByFspCode(fspCode) ==null){
				return createJsonResponseEntity(ResponseCodes.INVALID_FINANCIAL_SERVICE_PROVIDER, fspCode, mssId);
			}

			FinancialServiceProvider financialServiceProvider = financialServiceProviderService.findByFspCode(fspCode).orElseThrow();

			// Fetch all products from the database
			List<Product> productRecords = productRepository.findAll();

			for (BayPortProductApiReqDetailDto apiProduct : productDetailList) {
				String productCode = apiProduct.getProductCode();


				// Check if the product code already exists in the database
				boolean productExists = productRecords.stream()
						.anyMatch(existingProduct -> existingProduct.getProductCode().equals(productCode));

				// Throw an exception if duplicate product code found

				if (productExists) {
					return createJsonResponseEntity(ResponseCodes.DUPLICATE_REQUEST, fspCode, mssId);
				}

				Optional<Product> productExist=productRepository.findByProductCode(fspCode);
				if (productExist.isPresent()) {
					logger.info("Product alredy exists i.e with Product Code: "+ productExist.get().getProductCode());
					return createResponseEntity(ResponseCodes.DUPLICATE_REQUEST, fspCode, mssId);
				}

				// Create a new Product object
				Product product = new Product();


				utils.copyNonNullProperties(apiProduct, product);
				product.setFspCode(fspCode);
				product.setFspName(financialServiceProvider.getFspName());
				product.setProductCode(apiProduct.getProductCode());
				product.setProductName(apiProduct.getName());
				product.setProductDescription(apiProduct.getName());
				product.setMinimumTenure(apiProduct.getMinimumTenure());
				product.setMaximumTenure(apiProduct.getMaximumTenure());

				/*
				 * Annual Interest Rate
				 * */
				double annualInterestRateDecimal = Math.pow(1 + apiProduct.getMonthlyRate(), 12) - 1;
				double annualInterestRate=annualInterestRateDecimal * 100;

				product.setInterestRate((int) annualInterestRate);
				product.setInsurance(apiProduct.getCreditLife());
				product.setProcessingFee(apiProduct.getAdminFee());
				product.setMaxLoanAmount(apiProduct.getMaximumPrincipal());
				product.setMinLoanAmount(apiProduct.getMinimumPrincipal());
				product.setOtherCharges(apiProduct.getVatRate());
				product.setAnnualPercentageRate(apiProduct.getAnnualPercentageRate());
				product.setCreatedByUsername("Username");
				product.setRepaymentType("flat");
				product.setCurrency(currencyService.findById(1L).orElse(null));
//			    product.setUtumishiResponseEntity(utumishiResponseService.findById(1L).orElse(null));
				product.setFinancialServiceProvider(financialServiceProviderService.findByFspCode(fspCode).orElse(null));

				System.out.println("product code" + apiProduct.getProductCode());
				System.out.println("product max tenure" + apiProduct.getMaximumTenure());
				System.out.println("Max principle" + apiProduct.getMaximumPrincipal());

				// Perform insurance calculation
				BigDecimal minimumPrincipal=product.getMinLoanAmount();
				BigDecimal maximumPrincipal=product.getMaxLoanAmount();
				BigDecimal adminFee=apiProduct.getAdminFee();
				BigDecimal creditLife=apiProduct.getCreditLife();
				BigDecimal vatRate=apiProduct.getVatRate();
				product.setCurrencyName("TZS");

				BigDecimal insuranceAmount = (maximumPrincipal)
						.add(adminFee)
						.add(creditLife)
						.multiply(vatRate);

				product.setInsurance(insuranceAmount);


				// Save the product
				Product savedProduct = productRepository.save(product);

				System.out.println("******************product save success" + product );

				// publish to send to UTUMISHI
				utils.publishMsgToExchange(
						rabbitMqParameters.getExchange(),
						rabbitMqParameters.getProductUtumishiRoutingKey(),
						product, new HashMap<>());

				logger.info("product registration successfull i.e with product Code {}: " + product.getProductCode());
			}

			return createJsonResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

		} catch (Exception e) {
			logger.info(e.getMessage());
			return createJsonResponseEntity(ResponseCodes.INTERNAL_SERVER_ERROR, fspCode, mssId);
		}
	}



	@Override
	public ResponseEntity<String> saveProduct(String message) throws Exception {

		logger.info("Received product catalogy request: {}", message);

		JAXBContext jaxbContext = JAXBContext.newInstance(ProductRegistrationApiReqDto.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader stringReader = new StringReader(message);
		ProductRegistrationApiReqDto productRegistrationApiReqDto = (ProductRegistrationApiReqDto) unmarshaller.unmarshal(stringReader);
		String fspCode = productRegistrationApiReqDto.getProductRegistrationData().getRequestHeader().getFspCode();
		String mssId = productRegistrationApiReqDto.getProductRegistrationData().getRequestHeader().getMsgId();

		try {

			//fetch product list from the api
			List<ProductRegistrationApiReqDetailDto> productDetailList = productRegistrationApiReqDto.getProductRegistrationData().getProductDetailList();

			FinancialServiceProvider financialServiceProvider = financialServiceProviderService.findByFspCode(fspCode).orElseThrow();

			//check if fsp code is exist


			//fetch all products from database
			List<Product> productRecords = productRepository.findAll();

			for (ProductRegistrationApiReqDetailDto apiProduct : productDetailList) {
				String productCode = apiProduct.getProductCode();

				// Check if the product code already exists in the database
				boolean productExists = productRecords.stream()
						.anyMatch(existingProduct -> existingProduct.getProductCode().equals(productCode));

				// Throw an exception
				if (productExists) {
					return createResponseEntity(ResponseCodes.DUPLICATE_REQUEST, productCode, mssId);
				}

				// Save loan
				Product product = new Product();

				product.setFspCode(fspCode);
				product.setFspName(financialServiceProvider.getFspName());
				product.setDeductionCode(apiProduct.getProductCode());
				product.setProductCode(apiProduct.getProductCode());
				product.setProductName(apiProduct.getProductName());
				product.setProductDescription(apiProduct.getProductDescription());
				product.setMinimumTenure(apiProduct.getMinimumTenure());
				product.setMaximumTenure(apiProduct.getMaximumTenure());
				product.setInterestRate(apiProduct.getInterestRate());
				product.setProcessingFee(apiProduct.getProcessFee());
				product.setMaxLoanAmount(apiProduct.getMaxAmount());
				product.setMinLoanAmount(apiProduct.getMinAmount());
				product.setOtherCharges(BigDecimal.valueOf(0.25));
				product.setCreatedByUsername("Username");
				product.setRepaymentType("flat");
				product.setCurrency(currencyService.findById(1L).orElse(null));
				product.setFinancialServiceProvider(financialServiceProviderService.findByFspCode(fspCode).orElse(null));


				productRepository.save(product);

				logger.info("product registration successfull i.e with product Code: {}" + product.getProductCode());
			}

			return createResponseEntity(ResponseCodes.SUCCESSFUL_RECEIVED, fspCode, mssId);

		} catch (Exception e) {
			logger.info(e.getMessage());
			return createResponseEntity(ResponseCodes.ERROR_PROCESSING_REQUEST, "EMKOPO", mssId);
		}
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
		logger.info("Response for FSP Code {}: {}", fspCode, responseEntity.getBody());
		return responseEntity;
	}

	/*
	 * JSON RESPONSE
	 * */
	public ResponseEntity<String> createJsonResponseEntity(int responseCode, String fspCode, String mssId) throws Exception {
		String description = ResponseCodes.getDescription(responseCode);

		ResponseEntity<String> responseEntityJson = utils.messageResponseWrapperToPayPort(responseCode, description, fspCode,
				mssId, Constants.PRODUCT_RESPONSE);
		logger.info("Response for FSP Code {}: {}", fspCode, responseEntityJson.getBody());

		return responseEntityJson;
	}

	/*
	*Generate msg Id
	**/
	public static String generateMessageId() {
		return UUID.randomUUID().toString();
	}


}