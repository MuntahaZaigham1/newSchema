package com.fastcode.example.application.core.post;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fastcode.example.domain.core.post.*;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.application.core.post.dto.*;
import com.fastcode.example.domain.core.post.QPost;
import com.fastcode.example.domain.core.post.Post;

import com.fastcode.example.domain.core.writer.Writer;
import com.fastcode.example.domain.core.writer.IWriterRepository;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.time.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PostAppServiceTest {

	@InjectMocks
	@Spy
	protected PostAppService _appService;
	@Mock
	protected IPostRepository _postRepository;
	
	
    @Mock
	protected IWriterRepository _writerRepository;

	@Mock
	protected IPostMapper _mapper;

	@Mock
	protected Logger loggerMock;

	@Mock
	protected LoggingHelper logHelper;
	
    protected static Integer ID=15;
	 
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(_appService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}
	
	@Test
	public void findPostById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
		Optional<Post> nullOptional = Optional.ofNullable(null);
		Mockito.when(_postRepository.findById(any(Integer.class))).thenReturn(nullOptional);
		Assertions.assertThat(_appService.findById(ID )).isEqualTo(null);
	}
	
	@Test
	public void findPostById_IdIsNotNullAndIdExists_ReturnPost() {

		Post post = mock(Post.class);
		Optional<Post> postOptional = Optional.of((Post) post);
		Mockito.when(_postRepository.findById(any(Integer.class))).thenReturn(postOptional);
		
	    Assertions.assertThat(_appService.findById(ID )).isEqualTo(_mapper.postToFindPostByIdOutput(post));
	}
	
	
	@Test 
    public void createPost_PostIsNotNullAndPostDoesNotExist_StorePost() { 
 
        Post postEntity = mock(Post.class); 
    	CreatePostInput postInput = new CreatePostInput();
		
        Writer writer = mock(Writer.class);
		Optional<Writer> writerOptional = Optional.of((Writer) writer);
        postInput.setWriterId(15);
		
        Mockito.when(_writerRepository.findById(any(Integer.class))).thenReturn(writerOptional);
        
		
        Mockito.when(_mapper.createPostInputToPost(any(CreatePostInput.class))).thenReturn(postEntity); 
        Mockito.when(_postRepository.save(any(Post.class))).thenReturn(postEntity);

	   	Assertions.assertThat(_appService.create(postInput)).isEqualTo(_mapper.postToCreatePostOutput(postEntity));

    } 
    @Test
	public void createPost_PostIsNotNullAndPostDoesNotExistAndChildIsNullAndChildIsNotMandatory_StorePost() {

		Post post = mock(Post.class);
		CreatePostInput postInput = mock(CreatePostInput.class);
		
		
		Mockito.when(_mapper.createPostInputToPost(any(CreatePostInput.class))).thenReturn(post);
		Mockito.when(_postRepository.save(any(Post.class))).thenReturn(post);
	    Assertions.assertThat(_appService.create(postInput)).isEqualTo(_mapper.postToCreatePostOutput(post)); 
	}
	
    @Test
	public void updatePost_PostIsNotNullAndPostDoesNotExistAndChildIsNullAndChildIsNotMandatory_ReturnUpdatedPost() {

		Post post = mock(Post.class);
		UpdatePostInput postInput = mock(UpdatePostInput.class);
		
		Optional<Post> postOptional = Optional.of((Post) post);
		Mockito.when(_postRepository.findById(any(Integer.class))).thenReturn(postOptional);
		
		Mockito.when(_mapper.updatePostInputToPost(any(UpdatePostInput.class))).thenReturn(post);
		Mockito.when(_postRepository.save(any(Post.class))).thenReturn(post);
		Assertions.assertThat(_appService.update(ID,postInput)).isEqualTo(_mapper.postToUpdatePostOutput(post));
	}
	
		
	@Test
	public void updatePost_PostIdIsNotNullAndIdExists_ReturnUpdatedPost() {

		Post postEntity = mock(Post.class);
		UpdatePostInput post= mock(UpdatePostInput.class);
		
		Optional<Post> postOptional = Optional.of((Post) postEntity);
		Mockito.when(_postRepository.findById(any(Integer.class))).thenReturn(postOptional);
	 		
		Mockito.when(_mapper.updatePostInputToPost(any(UpdatePostInput.class))).thenReturn(postEntity);
		Mockito.when(_postRepository.save(any(Post.class))).thenReturn(postEntity);
		Assertions.assertThat(_appService.update(ID,post)).isEqualTo(_mapper.postToUpdatePostOutput(postEntity));
	}
    
	@Test
	public void deletePost_PostIsNotNullAndPostExists_PostRemoved() {

		Post post = mock(Post.class);
		Optional<Post> postOptional = Optional.of((Post) post);
		Mockito.when(_postRepository.findById(any(Integer.class))).thenReturn(postOptional);
 		
		_appService.delete(ID); 
		verify(_postRepository).delete(post);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<Post> list = new ArrayList<>();
		Page<Post> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindPostByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_postRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<Post> list = new ArrayList<>();
		Post post = mock(Post.class);
		list.add(post);
    	Page<Post> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindPostByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();

		output.add(_mapper.postToFindPostByIdOutput(post));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_postRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QPost post = QPost.postEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("body",searchFields);
		Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.body.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(post,map,searchMap)).isEqualTo(builder);
	}
	
	@Test (expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("xyz");
		_appService.checkProperties(list);
	}
	
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception {
		List<String> list = new ArrayList<>();
        list.add("body");
        list.add("title");
		_appService.checkProperties(list);
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QPost post = QPost.postEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("body");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(post.body.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QPost.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
   
    //Writer
	@Test
	public void GetWriter_IfPostIdAndWriterIdIsNotNullAndPostExists_ReturnWriter() {
		Post post = mock(Post.class);
		Optional<Post> postOptional = Optional.of((Post) post);
		Writer writerEntity = mock(Writer.class);

		Mockito.when(_postRepository.findById(any(Integer.class))).thenReturn(postOptional);

		Mockito.when(post.getWriter()).thenReturn(writerEntity);
		Assertions.assertThat(_appService.getWriter(ID)).isEqualTo(_mapper.writerToGetWriterOutput(writerEntity, post));
	}

	@Test 
	public void GetWriter_IfPostIdAndWriterIdIsNotNullAndPostDoesNotExist_ReturnNull() {
		Optional<Post> nullOptional = Optional.ofNullable(null);;
		Mockito.when(_postRepository.findById(any(Integer.class))).thenReturn(nullOptional);
		Assertions.assertThat(_appService.getWriter(ID)).isEqualTo(null);
	}

}
