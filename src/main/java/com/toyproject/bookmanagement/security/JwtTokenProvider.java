package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtTokenProvider {
	
	private final Key key;
	
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
	
	public JwtRespDto generateToken(Authentication authentication) {
		
		Date tokenExpiresDate = new Date(new Date().getTime() + (1000*60*60*24)); //현재시간 + 하루
		
		// StringBuilder를 이용해서 Authentication 객체에서 받은 roleName을 받는다.
		StringBuilder builder = new StringBuilder();
		
		// authentication객체에서 받은 roleName을 ,를 기준으로 다 받아줌. 
		authentication.getAuthorities().forEach(authroity ->{
			builder.append(authroity.getAuthority() + ",");
		});
		
		// index는 authoritiesBulBuilder의 맨 마지막 ,를 제거하기 위해 사용
		builder.delete(builder.length() - 1, builder.length());
		
		String authorities = builder.toString();
		
		// Post요청 Header에서 Authorization 키에 value 값에 authorities앞에 'Bearer' Type을 정해준다.
		
		//accessToken을 만들어야한다.
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName())			// 토큰의 제목(email)
				.claim("auth", authorities)						// auth (roleName)
				.setExpiration(tokenExpiresDate)				// 토큰 만료 시간
				.signWith(key, SignatureAlgorithm.HS256)		// 토큰 암호화
				.compact();
		
		
		
		return JwtRespDto.builder().grantType("Bearer").accessToken(accessToken).build();
	}
}
