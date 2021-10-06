package com.fastcode.example.restcontrollers.extended;

import org.springframework.web.bind.annotation.*;
import com.fastcode.example.restcontrollers.core.PostController;
import com.fastcode.example.application.extended.post.IPostAppServiceExtended;
import com.fastcode.example.application.extended.writer.IWriterAppServiceExtended;
import org.springframework.core.env.Environment;
import com.fastcode.example.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/post/extended")
public class PostControllerExtended extends PostController {

		public PostControllerExtended(IPostAppServiceExtended postAppServiceExtended, IWriterAppServiceExtended writerAppServiceExtended,
	     LoggingHelper helper, Environment env) {
		super(
		postAppServiceExtended,
    	writerAppServiceExtended,
		helper, env);
	}

	//Add your custom code here

}

