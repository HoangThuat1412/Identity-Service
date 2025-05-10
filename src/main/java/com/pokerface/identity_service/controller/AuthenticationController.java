package com.pokerface.identity_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.pokerface.identity_service.dto.request.ApiResponse;
import com.pokerface.identity_service.dto.request.AuthenticationRequest;
import com.pokerface.identity_service.dto.request.IntrospectRequest;
import com.pokerface.identity_service.dto.response.AuthenticationResponse;
import com.pokerface.identity_service.dto.response.IntrospectResponse;
import com.pokerface.identity_service.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
	AuthenticationService authenticationService;
	
	@PostMapping("/token")
	ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		var result = authenticationService.authenticate(request);
		return ApiResponse.<AuthenticationResponse>builder()
				.result(result)
				.build();
	}
	
	@PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
