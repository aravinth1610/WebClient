package com.example.demo.webClientController;

import java.util.LinkedHashMap;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.modalDTO.ModalDTO;
import com.example.demo.modalDTO.TestProt;
import com.example.demo.services.webClientServices;
import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

/*
 *
Post(JSON/XML/encoder/params/pathvariable): another API (get data)
Post: json to DTO (mapper)
Post : json to jsonObject
Post: json to List DTO (mapper)
Post: json to Object (mapper)
Post : json to Map
Post : json to xml
Post : xml to json
Post : encoder 
Post : json to encoder
Post : xml to encoder
Post : encoder to json 
Post : encoder to xml
Post : as string
Post : params
Post : pathvariable
 */

@RestController
public class WebClientController {

	private webClientServices webClientService;
	
	private WebClientController(webClientServices webClientService) {
	this.webClientService=webClientService;	
	}
	
	@PostMapping("/getjson")
	public void getMainSingleJson(@RequestBody ModalDTO dto) {	
		System.out.println(dto);
		webClientService.postSingleObjJson(dto);
	}
	
	@PostMapping("/postjson")
	public String postSingleJson(@RequestBody ModalDTO dto) {
		
		System.out.println("postJSON Value by web client------");
		System.out.println(dto);
		
		throw new RuntimeException();
		
		//return "postJsonReturn";
	}
	
	
	@PostMapping("/responeJolt")
	public JSONObject ResponseJoltConvert(@RequestBody TestProt dto) {
		return webClientService.AipResponseJoltConvert(dto);
	}

	
	
}
