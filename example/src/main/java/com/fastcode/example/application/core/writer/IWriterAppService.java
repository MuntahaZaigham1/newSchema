package com.fastcode.example.application.core.writer;

import org.springframework.data.domain.Pageable;
import com.fastcode.example.application.core.writer.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;

import java.util.*;

public interface IWriterAppService {
	
	//CRUD Operations
	CreateWriterOutput create(CreateWriterInput writer);

    void delete(Integer id);

    UpdateWriterOutput update(Integer id, UpdateWriterInput input);

    FindWriterByIdOutput findById(Integer id);
    List<FindWriterByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    
    //Join Column Parsers

	Map<String,String> parsePostsJoinColumn(String keysString);
}

