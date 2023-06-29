package com.honda.olympus.ms.statusgm_hdm.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.honda.olympus.ms.statusgm_hdm.domain.AfeEventCode;
import com.honda.olympus.ms.statusgm_hdm.domain.AfeFixedOrder;
import com.honda.olympus.ms.statusgm_hdm.domain.AfeStatus;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonResponse;
import com.honda.olympus.ms.statusgm_hdm.service.StatusGmHdmService;


@SpringBootTest
@MockBean(StatusGmHdmService.class)
@TestMethodOrder(OrderAnnotation.class)
public class AfeRepositorySpec 
{
	
	@Autowired
	AfeRepository afeRepo;
	
	
	@Test
	@Order(1)
	void afeRepositoryIsNotNull() {
		assertNotNull(afeRepo);
	}
	
	
	@Test
	@Order(2)
	void shouldFindFixedOrder() {
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setVehOrderNbr("ORD123");
		
		AfeFixedOrder fixedOrder = afeRepo.findFixedOrder(jsonResponse);
		assertNotNull(fixedOrder);
	}
	
	
	@Test
	@Order(3)
	void shouldFindStatus() {
		AfeFixedOrder fixedOrder = new AfeFixedOrder();
		fixedOrder.setId(3);
		
		AfeStatus afeStatus = afeRepo.findStatus(fixedOrder.getId());
		assertNotNull(afeStatus);
	}
	
	
	@Test
	@Order(4)
	void shouldFindEventCodeById() {
		AfeStatus afeStatus = new AfeStatus();
		afeStatus.setEventCodeDate( Timestamp.valueOf("2023-05-17 22:37:25.064").toLocalDateTime() );
		afeStatus.setEventCodeId(4);
		
		AfeEventCode eventCode = afeRepo.findEventCodeById(afeStatus.getId());
		assertNotNull(eventCode);
	}
	
	
	@Test
	@Order(5)
	void shouldFindEventCodeByNumber() {
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setCurrVehEvNtCd(2499);
		
		AfeEventCode eventCode = afeRepo.findEventCodeByNumber(jsonResponse.getCurrVehEvNtCd());
		assertNotNull(eventCode);
	}
	
	
	@Test
	@Order(6)
	@Disabled
	void shouldInsertEventCode() {
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setCurrVehEvNtCd(2500);
		jsonResponse.setCurrVehEvntDesc("detail description");
		
		int result = afeRepo.insertEventCode(jsonResponse);
		assertTrue(result == 1);
	}
	
	
	@Test 
	@Order(7)
	@Disabled
	void shouldUpdateStatus() {
		AfeEventCode eventCode = new AfeEventCode();
		eventCode.setId(1);
		
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setCurrEvntStatusDt( Timestamp.valueOf("2023-06-02 22:37:25.064").toLocalDateTime() );
		
		AfeFixedOrder fixedOrder = new AfeFixedOrder();
		fixedOrder.setId(3);
		
		int result = afeRepo.updateStatus(eventCode, jsonResponse, new AfeStatus());
		assertTrue(result == 1);
	}
	
	
	@Test
	@Order(8)
	@Disabled
	void shouldUpdateFixedOrder() {
		AfeFixedOrder fixedOrder = new AfeFixedOrder();
		fixedOrder.setId(3);
		
		int result = afeRepo.updateFixedOrder(fixedOrder);
		assertTrue(result == 1);
	}
	
}
