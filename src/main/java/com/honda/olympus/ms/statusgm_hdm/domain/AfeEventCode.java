package com.honda.olympus.ms.statusgm_hdm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfeEventCode 
{
	private Integer id;
	private Integer eventCodeNumber;
	private String description;
}
