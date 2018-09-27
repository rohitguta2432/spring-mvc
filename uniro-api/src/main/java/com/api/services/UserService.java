package com.api.services;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.api.dto.Filter;
import com.common.models.User;

public interface UserService extends UniroService<User> {

	List<User> getAllUsers(Pageable pageable, List<Filter> filters);

	User getUserByUserName(String userName);

	List<User> saveAll(List<User> users);

	User getUserByIdAndCrn(String id, String crn);

	User getCompanyOwnerByCrn(String crn);

	List<User> getUserEmployeeDetailById(String userId);

	List<User> uploadBulkUser(List<User> list);

}
