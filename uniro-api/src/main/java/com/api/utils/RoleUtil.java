package com.api.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.common.constants.Constants;
import com.common.models.UniroUser;

public class RoleUtil {

	public static boolean isAdmin(UniroUser hitechUser) {

		for (GrantedAuthority authorities : hitechUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_ADMIN)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isCurrentUserHasAdminRole() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UniroUser hitechUser = (UniroUser) auth.getPrincipal();
		for (GrantedAuthority authorities : hitechUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_ADMIN)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isCurrentUserHasSuperAdminRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UniroUser hitechUser = (UniroUser) auth.getPrincipal();
		for (GrantedAuthority authorities : hitechUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_SUPER_ADMIN)) {
				return true;
			}
		}

		return false;
	}

	public static UniroUser getCurrentUseInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		return (UniroUser) auth.getPrincipal();
	}
}

