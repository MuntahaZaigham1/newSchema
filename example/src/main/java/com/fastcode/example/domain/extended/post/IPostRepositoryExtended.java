package com.fastcode.example.domain.extended.post;

import org.springframework.stereotype.Repository;
import com.fastcode.example.domain.core.post.IPostRepository;
@Repository("postRepositoryExtended")
public interface IPostRepositoryExtended extends IPostRepository {

	//Add your custom code here
}

