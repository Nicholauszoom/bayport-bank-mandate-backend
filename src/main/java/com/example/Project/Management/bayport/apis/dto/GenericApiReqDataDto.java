package com.example.Project.Management.bayport.apis.dto;


import com.example.Project.Management.bayport.charge.dto.charges.LoanChargesApiRespDetailDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "Data")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({LoanChargesApiRespDetailDto.class})
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericApiReqDataDto<T> {

    @XmlElement(name = "Header")
    private ApiReqHeaderDto requestHeader;

    @XmlElement(name = "MessageDetails")
    private T messageDetails;

}