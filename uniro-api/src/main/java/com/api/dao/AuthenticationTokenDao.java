package com.api.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.common.models.AuthenticationToken;
import com.common.models.User;

public interface AuthenticationTokenDao extends PagingAndSortingRepository<AuthenticationToken, String> {

	List<AuthenticationToken> findAllByUserId(String userId);	
	
	void deleteByUserId(String userId);
}
