/**
 * 
 */
package com.blackcats.dev.email.configuration;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mail.ImapIdleChannelAdapter;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.messaging.SubscribableChannel;

/**
 * Jul 1, 2020
 *
 */
@Configuration
@EnableIntegration
public class EmailConfiguration {
	
	private Properties javaMailProperties() {
	    Properties javaMailProperties = new Properties();

	    javaMailProperties.setProperty("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	    javaMailProperties.setProperty("mail.imap.socketFactory.fallback","false");
	    javaMailProperties.setProperty("mail.store.protocol","imaps");
	    javaMailProperties.setProperty("mail.debug","true");
	    javaMailProperties.setProperty("mail.imap.ssl", "true");


	    return javaMailProperties;
	}
	
	/**
	 * Mail receiver requires encoded userid and encoded password for example 
	 * k@g.com will be k%40g.com
	 *
	 * @return the imap mail receiver
	 */
	@Bean
	public ImapMailReceiver mailReceiver() {
	    ImapMailReceiver mailReceiver = new ImapMailReceiver("imaps://[login]:[pass]@imap-mail.outlook.com:993/INBOX");
	    mailReceiver.setJavaMailProperties(javaMailProperties());
	    mailReceiver.setShouldDeleteMessages(false);
	    mailReceiver.setShouldMarkMessagesAsRead(true);
	   return mailReceiver;
	}

	@Bean
	public SubscribableChannel mailChannel() {
	    return MessageChannels.direct().get();
	}
	
	@Bean
	public ImapIdleChannelAdapter adapter() {
	    ImapIdleChannelAdapter imapIdleChannelAdapter = new ImapIdleChannelAdapter(mailReceiver());
	    imapIdleChannelAdapter.setOutputChannel(mailChannel());
	    imapIdleChannelAdapter.afterPropertiesSet();
	    return imapIdleChannelAdapter;
	}

	
}
