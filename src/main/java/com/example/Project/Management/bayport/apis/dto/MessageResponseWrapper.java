package com.example.Project.Management.bayport.apis.dto;


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
@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageResponseWrapper {

    @XmlElement(name = "Data")
    private MessageResponse data;

    @XmlElement(name = "Signature")
    private String signature;
}
