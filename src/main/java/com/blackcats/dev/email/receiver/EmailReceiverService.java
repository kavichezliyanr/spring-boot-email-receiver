package com.blackcats.dev.email.receiver;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;

@MessageEndpoint
public class EmailReceiverService {

    @ServiceActivator(inputChannel="emailChannel")
    public void handleMessage(Message message, @Header("emailUrl") String url) {
        System.out.println(message + " header:" + url);
    }

}
