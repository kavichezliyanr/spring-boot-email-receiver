package com.blackcats.dev.email.receiver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import com.blackcats.dev.email.model.Attachment;

import lombok.extern.slf4j.Slf4j;

@MessageEndpoint
@Slf4j
public class EmailReceiverService {

    @ServiceActivator(inputChannel="mailChannel")
    public void handleMessage(Message<?> message) throws Exception {
    	MimeMessage mimeMessage = (MimeMessage) message.getPayload();
        log.info("message received : "+mimeMessage.getSubject());
        List<Attachment> attachments = getAttachments(mimeMessage);

        attachments.forEach(action->{
        	File targetFile = new File(action.getFileName());
            try {
            	InputStream dest= null;
                dest=IOUtils.toBufferedInputStream(action.getInputStream());
				FileUtils.copyInputStreamToFile(dest, targetFile);
				action.getInputStream().close();
			} catch (IOException e) {
				log.error("unable to dowload the attachment",e);
			}
        });
        
    }
    
    
    public List<Attachment> getAttachments(MimeMessage message) throws Exception {
        Object content = message.getContent();
        if (content instanceof String)
            return null;        

        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            List<Attachment> result = new ArrayList<>();

            for (int i = 0; i < multipart.getCount(); i++) {
                result.addAll(getAttachments(multipart.getBodyPart(i)));
            }
            return result;

        }
        return null;
    }
    
	private List<Attachment> getAttachments(BodyPart part) throws Exception {
		List<Attachment> result = new ArrayList<>();
		Object content = part.getContent();
		if (content instanceof InputStream || content instanceof String) {
			if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || StringUtils.isNotBlank(part.getFileName())) {
				result.add(Attachment.builder().FileName(part.getFileName()).inputStream(part.getInputStream()).build());
				return result;
			} else {
				return new ArrayList<>();
			}
		}

		if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				result.addAll(getAttachments(bodyPart));
			}
		}
		return result;
	}

}
