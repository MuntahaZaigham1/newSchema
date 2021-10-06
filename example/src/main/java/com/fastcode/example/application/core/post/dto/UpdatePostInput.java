package com.fastcode.example.application.core.post.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdatePostInput {

  	@NotNull(message = "body Should not be null")
  	private String body;
  	
  	@NotNull(message = "id Should not be null")
  	private Integer id;
  	
  	@NotNull(message = "title Should not be null")
  	private String title;
  	
  	private Integer writerId;
  	private Long versiono;
  
}

