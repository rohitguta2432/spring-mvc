package com.api.services;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.api.dao.UserDao;
import com.api.dto.Filter;
import com.api.exception.UniroException;
import com.api.utils.QueryBuilder;
import com.api.utils.RoleUtil;
import com.common.constants.Constants;
import com.common.models.Company;
import com.common.models.User;

import lombok.extern.slf4j.Slf4j;

/**
 * @author RITESH SINGH
 * @since JDK 1.8
 * @version 1.0
 *
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CompanyService companyService;

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
			Pattern.CASE_INSENSITIVE);
	private static Pattern DATE_PATTERN = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/(\\d\\d\\d\\d)");

	@Override
	public User save(User entity) {

		if (ObjectUtils.isEmpty(entity)) {
			log.error("Error while saving user.");
			throw new UniroException("Error while saving user.");
		}

		if (StringUtils.isEmpty(entity.getId())) {

			this.validateExistingUser(entity);

			entity.setCreatedDate();

		} else {

			entity = this.updateValidation(entity);
			entity.setUpdatedDate();
		}

		log.debug("Saved user.");
		return userDao.save(entity);
	}

	private User updateValidation(User entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Error while updating user.");
			throw new UniroException("Error while updating user.");
		}

		User user = this.get(entity.getId().replaceAll("^\\\"|\\\"$", ""));
		if (ObjectUtils.isEmpty(user)) {
			log.info("Error while updating user.");
			throw new UniroException("Error while updating user.");
		}
		try {
			user.setDob(entity.getDob());
		} catch (UniroException ex) {
			log.info("Error while updating user's dob.");
			throw new UniroException("Error while updating user's dob.");
		}
		try {
			user.setEmail(entity.getEmail());
		} catch (UniroException ex) {
			log.info("Error while updating user's Email.");
			throw new UniroException("Error while updating user's Email.");
		}
		try {
			user.setFirstName(entity.getFirstName());
		} catch (UniroException ex) {
			log.info("Error while updating user's FirstName.");
			throw new UniroException("Error while updating user's FirstName.");
		}
		try {
			user.setLastName(entity.getLastName());
		} catch (UniroException ex) {
			log.info("Error while updating user's LastName.");
			throw new UniroException("Error while updating user's LastName.");
		}
		try {
			user.setYearExperience(entity.getYearExperience());
		} catch (UniroException ex) {
			log.info("Error while updating user's Year Experience.");
			throw new UniroException("Error while updating user's Year Experience.");
		}
		try {
			user.setEmployeeId(entity.getEmployeeId());
		} catch (UniroException ex) {
			log.info("Error while updating user's EmployeeId.");
			throw new UniroException("Error while updating user's EmployeeId.");
		}
		try {
			user.setAvailability(entity.getAvailability());
		} catch (UniroException ex) {
			log.info("Error while updating user's Job Type.");
			throw new UniroException("Error while updating user's Job Type.");
		}
		try {
			user.setPhone(entity.getPhone());
		} catch (UniroException ex) {
			log.info("Error while updating user's Phone.");

			throw new UniroException("Error while updating user's Phone.");
		}
		try {
			user.getAddress().setCity(entity.getAddress().getCity());
		} catch (UniroException ex) {
			log.info("Error while updating user's City.");
			throw new UniroException("Error while updating user's City.");
		}
		try {
			user.getAddress().setCountry(entity.getAddress().getCountry());
		} catch (UniroException ex) {
			log.info("Error while updating user's Country.");
			throw new UniroException("Error while updating user's Country.");
		}
		try {
			user.getAddress().setFullAddress(entity.getAddress().getFullAddress());
		} catch (UniroException ex) {
			log.info("Error while updating user's FullAddress.");
			throw new UniroException("Error while updating user's FullAddress.");
		}
		try {
			user.getAddress().setPincode(entity.getAddress().getPincode());
		} catch (UniroException ex) {
			log.info("Error while updating user's pincode.");
			throw new UniroException("Error while updating user's pincode.");
		}
		try {
			user.getAddress().setState(entity.getAddress().getState());
		} catch (UniroException ex) {
			log.info("Error while updating user's state.");
			throw new UniroException("Error while updating user's state.");
		}

		try {
			user.setHashedUserImage(
					entity.getHashedUserImage() == "" ? user.getHashedUserImage() : entity.getHashedUserImage());
		} catch (UniroException ex) {
			log.info("Error while updating user's image.");
			throw new UniroException("Error while updating user's image.");
		}

		return user;
	}

	private void validateExistingUser(User entity) {
		User user = this.getUserByUserName(entity.getUsername());
		if (!ObjectUtils.isEmpty(user)) {
			throw new UniroException("Username is already used.");
		}
	}

	@Override
	public List<User> saveAll(List<User> users) {

		return (List<User>) userDao.saveAll(users);
	}

	@Override
	public User update(String id, User entity) {
		return null;
	}

	@Override
	public User get(String id) {
		// Optional<User> user = userDao.findById(id);
		return userDao.findOneByIdAndStatus(id, Constants.STATUS_ACTIVE);
	}

	@Override
	public User getUserByIdAndCrn(String id, String crn) {
		// Optional<User> user = userDao.findById(id);
		return userDao.findOneByIdAndCrnAndStatus(id, crn, Constants.STATUS_ACTIVE);
	}

	@Override
	public User getUserByUserName(String userName) {
		return userDao.findOneByUsernameAndStatus(userName, Constants.STATUS_ACTIVE);
	}

	@Override
	public List<User> getAllUsers(Pageable pageable, List<Filter> filters) {
		try {

			Query query = QueryBuilder.createQuery(filters, pageable);

			if (ObjectUtils.isEmpty(filters)) {

				if (RoleUtil.isCurrentUserHasSuperAdminRole()) {
					query.addCriteria(Criteria.where(Constants.ROLE).is(Constants.ROLE_ADMIN));
				}
			}

			List<User> users = mongoTemplate.find(query, User.class);
			
			return users.parallelStream().map(setCompanyAndParentInfo).collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Error while fetching users.", e);
			throw new UniroException("Error while fetching users.");
		}
	}

	@Override
	public List<User> getUserEmployeeDetailById(String userId) {

		if (ObjectUtils.isEmpty(userId)) {
			log.error("User id cannot be null or empty.");
			throw new UniroException("User id cannot be null or empty.");
		}
		List<User> users = null;
		User user = this.get(userId);
		if (ObjectUtils.isEmpty(user)) {
			log.error("User not found by given id.");
			throw new UniroException("User not found by given id.");
		}

		String role = user.getAuthorities();

		if(Constants.ROLE_SUPER_ADMIN.equals(role)){
			users = userDao.findAllByAuthoritiesAndParentIdAndStatus(Constants.ROLE_ADMIN, userId,
					Constants.STATUS_ACTIVE);
		}
		
		if (Constants.ROLE_ADMIN.equals(role)) {
			users = userDao.findAllByAuthoritiesAndParentIdAndStatus(Constants.ROLE_FLEET_MANAGER, userId,
					Constants.STATUS_ACTIVE);
		}

		if (Constants.ROLE_FLEET_MANAGER.equals(role)) {
			users = userDao.findAllByAuthoritiesAndParentIdAndStatus(Constants.ROLE_DRIVER, userId,
					Constants.STATUS_ACTIVE);
		}
	
		return users.parallelStream().map(setCompanyAndParentInfo).collect(Collectors.toList());
	}

	@Override
	public List<User> uploadBulkUser(List<User> list) {
		if (ObjectUtils.isEmpty(list)) {
			throw new UniroException("User list cannot be empty or null");
		}

		LinkedList<User> errorlist = new LinkedList<>();
		LinkedList<User> userList = new LinkedList<>();
		LinkedList<String> errorMsg = new LinkedList<>();

		list.stream().forEach(user -> {

			if (StringUtils.isEmpty(user.getFullName()) && user.getFullName().length() > Constants.MAXSIZE) {
				errorMsg.add("User name length must be less than 50 characters");
			}

			if (StringUtils.isEmpty(user.getEmail())) {
				errorMsg.add("User email id cannot be empty");
			} else {
				if (!emailValidate(user.getEmail())) {
					errorMsg.add("Invalid email id");
				} else {
					User isExisting = userDao.findOneByUsernameAndStatus(user.getEmail(), Constants.STATUS_ACTIVE);
					if (!ObjectUtils.isEmpty(isExisting)) {
						errorMsg.add("User email id already exist");
					}
				}
			}

			if (ObjectUtils.isEmpty(user.getDob())) {
				errorMsg.add("User Date of birth cannot be empty");
			} else {
				if (!dateValidation(user.getDob())) {
					errorMsg.add("Invalid date of birth");
				}
			}

			if (ObjectUtils.isEmpty(user.getYearExperience())) {
				errorMsg.add("User experience cannot be empty");
			}

			if (ObjectUtils.isEmpty(user.getEmployeeId())) {
				errorMsg.add("User employee id cannot be empty");
			}

			if (!ObjectUtils.isEmpty(user.getAddress())) {
				if (ObjectUtils.isEmpty(user.getAddress().getPincode())) {
					errorMsg.add("User pincode cannot be empty");
				}
			}

			if (ObjectUtils.isEmpty(user.getPhone())) {
				errorMsg.add("User mobile no cannot be empty");
			}

			if (!ObjectUtils.isEmpty(errorMsg)) {
				user.setError(errorMsg.toString());
				user.setErrorStatus(Constants.ERROR);
				errorlist.add(user);
			} else {
				userList.add(user);
			}

			errorMsg.clear();
		});

		userDao.saveAll(userList);
		return errorlist;
	}

	public static boolean emailValidate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	public static boolean dateValidation(String date) {
		return DATE_PATTERN.matcher(date).matches();
	}

	@Override
	public User getCompanyOwnerByCrn(String crn) {

		if (ObjectUtils.isEmpty(crn)) {
			log.error("crn cannot be null or empty.");
			throw new UniroException("crn cannot be null or empty.");
		}
		List<User> users = userDao.findAllByCrnAndAuthoritiesAndStatus(crn, Constants.ROLE_ADMIN, Constants.STATUS_ACTIVE); 
		
		return !ObjectUtils.isEmpty(users) && users.size() > 0 ? users.get(0) : null;
	}

	private Function<User, User> setCompanyAndParentInfo = (user -> {
		
		try{
			Company company = companyService.getByCrn(user.getCrn());
			User parentUser = userDao.findOneById(user.getParentId());
			user.setCompanyName(company.getName());
			user.setParentName(parentUser.getFullName());
			user.setParentType(parentUser.getAuthorities());
		}catch(Exception e){
			log.info("error wile setup company and parent ingo in users.");
		}
		
		return user;
	});
	
}
