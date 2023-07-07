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
public class AfeService {

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
                logEvent(eventHandler.findFixedOrdersError(jsonResponse));
            }
            return fixedOrder;
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return null;
        }
    }


    public AfeStatus findStatus(Integer fixedOrderId, JsonResponse jsonResponse) {
        try {
            AfeStatus afeStatus = afeRepository.findStatus(fixedOrderId);
            if (afeStatus == null) {
                logEvent(eventHandler.findStatusError(jsonResponse));
            }
            return afeStatus;
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return null;
        }
    }


    public AfeEventCode findEventCodeById(Integer statusId) {
        try {
            AfeEventCode eventCode = afeRepository.findEventCodeById(statusId);
            if (eventCode == null) {
                logEvent(eventHandler.findEventCodeByIdError(statusId));
            }
            return eventCode;
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return null;
        }
    }


    public AfeEventCode findEventCodeByNumber(Integer codeNumber) {
        try {
            return afeRepository.findEventCodeByNumber(codeNumber);
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return null;
        }
    }


    public int insertEventCode(JsonResponse jsonResponse) {
        try {
            int result = afeRepository.insertEventCode(jsonResponse);
            if (result < 1) {
                logEvent(eventHandler.insertEventCodeError());
            }
            return result;
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return -1;
        }
    }

    public int insertStatusHistory(AfeStatus status, AfeEventCode eventCode, JsonResponse jsonResponse) {
        try {
            int result = afeRepository.insertStatusHistory(status, eventCode, jsonResponse);
            if (result < 1) {
                logEventService.logEvent(eventHandler.failInsertStatusHistory());
                this.notificationService.sendNotification(eventHandler.failInsertStatusHistoryNotification());
            }
            return result;
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return -1;
        }
    }

    public int insertEventStatus(AfeFixedOrder fixedOrder, AfeEventCode eventCode, JsonResponse jsonResponse) {
        try {
            int result = afeRepository.insertEventStatus(fixedOrder, eventCode, jsonResponse);
            if (result < 1) {
                logEventService.logEvent(eventHandler.failInsertEventStatus());
                this.notificationService.sendNotification(eventHandler.failInsertStatusHistoryNotification());

            }
            return result;
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return -1;
        }
    }


    public int updateStatus(AfeEventCode eventCode, JsonResponse jsonResponse, AfeStatus status) {
        try {
            int result = afeRepository.updateStatus(eventCode, jsonResponse, status);
            if (result < 1) {
                logEvent(eventHandler.updateStatusError());
            }
            return result;
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return -1;
        }
    }

    public int updateEventStatus(AfeEventCode eventCode, JsonResponse jsonResponse, AfeFixedOrder fixedOrder) {
        try {
            int result = afeRepository.updateEventStatus(eventCode, jsonResponse, fixedOrder);
            if (result < 1) {
                logEvent(eventHandler.updateStatusError());
            }
            return result;
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return -1;
        }
    }


    public int updateFixedOrder(AfeFixedOrder fixedOrder, JsonResponse jsonResponse) {
        try {
            int result = afeRepository.updateFixedOrder(fixedOrder);
            if (result < 1) {
                logEvent(eventHandler.updateFixedOrderError());
            } else {
                logEvent(eventHandler.updateFixedOrderOk(jsonResponse));
            }
            return result;
        } catch (DataAccessException exception) {
            handleDataAccessException(exception);
            return -1;
        }
    }


    private void logEvent(Event event) {
        logEventService.logEvent(event);
    }


    private void handleDataAccessException(DataAccessException exception) {
        Event event = eventHandler.dbConnectionError();
        logEventService.logEvent(event);
        notificationService.sendNotification(eventHandler.dbConnectionNotification());
    }

}
