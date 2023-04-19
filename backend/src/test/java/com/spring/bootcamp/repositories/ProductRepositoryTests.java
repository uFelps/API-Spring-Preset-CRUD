package com.spring.bootcamp.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.spring.bootcamp.entities.Product;
import com.spring.bootcamp.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long exintingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	
	@BeforeEach
	void setUp() throws Exception{
		exintingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		
	}

	
	@Test
	public void findByIdShouldReturnOptionalNotNullWhenIdExists() {
		
		Optional<Product> product = repository.findById(exintingId);
		
		Assertions.assertTrue(product.isPresent());
		
	}
	
	
	@Test
	public void findByIdShouldReturnOptionalNullWhenIdNotExists() {
		
		Optional<Product> product = repository.findById(nonExistingId);
		
		Assertions.assertFalse(product.isPresent());
	}
	
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}

	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		
		
		repository.deleteById(1L);
		
		Optional<Product> result = repository.findById(exintingId);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {		
			repository.deleteById(nonExistingId);
			
		});
		
	}

}
