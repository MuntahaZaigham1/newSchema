package com.fastcode.example.domain.core.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.*;
@Repository("postRepository")
public interface IPostRepository extends JpaRepository<Post, Integer>,QuerydslPredicateExecutor<Post> {

    
	
}

