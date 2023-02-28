package com.spring.bootcamp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.bootcamp.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
