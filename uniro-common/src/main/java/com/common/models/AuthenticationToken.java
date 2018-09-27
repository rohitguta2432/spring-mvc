package com.common.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "auth_token")
@JsonInclude(content = Include.NON_NULL)
@NoArgsConstructor
public class AuthenticationToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String userId;
	private String authToken;
	private Long createDate;
	private Long lastAccessed;
	private boolean isAdminToken;
	private String deviceToken;
	
	public AuthenticationToken(String userId, String authToken, boolean isAdminToken, String deviceToken) {

		this.userId = userId;
		this.authToken = authToken;
		this.isAdminToken = isAdminToken;
		this.deviceToken = deviceToken;
		this.onCreate();
	}

	/**
	 * <b>Instantiates a new authentication token</b>
	 *
	 * @param userId User id as Long
	 * @param authToken Auth token as String
	 * @param isAdminToken Is admin token as Boolean
	 * @param businessUnitId Business unit id as Long
	 */
	public AuthenticationToken(String userId, String authToken, boolean isAdminToken) {
		this.userId = userId;
		this.authToken = authToken;
		this.isAdminToken = isAdminToken;
		this.onCreate();
	}

	protected void onCreate() {
		createDate = new Date().getTime();
		lastAccessed = new Date().getTime();
	}

}
