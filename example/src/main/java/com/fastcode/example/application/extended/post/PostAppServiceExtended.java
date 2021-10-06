package com.fastcode.example.application.extended.post;

import org.springframework.stereotype.Service;
import com.fastcode.example.application.core.post.PostAppService;

import com.fastcode.example.domain.extended.post.IPostRepositoryExtended;
import com.fastcode.example.domain.extended.writer.IWriterRepositoryExtended;
import com.fastcode.example.commons.logging.LoggingHelper;

@Service("postAppServiceExtended")
public class PostAppServiceExtended extends PostAppService implements IPostAppServiceExtended {

	public PostAppServiceExtended(IPostRepositoryExtended postRepositoryExtended,
				IWriterRepositoryExtended writerRepositoryExtended,IPostMapperExtended mapper,LoggingHelper logHelper) {

		super(postRepositoryExtended,
		writerRepositoryExtended,mapper,logHelper);

	}

 	//Add your custom code here
 
}

