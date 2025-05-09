package com.pokerface.identity_service.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
	@Size(min = 5, message = "USERNAME_INVALID")
	String username;
	
	@Size(min = 8, message = "INVALID_PASSWORD")
	String password;
	String firstName;
	String lastName;
	LocalDate dob;
	
}
