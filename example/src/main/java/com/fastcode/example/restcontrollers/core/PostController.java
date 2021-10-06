package com.fastcode.example.restcontrollers.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fastcode.example.commons.search.SearchCriteria;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.commons.search.OffsetBasedPageRequest;
import com.fastcode.example.application.core.post.IPostAppService;
import com.fastcode.example.application.core.post.dto.*;
import com.fastcode.example.application.core.writer.IWriterAppService;
import java.util.*;
import java.time.*;
import com.fastcode.example.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

	@Qualifier("postAppService")
	@NonNull protected final IPostAppService _postAppService;
    @Qualifier("writerAppService")
	@NonNull  protected final IWriterAppService  _writerAppService;

	@NonNull protected final LoggingHelper logHelper;

	@NonNull protected final Environment env;

	@RequestMapping(method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<CreatePostOutput> create( @RequestBody @Valid CreatePostInput post) {
		CreatePostOutput output=_postAppService.create(post);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete post ------------
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = {"application/json"})
	public void delete(@PathVariable String id) {

    	FindPostByIdOutput output = _postAppService.findById(Integer.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a post with a id=%s", id)));

    	_postAppService.delete(Integer.valueOf(id));
    }


	// ------------ Update post ------------
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<UpdatePostOutput> update(@PathVariable String id,  @RequestBody @Valid UpdatePostInput post) {

	    FindPostByIdOutput currentPost = _postAppService.findById(Integer.valueOf(id));
		Optional.ofNullable(currentPost).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Post with id=%s not found.", id)));


		post.setVersiono(currentPost.getVersiono());
	    UpdatePostOutput output = _postAppService.update(Integer.valueOf(id),post);
		return new ResponseEntity(output, HttpStatus.OK);
	}
    

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindPostByIdOutput> findById(@PathVariable String id) {

    	FindPostByIdOutput output = _postAppService.findById(Integer.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}
	@RequestMapping(method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {

		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);

		return ResponseEntity.ok(_postAppService.find(searchCriteria,Pageable));
	}
	
	@RequestMapping(value = "/{id}/writer", method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<GetWriterOutput> getWriter(@PathVariable String id) {
    	GetWriterOutput output= _postAppService.getWriter(Integer.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}

}


