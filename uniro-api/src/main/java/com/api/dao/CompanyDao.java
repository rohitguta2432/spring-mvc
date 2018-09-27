package com.api.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.common.models.Company;

@Repository
public interface CompanyDao extends PagingAndSortingRepository<Company, String> {

	Company findOneByCrnAndStatus(String crn, int status);
	Company findOneById(String id, int status);
	boolean existsByCrnAndStatus(String crn, int status);
	
}
