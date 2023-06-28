package com.honda.olympus.ms.statusgm_hdm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.honda.olympus.ms.statusgm_hdm.handler.AfeEventHandler;
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
public class StatusGmHdmService {

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

    @Autowired
    private AfeEventHandler afeEventHandler;


    @Scheduled(fixedDelayString = "${timelapse}", timeUnit = TimeUnit.MINUTES)
    public void launchProcess() {
        log.info("Process StatusGmHdm Start");

        JsonMaxTransit request = new JsonMaxTransit("STATUS", new ArrayList<>());
        List<JsonResponse> jsonRespList = maxTransitService.sendRequest(request);

        if (jsonRespList == null) {
            return;
        }

        if (jsonRespList.isEmpty()) {
            logEvent(eventHandler.emptyResponseError());
            return;
        }

        List<Map<String, Integer>> details = new ArrayList<>();

        for (JsonResponse jsonResp : jsonRespList) {
            if (!StringUtils.hasText(jsonResp.getVehOrderNbr())) {
                logEvent(eventHandler.vehOrderNbrError(jsonResp));
                continue;
            }

            AfeFixedOrder fixedOrder = afeService.findFixedOrders(jsonResp);
            if (fixedOrder == null) continue;


            AfeStatus status = afeService.findStatus(fixedOrder.getId(), jsonResp);

            if (status == null) {
                Event event = new Event();
                event.setStatus(Status._FAIL);
                event.setSource("ms.statusgm_hdm");
                if (jsonResp.getCurrVehEvNtCd() != null && jsonResp.getCurrVehEvNtCd() != 2_000) {
                    event.setMsg(String.format("El status '%s' NO corresponde con la secuencia de creación", jsonResp.getCurrVehEvNtCd()));
                    logEventService.logEvent(event);
                    continue;
                }

                AfeEventCode eventCode = afeService.findEventCodeByNumber(jsonResp.getCurrVehEvNtCd());

                if (eventCode == null) {
                    event.setMsg(String.format("NO se encontro numero de codigo '%s' en la tabla EVENT_CODE con el query '%s'", jsonResp.getCurrVehEvNtCd(), "findEventCodeByNumber"));
                    logEventService.logEvent(event);
                    continue;
                }

                int eventStatus = afeService.insertEventStatus(fixedOrder, eventCode, jsonResp);
                if (eventStatus < 1) {
                    continue;
                }

                status = afeService.findStatus(fixedOrder.getId(), jsonResp);
                afeService.insertStatusHistory(status, eventCode, jsonResp);
                continue;
            }

            AfeEventCode eventCode = afeService.findEventCodeById(status.getEventCodeId());
            if (eventCode == null) continue;

            if (!(eventCode.getEventCodeNumber() > (jsonResp.getCurrVehEvNtCd() != null ? jsonResp.getCurrVehEvNtCd() : 0))) {
                logEvent(eventHandler.maxtransitCodeNumber(jsonResp));
                continue;
            }

            eventCode = afeService.findEventCodeByNumber(jsonResp.getCurrVehEvNtCd());
            if (eventCode == null) {
                continue;
            }

            int result = afeService.updateStatus(eventCode, jsonResp, status);
            if (result < 1) {
                logEvent(afeEventHandler.failUpdateEvent());
                continue;
            }

            int eventCodeResult = afeService.insertStatusHistory(status, eventCode, jsonResp);

            if (eventCodeResult >= 1) {
                HashMap<String, Integer> fixedOrderMap = new HashMap<>();
                fixedOrderMap.put("fixed_order_id", fixedOrder.getId());
                details.add(fixedOrderMap);
            }

        }//for end


        if (!details.isEmpty()) {
            genstaafeHdmService.sendMessage(new Message(Status._SUCCESS, Status.SUCCESS, details));
        }

        log.info("Process StatusGmHdm End");


    }


    private void logEvent(Event event) {
        logEventService.logEvent(event);
        notificationService.sendNotification(event);
    }

}
