package com.pokerface.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pokerface.identity_service.dto.request.UserCreationRequest;
import com.pokerface.identity_service.dto.request.UserUpdateRequest;
import com.pokerface.identity_service.dto.response.UserResponse;
import com.pokerface.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	User toUser(UserCreationRequest request);
	
	UserResponse toUserResponse(User user);
	void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
