package com.fastcode.example.application.extended.post;

import org.mapstruct.Mapper;
import com.fastcode.example.application.core.post.IPostMapper;

@Mapper(componentModel = "spring")
public interface IPostMapperExtended extends IPostMapper {

}

