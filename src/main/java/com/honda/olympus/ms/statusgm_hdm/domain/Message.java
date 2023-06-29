package com.honda.olympus.ms.statusgm_hdm.domain;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message 
{
	private Integer status;
	private String msg; 
	private List<Map<String, Integer>> details;
}
