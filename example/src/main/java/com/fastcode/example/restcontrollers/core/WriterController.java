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
import com.fastcode.example.application.core.writer.IWriterAppService;
import com.fastcode.example.application.core.writer.dto.*;
import com.fastcode.example.application.core.post.IPostAppService;
import com.fastcode.example.application.core.post.dto.FindPostByIdOutput;
import java.util.*;
import java.time.*;
import com.fastcode.example.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/writer")
@RequiredArgsConstructor
public class WriterController {

	@Qualifier("writerAppService")
	@NonNull protected final IWriterAppService _writerAppService;
    @Qualifier("postAppService")
	@NonNull  protected final IPostAppService  _postAppService;

	@NonNull protected final LoggingHelper logHelper;

	@NonNull protected final Environment env;

	@RequestMapping(method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<CreateWriterOutput> create( @RequestBody @Valid CreateWriterInput writer) {
		CreateWriterOutput output=_writerAppService.create(writer);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete writer ------------
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = {"application/json"})
	public void delete(@PathVariable String id) {

    	FindWriterByIdOutput output = _writerAppService.findById(Integer.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a writer with a id=%s", id)));

    	_writerAppService.delete(Integer.valueOf(id));
    }


	// ------------ Update writer ------------
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<UpdateWriterOutput> update(@PathVariable String id,  @RequestBody @Valid UpdateWriterInput writer) {

	    FindWriterByIdOutput currentWriter = _writerAppService.findById(Integer.valueOf(id));
		Optional.ofNullable(currentWriter).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Writer with id=%s not found.", id)));


		writer.setVersiono(currentWriter.getVersiono());
	    UpdateWriterOutput output = _writerAppService.update(Integer.valueOf(id),writer);
		return new ResponseEntity(output, HttpStatus.OK);
	}
    

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindWriterByIdOutput> findById(@PathVariable String id) {

    	FindWriterByIdOutput output = _writerAppService.findById(Integer.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}
	@RequestMapping(method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {

		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);

		return ResponseEntity.ok(_writerAppService.find(searchCriteria,Pageable));
	}
	
	@RequestMapping(value = "/{id}/posts", method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity getPosts(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);

		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_writerAppService.parsePostsJoinColumn(id);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));

		searchCriteria.setJoinColumns(joinColDetails);

    	List<FindPostByIdOutput> output = _postAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}

}


