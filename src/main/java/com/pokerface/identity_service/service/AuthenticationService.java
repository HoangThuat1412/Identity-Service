package com.pokerface.identity_service.service;

import java.io.Console;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.wiring.ClassNameBeanWiringInfoResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pokerface.identity_service.dto.request.AuthenticationRequest;
import com.pokerface.identity_service.dto.request.IntrospectRequest;
import com.pokerface.identity_service.dto.response.AuthenticationResponse;
import com.pokerface.identity_service.dto.response.IntrospectResponse;
import com.pokerface.identity_service.exception.AppException;
import com.pokerface.identity_service.exception.ErrorCode;
import com.pokerface.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
	UserRepository userRepository;
	
	@NonFinal
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY;
	
	 public IntrospectResponse introspect(IntrospectRequest request)
	            throws JOSEException, ParseException {
	        var token = request.getToken();

	        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

	        SignedJWT signedJWT = SignedJWT.parse(token);

	        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

	        var verified = signedJWT.verify(verifier);

	        return IntrospectResponse.builder()
	                .valid(verified && expiryTime.after(new Date()))
	                .build();
	    }
	
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		var user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() ->new AppException(ErrorCode.USER_NOT_EXISTED));
		
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		boolean authenticated =  passwordEncoder.matches
				(request.getPassword(), user.getPassword());
		
		if(!authenticated) 
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		
		 var token = generateToken(request.getUsername());
		 
		 return AuthenticationResponse.builder()
	                .token(token)
	                .authenticated(true)
	                .build();
	}
	
	 private String generateToken(String username) {
		 JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		 
		 JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				 .subject(username)
				 .issuer("pokerface.com")
				 .issueTime(new Date())
				 .expirationTime(new Date(
						 Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
						 ))
				 .claim("userId", "Custom")
				 .build();
		 
		 Payload payload = new Payload(jwtClaimsSet.toJSONObject());
		 
		 JWSObject jwsObject = new JWSObject(header, payload);
		 
	     try {
			jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	    
	 }
}
