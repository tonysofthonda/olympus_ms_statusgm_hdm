package com.honda.olympus.ms.statusgm_hdm.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.honda.olympus.ms.statusgm_hdm.domain.AfeStatus;
import com.honda.olympus.ms.statusgm_hdm.domain.Event;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonResponse;
import com.honda.olympus.ms.statusgm_hdm.domain.Status;
import com.honda.olympus.ms.statusgm_hdm.property.Properties;
import com.honda.olympus.ms.statusgm_hdm.property.Query;
import com.honda.olympus.ms.statusgm_hdm.property.Service;


@Component
public class AfeEventHandler {

    private static final String MSG_DB_CONNECTION_ERROR = "No es posible conectarse a la base de datos %s, %s, %s, %s";
    private static final String MSG_FIND_FIXED_ORDERS_ERROR = "NO se encontró el veh_order_nbr '%s' en la tabla AFE_FIXED_ORDER_EV con el query 1 (%s)";
    private static final String MSG_FIND_STATUS_ERROR = "NO se encontró el número de orden '%s' en la tabla AFE_STATUS_EV con el query 2 (%s)";
    private static final String MSG_FIND_EVENT_CODE_BY_ID_ERROR = "NO se encontró el codigo de estatus '%s' en la tabla EVENT_CODE con el query 3 (%s)";
    private static final String MSG_INSERT_EVENT_CODE_ERROR = "Fallo en la ejecución del query de inserción en la tabla EVENT_CODE con el query 5 (%s)";
    private static final String MSG_UPDATE_STATUS_ERROR = "Fallo en la ejecución del query de actualización en la tabla AFE_STATUS_EV con el query 6 (%s)";
    private static final String MSG_UPDATE_FIXED_ORDER_OK = "El proceso fue realizado con éxito para la orden '%s' y estatus '%s'";
    private static final String MSG_UPDATE_FIXED_ORDER_ERROR = "Fallo en la ejecución del query de actualización en la tabla AFE_FIXED_ORDER_EV con el query 7 (%s)";
    private static final String MSG_FAIL_UPDATE_EVENT = "Fallo de ejecución del query de actualización en la tabla '%s' con el query '%s'";
    private static final String MSG_FAIL_INSERT = "Fallo en la ejecución del query de inserción en la tabla '%s' con el query '%s'";

    private static final String EMPTY = "";


    @Autowired
    private Properties props;
    @Autowired
    private Service service;
    @Autowired
    private Query query;


    public Event dbConnectionError() {
        String message = String.format(MSG_DB_CONNECTION_ERROR, props.getDbname(), props.getDbhost(), props.getDbport(), props.getDbuser());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event findFixedOrdersError(JsonResponse jsonResponse) {
        String message = String.format(MSG_FIND_FIXED_ORDERS_ERROR, jsonResponse.getVehOrderNbr(), query.getFindFixedOrderKEY());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event findStatusError(JsonResponse jsonResponse) {
        String message = String.format(MSG_FIND_STATUS_ERROR, jsonResponse.getVehOrderNbr(), query.getFindStatusKEY());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event findEventCodeByIdError(Integer statusId) {
        String message = String.format(MSG_FIND_EVENT_CODE_BY_ID_ERROR, statusId, query.getFindEventCodeByIdKEY());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event insertEventCodeError() {
        String message = String.format(MSG_INSERT_EVENT_CODE_ERROR, query.getInsertEventCodeKEY());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event updateStatusError() {
        String message = String.format(MSG_UPDATE_STATUS_ERROR, query.getUpdateStatusKEY());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event updateFixedOrderOk(JsonResponse jsonResponse) {
        String message = String.format(MSG_UPDATE_FIXED_ORDER_OK, jsonResponse.getVehOrderNbr(), jsonResponse.getCurrVehEvNtCd());
        return new Event(service.getServiceName(), Status._SUCCESS, message, EMPTY);
    }


    public Event updateFixedOrderError() {
        String message = String.format(MSG_UPDATE_FIXED_ORDER_ERROR, query.getUpdateFixedOrderKEY());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }

    public Event failUpdateEvent() {
        return new Event(service.getServiceName(), Status._FAIL,
                String.format(MSG_FAIL_UPDATE_EVENT, "AGE_EVENT_STATUS", query.getUpdateEventStatusKEY()),
                EMPTY);
    }

    public Event failInsertStatusHistory() {
        return new Event(service.getServiceName(), Status._FAIL,
                String.format(MSG_FAIL_INSERT, "AFE_EVENT_STATUS_HISTORY", query.getInsertHistoryKEY()),
                EMPTY);
    }

    public Event failInsertEventStatus() {
        return new Event(service.getServiceName(), Status._FAIL,
                String.format(MSG_FAIL_INSERT, "AFE_EVENT_STATUS", query.getInsertEventStatusKEY()), EMPTY);
    }


}
