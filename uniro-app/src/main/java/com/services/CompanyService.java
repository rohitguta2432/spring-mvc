package com.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.common.models.Company;
import com.util.UniroConstant;
import com.util.RestServiceUtil;
import com.util.URLConstants;
import com.util.UserRoleUtil;
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
public class CompanyService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private RestServiceUtil restServiceUtil;
	
	public Company saveCompany(Company company) {
		if (ObjectUtils.isEmpty(company)) {
			throw new UniroWebException("company info cannot be empty");
		}
		int status;
		try {

			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_COMPANY_DETAILS, company, null,
					HttpMethod.POST);
			
			status = response.get(UniroConstant.STATUS_CODE).intValue();
			String errorMsg = response.get(UniroConstant.MESSAGE).asText();
			if (status != 200) {
				throw new UniroWebException(errorMsg, status);
			}
			
			try{
				if (!ObjectUtils.isEmpty(response.get(UniroConstant.DATA))) {
					String data = response.get(UniroConstant.DATA).toString();
					return OBJECT_MAPPER.readValue(data, new TypeReference<Company>() {});
				}
				return new Company();
			}catch(IOException ex){
				throw new UniroWebException("Response Converson error for Company on web.", 500);
			}
		} catch (UniroWebException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new UniroWebException("Error while saving company information", 401);
			}
			
			if (errorCode.equals("403")) {
				log.error("Access Denied this api.");
				throw new UniroWebException("Access Denied this api.", 403);
			}
			
			if (e.code == 500) {
				log.error(e.getMessage());
				throw new UniroWebException(e.getMessage(), e.code);
			}
			log.error("Error while saving company information");
			throw new UniroWebException("Error while saving company information");
		}
	}

	public List<Company> getAllCompaines() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_COMPAINES, null, null, HttpMethod.GET);
			
			status = response.get(UniroConstant.STATUS_CODE).intValue();
			String errorMsg = response.get(UniroConstant.MESSAGE).asText();
			if (status != 200) {
				throw new UniroWebException(errorMsg, status);
			}
			
			try{
				if (!ObjectUtils.isEmpty(response.get(UniroConstant.DATA))) {
					String data = response.get(UniroConstant.DATA).toString();
					return OBJECT_MAPPER.readValue(data, new TypeReference<List<Company>>() {});
				}
				return new ArrayList<>();
			}catch(IOException ex){
				throw new UniroWebException("Response Converson error for Companies on web.", 500);
			}

		} catch (UniroWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new UniroWebException("Error while fetching all companies details", 401);
			}
			
			if (errorCode.equals("403")) {
				log.error("Access Denied this api.");
				throw new UniroWebException("Access Denied this api.", 403);
			}
			
			if (e.code == 500) {
				log.error(e.getMessage());
				throw new UniroWebException(e.getMessage(), e.code);
			}
			log.error("Error while fetching all companies details");
			throw new UniroWebException("Error while fetching all companies details");
		}
	}

	public Company getCompanyById(String companyId) {

		int status;
		String url = String.format(URLConstants.GET_COMPANY_DETAIL_BY_ID, companyId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			
			status = response.get(UniroConstant.STATUS_CODE).intValue();
			String errorMsg = response.get(UniroConstant.MESSAGE).asText();
			if (status != 200) {
				throw new UniroWebException(errorMsg, status);
			}
			
			try{
				if (!ObjectUtils.isEmpty(response.get(UniroConstant.DATA))) {
					String data = response.get(UniroConstant.DATA).toString();
					return OBJECT_MAPPER.readValue(data, new TypeReference<Company>() {});
				}
				return new Company();
			}catch(IOException ex){
				throw new UniroWebException("Response Converson error for Company on web.", 500);
			}
		} catch (UniroWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new UniroWebException("Error while fetching company by id", 401);
			}
			
			if (errorCode.equals("403")) {
				log.error("Access Denied this api.");
				throw new UniroWebException("Access Denied this api.", 403);
			}
			
			if (e.code == 500) {
				log.error(e.getMessage());
				throw new UniroWebException(e.getMessage(), e.code);
			}
			log.error("Error while fetching company by id");
			throw new UniroWebException("Error while fetching company by id");
		}
	}

	public Company deleteCompany(String companyId) {

		if (ObjectUtils.isEmpty(companyId)) {
			throw new UniroWebException("Invalid company id");
		}
		try {
			Company company = this.getCompanyById(companyId);
			if (!ObjectUtils.isEmpty(company)) {
				company.setStatus(0);
				this.saveCompany(company);
			}
			return company;
		} catch (UniroWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new UniroWebException("Error while deleting company information", 401);
			}
			
			if (errorCode.equals("403")) {
				log.error("Access Denied this api.");
				throw new UniroWebException("Access Denied this api.", 403);
			}
			
			log.error("Error while deleting company information");
			throw new UniroWebException("Error while deleting company information");
		}
	}

	public Boolean validateBycrnNo(String crn) {

		if (ObjectUtils.isEmpty(crn)) {
			throw new UniroWebException("Invalid crnNo");
		}
		try {
			String url = String.format(URLConstants.VALIDATE_BY_CRN_NO,crn);
			
			JsonNode response = restServiceUtil.makeRequest(url, null, null,HttpMethod.GET);
			
			int status = response.get(UniroConstant.STATUS_CODE).intValue();
			String errorMsg = response.get(UniroConstant.MESSAGE).asText();
			if (status != 200) {
				throw new UniroWebException(errorMsg, status);
			}
			
			try{
				if (!ObjectUtils.isEmpty(response.get(UniroConstant.DATA))) {
					String data = response.get(UniroConstant.DATA).toString();
					return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {});
				}
				return false;
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
	
	public Company getCurrentCompany(){
		
		
		String crn = UserRoleUtil.getCurrentUserCrn();
		
		try {
			String url = String.format(URLConstants.FETCH_COMPANY_BY_CRN_NO,crn);
			
			JsonNode response = restServiceUtil.makeRequest(url, null, null,HttpMethod.GET);
			
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
