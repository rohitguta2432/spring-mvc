package com.common.models;

import java.util.Comparator;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import com.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author OrangeMantra
 * @since JDK 1.8
 * @version 1.0
 *
 */
@Data
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@Document(collection = "users")
@JsonInclude(content = Include.NON_NULL)
@Getter
@Setter
public class User extends AbstractEntity { 

  private static final long serialVersionUID = 1L;	
	
  @Id
  private String id;
  private String firstName;
  private String lastName;
  private String fullName;
  private String email;
  private int status = Constants.STATUS_ACTIVE;
  private String username;
  private String parentId = Constants.ADMIN;
  private String password;
  private String phone;
  private String authorities;
  private Address address;
  private String crn = "super_admin";
  private String hashedUserImage;
  private String dob;
  private double yearExperience;
  private String availability;
  private String employeeId;
  
  @Transient
  private MultipartFile userProfileFileUpload;
  
  @Transient
  private String error;
  
  @Transient
  private int errorStatus=1;
  
  @Transient
  private String companyName;

  @Transient
  private String parentName;
  
  @Transient
  private String parentType;
}
