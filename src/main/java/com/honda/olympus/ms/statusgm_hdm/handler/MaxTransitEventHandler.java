package com.honda.olympus.ms.statusgm_hdm.handler;

import com.honda.olympus.ms.statusgm_hdm.domain.AfeStatus;
import com.honda.olympus.ms.statusgm_hdm.property.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.honda.olympus.ms.statusgm_hdm.domain.Event;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonResponse;
import com.honda.olympus.ms.statusgm_hdm.domain.Status;
import com.honda.olympus.ms.statusgm_hdm.property.Properties;
import com.honda.olympus.ms.statusgm_hdm.property.Service;


@Component
public class MaxTransitEventHandler {

    private static final String MSG_TIMEOUT_ERROR = "Tiempo de espera agotado en la consulta a la API MAX TRANSIT ubicada en '%s'";
    private static final String MSG_HTTP_STATUS_ERROR = "La API de max transit retorno un error: %s";
    private static final String MSG_EMPTY_RESPONSE_ERROR = "La respuesta de MAXTRANSIT no tiene elementos";
    private static final String MSG_VEH_ORDER_NBR_ERROR = "NO tiene un valor mayor o igual a cero el veh_order_nbr '%s'";
    private static final String MSG_VEH_ORDER_NBR_NOTIFICATION = "105 Error al guardar en MaxTransit. El veh_order_nbr, No tiene información";
    private static final String MSG_STATUS_DATE_ERROR = "La fecha del estatus '%s' y el codigo del estatus '%s' son menores a los actuales";
    private static final String MSG_STATUS_CODE_ERROR = "El codigo del estatus '%s' es menor a 1000 o mayor a 5000";
    private static final String MSG_NO_CONN_ERROR = "Sin conexión a la API MAX TRANSIT";
    private static final String MSG_MAXTRANSIT_CODE_NUMBER = "El code number '%s' es menor del estatus actual";
    private static final String MSG_EVENT_CODE_NOT_FOUND = "NO se encontró número de código %s en la tabla EVENT_CODE con el query %s";
    private static final String MSG_UPDATE_STATUS_SUCCESS = "La actualización en la tabla EVENT_STATUS se realizo de manera exitosa.";
    private static final String MSG_INSERT_HISTORY_STATUS_SUCCESS = "La inserción a AFE_EVENT_STATUS_HISTORY se realizo de manera exitosa.";
    private static final String MSG_TIMEOUT_NOTIFICATION = "105 No se guardo correctamente. El API MAXTRANSIT retorno un error";
    private static final String MSG_EMPTY_RESPONSE_NOTIFICATION = "105 No se guardo correctamente. El API MAXTRANSIT retorno una respuesta sin detalles";
    private static final String MSG_EMPTY_ORDER_NUMBER_ERROR = "105 Error al guardar En MaxTransit. Error al obtener la información";
    private static final String MSG_NO_EVENT_CODE_ERROR = "No se encontró el event code %s en la tabla EVENT_CODE";
    private static final String MSG_NO_STATUS_FIND = "105 Error al guardar en MaxTransit. Error el curr_veh_nt_cd NO corresponde con la secuencia de creación";


    private static final String EMPTY = "";


    @Autowired
    private Query query;
    @Autowired
    private Properties props;
    @Autowired
    private Service service;


    public Event timeoutError() {
        String message = String.format(MSG_TIMEOUT_ERROR, props.getUrlmax());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event httpStatusError(String response) {
        String message = String.format(MSG_HTTP_STATUS_ERROR, response);
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event emptyResponseError() {
        return new Event(service.getServiceName(), Status._FAIL, MSG_EMPTY_RESPONSE_ERROR, EMPTY);
    }

    public Event emptyResponseNotification() {
        return new Event(service.getServiceName(), Status._FAIL, MSG_EMPTY_RESPONSE_NOTIFICATION, EMPTY);
    }

    public Event vehOrderNbrError(JsonResponse response) {
        String message = String.format(MSG_VEH_ORDER_NBR_ERROR, response.getVehOrderNbr());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }

    public Event vehOrderNbrNotification() {
        return new Event(service.getServiceName(), Status._FAIL, MSG_VEH_ORDER_NBR_NOTIFICATION, EMPTY);
    }


    public Event statusDateError(JsonResponse response) {
        String message = String.format(MSG_STATUS_DATE_ERROR, response.getCurrEvntStatusDt(), response.getCurrVehEvNtCd());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event statusCodeError(JsonResponse response) {
        String message = String.format(MSG_STATUS_CODE_ERROR, response.getCurrVehEvNtCd());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }

    public Event eventCodeNotFoundError(JsonResponse response) {
        String message = String.format(MSG_EVENT_CODE_NOT_FOUND, response.getCurrVehEvNtCd(), query.getFindEventCodeByNumberKEY());
        return new Event(service.getServiceName(), Status._FAIL, message, EMPTY);
    }


    public Event noConnectionError() {
        return new Event(service.getServiceName(), Status._FAIL, MSG_NO_CONN_ERROR, EMPTY);
    }

    public Event maxtransitCodeNumber(JsonResponse response) {
        return new Event(service.getServiceName(), Status._FAIL, String.format(MSG_MAXTRANSIT_CODE_NUMBER, response.getCurrVehEvNtCd()), EMPTY);
    }

    public Event updateStatusSuccess() {
        return new Event(service.getServiceName(), Status._SUCCESS, MSG_UPDATE_STATUS_SUCCESS, EMPTY);
    }

    public Event insertStatusHistorySuccess() {
        return new Event(service.getServiceName(), Status._FAIL, MSG_INSERT_HISTORY_STATUS_SUCCESS, EMPTY);
    }

    public Event timeoutNotification() {
        return new Event(service.getServiceName(), Status._FAIL, MSG_TIMEOUT_NOTIFICATION, EMPTY);
    }

    public Event maxtransitError() {
        return new Event(service.getServiceName(), Status._FAIL, MSG_EMPTY_ORDER_NUMBER_ERROR, EMPTY);
    }

    public Event eventCodeError(AfeStatus status) {
        return new Event(service.getServiceName(), Status._FAIL, String.format(MSG_NO_EVENT_CODE_ERROR, status.getEventCodeId()), EMPTY);
    }

    public Event noStatusFindNotification() {
        return new Event(service.getServiceName(), Status._FAIL, MSG_NO_STATUS_FIND, EMPTY);
    }
}
