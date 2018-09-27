package com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.models.Company;
import com.services.CompanyService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author RITESH SINGH
 * @since JDK 1.8
 * @version 1.0
 *
 */
@Slf4j
@Controller
@RequestMapping(value = "company")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;

	@GetMapping("")
	public String companyList(Model model) {
		log.info("fetching list of all companies");	
		model.addAttribute("companies", companyService.getAllCompaines());		
		model.addAttribute("companyListActive", true);
		model.addAttribute("setting", true);
		return "company/view";
	}
	
	
	@GetMapping("create")
	public String create(Model model) {
		model.addAttribute("title", "Create");
		model.addAttribute("companyCreateActive", true);
		model.addAttribute("setting", true);
		return "company/create";
	}
	
	@PostMapping(value = "save")
	public String saveCompany(@ModelAttribute("company") Company company, Model model, RedirectAttributes redirectAttributes) {
		log.info("saving company details");
		companyService.saveCompany(company);
		redirectAttributes.addFlashAttribute("msg", "Company saved successfully");
		return "redirect:/company";
	}
	
	@GetMapping(value = "update/{id}")
	public String updateCompany(@PathVariable("id") String id, Model model) {		
		model.addAttribute("company",companyService.getCompanyById(id));
		model.addAttribute("title", "Update");
		return "company/create";
	}
	
	@GetMapping(value = "delete/{id}")
	public String deleteCompany(@PathVariable("id") String id) {
		companyService.deleteCompany(id);
		return "redirect:/company";
	}
	
	@GetMapping(value = "validateBycrnNo/{crn}")
	@ResponseBody
	public boolean validateByDeviceId(@PathVariable("crn") String crn ) {
		return companyService.validateBycrnNo(crn);
	}
}