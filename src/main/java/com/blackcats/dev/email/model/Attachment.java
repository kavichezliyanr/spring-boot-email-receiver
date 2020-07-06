/**
 * 
 */
package com.blackcats.dev.email.model;

import java.io.InputStream;

import lombok.Builder;
import lombok.Data;

/**
 * Jul 6, 2020
 *
 */
@Data
@Builder
public class Attachment {
	
	private InputStream inputStream;
	
	private String FileName;

}
