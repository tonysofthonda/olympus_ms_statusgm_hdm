package com.honda.olympus.ms.statusgm_hdm.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;


@Getter
@Component
public class Service 
{
	@Value("${service.name}") private String serviceName;
	@Value("${service.version}") private String serviceVersion;
	
	@Value("${ms.logevent.protocol}") private String logEventProtocol;
	@Value("${ms.logevent.host}") private String logEventHost;
	@Value("${ms.logevent.port}") private int logEventPort;
	@Value("${ms.logevent.path}") private String logEventPath;
	
	@Value("${ms.notification.protocol}") private String notificationProtocol;
	@Value("${ms.notification.host}") private String notificationHost;
	@Value("${ms.notification.port}") private int notificationPort;
	@Value("${ms.notification.path}") private String notificationPath;
	
	@Value("${ms.genstaafe_hdm.protocol}") private String genstaafeHdmProtocol;
	@Value("${ms.genstaafe_hdm.host}") private String genstaafeHdmHost;
	@Value("${ms.genstaafe_hdm.port}") private int genstaafeHdmPort;
	@Value("${ms.genstaafe_hdm.path}") private String genstaafeHdmPath;
}
