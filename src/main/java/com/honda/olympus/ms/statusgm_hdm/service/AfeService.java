package com.honda.olympus.ms.statusgm_hdm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.honda.olympus.ms.statusgm_hdm.domain.AfeEventCode;
import com.honda.olympus.ms.statusgm_hdm.domain.AfeFixedOrder;
import com.honda.olympus.ms.statusgm_hdm.domain.AfeStatus;
import com.honda.olympus.ms.statusgm_hdm.domain.Event;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonResponse;
import com.honda.olympus.ms.statusgm_hdm.handler.AfeEventHandler;
import com.honda.olympus.ms.statusgm_hdm.repository.AfeRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AfeService 
{	
	
	@Autowired 
	private AfeRepository afeRepository;
	
	@Autowired 
	private LogEventService logEventService;
	@Autowired 
	private NotificationService notificationService;
	
	@Autowired
	private AfeEventHandler eventHandler;
	
	
	public AfeFixedOrder findFixedOrders(JsonResponse jsonResponse) {
		try {
			AfeFixedOrder fixedOrder = afeRepository.findFixedOrder(jsonResponse);
			if (fixedOrder == null) {
				logEvent( eventHandler.findFixedOrdersError(jsonResponse) );
			}
			return fixedOrder;
		} 
		catch (DataAccessException exception) {
			handleDataAccessException(exception);
			return null;
		}
	}
	

	public AfeStatus findStatus(AfeFixedOrder fixedOrder, JsonResponse jsonResponse) {
		try {
			AfeStatus afeStatus = afeRepository.findStatus(fixedOrder);
			if (afeStatus == null) {
				logEvent( eventHandler.findStatusError(jsonResponse) );
			}
			return afeStatus;
		} 
		catch (DataAccessException exception) {
			handleDataAccessException(exception);
			return null;
		}
	}
	
	
	public AfeEventCode findEventCodeById(AfeStatus status) {
		try {
			AfeEventCode eventCode = afeRepository.findEventCodeById(status);
			if (eventCode == null) {
				logEvent( eventHandler.findEventCodeByIdError(status) );
			}
			return eventCode;
		}
		catch (DataAccessException exception) {
			handleDataAccessException(exception);
			return null;
		}
	}
	
	
	public AfeEventCode findEventCodeByNumber(JsonResponse jsonResponse) {
		try {
			return afeRepository.findEventCodeByNumber(jsonResponse);
		}
		catch (DataAccessException exception) {
			handleDataAccessException(exception);
			return null;
		}
	}
	
	
	public int insertEventCode(JsonResponse jsonResponse) {
		try {
			int result = afeRepository.insertEventCode(jsonResponse);
			if (result < 1) {
				logEvent( eventHandler.insertEventCodeError() );
			}
			return result;
		}
		catch (DataAccessException exception) {
			handleDataAccessException(exception);
			return -1;
		}
	}
	
	
	public int updateStatus(AfeEventCode eventCode, JsonResponse jsonResponse, AfeFixedOrder fixedOrder) {
		try {
			int result = afeRepository.updateStatus(eventCode, jsonResponse, fixedOrder);
			if (result < 1) {
				logEvent( eventHandler.updateStatusError() );
			}
			return result;
		}
		catch (DataAccessException exception) {
			handleDataAccessException(exception);
			return -1;
		}
	}
	
	
	public int updateFixedOrder(AfeFixedOrder fixedOrder, JsonResponse jsonResponse) {
		try {
			int result = afeRepository.updateFixedOrder(fixedOrder);
			if (result < 1) {
				logEvent( eventHandler.updateFixedOrderError() );
			}
			else {
				logEvent( eventHandler.updateFixedOrderOk(jsonResponse) );
			}
			return result;
		}
		catch (DataAccessException exception) {
			handleDataAccessException(exception);
			return -1;
		}
	}
	
	
	private void logEvent(Event event) {
		log.error("### {}", event.getMsg());
		logEventService.logEvent(event);
	}
	
	
	private void handleDataAccessException(DataAccessException exception) {
		log.error("### Error found while connecting to DB: {}", exception);
		Event event = eventHandler.dbConnectionError();
		logEventService.logEvent(event);
		notificationService.sendNotification(event);
	}
	
}
