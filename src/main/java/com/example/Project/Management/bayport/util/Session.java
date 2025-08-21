package com.example.Project.Management.bayport.util;

import lombok.*;

import java.util.Date;

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
