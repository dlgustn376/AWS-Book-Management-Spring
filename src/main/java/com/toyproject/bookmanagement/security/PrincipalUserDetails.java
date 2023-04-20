package com.toyproject.bookmanagement.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.toyproject.bookmanagement.entity.Authority;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PrincipalUserDetails implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5054909199192475039L;
	
	private int userId;
	private String email;
	private String password;
//	private String name;
//	private String provider;
	private List<Authority> authorities;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		// 여기서 SimpleGrantedAuthority List로 RoleName을 가져옴.
		this.authorities.forEach(authority-> {
			authorities.add(new SimpleGrantedAuthority(authority.getRole().getRoleName()));
		});
		
		return authorities;  //simpleGrantedAuthority를 담는 List이다.
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
