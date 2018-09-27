package com.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.common.models.Company;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.web.UniroWebException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author RITESH SINGH
 * @since JDK 1.8
 * @version 1.0
 *
 */
@Component
@Slf4j
public class AuthorizationUtil {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final HttpUtil httpUtil;

	@Autowired
	public AuthorizationUtil(HttpUtil httpUtil) {
		this.httpUtil = httpUtil;
	}

	public JsonNode generateAuthToken(String userName, String password) {

		Map<String, String> headers = new HashMap<>();
		headers.put(UniroConstant.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(Constants.ADMIN_REQUEST, "TRUE");
		String response = httpUtil.postOrPut(URLConstants.AUTHORIZATION_URL, getAuthPayload(userName, password),
				headers, HttpMethod.POST);
		JsonNode responseData;
		try {
			responseData = parseResponse(response);

		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error while parsing authorization token response");
			throw new UniroWebException("Error while parsing authorization token response");
		}

		if (responseData == null) {
			log.error("No response obtained from Authorization api");
			throw new UniroWebException("No response obtained from Authorization api");
		}
		return responseData;
	}

	private JsonNode parseResponse(String response) throws JsonParseException, JsonMappingException, IOException {
		return OBJECT_MAPPER.readTree(response);
	}

	private Map<String, String> getAuthPayload(String userName, String password) {
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put(UniroConstant.U_KEY, userName);
		dataMap.put(UniroConstant.P_KEY, password);
		return dataMap;
	}

	public JsonNode makeRequestPost(String url, Object entity) {
		Map<String, String> headers = new HashMap<>();
		headers.put(UniroConstant.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(Constants.ADMIN_REQUEST, "TRUE");
		headers.put(Constants.X_AUTH_HEADER, "admin");

		String response = httpUtil.postOrPut(url, entity, headers, HttpMethod.POST);
		JsonNode responseData;
		try {
			responseData = parseResponse(response);

		} catch (IOException e) {
			e.printStackTrace();
			log.error("No response obtained from Authorization api");
			throw new UniroWebException("No response obtained from Authorization api");
		}

		if (responseData == null) {
			log.error("No response obtained from Authorization api");
			throw new UniroWebException("No response obtained from Authorization api");
		}
		return responseData;
	}

	public JsonNode makeRequestGet(String url) {
		Map<String, String> headers = new HashMap<>();
		headers.put(UniroConstant.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(Constants.ADMIN_REQUEST, "TRUE");
		headers.put(Constants.X_AUTH_HEADER, "admin");
		try {
			String response = httpUtil.get(url, headers, HttpMethod.GET);
			JsonNode responseData;

			responseData = parseResponse(response);

			if (responseData == null) {
				log.error("No response obtained from Authorization api");
				throw new UniroWebException("No response obtained from Authorization api");
			}
			return responseData;

		} catch (UniroWebException | IOException e) {
			e.printStackTrace();
			log.error("Error while making request to rest-api url");
			throw new UniroWebException("Error while making request to rest-api url");
		}

	}
	
	
	public Company getCompanyByCrn(String crn){
		
		if (ObjectUtils.isEmpty(crn)) {
			throw new UniroWebException("Invalid crnNo");
		}
		try {
			String url = String.format(URLConstants.FETCH_COMPANY_BY_CRN_NO,crn);
			
			JsonNode response = this.makeRequestGet(url);
			
			int status = response.get(UniroConstant.STATUS_CODE).intValue();
			String errorMsg = response.get(UniroConstant.MESSAGE).asText();
			if (status != 200) {
				throw new UniroWebException(errorMsg, status);
			}
			
			try{
				if (!ObjectUtils.isEmpty(response.get(UniroConstant.DATA))) {
					String data = response.get(UniroConstant.DATA).toString();
					return OBJECT_MAPPER.readValue(data, new TypeReference<Company>() {});
				}
				return null;
			}catch(IOException ex){
				throw new UniroWebException("Response Converson error for Devices on web.", 500);
			}
			
		} catch (UniroWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new UniroWebException("Error while deleting device information", 401);
			}
			
			if (errorCode.equals("403")) {
				log.error("Access Denied this api.");
				throw new UniroWebException("Access Denied this api.", 403);
			}
			
			if (e.code == 500) {
				log.error(e.getMessage());
				throw new UniroWebException(e.getMessage(), e.code);
			}
			log.error("Error while deleting device information");
			throw new UniroWebException("Error while deleting device information");
		}
	}
}
