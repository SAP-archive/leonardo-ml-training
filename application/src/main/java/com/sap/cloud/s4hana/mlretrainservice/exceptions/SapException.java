package com.sap.cloud.s4hana.mlretrainservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE,reason = "Internal Server Error")
@Getter
public class SapException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5033497735965929533L;

	public SapException() {
		super();
	}

	public SapException(String message, Throwable cause) {
		super(message, cause);
	}

	public SapException(String message) {
		super(message);
	}

	public SapException(Throwable cause) {
		super(cause);
	}

}