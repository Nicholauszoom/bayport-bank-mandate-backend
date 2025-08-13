package com.dflex.emkopo.api.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@XmlRootElement(name = "Header")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiReqHeaderDto {
	
	@XmlElement(name = "Sender")
	private String sender;
	
	@XmlElement(name = "Receiver")
	private String receiver;
	
	@XmlElement(name = "FSPCode")
	private String fspCode;
	
	@XmlElement(name = "MsgId")
	private String msgId;
	
	@XmlElement(name = "MessageType")
	private String messageType;

}
