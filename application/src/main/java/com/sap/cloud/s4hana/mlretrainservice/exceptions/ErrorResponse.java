package com.sap.cloud.s4hana.mlretrainservice.exceptions;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class ErrorResponse {
	
	@Getter @Setter
    private String message;
	
	@Getter @Setter
    private List<String> details;

}
