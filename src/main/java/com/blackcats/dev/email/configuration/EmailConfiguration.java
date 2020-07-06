/**
 * 
 */
package com.blackcats.dev.email.configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(EmailReceiverProperties.class)
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
	 * @throws UnsupportedEncodingException 
	 */
	@Bean
	public ImapMailReceiver mailReceiver(EmailReceiverProperties emailProperties) throws UnsupportedEncodingException {
	    ImapMailReceiver mailReceiver = new ImapMailReceiver(getEncodedUrl(emailProperties));
	    mailReceiver.setJavaMailProperties(javaMailProperties());
	    mailReceiver.setShouldDeleteMessages(false);
	    mailReceiver.setShouldMarkMessagesAsRead(true);
	   return mailReceiver;
	}
	
	/**
	 * Gets the encoded url.
	 *
	 * @param emailProperties the email properties
	 * @return the encoded url
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private String getEncodedUrl(EmailReceiverProperties emailProperties) throws UnsupportedEncodingException {
		String user = URLEncoder.encode(emailProperties.getUsername(), "UTF-8");
	    String password = URLEncoder.encode(emailProperties.getPassword(), "UTF-8");
	    StringBuilder urlBuilder = new StringBuilder();
	    urlBuilder.append("imaps://").append(user).append(":")
	    .append(password).append("@").append(emailProperties.getHost()).append(":")
	    .append(emailProperties.getPort()).append("/inbox");
	    return urlBuilder.toString();
	}

	@Bean
	public SubscribableChannel mailChannel() {
	    return MessageChannels.direct().get();
	}
	
	@Bean
	public ImapIdleChannelAdapter adapter(EmailReceiverProperties emailProperties) throws UnsupportedEncodingException {
	    ImapIdleChannelAdapter imapIdleChannelAdapter = new ImapIdleChannelAdapter(mailReceiver(emailProperties));
	    imapIdleChannelAdapter.setOutputChannel(mailChannel());
	    imapIdleChannelAdapter.afterPropertiesSet();
	    return imapIdleChannelAdapter;
	}

	
}
