package com.honda.olympus.ms.statusgm_hdm.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class RestTemplateFactory
{
	
	private int timeout;
	
	
	public RestTemplateFactory(@Value("${timewait}") int timewait) {
		timeout = timewait * 1_000;
		log.info("# timewait: {} seconds", timewait);
	}
	
	
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		
		SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
		rf.setReadTimeout(timeout);
		rf.setConnectTimeout(timeout);
		
		return restTemplate;
	}
}
