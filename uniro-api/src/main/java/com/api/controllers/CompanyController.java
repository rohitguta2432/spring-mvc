package com.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.Filter;
import com.api.dto.Response;
import com.common.models.Company;
import com.api.services.CompanyService;
import com.api.utils.QueryParser;

/**
 * @author RITESH SINGH
 * @since JDK 1.8
 * @version 1.0
 *
 */
@RestController
@RequestMapping(value = "company", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@RequestMapping(value = "{crn}")
	public ResponseEntity<Response<Company>> getCompanyByCrn(@PathVariable("crn") String crn) {
		return new ResponseEntity<>(
				new Response<>(HttpStatus.OK.value(), "Company fetched successfully.", companyService.getByCrn(crn)),
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Company>> save(@RequestBody Company company) {
		return new ResponseEntity<>(
				new Response<>(HttpStatus.OK.value(), "Company saved successfully.", companyService.save(company)),
				HttpStatus.OK);
	}

	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Company>>> getAllCompanies(
			@RequestParam(value = "q", required = false) String query, Pageable pageable) {
		List<Filter> filters = QueryParser.parse(query);
		return new ResponseEntity<>(new Response<>(HttpStatus.OK.value(), "Companies fetched successfully",
				companyService.getAllCompanies(pageable, filters),
				companyService.getAllCompanies(null, filters).size()), HttpStatus.OK);
	}

	@RequestMapping(value = "delete/{id}/{updatedBy}", method = RequestMethod.DELETE)
	public ResponseEntity<Response<Boolean>> delete(@PathVariable("id") String id,
			@PathVariable("updatedBy") String updatedBy) {
		return new ResponseEntity<>(new Response<>(HttpStatus.OK.value(), "Company deleted successfully.", true),
				HttpStatus.OK);
	}

	@RequestMapping(value = "getCompanyById/{companyId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Company>> getCompanyById(@PathVariable("companyId") String companyId) {
		return new ResponseEntity<Response<Company>>(new Response<Company>(HttpStatus.OK.value(),
				"Company  information fetched successfully.", companyService.get(companyId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "validateByCRNno/{crn}", method = RequestMethod.GET)
	public ResponseEntity<Response<Boolean>> validateBycrnNo(
			@PathVariable("crn") String crn) {
		boolean flag=companyService.validateBycrnNo(crn);
		 return  flag ?  new ResponseEntity<>(new Response<>(HttpStatus.OK.value(), "CRN No. already taken", flag),
					HttpStatus.OK) : 
		new ResponseEntity<>(new Response<>(HttpStatus.OK.value(), "CRN No. not taken", flag),HttpStatus.OK);		
	}
	
}
