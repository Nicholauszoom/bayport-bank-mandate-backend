package com.dflex.emkopo.commons.utils;

import java.util.Date;

import lombok.AllArgsConstructor;
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
public class Session {

	private Date todaysDate;
	
	private Integer dayOfYear;
	
	private String shortYear;
	
	private String currentTime;
	
	private Integer hourOfDay;
	
	private Long financialYear;
	
	private Long calendarYear;
	
}
