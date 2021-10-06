package com.fastcode.example.application.core.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.fastcode.example.domain.core.writer.Writer;
import com.fastcode.example.application.core.post.dto.*;
import com.fastcode.example.domain.core.post.Post;
import java.time.*;

@Mapper(componentModel = "spring")
public interface IPostMapper {
   Post createPostInputToPost(CreatePostInput postDto);
   
   @Mappings({ 
   @Mapping(source = "entity.writer.id", target = "writerId"),                   
   @Mapping(source = "entity.writer.id", target = "writerDescriptiveField"),                    
   }) 
   CreatePostOutput postToCreatePostOutput(Post entity);
   
    Post updatePostInputToPost(UpdatePostInput postDto);
    
    @Mappings({ 
    @Mapping(source = "entity.writer.id", target = "writerId"),                   
    @Mapping(source = "entity.writer.id", target = "writerDescriptiveField"),                    
   	}) 
   	UpdatePostOutput postToUpdatePostOutput(Post entity);
   	@Mappings({ 
   	@Mapping(source = "entity.writer.id", target = "writerId"),                   
   	@Mapping(source = "entity.writer.id", target = "writerDescriptiveField"),                    
   	}) 
   	FindPostByIdOutput postToFindPostByIdOutput(Post entity);

   @Mappings({
   @Mapping(source = "writer.id", target = "id"),                  
   @Mapping(source = "foundPost.id", target = "postId"),
   })
   GetWriterOutput writerToGetWriterOutput(Writer writer, Post foundPost);
   
}

