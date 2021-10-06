package com.fastcode.example.application.core.writer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.fastcode.example.application.core.writer.dto.*;
import com.fastcode.example.domain.core.writer.IWriterRepository;
import com.fastcode.example.domain.core.writer.QWriter;
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

@Service("writerAppService")
@RequiredArgsConstructor
public class WriterAppService implements IWriterAppService {
	@Qualifier("writerRepository")
	@NonNull protected final IWriterRepository _writerRepository;

	
	@Qualifier("IWriterMapperImpl")
	@NonNull protected final IWriterMapper mapper;

	@NonNull protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateWriterOutput create(CreateWriterInput input) {

		Writer writer = mapper.createWriterInputToWriter(input);

		Writer createdWriter = _writerRepository.save(writer);
		return mapper.writerToCreateWriterOutput(createdWriter);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateWriterOutput update(Integer writerId, UpdateWriterInput input) {

		Writer existing = _writerRepository.findById(writerId).get();

		Writer writer = mapper.updateWriterInputToWriter(input);
		writer.setPostsSet(existing.getPostsSet());
		
		Writer updatedWriter = _writerRepository.save(writer);
		return mapper.writerToUpdateWriterOutput(updatedWriter);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer writerId) {

		Writer existing = _writerRepository.findById(writerId).orElse(null); 
	 	
	 	_writerRepository.delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindWriterByIdOutput findById(Integer writerId) {

		Writer foundWriter = _writerRepository.findById(writerId).orElse(null);
		if (foundWriter == null)  
			return null; 
 	   
 	    return mapper.writerToFindWriterByIdOutput(foundWriter);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindWriterByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<Writer> foundWriter = _writerRepository.findAll(search(search), pageable);
		List<Writer> writerList = foundWriter.getContent();
		Iterator<Writer> writerIterator = writerList.iterator(); 
		List<FindWriterByIdOutput> output = new ArrayList<>();

		while (writerIterator.hasNext()) {
		Writer writer = writerIterator.next();
 	    output.add(mapper.writerToFindWriterByIdOutput(writer));
		}
		return output;
	}
	
	protected BooleanBuilder search(SearchCriteria search) throws Exception {

		QWriter writer= QWriter.writerEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(writer, map,search.getJoinColumns());
		}
		return null;
	}
	
	protected void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("email") ||
				list.get(i).replace("%20","").trim().equals("firstName") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("lastName") ||
				list.get(i).replace("%20","").trim().equals("username")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	protected BooleanBuilder searchKeyValuePair(QWriter writer, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("email")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(writer.email.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals")) {
					builder.and(writer.email.eq(details.getValue().getSearchValue()));
				} else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(writer.email.ne(details.getValue().getSearchValue()));
				}
			}
            if(details.getKey().replace("%20","").trim().equals("firstName")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(writer.firstName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals")) {
					builder.and(writer.firstName.eq(details.getValue().getSearchValue()));
				} else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(writer.firstName.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("id")) {
				if(details.getValue().getOperator().equals("contains") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(writer.id.like(details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(writer.id.eq(Integer.valueOf(details.getValue().getSearchValue())));
				} else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(writer.id.ne(Integer.valueOf(details.getValue().getSearchValue())));
				} else if(details.getValue().getOperator().equals("range")) {
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue())) {
                	   builder.and(writer.id.between(Integer.valueOf(details.getValue().getStartingValue()), Integer.valueOf(details.getValue().getEndingValue())));
                   } else if(StringUtils.isNumeric(details.getValue().getStartingValue())) {
                	   builder.and(writer.id.goe(Integer.valueOf(details.getValue().getStartingValue())));
                   } else if(StringUtils.isNumeric(details.getValue().getEndingValue())) {
                	   builder.and(writer.id.loe(Integer.valueOf(details.getValue().getEndingValue())));
				   }
				}
			}
            if(details.getKey().replace("%20","").trim().equals("lastName")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(writer.lastName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals")) {
					builder.and(writer.lastName.eq(details.getValue().getSearchValue()));
				} else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(writer.lastName.ne(details.getValue().getSearchValue()));
				}
			}
            if(details.getKey().replace("%20","").trim().equals("username")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(writer.username.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals")) {
					builder.and(writer.username.eq(details.getValue().getSearchValue()));
				} else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(writer.username.ne(details.getValue().getSearchValue()));
				}
			}
	    
		}
		
		return builder;
	}
	
	public Map<String,String> parsePostsJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("writerId", keysString);
		  
		return joinColumnMap;
	}
    
}



