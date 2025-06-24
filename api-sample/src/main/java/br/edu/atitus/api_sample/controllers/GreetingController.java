package br.edu.atitus.api_sample.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/greeting")
public class GreetingController {

	@GetMapping({"", "/", "/{namePath}"})
	public ResponseEntity<String> getGreeting(
			@RequestParam(required = false) String name,
			@PathVariable(required = false) String namePath) {
		if (name == null)
			name = namePath != null ? namePath : "World";
		
		String returnGreeting = String.format("%s %s!", "Hello", name);
		return ResponseEntity.ok(returnGreeting);
	}
	
	@PostMapping
	public ResponseEntity<String> postGreeting(
			@RequestBody String body) throws Exception {
		
		if (body.length() > 50)
			throw new Exception("Mensagem deve ter no máximo 50 caracteres");
		
		return ResponseEntity.ok(body);
	}
	
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<String> handlerException(Exception e) {
		String messageError = e.getMessage().replaceAll("\r\n", "");
		return ResponseEntity.badRequest().body(messageError);
	}
	
	
	
	
}
