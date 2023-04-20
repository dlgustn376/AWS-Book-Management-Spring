package com.toyproject.bookmanagement.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;
import com.toyproject.bookmanagement.dto.auth.LoginReqDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.entity.Authority;
import com.toyproject.bookmanagement.entity.User;
import com.toyproject.bookmanagement.exception.CustomException;
import com.toyproject.bookmanagement.exception.ErrorMap;
import com.toyproject.bookmanagement.repository.UserRepository;
import com.toyproject.bookmanagement.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService{
	
	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;
	
	public void checkDuplicatedEmail(String email) {
		
		User userEntity = userRepository.findUserByEmail(email);
		
		if(userEntity != null) {
			throw new CustomException("Duplicated Email", 
					ErrorMap.builder()
					.put("email","이미 사용중인 이메일입니다.").bulid());
		}
	}
	
	public void signup(SignupReqDto signupReqDto) {
		
		User userEntity = signupReqDto.toEntity();
		userRepository.saveUser(userEntity);
		
		userRepository.saveAuthority(Authority.builder()
				.userId(userEntity.getUserId())
				.roleId(1)
				.build());
		//Authority를 List로 받아오는 경우.
		//List<Authority> authorities = new ArrayList<>();
		//authorities.add(Authority.builder()
		//		.userId(userEntity.getUserId())
		//		.roleId(1)
		//		.build());
		
	}
	
	public JwtRespDto signin(LoginReqDto loginReqDto) {
		
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(loginReqDto.getEmail(), loginReqDto.getPassword());
		
		// loadUserByUsername을 호출해 준다.
		Authentication authentication = 
				authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		
		return jwtTokenProvider.generateToken(authentication);
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// authenticationManager가 하는 일이다. 여기서는 username은 email이다.
		User userEntity = userRepository.findUserByEmail(username);
		
		if(userEntity == null) {
			throw new CustomException("로그인 실패",
					ErrorMap.builder()
					.put("email", "사용자 정보를 확인하세요.")
					.bulid());
		}
		
		return userEntity.toPrincipal();
	}
	
	
	
	
}
