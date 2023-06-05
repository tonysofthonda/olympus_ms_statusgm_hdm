package com.honda.olympus.ms.statusgm_hdm.property;

import org.springframework.stereotype.Component;

import lombok.Getter;


@Getter
@Component
public class Query 
{
	private final String findFixedOrderKEY = "findFixedOrder";
	private final String findStatusKEY  = "findStatus";
	private final String findEventCodeByIdKEY = "findEventCodeById";
	private final String findEventCodeByNumberKEY = "findEventCodeByNumber";
	private final String insertEventCodeKEY = "insertEventCode";
	private final String updateStatusKEY = "updateStatus";
	private final String updateFixedOrderKEY = "updateFixedOrder";
}
