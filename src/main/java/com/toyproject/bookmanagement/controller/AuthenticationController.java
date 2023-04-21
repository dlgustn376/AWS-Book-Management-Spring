package com.toyproject.bookmanagement.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toyproject.bookmanagement.aop.annotation.ValidAspect;
import com.toyproject.bookmanagement.dto.auth.LoginReqDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	@ValidAspect
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginReqDto loginReqDto, BindingResult bindingResult) {
//		요청에 응답을 해줘야 함.
//		System.out.println(loginReqDto);
		return ResponseEntity.ok(authenticationService.signin(loginReqDto));
	}
	

	@ValidAspect
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult){
		authenticationService.checkDuplicatedEmail(signupReqDto.getEmail());
		authenticationService.signup(signupReqDto);
		return ResponseEntity.ok().body(true);
	}
	
	// 클라이언트에서의 요청에 대한 응답을 보내줌
	@GetMapping("/authenticated")
	public ResponseEntity<?> authenticated(String accessToken){
		return ResponseEntity.ok().body(authenticationService.authenticated(accessToken));
	}
	
}
