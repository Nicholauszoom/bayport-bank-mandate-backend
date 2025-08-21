package com.example.Project.Management.bayport.apis.dto;

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
