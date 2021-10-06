package com.fastcode.example.application.core.writer.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindWriterByIdOutput {

  	private String email;
  	private String firstName;
  	private Integer id;
  	private String lastName;
  	private String username;
	private Long versiono;
 
}

