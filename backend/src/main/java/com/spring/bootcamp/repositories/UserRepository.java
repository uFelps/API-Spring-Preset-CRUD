package com.spring.bootcamp.repositories;

import com.spring.bootcamp.entities.Category;
import com.spring.bootcamp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);

}
