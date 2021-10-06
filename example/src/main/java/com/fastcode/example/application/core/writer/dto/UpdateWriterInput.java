package com.fastcode.example.application.core.writer.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateWriterInput {

  	@NotNull(message = "email Should not be null")
 	@Length(max = 45, message = "email must be less than 45 characters")
  	private String email;
  	
  	@NotNull(message = "firstName Should not be null")
 	@Length(max = 45, message = "firstName must be less than 45 characters")
  	private String firstName;
  	
  	@NotNull(message = "id Should not be null")
  	private Integer id;
  	
  	@NotNull(message = "lastName Should not be null")
 	@Length(max = 45, message = "lastName must be less than 45 characters")
  	private String lastName;
  	
 	@Length(max = 50, message = "username must be less than 50 characters")
  	private String username;
  	
  	private Long versiono;
  
}

