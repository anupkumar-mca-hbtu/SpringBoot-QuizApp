package com.example.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

	
	private String name;
    private String email;
    private LocalDate dob;
    private String mobile;
    private String password;
    private String state;
    private String city;
    private String captcha;
}
