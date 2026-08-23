package com.paraizo.mysocial.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paraizo.mysocial.model.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long>{
    
}
