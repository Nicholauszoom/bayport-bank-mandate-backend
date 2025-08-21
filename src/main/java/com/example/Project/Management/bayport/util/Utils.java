package com.example.Project.Management.bayport.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
import com.example.Project.Management.bayport.apis.dto.MessageResponse;
import com.example.Project.Management.bayport.apis.dto.MessageResponseWrapper;
import com.example.Project.Management.bayport.apis.dto.ResponseMessageDetail;
import com.example.Project.Management.bayport.apis.dto.accountValidationResponse.MessageAccValidationResponse;
import com.example.Project.Management.bayport.apis.dto.accountValidationResponse.MessageAccValidationResponseWrapper;
import com.example.Project.Management.bayport.apis.dto.accountValidationResponse.ResponseAccValidationMessageDetail;
import com.example.Project.Management.bayport.common.DigitalSignature;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.repositories.FinancialServiceProviderRepository;
import com.example.Project.Management.bayport.product.service.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

/**
 *
 * @author Augustino Mwageni
 *
 */
@Component
@SuppressWarnings("rawtypes")
public class Utils {

    private final MessageSource messageSource;

    private final FinancialServiceProviderRepository financialServiceProviderRepository;

    Locale currentLocale = LocaleContextHolder.getLocale();

    private Long tokenSuffix = 0L;

    private RestTemplate restTemplate = new RestTemplate();

    private DigitalSignature digitalSignature;

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    public Utils(MessageSource messageSource, FinancialServiceProviderRepository financialServiceProviderRepository) {
        this.messageSource = messageSource;
        this.financialServiceProviderRepository = financialServiceProviderRepository;
    }

    @Autowired
    void setDigitalSignature(DigitalSignature digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    /**
     * Validate Xml Schema against Xsd
     * @param requestXsdSchema
     * @param requestContent
     * @return Map
     */
    public Map<String, String> validateRequestXMLSchema(InputStream  requestXsdSchema, String requestContent) {

        Map<String,String> result = new ConcurrentHashMap<String,String>();
        String saxException = "";
        String code = "";
        String remark = "";
        String message = "";

        try {

            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(requestXsdSchema));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(requestContent)));

