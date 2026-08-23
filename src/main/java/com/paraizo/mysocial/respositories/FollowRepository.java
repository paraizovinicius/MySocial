package com.paraizo.mysocial.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paraizo.mysocial.model.Follow;

@Repository
public interface FollowRepository extends CrudRepository<Follow, Long> {
    
}
