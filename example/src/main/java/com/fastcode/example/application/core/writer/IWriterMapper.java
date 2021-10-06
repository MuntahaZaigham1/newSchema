package com.fastcode.example.application.core.writer;

import org.mapstruct.Mapper;
import com.fastcode.example.application.core.writer.dto.*;
import com.fastcode.example.domain.core.writer.Writer;
import java.time.*;

@Mapper(componentModel = "spring")
public interface IWriterMapper {
   Writer createWriterInputToWriter(CreateWriterInput writerDto);
   CreateWriterOutput writerToCreateWriterOutput(Writer entity);
   
    Writer updateWriterInputToWriter(UpdateWriterInput writerDto);
    
   	UpdateWriterOutput writerToUpdateWriterOutput(Writer entity);
   	FindWriterByIdOutput writerToFindWriterByIdOutput(Writer entity);

}

