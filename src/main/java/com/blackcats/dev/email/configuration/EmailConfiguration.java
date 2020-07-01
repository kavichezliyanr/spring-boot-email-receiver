/**
 * 
 */
package com.blackcats.dev.email.configuration;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mail.ImapIdleChannelAdapter;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.messaging.MessageChannel;

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

	    return javaMailProperties;
	}
	
	@Bean
	public ImapIdleChannelAdapter mailAdapter() {
	    ImapMailReceiver mailReceiver = new ImapMailReceiver("imaps://login:pass@imap.gmail.com:993/INBOX");
	    mailReceiver.setJavaMailProperties(javaMailProperties());
	    mailReceiver.setShouldDeleteMessages(false);
	    mailReceiver.setShouldMarkMessagesAsRead(true);
	    ImapIdleChannelAdapter imapIdleChannelAdapter = new ImapIdleChannelAdapter(mailReceiver);
	    imapIdleChannelAdapter.setOutputChannel(emailChannel());
	    imapIdleChannelAdapter.afterPropertiesSet();
	    return imapIdleChannelAdapter;
	}

	@Bean
	public MessageChannel emailChannel() {
	 return new DirectChannel();
	}
	
}
