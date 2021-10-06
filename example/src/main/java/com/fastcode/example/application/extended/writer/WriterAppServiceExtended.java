package com.fastcode.example.application.extended.writer;

import org.springframework.stereotype.Service;
import com.fastcode.example.application.core.writer.WriterAppService;

import com.fastcode.example.domain.extended.writer.IWriterRepositoryExtended;
import com.fastcode.example.commons.logging.LoggingHelper;

@Service("writerAppServiceExtended")
public class WriterAppServiceExtended extends WriterAppService implements IWriterAppServiceExtended {

	public WriterAppServiceExtended(IWriterRepositoryExtended writerRepositoryExtended,
				IWriterMapperExtended mapper,LoggingHelper logHelper) {

		super(writerRepositoryExtended,
		mapper,logHelper);

	}

 	//Add your custom code here
 
}

