package com.paraizo.mysocial.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paraizo.mysocial.model.PostImage;

@Repository
public interface PostImageRepository extends CrudRepository<PostImage, Long> {
    
}
