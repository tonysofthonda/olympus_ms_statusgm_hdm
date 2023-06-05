package com.honda.olympus.ms.statusgm_hdm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfeFixedOrder 
{
	private Integer id;
	private Integer requestId;
	private String orderNumber;
	private String envioFlag;
}
