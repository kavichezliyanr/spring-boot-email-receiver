package com.blackcats.dev.email.receiver;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@MessageEndpoint
public class EmailReceiverService {

    @ServiceActivator(inputChannel="mailChannel")
    public void handleMessage(Message<?> message) {
        System.out.println("message received : "+message.getPayload().toString() );
    }

}
