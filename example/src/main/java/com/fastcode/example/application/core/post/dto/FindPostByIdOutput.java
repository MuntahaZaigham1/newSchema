package com.fastcode.example.application.core.post.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindPostByIdOutput {

  	private String body;
  	private Integer id;
  	private String title;
  	private Integer writerId;
  	private Integer writerDescriptiveField;
	private Long versiono;
 
}

