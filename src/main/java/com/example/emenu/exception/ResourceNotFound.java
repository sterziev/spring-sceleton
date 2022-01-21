package com.example.emenu.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;

@Slf4j
public class ResourceNotFound extends GlobalException {
	private static final long serialVersionUID = 8401388445867962172L;
	public static final String RESOURCE_NOT_FOUND = "ResourceNotFound";

	public ResourceNotFound(String message) {
		super(message , RESOURCE_NOT_FOUND);

		StringBuilder msg = new StringBuilder(DEFAULT_ERROR_MSG);
		for (int i = 0; i < Math.min(this.getStackTrace().length, 2); i++) {
			msg.append(System.lineSeparator()).append(this.getStackTrace()[i]);
		}
		log.error(msg.toString());
	}

	public ResourceNotFound(Logger logger) {
		super(RESOURCE_NOT_FOUND);

		StringBuilder msg = new StringBuilder(DEFAULT_ERROR_MSG);
		for (int i = 0; i < Math.min(this.getStackTrace().length, 2); i++) {
			msg.append(System.lineSeparator()).append(this.getStackTrace()[i]);
		}
		logger.error(msg.toString());
	}

	public ResourceNotFound(String message, Logger logger) {
		super(message, RESOURCE_NOT_FOUND);

		StringBuilder msg = new StringBuilder(message);
		for (int i = 0; i < Math.min(this.getStackTrace().length, 2); i++) {
			msg.append(System.lineSeparator()).append(this.getStackTrace()[i]);
		}
		logger.error(msg.toString());
	}
}
