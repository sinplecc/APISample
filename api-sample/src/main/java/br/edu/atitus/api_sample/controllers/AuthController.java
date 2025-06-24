package br.edu.atitus.api_sample.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.api_sample.components.JwtUtil;
import br.edu.atitus.api_sample.dtos.SigninDTO;
import br.edu.atitus.api_sample.dtos.SignupDTO;
import br.edu.atitus.api_sample.entities.UserEntity;
import br.edu.atitus.api_sample.entities.UserType;
import br.edu.atitus.api_sample.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final UserService service;
	private final AuthenticationConfiguration authConfig;

	public AuthController(UserService service, AuthenticationConfiguration authConfig) {
		super();
		this.service = service;
		this.authConfig = authConfig;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<UserEntity> signup(
			@RequestBody SignupDTO dto) throws Exception {
		UserEntity newUser = new UserEntity();
		BeanUtils.copyProperties(dto, newUser);
		newUser.setType(UserType.Common);
		service.save(newUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<String> signin(@RequestBody SigninDTO dto) throws AuthenticationException, Exception {
		authConfig.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
		String jwt = JwtUtil.generateToken(dto.email());
		return ResponseEntity.ok(jwt);
	}
	
	
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<String> handlerException(Exception e) {
		String messageError = e.getMessage().replaceAll("\r\n", "");
		return ResponseEntity.badRequest().body(messageError);
	}

}
