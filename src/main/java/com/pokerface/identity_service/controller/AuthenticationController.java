package com.pokerface.identity_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokerface.identity_service.dto.request.ApiResponse;
import com.pokerface.identity_service.dto.request.AuthenticationRequest;
import com.pokerface.identity_service.dto.response.AuthenticationResponse;
import com.pokerface.identity_service.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
	AuthenticationService authenticationService;
	
	@PostMapping("/log-in")
	ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		boolean result = authenticationService.authenticate(request);
		return ApiResponse.<AuthenticationResponse>builder()
				.result(AuthenticationResponse.builder()
					.authenticated(result)
					.build())
				.build();
	}
}
