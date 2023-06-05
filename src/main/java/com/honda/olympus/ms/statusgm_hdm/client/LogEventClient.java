package com.honda.olympus.ms.statusgm_hdm.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.honda.olympus.ms.statusgm_hdm.property.Service;
import com.honda.olympus.ms.statusgm_hdm.domain.Event;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class LogEventClient 
{
	
	private String url; 
	
	@Autowired
	public LogEventClient(Service service) throws MalformedURLException {
		url = new URL(
			service.getLogEventProtocol(),
			service.getLogEventHost(), 
			service.getLogEventPort(), 
			service.getLogEventPath() ).toString();
		
		log.info("# ms.logevent url: {}", url);
	}
	
	public ResponseEntity<String> logEvent(Event event) {
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<Event> httpEntity = new HttpEntity<>(event, headers);
		return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
	}
	
}
