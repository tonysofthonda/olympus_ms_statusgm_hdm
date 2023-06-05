package com.honda.olympus.ms.statusgm_hdm.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.honda.olympus.ms.statusgm_hdm.domain.JsonMaxTransit;
import com.honda.olympus.ms.statusgm_hdm.domain.JsonResponse;
import com.honda.olympus.ms.statusgm_hdm.property.Properties;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class MaxTransitClient 
{
	
	private String urlmax;
	private RestTemplateFactory rtFactory;
	
	
	@Autowired 
	public MaxTransitClient(RestTemplateFactory rtFactory, Properties props) {
		this.rtFactory = rtFactory;
		log.info("# urlmax: {}", urlmax = props.getUrlmax());
	}
	
	
	public ResponseEntity<List<JsonResponse>> sendRequest(JsonMaxTransit jsonMaxTransit) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<JsonMaxTransit> httpEntity = new HttpEntity<>(jsonMaxTransit, headers);
		return rtFactory.getRestTemplate()
			.exchange(urlmax, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<JsonResponse>>(){});
	}
	
}
