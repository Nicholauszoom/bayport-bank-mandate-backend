package com.example.Project.Management.bayport.apis.dto.accountValidationResponse;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageAccValidationResponse {

    @XmlElement(name = "Header")
    private ApiReqHeaderDto header;

    @XmlElement(name = "MessageDetails")
    private ResponseAccValidationMessageDetail messageDetails;
}
