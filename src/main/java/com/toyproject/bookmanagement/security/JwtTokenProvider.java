package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	// 클라이언트에서 보낸 토큰이 유효한지 검사.
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch(ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("IllegalArgument JWT Token", e);
		} catch (Exception e) {
			log.info("JWT Token Error", e);
		}
		return false;
	}
	
	// 클라이언트에서 accessToken을 주어야 비교를 할 수 있다.
	public String getToken(String token) {
		String type = "Bearer";
		if(StringUtils.hasText(token)&&token.startsWith(type)) {
			return token.substring(type.length() + 1);
		}	
		return null;
	}
	
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();				// 정보를 꺼내 옴(권한, email)
	}
	
	
}



