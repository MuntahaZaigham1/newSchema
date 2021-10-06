package com.fastcode.example.restcontrollers.core;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;
import java.time.*;
import java.math.BigDecimal;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import org.springframework.core.env.Environment;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.application.core.post.PostAppService;
import com.fastcode.example.application.core.post.dto.*;
import com.fastcode.example.domain.core.post.IPostRepository;
import com.fastcode.example.domain.core.post.Post;

import com.fastcode.example.domain.core.writer.IWriterRepository;
import com.fastcode.example.domain.core.writer.Writer;
import com.fastcode.example.application.core.writer.WriterAppService;    
import com.fastcode.example.DatabaseContainerConfig;
import com.fastcode.example.domain.core.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class PostControllerTest extends DatabaseContainerConfig{
	
	@Autowired
	protected SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired
	@Qualifier("postRepository") 
	protected IPostRepository post_repository;
	
	@Autowired
	@Qualifier("writerRepository") 
	protected IWriterRepository writerRepository;
	

	@SpyBean
	@Qualifier("postAppService")
	protected PostAppService postAppService;
	
    @SpyBean
    @Qualifier("writerAppService")
	protected WriterAppService  writerAppService;
	
	@SpyBean
	protected LoggingHelper logHelper;

	@SpyBean
	protected Environment env;

	@Mock
	protected Logger loggerMock;

	protected Post post;

	protected MockMvc mvc;
	
	@Autowired
	EntityManagerFactory emf;
	
    static EntityManagerFactory emfs;
    
    static int relationCount = 10;
    static int yearCount = 1971;
    static int dayCount = 10;
	private BigDecimal bigdec = new BigDecimal(1.2);
    
	int countWriter = 10;
	
	@PostConstruct
	public void init() {
	emfs = emf;
	}

	@AfterClass
	public static void cleanup() {
		EntityManager em = emfs.createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("truncate table s3.post CASCADE").executeUpdate();
		em.createNativeQuery("truncate table s3.writer CASCADE").executeUpdate();
		em.getTransaction().commit();
	}
	
	public Writer createWriterEntity() {
	
		if(countWriter>60) {
			countWriter = 10;
		}

		if(dayCount>=31) {
			dayCount = 10;
			yearCount=yearCount++;
		}
		
		Writer writerEntity = new Writer();
		
  		writerEntity.setEmail(String.valueOf(relationCount));
  		writerEntity.setFirstName(String.valueOf(relationCount));
		writerEntity.setId(relationCount);
  		writerEntity.setLastName(String.valueOf(relationCount));
  		writerEntity.setUsername(String.valueOf(relationCount));
		writerEntity.setVersiono(0L);
		relationCount++;
		if(!writerRepository.findAll().contains(writerEntity))
		{
			 writerEntity = writerRepository.save(writerEntity);
		}
		countWriter++;
	    return writerEntity;
	}

	public Post createEntity() {
		Writer writer = createWriterEntity();
	
		Post postEntity = new Post();
		postEntity.setBody("1");
		postEntity.setId(1);
		postEntity.setTitle("1");
		postEntity.setVersiono(0L);
		postEntity.setWriter(writer);
		
		return postEntity;
	}
    public CreatePostInput createPostInput() {
	
	    CreatePostInput postInput = new CreatePostInput();
  		postInput.setBody("5");
  		postInput.setTitle("5");
		
		return postInput;
	}

	public Post createNewEntity() {
		Post post = new Post();
		post.setBody("3");
		post.setId(3);
		post.setTitle("3");
		
		return post;
	}
	
	public Post createUpdateEntity() {
		Post post = new Post();
		post.setBody("4");
		post.setId(4);
		post.setTitle("4");
		
		return post;
	}
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
    
		final PostController postController = new PostController(postAppService, writerAppService,
	logHelper,env);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(postController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		post= createEntity();
		List<Post> list= post_repository.findAll();
		if(!list.contains(post)) {
			post=post_repository.save(post);
		}

	}
	
	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/post/" + post.getId()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() {

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(get("/post/999")
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}
	@Test
	public void CreatePost_PostDoesNotExist_ReturnStatusOk() throws Exception {
		CreatePostInput postInput = createPostInput();	
		
	    
		Writer writer =  createWriterEntity();

		postInput.setWriterId(Integer.parseInt(writer.getId().toString()));
		
		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
	
		String json = ow.writeValueAsString(postInput);

		mvc.perform(post("/post").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());
	}     
	

	@Test
	public void DeletePost_IdIsNotValid_ThrowEntityNotFoundException() {

        doReturn(null).when(postAppService).findById(999);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/post/999")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a post with a id=999"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 	Post entity =  createNewEntity();
	 	entity.setVersiono(0L);
		Writer writer = createWriterEntity();
		entity.setWriter(writer);
		entity = post_repository.save(entity);
		

		FindPostByIdOutput output= new FindPostByIdOutput();
		output.setBody(entity.getBody());
		output.setId(entity.getId());
		output.setTitle(entity.getTitle());
		
         Mockito.doReturn(output).when(postAppService).findById(entity.getId());
       
    //    Mockito.when(postAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/post/" + entity.getId()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdatePost_PostDoesNotExist_ReturnStatusNotFound() throws Exception {
   
        doReturn(null).when(postAppService).findById(999);
        
        UpdatePostInput post = new UpdatePostInput();
  		post.setBody("999");
		post.setId(999);
  		post.setTitle("999");

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(post);

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(
		 	put("/post/999")
		 	.contentType(MediaType.APPLICATION_JSON)
		 	.content(json))
			.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. Post with id=999 not found."));
	}    

	@Test
	public void UpdatePost_PostExists_ReturnStatusOk() throws Exception {
		Post entity =  createUpdateEntity();
		entity.setVersiono(0L);
		
		Writer writer = createWriterEntity();
		entity.setWriter(writer);
		entity = post_repository.save(entity);
		FindPostByIdOutput output= new FindPostByIdOutput();
		output.setBody(entity.getBody());
		output.setId(entity.getId());
		output.setTitle(entity.getTitle());
		output.setVersiono(entity.getVersiono());
		
        Mockito.when(postAppService.findById(entity.getId())).thenReturn(output);
        
        
		
		UpdatePostInput postInput = new UpdatePostInput();
		postInput.setBody(entity.getBody());
		postInput.setId(entity.getId());
		postInput.setTitle(entity.getTitle());
		
		postInput.setWriterId(Integer.parseInt(writer.getId().toString()));

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(postInput);
	
		mvc.perform(put("/post/" + entity.getId()+"/").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		Post de = createUpdateEntity();
		de.setId(entity.getId());
		post_repository.delete(de);
		

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/post?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/post?search=postid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property postid not found!"));

	} 
		
	
	@Test
	public void GetWriter_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() {
  
	   org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/post/999/writer")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));
	
	}    
	
	@Test
	public void GetWriter_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/post/" + post.getId()+ "/writer")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
    
}

