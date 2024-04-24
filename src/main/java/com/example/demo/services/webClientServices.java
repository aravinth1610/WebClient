package com.example.demo.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bazaarvoice.jolt.JsonUtils;
import com.example.demo.modalDTO.ModalDTO;
import com.example.demo.modalDTO.TestProt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.pattern.parser.Parser;
import reactor.core.publisher.Mono;

@Service
public class webClientServices {

	@Autowired
	private WebClient webClient;

	public void postSingleObjJson(ModalDTO modelDto) {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		ModalDTO dto = new ModalDTO();
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/YYY");
		String createDate = formater.format(new Date());
		BeanUtils.copyProperties(modelDto, dto);
		dto.setCreateOn(createDate);
		dto.setUpdateOn(null);
		System.out.println(dto.toString());

		webClient.post().uri("/postjson").headers(headers -> headers.addAll(httpHeaders)).bodyValue(dto)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						// Handle 200 OK specifically
						System.out.println(HttpStatus.OK);

						return response.bodyToMono(String.class)
								// .doOnSuccess(result -> System.out.println("Success with or without result: "
								// + result))
								// .doOnError(error -> System.err.println("Error in stream: " +
								// error.getMessage()))
								.doOnNext(body -> {
									System.out.println("200 OK response body: " + body);
								});
					} else if (response.statusCode().is2xxSuccessful()) {
						// Handle other 2xx success statuses
						return response.bodyToMono(String.class)
								.doOnNext(body -> System.out.println("Other 2xx success response body: " + body));
					} else {
						// Handle non-success statuses
						return Mono.error(new RuntimeException("Non-success response received"));
					}
				})

				.subscribe(result -> {
					System.out.println(result);

					// This block is for further processing of the response
					// 'result' will be whatever was returned from the above .exchangeToMono
				}, error -> {
					// Handle errors, such as network issues or the Mono.error from above
					System.err.println("Error occurred: " + error.getMessage());
				}, () -> {
					// This block is executed upon successful completion of the entire Mono pipeline
					System.out.println("Request completed successfully.");
				});

	}

	public JSONObject AipResponseJoltConvert(TestProt dto) {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		String serviceUrl = "http://localhost:8080/proteanAPI-1.0/getFormResponseJSONById?pkFormdata="
				+ dto.getResponseId() + "&formId=" + dto.getFormId();
		JSONObject sourceObject = new JSONObject();
		webClient.get().uri(serviceUrl).headers(headers -> headers.addAll(httpHeaders))
				// .bodyValue(dto)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						// Handle 200 OK specifically
						System.out.println(HttpStatus.OK);

						return response.bodyToMono(String.class);
						// .doOnSuccess(result -> System.out.println("Success with or without result: "
						// + result));
						// .doOnError(error -> System.err.println("Error in stream: " +
						// error.getMessage()))
						// .doOnNext(body -> {
						// System.out.println("200 OK response body: " + body);
						// });
					} else if (response.statusCode().is2xxSuccessful()) {
						// Handle other 2xx success statuses
						return response.bodyToMono(String.class)
								.doOnNext(body -> System.out.println("Other 2xx success response body: " + body));
					} else {
						// Handle non-success statuses
						return Mono.error(new RuntimeException("Non-success response received"));
					}
				})

				.subscribe(result -> {
					System.out.println("rest-- " + result);

					String response = result;

					Object sourceJSONObject = JsonUtils.jsonToObject(response);

					if (sourceJSONObject instanceof LinkedHashMap) {
						ObjectMapper m = new ObjectMapper();
						Map<String,Object> props = m.convertValue(sourceJSONObject, new TypeReference<Map<String, Object>>() {});
					sourceObject.putAll(props);
						
//						sourceObject.putAll((LinkedHashMap) sourceJSONObject);
//						System.out.println(sourceObject+"json Object-----");
					
						
					//	return sourceObject;
						// we can put to Json Jolt --
					//	 transformedOutput = chainr.transform(sourceObject);
						
					}

					// This block is for further processing of the response
					// 'result' will be whatever was returned from the above .exchangeToMono
				}, error -> {
					// Handle errors, such as network issues or the Mono.error from above
					System.err.println("Error occurred: " + error.getMessage());
				});
//				, () -> {
//					// This block is executed upon successful completion of the entire Mono pipeline
//					System.out.println("Request completed successfully.");
//				});
		System.out.println(sourceObject+"out----------");
		return sourceObject;
	}

	public static Map<String, String> convertToJsonMap(JsonNode node, String currentPath) {
		Map<String, String> resultMap = new HashMap<>();

		if (node.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> entry = fields.next();
				String newPath = currentPath.isEmpty() ? entry.getKey() : currentPath + "." + entry.getKey();
				resultMap.putAll(convertToJsonMap(entry.getValue(), newPath));
			}
		} else if (node.isArray()) {
			// Handle array or list structures if necessary
			// This example skips arrays for simplicity
		} else {
			resultMap.put(currentPath, node.asText());
		}

		return resultMap;
	}
	
	
	@Override
	public void savePFormRespAutoPromote(AutoPromoteFormRequest autoFormRequest) {
 
		AutoPromoteFormResponse autoPromoteResponse = null;
 
		AutoPromoteModal promoteResponse = aithFormCombinationRepository
				.findFormCombinationsByFormMaster(Long.parseLong(autoFormRequest.getFormId()));
		System.out.println(promoteResponse);
		if (promoteResponse.getIsPromoteCase().equalsIgnoreCase("yes") && promoteResponse != null) {
			ABIRequestDto abiRequestDto = new ABIRequestDto();
			BeanUtils.copyProperties(autoFormRequest, abiRequestDto);
			  ABIClientApiCalls(abiRequestDto);
			 
		}else {
		 
		}
 
	}
 
	// This is Asynchronous
	private void ABIClientApiCalls(ABIRequestDto abiRequestDto) {
		System.out.println("----------------");
		String basicAuthHeader = "basic " + Base64Utils.encodeToString((userName + ":" + password).getBytes());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.AUTHORIZATION, basicAuthHeader);
		Mono<AutoPromoteFormResponse> autoPromote = abiWebClientConfig.ABIwebclient().post().bodyValue(abiRequestDto)
				.headers(headers -> headers.addAll(httpHeaders)).exchangeToMono(response -> {
					System.out.println(response.statusCode());
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(AutoPromoteFormResponse.class);
					} else if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
						// return Mono.error(new RuntimeException("Internal Server Error"));
						throw new AuthAPIException(HttpStatus.UNAUTHORIZED.toString(), "ABI Invalid Credentials");
					} else if (response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
						throw new AuthAPIException(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
								"ABI Internal Server Error");
					} else {
						throw new BadRequestException(HttpStatus.BAD_REQUEST.toString(),
								"Non-success response received");
					}
				});  autoPromote.subscribe();
	
	

}
