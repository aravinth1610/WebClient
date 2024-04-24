package com.example.demo.modalDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestProt {

	private Long formId;
	private String formCode;
	private String confirmationId;
	private String product;
	private String client;
	private Long responseId;
	private Long userId;
	private String inputJSON;
	
}
