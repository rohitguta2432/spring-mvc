package com.api.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.api.dto.Filter;
import com.common.models.Company;

public interface CompanyService extends UniroService<Company> {

	Company getByCrn(String crn);

	List<Company> getAllCompanies(Pageable pageable, List<Filter> filters);

	boolean validateBycrnNo(String crn);
	

}
