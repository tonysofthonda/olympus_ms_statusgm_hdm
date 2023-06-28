package com.honda.olympus.ms.statusgm_hdm.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.honda.olympus.ms.statusgm_hdm.domain.AfeEventCode;
import com.honda.olympus.ms.statusgm_hdm.domain.AfeFixedOrder;
import com.honda.olympus.ms.statusgm_hdm.domain.AfeStatus;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonResponse;

import javax.servlet.http.HttpServletRequest;

import static com.honda.olympus.ms.statusgm_hdm.util.SqlUtil.getInt;


@Repository
@PropertySource("classpath:query.properties")
public class AfeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HttpServletRequest request;

    @Value("${findFixedOrder}")
    private String findFixedOrderSQL;
    @Value("${findStatus}")
    private String findStatusSQL;
    @Value("${findEventCodeById}")
    private String findEventCodeByIdSQL;
    @Value("${findEventCodeByNumber}")
    private String findEventCodeByNumberSQL;
    @Value("${insertEventCode}")
    private String insertEventCodeSQL;
    @Value("${insertHistory}")
    private String insertStatusHistorySQL;
    @Value("${updateStatus}")
    private String updateStatusSQL;
    @Value("${updateEventStatus}")
    private String updateEventStatusSQL;
    @Value("${updateFixedOrder}")
    private String updateFixedOrderSQL;

    @Value("${insertEventStatus}")
    private String insertEventStatusSQL;


    public AfeFixedOrder findFixedOrder(JsonResponse jsonResponse) {
        List<AfeFixedOrder> list = jdbcTemplate.query(
                findFixedOrderSQL,
                (rs, rowNum) ->
                {
                    AfeFixedOrder fixedOrder = new AfeFixedOrder();

                    fixedOrder.setId(getInt(rs, "id"));
                    fixedOrder.setRequestId(rs.getString("requestId"));

                    return fixedOrder;
                },
                jsonResponse.getVehOrderNbr());

        return list.isEmpty() ? null : list.get(0);
    }


    public AfeStatus findStatus(Integer fixedOrderId) {
        List<AfeStatus> list = jdbcTemplate.query(
                findStatusSQL,
                (rs, rowNum) ->
                {
                    AfeStatus afeStatus = new AfeStatus();

                    afeStatus.setId(getInt(rs, "id"));
                    afeStatus.setEventCodeDate(rs.getObject("eventCodeDate", LocalDateTime.class));
                    afeStatus.setEventCodeId(getInt(rs, "eventCodeId"));

                    return afeStatus;
                },
                fixedOrderId);

        return list.isEmpty() ? null : list.get(0);
    }


    public AfeEventCode findEventCodeById(Integer id) {
        List<AfeEventCode> list = jdbcTemplate.query(
                findEventCodeByIdSQL,
                (rs, rowNum) ->
                {
                    AfeEventCode eventCode = new AfeEventCode();
                    eventCode.setEventCodeNumber(getInt(rs, "eventCodeNumber"));
                    eventCode.setId(getInt(rs, "id"));
                    return eventCode;
                },
                id);

        return list.isEmpty() ? null : list.get(0);
    }


    public AfeEventCode findEventCodeByNumber(Integer codeNumber) {
        List<AfeEventCode> list = jdbcTemplate.query(
                findEventCodeByNumberSQL,
                (rs, rowNum) ->
                {
                    AfeEventCode eventCode = new AfeEventCode();

                    eventCode.setId(getInt(rs, "id"));
                    eventCode.setEventCodeNumber(getInt(rs, "eventCodeNumber"));

                    return eventCode;
                },
                codeNumber
        );

        return list.isEmpty() ? null : list.get(0);
    }


    public int insertEventCode(JsonResponse jsonResponse) {
        return jdbcTemplate.update(insertEventCodeSQL, jsonResponse.getCurrVehEvNtCd(), jsonResponse.getCurrVehEvntDesc());
    }

    public int insertStatusHistory(AfeStatus status,AfeEventCode eventCode, JsonResponse jsonResponse) {
        return jdbcTemplate.update(insertStatusHistorySQL,
                status.getId(),
                eventCode.getId(),
                jsonResponse.getCurrEvntStatusDt(),
                jsonResponse.getCurrVehEvntDesc(),
                this.request.getRemoteAddr() + Instant.now()
        );
    }

    public int insertEventStatus(AfeFixedOrder fixedOrder, AfeEventCode eventCode, JsonResponse jsonResponse) {
        return jdbcTemplate.update(insertEventStatusSQL,
                fixedOrder.getId(),
                jsonResponse.getExternConfigTypes(),
                jsonResponse.getVoLastChgTimstm(),
                jsonResponse.getPegOption(),
                jsonResponse.getTargetProdnDt(),
                jsonResponse.getEstdDelvryDt(),
                eventCode.getId(),
                this.request.getRemoteAddr() + Instant.now(),
                1
        );
    }


    public int updateStatus(AfeEventCode eventCode, JsonResponse jsonResponse, AfeStatus status) {

        return jdbcTemplate.update(updateEventStatusSQL,
                jsonResponse.getExternConfigTypes(),
                jsonResponse.getVoLastChgTimstm(),
                jsonResponse.getPegOption(),
                jsonResponse.getTargetProdnDt(),
                jsonResponse.getEstdDelvryDt(),
                eventCode.getId(),
                this.request.getRemoteAddr() + Instant.now(),
                status.getId()
        );
    }


    public int updateEventStatus(AfeEventCode eventCode, JsonResponse jsonResponse, AfeFixedOrder fixedOrder) {
        return jdbcTemplate.update(updateStatusSQL, eventCode.getId(), jsonResponse.getCurrEvntStatusDt(), fixedOrder.getId());
    }


    public int updateFixedOrder(AfeFixedOrder fixedOrder) {
        return jdbcTemplate.update(updateFixedOrderSQL, fixedOrder.getId());
    }

}
