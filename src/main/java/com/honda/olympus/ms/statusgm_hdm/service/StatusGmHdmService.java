package com.honda.olympus.ms.statusgm_hdm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.honda.olympus.ms.statusgm_hdm.domain.AfeEventCode;
import com.honda.olympus.ms.statusgm_hdm.domain.AfeFixedOrder;
import com.honda.olympus.ms.statusgm_hdm.domain.AfeStatus;
import com.honda.olympus.ms.statusgm_hdm.domain.Event;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonMaxTransit;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonResponse;
import com.honda.olympus.ms.statusgm_hdm.domain.Message;
import com.honda.olympus.ms.statusgm_hdm.domain.Status;
import com.honda.olympus.ms.statusgm_hdm.handler.MaxTransitEventHandler;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class StatusGmHdmService 
{
	
	@Autowired 
	private AfeService afeService;
	@Autowired 
	private MaxTransitService maxTransitService;
	
	@Autowired 
	private LogEventService logEventService;
	@Autowired 
	private NotificationService notificationService;
	@Autowired
	private GenstaafeHdmService genstaafeHdmService;
	
	@Autowired 
	private MaxTransitEventHandler eventHandler;
	
	
	@Scheduled(fixedDelayString = "${timelapse}", timeUnit = TimeUnit.MINUTES)
	public void launchProcess() 
	{
		log.info("\n\n<<<<<<<  Process StatusGmHdm Start  >>>>>>>\n");
		
		
		log.info(">>> sending jsonMaxTransit");
		JsonMaxTransit request = new JsonMaxTransit("request", new ArrayList<>());
		List<JsonResponse> jsonRespList = maxTransitService.sendRequest(request);
		
		
		log.info(">>> validating response");
		if (jsonRespList == null) return;
		if (jsonRespList.isEmpty()) {
			logEvent( eventHandler.emptyResponseError() );
			return;
		}
		
		
		log.info(">>> iterating response list");
		List<String> details = new ArrayList<>();
		
		for (JsonResponse jsonResp: jsonRespList) 
		{
			log.info("\n\n::::::: {} :::::::\n", jsonResp.toString());
			
			log.info(">>> validating vehOrderNbr");
			// jsonResp.getVehOrderNbr() == null || jsonResp.getVehOrderNbr() < 0
			if (!StringUtils.hasText( jsonResp.getVehOrderNbr() )) {
				logEvent( eventHandler.vehOrderNbrError(jsonResp) );
				continue;
			}
			
			log.info(">>> finding fixed order");
			AfeFixedOrder fixedOrder = afeService.findFixedOrders(jsonResp);
			if (fixedOrder == null) continue;
			
			
			log.info(">>> finding status");
			AfeStatus status = afeService.findStatus(fixedOrder, jsonResp);
			if (status == null) continue;
			
			
			log.info(">>> finding event code by id");
			AfeEventCode eventCode = afeService.findEventCodeById(status);
			if (eventCode == null) continue;
			
			
			log.info(">>> validating currVehEvntCd and currEvntStatusDt");
			boolean isNewer = jsonResp.getCurrVehEvntCd() > eventCode.getEventCodeNumber() && 
					          jsonResp.getCurrEvntStatusDt().isAfter(status.getEventCodeDate());	
			if (!isNewer) {
				logEvent( eventHandler.statusDateError(jsonResp) );
				continue;
			}
			
			
			log.info(">>> validating currVehEvntCd range");
			boolean inRange = jsonResp.getCurrVehEvntCd() >= 1_000 && jsonResp.getCurrVehEvntCd() <= 5_000; 
			if (!inRange) {
				logEvent( eventHandler.statusCodeError(jsonResp) );
				continue;
			}
			
			
			log.info(">>> finding event code by number");
			eventCode = afeService.findEventCodeByNumber(jsonResp);
			if (eventCode == null) 
			{
				log.info(">>> inserting new event code");
				int result = afeService.insertEventCode(jsonResp);
				if (result < 1) continue;
			}
			
			
			log.info(">>> updating status");
			int result = afeService.updateStatus(eventCode, jsonResp, fixedOrder);
			if (result < 1) continue;
			
			
			log.info(">>> updating fixed order");
			result = afeService.updateFixedOrder(fixedOrder, jsonResp);
			if (result >= 1) 
			{
				log.info("<<< gathering fixed order id ");
				details.add( String.valueOf(fixedOrder.getId()) );
			}
			
		}//for end	
		
		
		if (!details.isEmpty()) {
			log.info("<<< sending message to ms.genstaafe_hdm");
			genstaafeHdmService.sendMessage( new Message(Status._SUCCESS, Status.SUCCESS, details) );
		}
		
		log.info("\n\n<<<<<<<  Process StatusGmHdm End >>>>>>>\n");
	}
	
	
	private void logEvent(Event event) {
		log.error("### {}", event.getMsg());
		logEventService.logEvent(event);
		notificationService.sendNotification(event);
	}
	
}
