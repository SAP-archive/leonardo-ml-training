package com.sap.cloud.s4hana.mlretrainservice.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;

@ControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

	private static final Logger logger = CloudLoggerFactory.getLogger(GlobalExceptionController.class);

	@ExceptionHandler(ApiException.class)
	public final ResponseEntity<ErrorResponse> handleCustomException(ApiException ex, WebRequest request) {
		
		logger.error(ex.getMessage(), ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(Integer.toString(ex.getCode()), details);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorResponse> handleAllException(Exception ex) {

		logger.error(ex.getMessage(), ex);
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(Integer.toString(ex.hashCode()), details);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
