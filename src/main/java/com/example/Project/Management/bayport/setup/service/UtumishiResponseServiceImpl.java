package com.dflex.emkopo.app.setup.service;


import com.dflex.emkopo.app.loan.dto.registration.LoanRegistrationApiReqDto;
import com.dflex.emkopo.app.loan.service.LoanServiceImpl;
import com.dflex.emkopo.app.setup.dto.response.ResponseApiDto;
import com.dflex.emkopo.app.setup.dto.response.portal.ResponseApiWrapperDto;
import com.dflex.emkopo.app.setup.entity.UtumishiResponse;
import com.dflex.emkopo.app.setup.repository.UtumishiResponseRepository;
import com.dflex.emkopo.commons.utils.RabbitMqParameters;
import com.dflex.emkopo.commons.utils.ResponseCodes;
import com.dflex.emkopo.commons.utils.Utils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UtumishiResponseServiceImpl implements UtumishiResponseService{

    private static final Logger logger = LogManager.getLogger(LoanServiceImpl.class);
    private UtumishiResponseRepository utumishiResponseRepository;

    @Autowired
    private RabbitMqParameters rabbitMqParameters;

    @Autowired
    private Utils utils;

    @Autowired
    public UtumishiResponseServiceImpl(UtumishiResponseRepository utumishiResponseRepository, RabbitMqParameters rabbitMqParameters, Utils util) {
        this.utumishiResponseRepository = utumishiResponseRepository;
        this.rabbitMqParameters = rabbitMqParameters;
        this.utils = util;
    }


//    @Override
//    public UtumishiResponse saveUtumishiResponse(UtumishiResponse utumishiResponse) {
//
//        return utumishiResponseRepository.save(utumishiResponse);
//    }

    @Override
    public ResponseEntity<String> saveUtumishiResponse(String message) throws Exception {
        logger.info("Received response from utumishi: {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(ResponseApiDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(message);
        ResponseApiDto responseApiDto = (ResponseApiDto) unmarshaller.unmarshal(stringReader);
        String fspCode = responseApiDto.getResponseDataDto().getRequestHeader().getFspCode();
        String mssId = responseApiDto.getResponseDataDto().getRequestHeader().getMsgId();
        try {
            Date currentTime = new Date();
            UtumishiResponse utumishiResponse = new UtumishiResponse();
            utumishiResponse.setResponseCode(responseApiDto.getResponseDataDto().getResponseDetailDto().getResponseCode());
            utumishiResponse.setResponseDescription(responseApiDto.getResponseDataDto().getResponseDetailDto().getDescription());
            utumishiResponse.setResponseReceivedDate(currentTime);
//            utumishiResponseRepository.save(utumishiResponse);

            System.out.println("saved utumishi response ");

            ResponseApiWrapperDto responseApiWrapperDto = new ResponseApiWrapperDto();
            responseApiWrapperDto.setResponseCode(responseApiDto.getResponseDataDto().getResponseDetailDto().getResponseCode());
            responseApiWrapperDto.setDescription(responseApiDto.getResponseDataDto().getResponseDetailDto().getDescription());

            utils.publishMsgToExchange(
                    rabbitMqParameters.getExchange(),
                    rabbitMqParameters.getProductUtumishiRoutingKey(),
                    responseApiWrapperDto, new HashMap<>());

            logger.info("utumishi resposne success saved");

           return ResponseEntity.ok("success");
        } catch (Exception e) {
            logger.info("utumishi response fail while save");
            return ResponseEntity.ok(e.getMessage());
        }
    }

        @Override
        public Optional<UtumishiResponse> findById ( long id){
            return utumishiResponseRepository.findById(id);
        }

}
