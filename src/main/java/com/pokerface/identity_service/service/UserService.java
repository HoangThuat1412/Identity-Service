package com.pokerface.identity_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pokerface.identity_service.dto.request.UserCreationRequest;
import com.pokerface.identity_service.dto.request.UserUpdateRequest;
import com.pokerface.identity_service.dto.response.UserResponse;
import com.pokerface.identity_service.entity.User;
import com.pokerface.identity_service.exception.AppException;
import com.pokerface.identity_service.exception.ErrorCode;
import com.pokerface.identity_service.mapper.UserMapper;
import com.pokerface.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
	UserRepository userRepository;
	UserMapper userMapper;
	
	public User createUser(UserCreationRequest request) {
		if(userRepository.existsByUsername(request.getUsername()))
			throw new AppException(ErrorCode.USER_EXISTED);
		User user = userMapper.toUser(request);
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		return userRepository.save(user);
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
	
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	
	
	public UserResponse getUser(String id) {
		return userMapper.toUserResponse(userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found")));
	}
	
}
