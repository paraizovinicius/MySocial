package com.paraizo.mysocial.respositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.paraizo.mysocial.model.User;;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    
}
