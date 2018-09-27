package com.common.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id", "crn"}, callSuper = false)
@Document(collection = "company")
@JsonInclude(content = Include.NON_NULL)
public class Company extends AbstractEntity {

	private static final long serialVersionUID = 2110466220667261027L;

	@Id
	private String id;
	private String name;
	private String description;
	private String type;
	private String crn;
	private Address address;
	private Contacts contacts;
	private String url;
	private int status = Constants.STATUS_ACTIVE;

}
