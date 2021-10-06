package com.fastcode.example.restcontrollers.extended;

import org.springframework.web.bind.annotation.*;
import com.fastcode.example.restcontrollers.core.WriterController;
import com.fastcode.example.application.extended.writer.IWriterAppServiceExtended;
import com.fastcode.example.application.extended.post.IPostAppServiceExtended;
import org.springframework.core.env.Environment;
import com.fastcode.example.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/writer/extended")
public class WriterControllerExtended extends WriterController {

		public WriterControllerExtended(IWriterAppServiceExtended writerAppServiceExtended, IPostAppServiceExtended postAppServiceExtended,
	     LoggingHelper helper, Environment env) {
		super(
		writerAppServiceExtended,
    	postAppServiceExtended,
		helper, env);
	}

	//Add your custom code here

}

