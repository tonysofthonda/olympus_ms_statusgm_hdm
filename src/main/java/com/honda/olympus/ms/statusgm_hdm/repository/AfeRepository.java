package com.honda.olympus.ms.statusgm_hdm.repository;

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

import static com.honda.olympus.ms.statusgm_hdm.util.SqlUtil.getInt;


@Repository
@PropertySource("classpath:query.properties")
public class AfeRepository 
{ 
		
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Value("${findFixedOrder}") private String findFixedOrderSQL;
	@Value("${findStatus}")  private String findStatusSQL;
	@Value("${findEventCodeById}") private String findEventCodeByIdSQL;
	@Value("${findEventCodeByNumber}") private String findEventCodeByNumberSQL;
	@Value("${insertEventCode}") private String insertEventCodeSQL;
	@Value("${updateStatus}") private String updateStatusSQL;	
	@Value("${updateFixedOrder}") private String updateFixedOrderSQL;	
	
	
	public AfeFixedOrder findFixedOrder(JsonResponse jsonResponse) 
	{
		List<AfeFixedOrder> list = jdbcTemplate.query(
			findFixedOrderSQL, 
			(rs, rowNum) -> 
			{
				AfeFixedOrder fixedOrder = new AfeFixedOrder();
				
				fixedOrder.setId( getInt(rs, "id") );
				fixedOrder.setRequestId( getInt(rs, "requestId") );
				
				return fixedOrder;
			}, 
			jsonResponse.getVehOrderNbr());
		
		return list.isEmpty() ? null : list.get(0);
	}
	
	
	public AfeStatus findStatus(AfeFixedOrder fixedOrder) 
	{
		List<AfeStatus> list = jdbcTemplate.query(
			findStatusSQL, 
			(rs, rowNum) -> 
			{
				AfeStatus afeStatus = new AfeStatus();
				
				afeStatus.setEventCodeDate( rs.getObject("eventCodeDate", LocalDateTime.class) );
				afeStatus.setEventCodeId( getInt(rs, "eventCodeId") );
				
				return afeStatus;
			}, 
			fixedOrder.getId());
		
		return list.isEmpty() ? null : list.get(0);
	}
	
	
	public AfeEventCode findEventCodeById(AfeStatus status) 
	{
		List<AfeEventCode> list = jdbcTemplate.query(
			findEventCodeByIdSQL, 
			(rs, rowNum) -> 
			{
				AfeEventCode eventCode = new AfeEventCode();
				eventCode.setEventCodeNumber( getInt(rs, "eventCodeNumber") );
				return eventCode; 
			}, 
			status.getEventCodeId());
		
		return list.isEmpty() ? null : list.get(0);
	}
	
	
	public AfeEventCode findEventCodeByNumber(JsonResponse jsonResponse) 
	{
		List<AfeEventCode> list = jdbcTemplate.query(
			findEventCodeByNumberSQL, 
			(rs, rowNum) -> 
			{
				AfeEventCode eventCode = new AfeEventCode();
				
				eventCode.setId( getInt(rs, "id") );
				eventCode.setEventCodeNumber( getInt(rs, "eventCodeNumber") );
				
				return eventCode;
			},
			jsonResponse.getCurrVehEvntCd());
		
		return list.isEmpty() ? null : list.get(0);
	}
	
	
	public int insertEventCode(JsonResponse jsonResponse) {
		return jdbcTemplate.update(insertEventCodeSQL, jsonResponse.getCurrVehEvntCd(), jsonResponse.getCurrVehEvntDesc());
	}
	
	
	public int updateStatus(AfeEventCode eventCode, JsonResponse jsonResponse, AfeFixedOrder fixedOrder) {
		return jdbcTemplate.update(updateStatusSQL, eventCode.getId(), jsonResponse.getCurrEvntStatusDt(), fixedOrder.getId());
	}
	
	
	public int updateFixedOrder(AfeFixedOrder fixedOrder) {
		return jdbcTemplate.update(updateFixedOrderSQL, fixedOrder.getId());
	}
	
}
