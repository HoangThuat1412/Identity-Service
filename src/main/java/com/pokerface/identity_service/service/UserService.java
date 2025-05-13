package com.pokerface.identity_service.service;

import java.awt.Container;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pokerface.identity_service.dto.request.UserCreationRequest;
import com.pokerface.identity_service.dto.request.UserUpdateRequest;
import com.pokerface.identity_service.dto.response.UserResponse;
import com.pokerface.identity_service.entity.User;
import com.pokerface.identity_service.enums.Role;
import com.pokerface.identity_service.exception.AppException;
import com.pokerface.identity_service.exception.ErrorCode;
import com.pokerface.identity_service.mapper.UserMapper;
import com.pokerface.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
	UserRepository userRepository;
	UserMapper userMapper;
	PasswordEncoder passwordEncoder;
	
	public UserResponse createUser(UserCreationRequest request) {
		if(userRepository.existsByUsername(request.getUsername()))
			throw new AppException(ErrorCode.USER_EXISTED);
		User user = userMapper.toUser(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		HashSet<String> roles = new HashSet<>();
		roles.add(Role.USER.name());
		
		user.setRoles(roles);
		
		return userMapper.toUserResponse(userRepository.save(user));
	}
	
	public UserResponse getMyInfor() {
		var context = SecurityContextHolder.getContext();
		context.getAuthentication().getName();
		String name = context.getAuthentication().getName();
		
		User user = userRepository.findByUsername(name).orElseThrow(
				() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		
		return userMapper.toUserResponse(user);
	}
	
	public UserResponse updateUser(String userID, UserUpdateRequest request) {
		User user = userRepository.findById(userID)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		userMapper.updateUser(user, request);
		
		return userMapper.toUserResponse(userRepository.save(user));
	}
	
	public void deleteUser(String userId) {
		userRepository.deleteById(userId);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserResponse> getUsers() {
		log.info("In method get Users");
		return userRepository.findAll().stream()
				.map(userMapper::toUserResponse).toList();
	}
	
	
	@PostAuthorize("returnObject.username == authentication.name")
	public UserResponse getUser(String id) {
		log.info("In method get user by Id");
		return userMapper.toUserResponse(userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found")));
	}
	
}
