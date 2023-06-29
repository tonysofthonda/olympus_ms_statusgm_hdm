package com.honda.olympus.ms.statusgm_hdm.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfeStatus 
{
	private Integer id;
	private LocalDateTime eventCodeDate;
	private Integer eventCodeId;
	private Integer fixedOrderId;
}
