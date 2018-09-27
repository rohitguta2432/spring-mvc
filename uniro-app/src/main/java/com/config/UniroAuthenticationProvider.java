package com.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.common.models.Company;
import com.fasterxml.jackson.databind.JsonNode;
import com.util.AuthorizationUtil;
import com.util.Constants;
import com.util.UniroConstant;
import com.web.UniroWebException;

/**
 * @author RITESH SINGH
 * @since JDK 1.8
 * @version 1.0
 *
 */
@Component("uniroAuthenticationProvider")
public class UniroAuthenticationProvider implements AuthenticationProvider {


	@Autowired
	private AuthorizationUtil authorizationUtil;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		JsonNode responseData = authorizationUtil.generateAuthToken(username, password);

		if (responseData == null) {
			throw new UniroWebException("Internal server error, please contact administrator.");
		}
		int statusCode = Integer.parseInt(responseData.get(UniroConstant.STATUS_CODE).toString());

		if (statusCode == 401 || statusCode == 900 || statusCode == 423 || statusCode == 500) {
			if (statusCode == 423) {
				throw new BadCredentialsException("Already Logged In");
			}
			throw new BadCredentialsException("Invalid credentials.");
		}

		JsonNode dataNode = responseData.get(UniroConstant.DATA);
		JsonNode userNode = dataNode.get(Constants.USER);
		JsonNode authoritiesNode = userNode.get(Constants.AUTHORITIES);
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		session.setAttribute(Constants.USER, username);
		session.setAttribute(Constants.USER_ID, userNode.get(Constants.ID));
		session.setAttribute(Constants.CRN, userNode.get(Constants.CRN));
		session.setAttribute(Constants.AUTHORITIES, userNode.get(Constants.AUTHORITIES));
		session.setAttribute(UniroConstant.AUTH_HEADER, dataNode.get(Constants.AUTH_TOKEN).asText());
		session.setAttribute(Constants.USER_PROFILE_IMAGE_NAME, (userNode.get(Constants.USER_PROFILE_IMAGE_NAME)!=null) ? userNode.get(Constants.USER_PROFILE_IMAGE_NAME).asText() : "");
		//session.setAttribute(Constants.USER_PROFILE_IMAGE_NAME, (userNode.get(Constants.USER_PROFILE_IMAGE_NAME)!=null) ? userNode.get(Constants.USER_PROFILE_IMAGE_NAME).asText() : "");
		
		try{
		if(!ObjectUtils.isEmpty(userNode.get(Constants.CRN))){
			Company company= authorizationUtil.getCompanyByCrn(userNode.get(Constants.CRN).asText());
			if(!ObjectUtils.isEmpty(company)){
				session.setAttribute(Constants.COMPANY_NAME, company.getName());
			}
		}
		}catch(UniroWebException ex){
			ex.printStackTrace();
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();

		authoritiesNode.forEach(authority -> {
			authorities.add(new SimpleGrantedAuthority(authority.get(Constants.AUTHORITY).asText()));
		});
		return new UsernamePasswordAuthenticationToken(username, password, authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
}