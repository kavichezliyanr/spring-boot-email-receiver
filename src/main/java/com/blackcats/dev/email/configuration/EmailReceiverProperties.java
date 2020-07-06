/**
 * 
 */
package com.blackcats.dev.email.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Jul 6, 2020
 *
 */
@ConfigurationProperties(prefix = "spring.email.receive")
@Data
public class EmailReceiverProperties {
	
	private String username;
	
	private String password;
	
	private String host;
	
	private String port;

}
