package com.honda.olympus.ms.statusgm_hdm.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;


@Getter
@Component
public class Properties 
{	
	@Value("${dbhost}") private String dbhost;
	@Value("${dbport}") private int dbport;
	@Value("${dbuser}") private String dbuser;
	@Value("${dbname}") private String dbname;
	
	@Value("${urlmax}")  private String urlmax;
	
	@Value("${timewait}")  private int timewait;
	@Value("${timelapse}") private int timelapse;	
}
