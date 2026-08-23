package com.paraizo.mysocial.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paraizo.mysocial.model.PostComment;

@Repository
public interface PostCommentRepository extends CrudRepository<PostComment, Long>{
    
}
