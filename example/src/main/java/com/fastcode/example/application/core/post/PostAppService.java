package com.fastcode.example.application.core.post;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.fastcode.example.application.core.post.dto.*;
import com.fastcode.example.domain.core.post.IPostRepository;
import com.fastcode.example.domain.core.post.QPost;
import com.fastcode.example.domain.core.post.Post;
import com.fastcode.example.domain.core.writer.IWriterRepository;
import com.fastcode.example.domain.core.writer.Writer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import java.time.*;
import java.util.*;

@Service("postAppService")
@RequiredArgsConstructor
public class PostAppService implements IPostAppService {
	@Qualifier("postRepository")
	@NonNull protected final IPostRepository _postRepository;

	
    @Qualifier("writerRepository")
	@NonNull protected final IWriterRepository _writerRepository;

	@Qualifier("IPostMapperImpl")
	@NonNull protected final IPostMapper mapper;

	@NonNull protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreatePostOutput create(CreatePostInput input) {

		Post post = mapper.createPostInputToPost(input);
		Writer foundWriter = null;
	  	if(input.getWriterId()!=null) {
			foundWriter = _writerRepository.findById(input.getWriterId()).orElse(null);
			
			if(foundWriter!=null) {
				foundWriter.addPosts(post);
				//post.setWriter(foundWriter);
			}
		}

		Post createdPost = _postRepository.save(post);
		return mapper.postToCreatePostOutput(createdPost);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdatePostOutput update(Integer postId, UpdatePostInput input) {

		Post existing = _postRepository.findById(postId).get();

		Post post = mapper.updatePostInputToPost(input);
		Writer foundWriter = null;
        
	  	if(input.getWriterId()!=null) { 
			foundWriter = _writerRepository.findById(input.getWriterId()).orElse(null);
		
			if(foundWriter!=null) {
				foundWriter.addPosts(post);
			//	post.setWriter(foundWriter);
			}
		}
		
		Post updatedPost = _postRepository.save(post);
		return mapper.postToUpdatePostOutput(updatedPost);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer postId) {

		Post existing = _postRepository.findById(postId).orElse(null); 
	 	
        if(existing.getWriter() !=null)
        {
        existing.getWriter().removePosts(existing);
        }
	 	_postRepository.delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindPostByIdOutput findById(Integer postId) {

		Post foundPost = _postRepository.findById(postId).orElse(null);
		if (foundPost == null)  
			return null; 
 	   
 	    return mapper.postToFindPostByIdOutput(foundPost);
	}
	
    //Writer
	// ReST API Call - GET /post/1/writer
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public GetWriterOutput getWriter(Integer postId) {

		Post foundPost = _postRepository.findById(postId).orElse(null);
		if (foundPost == null) {
			logHelper.getLogger().error("There does not exist a post wth a id=%s", postId);
			return null;
		}
		Writer re = foundPost.getWriter();
		return mapper.writerToGetWriterOutput(re, foundPost);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindPostByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<Post> foundPost = _postRepository.findAll(search(search), pageable);
		List<Post> postList = foundPost.getContent();
		Iterator<Post> postIterator = postList.iterator(); 
		List<FindPostByIdOutput> output = new ArrayList<>();

		while (postIterator.hasNext()) {
		Post post = postIterator.next();
 	    output.add(mapper.postToFindPostByIdOutput(post));
		}
		return output;
	}
	
	protected BooleanBuilder search(SearchCriteria search) throws Exception {

		QPost post= QPost.postEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(post, map,search.getJoinColumns());
		}
		return null;
	}
	
	protected void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
		        list.get(i).replace("%20","").trim().equals("writer") ||
				list.get(i).replace("%20","").trim().equals("writerId") ||
				list.get(i).replace("%20","").trim().equals("body") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("title")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	protected BooleanBuilder searchKeyValuePair(QPost post, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("body")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(post.body.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals")) {
					builder.and(post.body.eq(details.getValue().getSearchValue()));
				} else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(post.body.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("id")) {
				if(details.getValue().getOperator().equals("contains") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(post.id.like(details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(post.id.eq(Integer.valueOf(details.getValue().getSearchValue())));
				} else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(post.id.ne(Integer.valueOf(details.getValue().getSearchValue())));
				} else if(details.getValue().getOperator().equals("range")) {
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue())) {
                	   builder.and(post.id.between(Integer.valueOf(details.getValue().getStartingValue()), Integer.valueOf(details.getValue().getEndingValue())));
                   } else if(StringUtils.isNumeric(details.getValue().getStartingValue())) {
                	   builder.and(post.id.goe(Integer.valueOf(details.getValue().getStartingValue())));
                   } else if(StringUtils.isNumeric(details.getValue().getEndingValue())) {
                	   builder.and(post.id.loe(Integer.valueOf(details.getValue().getEndingValue())));
				   }
				}
			}
            if(details.getKey().replace("%20","").trim().equals("title")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(post.title.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals")) {
					builder.and(post.title.eq(details.getValue().getSearchValue()));
				} else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(post.title.ne(details.getValue().getSearchValue()));
				}
			}
	    
		     if(details.getKey().replace("%20","").trim().equals("writer")) {
				if(details.getValue().getOperator().equals("contains") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(post.writer.id.like(details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(post.writer.id.eq(Integer.valueOf(details.getValue().getSearchValue())));
				} else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(post.writer.id.ne(Integer.valueOf(details.getValue().getSearchValue())));
				} else if(details.getValue().getOperator().equals("range")) {
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue())) {
                	   builder.and(post.writer.id.between(Integer.valueOf(details.getValue().getStartingValue()), Integer.valueOf(details.getValue().getEndingValue())));
                   } else if(StringUtils.isNumeric(details.getValue().getStartingValue())) {
                	   builder.and(post.writer.id.goe(Integer.valueOf(details.getValue().getStartingValue())));
                   } else if(StringUtils.isNumeric(details.getValue().getEndingValue())) {
                	   builder.and(post.writer.id.loe(Integer.valueOf(details.getValue().getEndingValue())));
				   }
				}
			}
		}
		
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
		if(joinCol != null && joinCol.getKey().equals("writerId")) {
		    builder.and(post.writer.id.eq(Integer.parseInt(joinCol.getValue())));
		}
        
		if(joinCol != null && joinCol.getKey().equals("writer")) {
		    builder.and(post.writer.id.eq(Integer.parseInt(joinCol.getValue())));
        }
        }
		return builder;
	}
	
    
}



