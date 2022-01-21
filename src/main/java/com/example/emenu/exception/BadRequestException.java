package com.example.emenu.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;

@Slf4j
public class BadRequestException extends GlobalException{

	public static final String BAD_REQUEST_EXCEPTION = "BadRequestException";

	public BadRequestException(String message) {
		super(message , BAD_REQUEST_EXCEPTION);

		StringBuilder msg = new StringBuilder(DEFAULT_ERROR_MSG);
		for (int i = 0; i < Math.min(this.getStackTrace().length, 2); i++) {
			msg.append(System.lineSeparator()).append(this.getStackTrace()[i]);
		}
		log.error(msg.toString());
	}

	public BadRequestException(Logger logger) {
		super(BAD_REQUEST_EXCEPTION);

		StringBuilder msg = new StringBuilder(DEFAULT_ERROR_MSG);
		for (int i = 0; i < Math.min(this.getStackTrace().length, 2); i++) {
			msg.append(System.lineSeparator()).append(this.getStackTrace()[i]);
		}
		logger.error(msg.toString());
	}

	public BadRequestException(String message, Logger logger) {
		super(message, BAD_REQUEST_EXCEPTION);

		StringBuilder msg = new StringBuilder(message);
		for (int i = 0; i < Math.min(this.getStackTrace().length, 2); i++) {
			msg.append(System.lineSeparator()).append(this.getStackTrace()[i]);
		}
		logger.error(msg.toString());
	}
}
