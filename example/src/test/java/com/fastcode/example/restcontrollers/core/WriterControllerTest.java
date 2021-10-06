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
import com.fastcode.example.application.core.writer.WriterAppService;
import com.fastcode.example.application.core.writer.dto.*;
import com.fastcode.example.domain.core.writer.IWriterRepository;
import com.fastcode.example.domain.core.writer.Writer;

import com.fastcode.example.application.core.post.PostAppService;    
import com.fastcode.example.DatabaseContainerConfig;
import com.fastcode.example.domain.core.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class WriterControllerTest extends DatabaseContainerConfig{
	
	@Autowired
	protected SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired
	@Qualifier("writerRepository") 
	protected IWriterRepository writer_repository;
	

	@SpyBean
	@Qualifier("writerAppService")
	protected WriterAppService writerAppService;
	
    @SpyBean
    @Qualifier("postAppService")
	protected PostAppService  postAppService;
	
	@SpyBean
	protected LoggingHelper logHelper;

	@SpyBean
	protected Environment env;

	@Mock
	protected Logger loggerMock;

	protected Writer writer;

	protected MockMvc mvc;
	
	@Autowired
	EntityManagerFactory emf;
	
    static EntityManagerFactory emfs;
    
    static int relationCount = 10;
    static int yearCount = 1971;
    static int dayCount = 10;
	private BigDecimal bigdec = new BigDecimal(1.2);
    
	@PostConstruct
	public void init() {
	emfs = emf;
	}

	@AfterClass
	public static void cleanup() {
		EntityManager em = emfs.createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("truncate table s3.writer CASCADE").executeUpdate();
		em.getTransaction().commit();
	}
	

	public Writer createEntity() {
	
		Writer writerEntity = new Writer();
		writerEntity.setEmail("1");
		writerEntity.setFirstName("1");
		writerEntity.setId(1);
		writerEntity.setLastName("1");
		writerEntity.setUsername("1");
		writerEntity.setVersiono(0L);
		
		return writerEntity;
	}
    public CreateWriterInput createWriterInput() {
	
	    CreateWriterInput writerInput = new CreateWriterInput();
  		writerInput.setEmail("5");
  		writerInput.setFirstName("5");
  		writerInput.setLastName("5");
  		writerInput.setUsername("5");
		
		return writerInput;
	}

	public Writer createNewEntity() {
		Writer writer = new Writer();
		writer.setEmail("3");
		writer.setFirstName("3");
		writer.setId(3);
		writer.setLastName("3");
		writer.setUsername("3");
		
		return writer;
	}
	
	public Writer createUpdateEntity() {
		Writer writer = new Writer();
		writer.setEmail("4");
		writer.setFirstName("4");
		writer.setId(4);
		writer.setLastName("4");
		writer.setUsername("4");
		
		return writer;
	}
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
    
		final WriterController writerController = new WriterController(writerAppService, postAppService,
	logHelper,env);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(writerController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		writer= createEntity();
		List<Writer> list= writer_repository.findAll();
		if(!list.contains(writer)) {
			writer=writer_repository.save(writer);
		}

	}
	
	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/writer/" + writer.getId()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() {

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(get("/writer/999")
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}
	@Test
	public void CreateWriter_WriterDoesNotExist_ReturnStatusOk() throws Exception {
		CreateWriterInput writerInput = createWriterInput();	
		
		
		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
	
		String json = ow.writeValueAsString(writerInput);

		mvc.perform(post("/writer").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());
	}     
	

	@Test
	public void DeleteWriter_IdIsNotValid_ThrowEntityNotFoundException() {

        doReturn(null).when(writerAppService).findById(999);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/writer/999")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a writer with a id=999"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 	Writer entity =  createNewEntity();
	 	entity.setVersiono(0L);
		entity = writer_repository.save(entity);
		

		FindWriterByIdOutput output= new FindWriterByIdOutput();
		output.setEmail(entity.getEmail());
		output.setFirstName(entity.getFirstName());
		output.setId(entity.getId());
		output.setLastName(entity.getLastName());
		
         Mockito.doReturn(output).when(writerAppService).findById(entity.getId());
       
    //    Mockito.when(writerAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/writer/" + entity.getId()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateWriter_WriterDoesNotExist_ReturnStatusNotFound() throws Exception {
   
        doReturn(null).when(writerAppService).findById(999);
        
        UpdateWriterInput writer = new UpdateWriterInput();
  		writer.setEmail("999");
  		writer.setFirstName("999");
		writer.setId(999);
  		writer.setLastName("999");
  		writer.setUsername("999");

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(writer);

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(
		 	put("/writer/999")
		 	.contentType(MediaType.APPLICATION_JSON)
		 	.content(json))
			.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. Writer with id=999 not found."));
	}    

	@Test
	public void UpdateWriter_WriterExists_ReturnStatusOk() throws Exception {
		Writer entity =  createUpdateEntity();
		entity.setVersiono(0L);
		
		entity = writer_repository.save(entity);
		FindWriterByIdOutput output= new FindWriterByIdOutput();
		output.setEmail(entity.getEmail());
		output.setFirstName(entity.getFirstName());
		output.setId(entity.getId());
		output.setLastName(entity.getLastName());
		output.setUsername(entity.getUsername());
		output.setVersiono(entity.getVersiono());
		
        Mockito.when(writerAppService.findById(entity.getId())).thenReturn(output);
        
        
		
		UpdateWriterInput writerInput = new UpdateWriterInput();
		writerInput.setEmail(entity.getEmail());
		writerInput.setFirstName(entity.getFirstName());
		writerInput.setId(entity.getId());
		writerInput.setLastName(entity.getLastName());
		

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(writerInput);
	
		mvc.perform(put("/writer/" + entity.getId()+"/").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		Writer de = createUpdateEntity();
		de.setId(entity.getId());
		writer_repository.delete(de);
		

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/writer?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/writer?search=writerid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property writerid not found!"));

	} 
		
	
    @Test
	public void GetPosts_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("id", "1");

		Mockito.when(writerAppService.parsePostsJoinColumn("writerid")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/writer/1/posts?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetPosts_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {

		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("id", "1");
		
        Mockito.when(writerAppService.parsePostsJoinColumn("writerId")).thenReturn(joinCol);
		mvc.perform(get("/writer/1/posts?search=writerId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetPosts_searchIsNotEmpty() {
	
		Mockito.when(writerAppService.parsePostsJoinColumn(anyString())).thenReturn(null);
	 		  		    		  
	    org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(get("/writer/1/posts?search=writerId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid join column"));
	}    
    
}

