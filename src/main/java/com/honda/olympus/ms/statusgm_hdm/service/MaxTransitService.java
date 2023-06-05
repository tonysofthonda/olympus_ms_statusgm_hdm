package com.honda.olympus.ms.statusgm_hdm.service;

import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.honda.olympus.ms.statusgm_hdm.client.MaxTransitClient;
import com.honda.olympus.ms.statusgm_hdm.domain.Event;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonMaxTransit;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonResponse;
import com.honda.olympus.ms.statusgm_hdm.handler.MaxTransitEventHandler;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MaxTransitService 
{
	
	@Autowired
	private MaxTransitClient maxTransitClient;
	
	@Autowired 
	private LogEventService logEventService;
	@Autowired 
	private NotificationService notificationService;
	
	@Autowired
	private MaxTransitEventHandler eventHandler; 
	
	
	public List<JsonResponse> sendRequest(JsonMaxTransit jsonMaxTransit) {
		try {
			return maxTransitClient.sendRequest(jsonMaxTransit).getBody();
		}
		catch (ResourceAccessException exception) {
			if (exception.getCause() instanceof SocketTimeoutException) {
				sendEvent(eventHandler.timeoutError(), null);
				return null;
			}
			sendEvent(eventHandler.noConnectionError(), exception);
			return null;
		}
		catch (HttpClientErrorException exception) {
			sendEvent(eventHandler.httpStatusError(exception.getResponseBodyAsString()), null);
			return null;
		}
	}
	
	
	private void sendEvent(Event event, Exception exception) {
		log.error("### {}", event.getMsg(), exception);
		logEventService.logEvent(event);
		notificationService.sendNotification(event);
	}
	
}
