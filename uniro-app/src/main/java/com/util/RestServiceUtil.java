package com.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class RestServiceUtil {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final HttpUtil httpUtil;

	@Autowired
	public RestServiceUtil(HttpUtil httpUtil) {
		this.httpUtil = httpUtil;
	}

	public JsonNode makeRequest(String url, Object payload, Map<String, Object> parameters, HttpMethod method) {

		if (StringUtils.isEmpty(url)) {
			log.info("Url cannot be null/empty");
			throw new IllegalArgumentException("Url cannot be null/empty");
		}

		if ((HttpMethod.POST == method || HttpMethod.PUT == method) && ObjectUtils.isEmpty(payload)) {
			log.info("Cannot make post or put request with empty/null payload");
			throw new IllegalArgumentException("Cannot make post or put request with empty/null payload");
		}

		Map<String, String> headers = this.createHeaders();

		String response = null;

		try {
			if (HttpMethod.GET == method) {
				response = httpUtil.get(url, headers, method);
			} else if (HttpMethod.POST == method || HttpMethod.PUT == method) {
				response = httpUtil.postOrPut(url, payload, headers, method);
			}
		} catch (UniroWebException | IOException e) {
			log.error("Error while making request to rest-api url");
			e.printStackTrace();
			throw new UniroWebException("Error while making request to rest-api url");
		}

		JsonNode responseJson;
		try {
			
			responseJson = parseResponse(response);
		} catch (UniroWebException | IOException e) {
			log.error("Error while parsing response for url");
			e.printStackTrace();
			throw new UniroWebException("Error while parsing response for url");
		}

		int status = responseJson.get(UniroConstant.STATUS_CODE).asInt();
		if (status == 401) {
			log.info("The authorization token has expired, hence generating new one.");
			log.error("The rest service seems to be down, aborting process.");
			throw new UniroWebException("401", 401);
		}
		if (status == 403) {
			log.info("The authorization token has expired, hence generating new one.");

			log.error("The rest service seems to be down, aborting process.");
			throw new UniroWebException("403", 403);
		}
		return responseJson;
	}

	public void writeToStream(String url, OutputStream outputStream) {

		Map<String, String> headers = createHeaders();
		headers.remove(UniroConstant.CONTENT_TYPE);
		httpUtil.fetchDataAndWriteToStream(url, headers, outputStream);

	}

	private JsonNode parseResponse(String responseAsString) throws JsonProcessingException, IOException {
		return OBJECT_MAPPER.readTree(responseAsString);
	}

	private Map<String, String> createHeaders() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(UniroConstant.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		Object authenticationToken = session.getAttribute(UniroConstant.AUTH_HEADER);

		if (ObjectUtils.isEmpty(authenticationToken)) {
			log.info("Authentication token is not generate hence generating it.");
			throw new UniroWebException();
		}
		headerMap.put(UniroConstant.AUTH_HEADER, session.getAttribute(UniroConstant.AUTH_HEADER).toString());
		//headerMap.put(Constants.ADMIN_REQUEST, "TRUE");
		return headerMap;
	}

}
