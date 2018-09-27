package com.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.common.constants.Constants;
import com.api.dto.Filter;
import com.api.exception.UniroException;
import com.common.models.Company;
import com.api.dao.CompanyDao;
import com.api.utils.QueryBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author RITESH SINGH
 * @since JDK 1.8
 * @version 1.0
 *
 */

@Slf4j
@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CompanyDao companyDao;

	@Override
	public Company save(Company entity) {

		if (ObjectUtils.isEmpty(entity) || StringUtils.isEmpty(entity.getCrn())) {
			log.debug("Company entity or crn can not be null or empty.");
			throw new UniroException("Company entity or crn can not be null or empty.");
		}
		if (ObjectUtils.isEmpty(entity.getId())) {
			entity.setId(null);
		}

		if (ObjectUtils.isEmpty(entity.getId())) {
			if (companyDao.existsByCrnAndStatus(entity.getCrn(), Constants.STATUS_ACTIVE)) {

				log.debug("Company already registered with same crn.");
				throw new UniroException("Company already registered with same crn.");
			}
		} else {
			Company existingCompany = get(entity.getId());
			if (ObjectUtils.isEmpty(existingCompany)) {
				log.debug("Company not found by given id.");
				throw new UniroException("Company not found by given id.");
			} else {

				if (entity.getStatus() == 0) {
					existingCompany.setStatus(0);
				} else {				
					existingCompany.setName(entity.getName());
					existingCompany.setDescription(entity.getDescription());
					existingCompany.setType(entity.getType());
					existingCompany.setUrl(entity.getUrl());
					existingCompany.getAddress().setCity(entity.getAddress().getCity());
					existingCompany.getAddress().setState(entity.getAddress().getState());
					existingCompany.getAddress().setCountry(entity.getAddress().getCountry());
					existingCompany.getAddress().setFullAddress(entity.getAddress().getFullAddress());
					existingCompany.getAddress().setPincode(entity.getAddress().getPincode());
					existingCompany.getContacts().setEmail(entity.getContacts().getEmail());
					existingCompany.getContacts().setMobileNo(entity.getContacts().getMobileNo());
					existingCompany.getContacts().setTelephoneNo(entity.getContacts().getTelephoneNo());
					existingCompany.setUpdatedDate();
			
				}
			}

			return companyDao.save(existingCompany);
		}

		return companyDao.save(entity);
	}

	@Override
	public Company update(String id, Company entity) {

		return null;
	}

	@Override
	public Company get(String id) {
		return companyDao.findOneById(id, Constants.STATUS_ACTIVE);
	}

	@Override
	public Company getByCrn(String crn) {
		return companyDao.findOneByCrnAndStatus(crn, Constants.STATUS_ACTIVE);
	}

	@Override
	public List<Company> getAllCompanies(Pageable pageable, List<Filter> filters) {
		try {
			Query query = QueryBuilder.createQuery(filters, pageable);
			query.addCriteria(Criteria.where(Constants.STATUS).is(Constants.STATUS_ACTIVE));
			List<Company> companies = mongoTemplate.find(query, Company.class);
			return companies;
		} catch (Exception e) {
			log.error("Error while fetching companies.", e);
			throw new UniroException("Error while fetching companies.");
		}
	}

	
	@Override
	public boolean validateBycrnNo(String crn) {
		
		return companyDao.existsByCrnAndStatus(crn,Constants.STATUS_ACTIVE);
	}


}
