package com.pokerface.identity_service.dto.response;

import java.time.LocalDate;

import com.pokerface.identity_service.dto.request.UserUpdateRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
	String id;
	String username;
	String password;
	String firstName;
	String lastName;
	LocalDate dob;
}
