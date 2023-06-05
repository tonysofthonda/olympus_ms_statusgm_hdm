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

import com.honda.olympus.ms.statusgm_hdm.domain.Message;
import com.honda.olympus.ms.statusgm_hdm.property.Service;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class GenstaafeHdmClient 
{
	
	private String url;
	
	
	@Autowired
	public GenstaafeHdmClient(Service service) throws MalformedURLException {
		url = new URL(
			service.getGenstaafeHdmProtocol(), 
			service.getGenstaafeHdmHost(), 
			service.getGenstaafeHdmPort(), 
			service.getGenstaafeHdmPath()).toString();
		log.info("# ms.genstaafe_hdm url: {}", url);
	}
	
	
	public ResponseEntity<String> sendMessage(Message message) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<Message> httpEntity = new HttpEntity<>(message, headers);
		return new RestTemplate().exchange(url, HttpMethod.POST, httpEntity, String.class);
	}
	
}
