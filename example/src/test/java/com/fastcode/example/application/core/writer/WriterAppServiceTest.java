package com.fastcode.example.application.core.writer;

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

import com.fastcode.example.domain.core.writer.*;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.application.core.writer.dto.*;
import com.fastcode.example.domain.core.writer.QWriter;
import com.fastcode.example.domain.core.writer.Writer;

import com.fastcode.example.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.time.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class WriterAppServiceTest {

	@InjectMocks
	@Spy
	protected WriterAppService _appService;
	@Mock
	protected IWriterRepository _writerRepository;
	
	
	@Mock
	protected IWriterMapper _mapper;

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
	public void findWriterById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
		Optional<Writer> nullOptional = Optional.ofNullable(null);
		Mockito.when(_writerRepository.findById(any(Integer.class))).thenReturn(nullOptional);
		Assertions.assertThat(_appService.findById(ID )).isEqualTo(null);
	}
	
	@Test
	public void findWriterById_IdIsNotNullAndIdExists_ReturnWriter() {

		Writer writer = mock(Writer.class);
		Optional<Writer> writerOptional = Optional.of((Writer) writer);
		Mockito.when(_writerRepository.findById(any(Integer.class))).thenReturn(writerOptional);
		
	    Assertions.assertThat(_appService.findById(ID )).isEqualTo(_mapper.writerToFindWriterByIdOutput(writer));
	}
	
	
	@Test 
    public void createWriter_WriterIsNotNullAndWriterDoesNotExist_StoreWriter() { 
 
        Writer writerEntity = mock(Writer.class); 
    	CreateWriterInput writerInput = new CreateWriterInput();
		
        Mockito.when(_mapper.createWriterInputToWriter(any(CreateWriterInput.class))).thenReturn(writerEntity); 
        Mockito.when(_writerRepository.save(any(Writer.class))).thenReturn(writerEntity);

	   	Assertions.assertThat(_appService.create(writerInput)).isEqualTo(_mapper.writerToCreateWriterOutput(writerEntity));

    } 
	@Test
	public void updateWriter_WriterIdIsNotNullAndIdExists_ReturnUpdatedWriter() {

		Writer writerEntity = mock(Writer.class);
		UpdateWriterInput writer= mock(UpdateWriterInput.class);
		
		Optional<Writer> writerOptional = Optional.of((Writer) writerEntity);
		Mockito.when(_writerRepository.findById(any(Integer.class))).thenReturn(writerOptional);
	 		
		Mockito.when(_mapper.updateWriterInputToWriter(any(UpdateWriterInput.class))).thenReturn(writerEntity);
		Mockito.when(_writerRepository.save(any(Writer.class))).thenReturn(writerEntity);
		Assertions.assertThat(_appService.update(ID,writer)).isEqualTo(_mapper.writerToUpdateWriterOutput(writerEntity));
	}
    
	@Test
	public void deleteWriter_WriterIsNotNullAndWriterExists_WriterRemoved() {

		Writer writer = mock(Writer.class);
		Optional<Writer> writerOptional = Optional.of((Writer) writer);
		Mockito.when(_writerRepository.findById(any(Integer.class))).thenReturn(writerOptional);
 		
		_appService.delete(ID); 
		verify(_writerRepository).delete(writer);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<Writer> list = new ArrayList<>();
		Page<Writer> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindWriterByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_writerRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<Writer> list = new ArrayList<>();
		Writer writer = mock(Writer.class);
		list.add(writer);
    	Page<Writer> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindWriterByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();

		output.add(_mapper.writerToFindWriterByIdOutput(writer));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_writerRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QWriter writer = QWriter.writerEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("email",searchFields);
		Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
        builder.and(writer.email.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(writer,map,searchMap)).isEqualTo(builder);
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
        list.add("email");
        list.add("firstName");
        list.add("lastName");
        list.add("username");
		_appService.checkProperties(list);
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QWriter writer = QWriter.writerEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("email");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(writer.email.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QWriter.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}

	@Test
	public void ParsepostsJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
	    Map<String,String> joinColumnMap = new HashMap<String,String>();
		String keyString= "15";
		joinColumnMap.put("writerId", keyString);
		Assertions.assertThat(_appService.parsePostsJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}
