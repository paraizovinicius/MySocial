package com.paraizo.mysocial.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paraizo.mysocial.model.PostLike;

@Repository
public interface PostLikeRepository extends CrudRepository<PostLike, Long>{
    
}
