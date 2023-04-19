package com.toyproject.bookmanagement.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.toyproject.bookmanagement.entity.Authority;
import com.toyproject.bookmanagement.entity.User;

@Mapper
public interface UserRepository {
	
	public User findUserByEmail(String email);
	public int saveUser(User user);
	public void addAuthorities(List<Authority> authority);
}
