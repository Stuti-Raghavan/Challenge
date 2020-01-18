package com.db.awmd.challenge.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * Class to fetch External Properties
 *
 */
@RefreshScope
@Component
@ConfigurationProperties(prefix="spring")
public class ConfigServerProperties {
	
	@Getter
	private final Map<String,String> ResponseMessages=new HashMap<>(); 
	
}
