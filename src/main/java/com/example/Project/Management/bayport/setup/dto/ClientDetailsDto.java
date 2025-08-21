package com.example.Project.Management.bayport.setup.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author Augustino Mwageni
 * 
 * DTO Class for Database Table tab_payment_facilitator and tab_service_institution
 *
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
public class ClientDetailsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String clientCode;

	private String clientName;

	private Long clientCategoryId;
	
	private Long clientStatusId;
}