package com.spring.bootcamp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.bootcamp.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

}
