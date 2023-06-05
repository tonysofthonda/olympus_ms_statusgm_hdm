package com.honda.olympus.ms.statusgm_hdm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.honda.olympus.ms.statusgm_hdm.client.GenstaafeHdmClient;
import com.honda.olympus.ms.statusgm_hdm.domain.Message;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class GenstaafeHdmService 
{
	
	@Autowired
	private GenstaafeHdmClient genstaafeHdmClient; 
	
	
	public HttpStatus sendMessage(Message message) {
		try {
			ResponseEntity<String> response = this.genstaafeHdmClient.sendMessage(message);
			return response.getStatusCode();
		}
		catch (HttpClientErrorException exception) {
			log.error("### GenstaafeHdm request error: {}", exception.getResponseBodyAsString());
			return exception.getStatusCode();
		}
		catch (RestClientException exception) {
			log.error("### Unable to connect to the GenstaafeHdm endpoint !");
			return null;
		}
	}
	
}
