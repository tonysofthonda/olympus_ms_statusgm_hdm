package com.honda.olympus.ms.statusgm_hdm.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class JsonResponse 
{
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ;
	
	private String vehOrderNbr;
	private String externConfigTypes;
	private String externConfigId;
	private String vehIdentNbr;
	private String modlYrNbr;
	private String sellingSrcCd;
	private String nampltCd;
	private String optionCodes;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
	private LocalDateTime voLastChgTimstm;
	
	private String orderTypeCd;
	private String mdseModlDesgtr;
	private String pegOption;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
	private LocalDateTime targetProdnDt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
	private LocalDateTime estdDelvryDt;
	
	private Integer currVehEvntCd;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
	private LocalDateTime currEvntStatusDt;
	
	private String dirRecBusAstCd;
	private String dirRecBusFcnCd;
	private String shipBusnsAsctCd;
	private String shipBusnsFcnCd;
	private String currVehEvntDesc;
	private String vendorFacingCurrEvntCd;
	private String evntStatus;
}
