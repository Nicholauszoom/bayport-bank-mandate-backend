package com.example.Project.Management.bayport.product.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

/**
 * 
 * @author Augustino Mwageni
 *
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRegistrationApiReqDto {
	
	@XmlElement(name = "Data")
	private ProductRegistrationApiReqDataDto productRegistrationData;
	
	@XmlElement(name = "Signature")
	private String messageHash;
	
}
