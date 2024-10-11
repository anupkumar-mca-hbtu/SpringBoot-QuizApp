package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.LoginRequestDTO;
import com.example.dto.LoginResponseDTO;
import com.example.dto.RegisterRequestDTO;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.JwtUtil;
import com.example.service.MyUserDetailsService;

@RestController
public class AuthController {

	
	@Autowired
    private UserRepository userRepository;

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setDob(registerRequest.getDob());
        user.setMobile(registerRequest.getMobile());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setState(registerRequest.getState());
        user.setCity(registerRequest.getCity());
        user.setCaptcha(registerRequest.getCaptcha());

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("Invalid credentials");
        }

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new LoginResponseDTO(jwt));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Handle logout logic (invalidate session, etc.)
        return ResponseEntity.ok("Logged out successfully");
    }
}
