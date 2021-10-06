package com.fastcode.example.application.core.post;

import org.springframework.data.domain.Pageable;
import com.fastcode.example.application.core.post.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;

import java.util.*;

public interface IPostAppService {
	
	//CRUD Operations
	CreatePostOutput create(CreatePostInput post);

    void delete(Integer id);

    UpdatePostOutput update(Integer id, UpdatePostInput input);

    FindPostByIdOutput findById(Integer id);
    List<FindPostByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
	//Relationship Operations
    
    GetWriterOutput getWriter(Integer postid);
    
    //Join Column Parsers
}

