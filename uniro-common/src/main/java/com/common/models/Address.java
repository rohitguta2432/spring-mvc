package com.common.models;

import lombok.Data;

@Data
public class Address{

	private String fullAddress;
	private String city;
	private String state;
	private int pincode;
	private String country;
}