            code = messageSource.getMessage("code.1001", null, currentLocale);
            remark = messageSource.getMessage("message.1001", null, currentLocale);
            message = remark;

        } catch (IOException e){

            System.out.println("Exception: "+e.getMessage());
            code = messageSource.getMessage("code.1002", null, currentLocale);
            remark = messageSource.getMessage("message.1002", null, currentLocale);
            message = remark +","+e.getMessage();

        } catch(SAXException saxEx){

            String exSource = saxEx.getMessage().replaceAll("[.,\']","");
            exSource = exSource.substring(exSource.indexOf("for type")+8).trim();
            System.out.println("SAX Exception: "+saxEx.getMessage());
            saxException = saxEx.getMessage();
            message = saxException;

            switch(exSource) {
                case "strrequestid" :
                    code = messageSource.getMessage("code.1042", null, currentLocale);
                    remark = messageSource.getMessage("message.1042", null, currentLocale);
                    break;
                case "strcallbackurl" :
                    code = messageSource.getMessage("code.1017", null, currentLocale);
                    remark = messageSource.getMessage("message.1017", null, currentLocale);
                    break;
                case "intinvoicetype" :
                    code = messageSource.getMessage("code.1043", null, currentLocale);
                    remark = messageSource.getMessage("message.1043", null, currentLocale);
                case "intdtlcount" :
                    code = messageSource.getMessage("code.1044", null, currentLocale);
                    remark = messageSource.getMessage("message.1044", null, currentLocale);
                    break;
                case "strservicegroup" :
                    code = messageSource.getMessage("code.1045", null, currentLocale);
                    remark = messageSource.getMessage("message.1045", null, currentLocale);
                    break;
                case "strinvoicenumber" :
                    code = messageSource.getMessage("code.1019", null, currentLocale);
                    remark = messageSource.getMessage("message.1019", null, currentLocale);
                    break;
                case "strserviceinstitution" :
                    code = messageSource.getMessage("code.1018", null, currentLocale);
                    remark = messageSource.getMessage("message.1018", null, currentLocale);
                    break;
                case "strworkstation" :
                    code = messageSource.getMessage("code.1020", null, currentLocale);
                    remark = messageSource.getMessage("message.1020", null, currentLocale);
                    break;
                case "strinvoicedescription" :
                    code = messageSource.getMessage("code.1046", null, currentLocale);
                    remark = messageSource.getMessage("message.1046", null, currentLocale);
                    break;
                case "strcustomerid" :
                    code = messageSource.getMessage("code.1027", null, currentLocale);
                    remark = messageSource.getMessage("message.1027", null, currentLocale);
                    break;
                case "intcustomeridtype" :
                    code = messageSource.getMessage("code.1026", null, currentLocale);
                    remark = messageSource.getMessage("message.1026", null, currentLocale);
                    break;
                case "strcustomeraccount" :
                    code = messageSource.getMessage("code.1047", null, currentLocale);
                    remark = messageSource.getMessage("message.1047", null, currentLocale);
                    break;
                case "strcustomername" :
                    code = messageSource.getMessage("code.1048", null, currentLocale);
                    remark = messageSource.getMessage("message.1048", null, currentLocale);
                    break;
                case "strcustomerphonenumber" :
                    code = messageSource.getMessage("code.1049", null, currentLocale);
                    remark = messageSource.getMessage("message.1049", null, currentLocale);
                    break;
                case "strcustomeremail" :
                    code = messageSource.getMessage("code.1050", null, currentLocale);
                    remark = messageSource.getMessage("message.1050", null, currentLocale);
                    break;
                case "datetimeissue" :
                    code = messageSource.getMessage("code.1021", null, currentLocale);
                    remark = messageSource.getMessage("message.1021", null, currentLocale);
                    break;
                case "datetimeexpiry" :
                    code = messageSource.getMessage("code.1022", null, currentLocale);
                    remark = messageSource.getMessage("message.1022", null, currentLocale);
                    break;
                case "strissuedby" :
                    code = messageSource.getMessage("code.1051", null, currentLocale);
                    remark = messageSource.getMessage("message.1051", null, currentLocale);
                    break;
                case "strapprovedby" :
                    code = messageSource.getMessage("code.1052", null, currentLocale);
                    remark = messageSource.getMessage("message.1052", null, currentLocale);
                    break;
                case "decinvoiceamount" :
                    code = messageSource.getMessage("code.1025", null, currentLocale);
                    remark = messageSource.getMessage("message.1025", null, currentLocale);
                    break;
                case "intpaymentoption" :
                    code = messageSource.getMessage("code.1062", null, currentLocale);
                    remark = messageSource.getMessage("message.1062", null, currentLocale);
                    break;
                case "strcurrency" :
                    code = messageSource.getMessage("code.1055", null, currentLocale);
                    remark = messageSource.getMessage("message.1055", null, currentLocale);
                    break;
                case "decexchangerate" :
                    code = messageSource.getMessage("code.1056", null, currentLocale);
                    remark = messageSource.getMessage("message.1056", null, currentLocale);
                    break;
                case "intinvoiceplan" :
                    code = messageSource.getMessage("code.1057", null, currentLocale);
                    remark = messageSource.getMessage("message.1057", null, currentLocale);
                    break;
                case "strservicedepartment" :
                    code = messageSource.getMessage("code.1058", null, currentLocale);
                    remark = messageSource.getMessage("message.1058", null, currentLocale);
                    break;
                case "strservicetype" :
                    code = messageSource.getMessage("code.1059", null, currentLocale);
                    remark = messageSource.getMessage("message.1059", null, currentLocale);
                    break;
                case "strserviceref" :
                    code = messageSource.getMessage("code.1060", null, currentLocale);
                    remark = messageSource.getMessage("message.1060", null, currentLocale);
                    break;
                case "desserviceamount" :
                    code = messageSource.getMessage("code.1061", null, currentLocale);
                    remark = messageSource.getMessage("message.1061", null, currentLocale);
                    break;
                case "intpaymentpriority" :
                    code = messageSource.getMessage("code.1063", null, currentLocale);
                    remark = messageSource.getMessage("message.1063", null, currentLocale);
                    break;
                case "strhash" :
                    code = messageSource.getMessage("code.1016", null, currentLocale);
                    remark = messageSource.getMessage("message.1016", null, currentLocale);
                    break;
                default:
                    code = messageSource.getMessage("code.1005", null, currentLocale);
                    remark = messageSource.getMessage("message.1005", null, currentLocale);
            }
        }

        result.put("code", code);
        result.put("remark", remark);
        result.put("message", message);

        return result;
    }

    /**
     * Get xml string within a specific xml tag inclusively
     * @param xmlBody
     * @param xmlTag
     * @return String
     */
    public String getStringWithinXml(String xmlBody, String xmlTag) {
        String xmlString = "";
        if (xmlBody != null && !xmlBody.isEmpty() && xmlTag != null && !xmlTag.isEmpty()) {

            try {
                xmlString = xmlBody.substring(xmlBody.indexOf("<" + xmlTag + ">"),xmlBody.indexOf("</" + xmlTag + ">") + xmlTag.length() + 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return xmlString;
    }

    /**
     * Get xml string within a specific xml tag exclusively
     * @param xmlBody
     * @param xmlTag
     * @return String
     */
    public String getStringWithinXmlTagExclusive(String xmlBody, String xmlTag) {
        String xmlString = "";
        if (xmlBody != null && !xmlBody.isEmpty() && xmlTag != null && !xmlTag.isEmpty()) {

            try {
                xmlString = xmlBody.substring(xmlBody.indexOf("<" + xmlTag + ">")+xmlTag.length()+2,
                        xmlBody.indexOf("</" + xmlTag + ">") );
            } catch (Exception e) {
            }
        }
        return xmlString;
    }

    /**
     * This method generate string token based on current time
     * @return String
     * @throws
     */
    public String getToken(){

        String token = "";
        String currTokenSuffix = "";
        tokenSuffix ++;
        if(tokenSuffix < 10000) {
            currTokenSuffix = String.format("%04d",tokenSuffix);
        }else {
            tokenSuffix = 0L;
            currTokenSuffix = String.format("%04d",tokenSuffix);
        }

        token = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        token = token + currTokenSuffix;
        return token;
    }

    /**
     *
     * @param jaxbContext
     * @param object
     * @return
     */
    public String generateXmlString(JAXBContext jaxbContext, Object object) {

        String xmlInStringFormat = "";
        JAXBContext jaxbContext1 = null;
        StringWriter xmlStringWriter = null;
        Marshaller jaxbMarshaller1 = null;

        try {
            jaxbContext1 = jaxbContext;
            xmlStringWriter = new StringWriter();
            jaxbMarshaller1 = jaxbContext1.createMarshaller();
            jaxbMarshaller1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            jaxbMarshaller1.marshal(object, xmlStringWriter);
            xmlInStringFormat = xmlStringWriter.toString();
        } catch (Exception e) {
            xmlInStringFormat = "";
        }

        return xmlInStringFormat;
    }

    /**
     * Check if file exists
     * @param fullfilePath
     * @return boolean
     */
    public boolean isFileExist(String fullfilePath) {
        boolean result = false;
        if (!isNullOrEmpty(fullfilePath)) {
            File fis = new File(fullfilePath);
            if (fis.exists()) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Check String is null or empty
     * @param inputString
     * @return boolean
     */
    public boolean isNullOrEmpty(String inputString) {
        boolean result = false;
        if (inputString == null || inputString.isEmpty()) {
            result = true;
        }
        return result;
    }


    /**
     * Get current session parameters
     * @return SessionDto
     */
    public Session getSession() {

        Session session = new Session();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat shortYearFormat = new SimpleDateFormat("YY");

        //String dayOfYear = String.format("%03d",calendar.get(Calendar.DAY_OF_YEAR));
        Calendar calendar = Calendar.getInstance();
        Integer dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        Integer hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        Date date = calendar.getTime();
        String shortYear = shortYearFormat.format(date);

        session.setTodaysDate(date);
        session.setCurrentTime(timeFormat.format(date));
        session.setDayOfYear(dayOfYear);
        session.setHourOfDay(hourOfDay);
        session.setShortYear(shortYear);
        session.setFinancialYear(2024L);

        return session;
    }

    /**
     * Separate string by char at specific intervals
     * @param originalString
     * @param interval
     * @param separator
     * @return String
     */
    public String getCharDevidedString(String originalString,int interval,char separator) {

        String devidedStr = "";

        try {

            StringBuilder sb = new StringBuilder(originalString);
            for(int i = 0; i < (originalString.length() / interval)-1; i++) {
                sb.insert(((i + 1) * interval) + i, separator);
            }
            devidedStr = sb.toString();

        }catch(Exception ex) {
        }

        return devidedStr;
    }

    /**
     * Find minimum missing sequence in a range
     * @param
     * @param
     * @return Long
     */
    public Integer findMinMissingNumberInRange(List<Integer> codesArray) {

        Collections.sort(codesArray);
        Integer start = codesArray.get(0);
        Integer next = start ++;
        while (codesArray.contains(next)) {
            next ++;
        }
        return next;
    }

    /**
     * Generate Strong Random Password
     * @return String
     */
    private static final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String SYMBOL = "|$^+=!@#-:?*";

    private static final String PASSWORD_ALLOW =
            CHAR_LOWERCASE + CHAR_UPPERCASE + DIGIT + SYMBOL;

    private static SecureRandom random = new SecureRandom();

    public static String generateStrongPassword(int length) {


        StringBuilder result = new StringBuilder(length);

        // at least 4 chars (lowercase)
        String strLowerCase = generateRandomString(CHAR_LOWERCASE, 4);
        result.append(strLowerCase);

        // at least 4 chars (uppercase)
        String strUppercaseCase = generateRandomString(CHAR_UPPERCASE, 4);
        result.append(strUppercaseCase);

        // at least 3 digits
        String strDigit = generateRandomString(DIGIT, 3);
        result.append(strDigit);

        // at least 4 special characters (punctuation + symbols)
        String strSpecialChar = generateRandomString(SYMBOL, 4);
        result.append(strSpecialChar);

        // remaining, just random
        String strOther = generateRandomString(PASSWORD_ALLOW, length - 8);
        result.append(strOther);

        String password = result.toString();
        password = shuffleString(password);
        return password;
    }

    /**
     * Generate a random char[]
     * @param
     * @param size
     * @returnString
     */
    private static String generateRandomString(String input, int size) {

        if (input == null || input.length() <= 0)
            throw new IllegalArgumentException("Invalid input.");
        if (size < 1) throw new IllegalArgumentException("Invalid size.");

        StringBuilder result = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            // produce a random order
            int index = random.nextInt(input.length());
            result.append(input.charAt(index));
        }
        return result.toString();
    }

    /**
     * ShuffleString
     * @param
     * @return String
     */
    public static String shuffleString(String input) {
        List<String> result = Arrays.asList(input.split(""));
        Collections.shuffle(result);
        return result.stream().collect(Collectors.joining());
    }

    /**
     * Format string containing xml
     */
    public String formatXmlString(String xml) {

        try {

            InputSource src = new InputSource(new StringReader(xml));
            Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();

            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);

            return writer.writeToString(document);

        } catch (Exception e) {
            return xml;
        }
    }

    /**
     * Get date in milliseconds
     */
    public Long getDateInMilliseconds(Date inputDate) {

        Calendar calDate = Calendar.getInstance();
        calDate.setTime(inputDate);
        return calDate.getTimeInMillis();

    }

    /**
     * Get Days between two dates
     */
    public Double getDaysBetweenTwoDates(Date startDate,Date endDate) {

        Long startTimeInMills = getDateInMilliseconds(startDate);
        Long endTimeInMills = getDateInMilliseconds(endDate);
        Long differenceMills = Math.abs(startTimeInMills - endTimeInMills);

        if(differenceMills > 0) {
            return (double) (differenceMills /(1000L*60L*60L*24L));
        }
        return 0D;
    }

    /**
     * Get Local Date from Date
     * @param date
     * @return LocalDate
     */
    public LocalDate getLocalDateFromDate(Date date) {

        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();

        return localDate;
    }

    /**
     * Get Time Difference Between Dates Ignoring Time
     */
    public Integer getDaysBetweenTwoDatesIgnoringTime(Date startDate,Date endDate) {

        Integer days = Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays();

        return Math.abs(days);
    }

    /**
     * Get milliseconds between two dates
     */
    public Long getTimeBetweenTwoDates(Date startDate,Date endDate) {

        Long startTimeInMills = getDateInMilliseconds(startDate);
        Long endTimeInMills = getDateInMilliseconds(endDate);
        Long differenceMills = Math.abs(startTimeInMills - endTimeInMills);
        return differenceMills;
    }

    /**
     * Validate Phone Number
     * @return true or false
     */
    public Boolean isValidPhoneNumber(String phoneNumber) {

        if(phoneNumber.length() < 10) {
            return false;
        } else if(phoneNumber.substring(0, 3).equals("255") && phoneNumber.length() != 12) {
            return false;
        } else if(phoneNumber.substring(0, 1).equals("0") && phoneNumber.length() != 10){
            return false;
        } else {
            return true;
        }
    }

    /**
     * this method validates email address
     *
     * @return true or false
     */
    public boolean isValidEmailAddress(final String email) {

        Pattern pattern;
        Matcher matcher;
        final String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        pattern = Pattern.compile(emailPattern);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Convert Date to LocalDateTime
     * @param date
     * @return LocalDateTime
     */
    public LocalDateTime getLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * Convert Date to LocalDate
     * @param date
     * @return LocalDate
     */
    public LocalDate getLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Get Days between dates
     * @param startDate
     * @param endDate
     * @return Integer
     */
    public Integer getDaysBetweenLocalDates(LocalDate startDate,LocalDate endDate) {

        Long daysPassed = ChronoUnit.DAYS.between(startDate, endDate);
        return Math.abs(daysPassed.intValue());
    }

    /**
     * Get Days between dates
     * @param startDate
     * @param endDate
     * @return Integer
     */
    public Integer getMinutesBetweenLocalDateTimes(LocalDateTime startDate,LocalDateTime endDate) {

        Long minutes = ChronoUnit.MINUTES.between(startDate, endDate);
        return Math.abs(minutes.intValue());
    }

    /**
     * Get Miinutes between dates
     * @param startDate
     * @param endDate
     * @return Integer
     */
    public Integer getDaysBetweenLocalDateTimes(LocalDateTime startDate,LocalDateTime endDate) {

        Long hours = ChronoUnit.HOURS.between(startDate, endDate);
        Integer days = hours.intValue()/24;
        return Math.abs(days);
    }

    /**
     * This method communicate with outside systems using Rest Template
     * @param uri
     * @param contentToSend
     * @param contentType
     * @param method
     * @param messageHeaders
     * @return
     */
    public Response sendMsgToExternalSystem(String uri, String contentToSend, String contentType, String method,Hashtable<String, String> messageHeaders) {

        RestTemplate restTemplate = new RestTemplate(getRequestFactory(Constant.CONNECT_TIMEOUT));
        ResponseEntity<String> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        Response response = new Response();

        if(messageHeaders != null && !messageHeaders.isEmpty()) {
            if(messageHeaders.containsKey("username") && messageHeaders.containsKey("password")){

                String plainCreds = messageHeaders.get("username")+":" + messageHeaders.get("password");
                byte[] plainCredsBytes = plainCreds.getBytes();
                byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
                String base64Creds = new String(base64CredsBytes);
                headers.add("Authorization", "Basic " + base64Creds);
            }

            for (String key : messageHeaders.keySet()) {
                headers.add(key, messageHeaders.get(key));
            }
        }

        headers.setContentType(getMediaType(contentType));
        HttpEntity<String> request = new HttpEntity<String>(contentToSend, headers);

        try {

            if (method.toUpperCase().equals("POST")) {

                responseEntity = restTemplate.postForEntity(uri, request, String.class);

            } else if (method.toUpperCase().equals("GET")) {

                responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);

            } else if (method.toUpperCase().equals("PUT")) {

                responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);

            }else if (method.toUpperCase().equals("DELETE")) {

                responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, request,String.class);
            }


            response.setStatus(String.valueOf(responseEntity.getStatusCode().value()));
            response.setMessage(responseEntity.getStatusCode().toString());
            response.setData(responseEntity.getBody());

        } catch (HttpClientErrorException exception) {

            response.setStatus(String.valueOf(exception.getStatusCode().value()));
            response.setMessage(exception.getMessage());

        }catch (HttpStatusCodeException exception) {

            response.setStatus(String.valueOf(exception.getStatusCode().value()));
            response.setMessage(exception.getMessage());

        } catch (Exception e) {

            response.setStatus("0");
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * This method is used to retry to communicate with outside systems using Rest Template
     * after primary attempt failure
     * This method uses timeouts larger than primary attempt timeouts
     */

    public Response resendMsgToExternalSystem(String uri, String contentToSend, String contentType, String method,
                                              Hashtable<String, String> messageHeaders) {

        RestTemplate restTemplate = new RestTemplate(getRequestFactory(Constant.RETRY_CONNECT_TIMEOUT));
        ResponseEntity<String> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        Response response = new Response();

        if(messageHeaders != null && !messageHeaders.isEmpty()) {
            if(messageHeaders.containsKey("username") && messageHeaders.containsKey("password")){

                String plainCreds = messageHeaders.get("username")+":" + messageHeaders.get("password");
                byte[] plainCredsBytes = plainCreds.getBytes();
                byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
                String base64Creds = new String(base64CredsBytes);
                headers.add("Authorization", "Basic " + base64Creds);
            }

            for (String key : messageHeaders.keySet()) {
                headers.add(key, messageHeaders.get(key));
            }
        }

        headers.setContentType(getMediaType(contentType));
        HttpEntity<String> request = new HttpEntity<String>(contentToSend, headers);

        try {

            if (method.toUpperCase().equals("POST")) {

                responseEntity = restTemplate.postForEntity(uri, request, String.class);

            } else if (method.toUpperCase().equals("GET")) {

                responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);

            } else if (method.toUpperCase().equals("PUT")) {

                responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);

            }else if (method.toUpperCase().equals("DELETE")) {

                responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, request,String.class);
            }

            response.setStatus(String.valueOf(responseEntity.getStatusCode().value()));
            response.setMessage(responseEntity.getStatusCode().toString());
            response.setData(responseEntity.getBody());
        } catch (HttpClientErrorException exception) {

            response.setStatus(String.valueOf(exception.getStatusCode().value()));
            response.setMessage(exception.getMessage());

        } catch (HttpStatusCodeException exception) {

            response.setStatus(String.valueOf(exception.getStatusCode().value()));
            response.setMessage(exception.getMessage()==null?"":exception.getMessage());

        } catch (Exception e) {

            response.setStatus("0");
            response.setMessage(e.getMessage());
        }

        return response;
    }

    private ClientHttpRequestFactory getRequestFactory(Long connectTimeout) {
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(connectTimeout,TimeUnit.MILLISECONDS)
                .build();

        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .build();

        return new HttpComponentsClientHttpRequestFactory(client);
    }

    /**
     * This method gets request media type
     * @return MediaType
     */
    public MediaType getMediaType(String requestMediaType) {

        MediaType contentMediaType;

        switch (requestMediaType.toUpperCase()) {
            case "XML":
                contentMediaType = MediaType.APPLICATION_XML;
                break;
            case "JSON":
                contentMediaType = MediaType.APPLICATION_JSON;
                break;
            case "TXML":
                contentMediaType = MediaType.TEXT_XML;
                break;
            default:
                contentMediaType = MediaType.TEXT_PLAIN;
        }

        return contentMediaType;
    }



    /**
     * Get request Remote IP Address
     * @param request
     * @return String
     */
    public String getRequestIP(HttpServletRequest request) {
        for (String header: Constant.IP_HEADERS) {
            String value = request.getHeader(header);
            if (value == null || value.isEmpty()) {
                continue;
            }
            String[] parts = value.split("\\s*,\\s*");
            return parts[0];
        }
        return request.getRemoteAddr();
    }



    public String convertXmlToString(JAXBContext jaxbContext, Object object) {
        String xmlInStringFormat = "";
        JAXBContext jaxbContext1 = null;
        StringWriter xmlStringWriter = null;
        Marshaller jaxbMarshaller1 = null;

        try {
            jaxbContext1 = jaxbContext;
            xmlStringWriter = new StringWriter();
            jaxbMarshaller1 = jaxbContext1.createMarshaller();
            jaxbMarshaller1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            jaxbMarshaller1.marshal(object, xmlStringWriter);
            xmlInStringFormat = xmlStringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            xmlInStringFormat = "";
        }

        return xmlInStringFormat;

    }


    public String postData(String endpoint, String requestBody) {


        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        // Create an HttpEntity with headers and request body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send a POST request and receive the response as ResponseEntity
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(endpoint, requestEntity, String.class);


        // Extract and return the response body
        return responseEntity.getBody();
    }


    public String getReceipt(String paymentReference) {
        StringBuilder stringBuilder =  new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = simpleDateFormat.format(new Date());
        stringBuilder
                .append("DF-")
                .append(now)
                .append("-")
                .append(paymentReference);
        return stringBuilder.toString();
    }

    /**
     * @param jaxbContext
     * @param string
     * @return object
     * @author Edward Kimaro Unmarshall string object to object
     */
    public Object convertStringToXml(JAXBContext jaxbContext, String string) {
        Object obj;
        Unmarshaller jaxbUnMarshaller;
        StringReader reader;

        try {
            jaxbUnMarshaller = jaxbContext.createUnmarshaller();
            reader = new StringReader(string);
            obj = jaxbUnMarshaller.unmarshal(reader);

        } catch (Exception e) {
            obj = null;
        }

        return obj;
    }


    public MessageResponseWrapper messageResponseWrapper(String response) throws JAXBException {
        return (MessageResponseWrapper) convertStringToXml(JAXBContext.newInstance(MessageResponseWrapper.class),
                response);
    }


    public boolean isFutureDate(Date date) {
        Date currentDate = new Date();
        return date.after(currentDate);
    }


    private static String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set emptyNames = new HashSet();
        PropertyDescriptor[] var4 = pds;
        int var5 = pds.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            PropertyDescriptor pd = var4[var6];
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return (String[]) emptyNames.toArray(result);
    }


    public void copyNonNullProperties(Object source, Object destination) {
        BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
    }



    public ApiReqHeaderDto getApiRequestHeader(String fspCode, String receipt, String apiType, String sender){
        ApiReqHeaderDto apiReqHeaderDto = new ApiReqHeaderDto();
        apiReqHeaderDto.setFspCode(fspCode);
        apiReqHeaderDto.setMsgId(receipt);
        apiReqHeaderDto.setMessageType(apiType);
        apiReqHeaderDto.setReceiver(Constant.UTUMISHI_RECEIVER);
        apiReqHeaderDto.setSender(sender);

        return apiReqHeaderDto;
    }

    public ResponseEntity<String> messageResponseWrapperToUtumishi(int code, String message, String fspCode, String receipt, String apiType) throws Exception {
        MessageResponseWrapper messageResponseWrapper = new MessageResponseWrapper();

        ResponseMessageDetail responseMessageDetail = new ResponseMessageDetail(String.valueOf(code), message);

        Optional<FinancialServiceProvider> fsp = financialServiceProviderRepository.findByFspCode(fspCode);

        String sender = fsp.get().getFspShortName();

        ApiReqHeaderDto apiReqHeaderDto = getApiRequestHeader(fspCode, receipt, apiType, sender);
        MessageResponse messageResponse  = new MessageResponse();
        messageResponse.setMessageDetails(responseMessageDetail);
        messageResponse.setHeader(apiReqHeaderDto);


        String stringToBeSigned = convertXmlToString(JAXBContext.newInstance(MessageResponse.class), messageResponse);
        String signature = digitalSignature.createSignature(stringToBeSigned);
        messageResponseWrapper.setData(messageResponse);
        messageResponseWrapper.setSignature(signature);
        String requestToBeSend = convertXmlToString(JAXBContext.newInstance(MessageResponseWrapper.class), messageResponseWrapper);


        return ResponseEntity.ok(requestToBeSend);
    }



    public ResponseEntity<String> messageResponseWrapperToPayPort(int code, String message, String fspCode, String receipt, String apiType) throws Exception {
        MessageResponseWrapper messageResponseWrapper = new MessageResponseWrapper();
        ResponseMessageDetail responseMessageDetail = new ResponseMessageDetail(String.valueOf(code), message);

        Optional<FinancialServiceProvider> fsp = financialServiceProviderRepository.findByFspCode(fspCode);

        if (fsp.isEmpty()){
            return ResponseEntity.ok("invalid financial service provide with fspCode"+ fspCode);
        }
        String sender = fsp.get().getFspShortName();

        ApiReqHeaderDto apiReqHeaderDto = getApiRequestHeader(fspCode, receipt, apiType, sender);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessageDetails(responseMessageDetail);
        messageResponse.setHeader(apiReqHeaderDto);

        messageResponseWrapper.setData(messageResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String stringToBeSigned = objectMapper.writeValueAsString(messageResponse);
        String signature = digitalSignature.createSignature(stringToBeSigned);
        messageResponseWrapper.setSignature(signature);

        // Convert messageResponseWrapper to JSON string
        String jsonResponseString = objectMapper.writeValueAsString(messageResponseWrapper);

        return ResponseEntity.ok(jsonResponseString);
    }


    public ResponseEntity<String> messageAccountValidationResponseWrapperToUtumishi(Boolean valid, String message, String fspCode, String receipt, String apiType) throws Exception {
        MessageAccValidationResponseWrapper messageResponseWrapperAcc = new MessageAccValidationResponseWrapper();

        ResponseAccValidationMessageDetail responseMessageDetailAcc = new ResponseAccValidationMessageDetail(valid, message);
        Optional<FinancialServiceProvider> fspByCode = financialServiceProviderRepository.findByFspCode(fspCode);

        String sender = fspByCode.get().getFspShortName();

        ApiReqHeaderDto apiReqHeaderDto = getApiRequestHeader(fspCode, receipt, apiType, sender);
        MessageAccValidationResponse messageResponseAcc  = new MessageAccValidationResponse();
        messageResponseAcc.setMessageDetails(responseMessageDetailAcc);
        messageResponseAcc.setHeader(apiReqHeaderDto);


        String stringToBeSigned = convertXmlToString(JAXBContext.newInstance(MessageAccValidationResponse.class), messageResponseAcc);
        String signature = digitalSignature.createSignature(stringToBeSigned);
        messageResponseWrapperAcc.setData(messageResponseAcc);
        messageResponseWrapperAcc.setSignature(signature);
        String requestToBeSend = convertXmlToString(JAXBContext.newInstance(MessageAccValidationResponseWrapper.class), messageResponseWrapperAcc);


        return ResponseEntity.ok(requestToBeSend);
    }


}
