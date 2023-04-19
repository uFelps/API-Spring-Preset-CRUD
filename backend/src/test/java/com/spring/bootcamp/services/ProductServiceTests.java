package com.spring.bootcamp.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.spring.bootcamp.dto.ProductDTO;
import com.spring.bootcamp.entities.Category;
import com.spring.bootcamp.entities.Product;
import com.spring.bootcamp.repositories.CategoryRepository;
import com.spring.bootcamp.repositories.ProductRepository;
import com.spring.bootcamp.services.exceptions.DatabaseException;
import com.spring.bootcamp.services.exceptions.ResourceNotFoundException;
import com.spring.bootcamp.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO dto;
	
	
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 500L;
		dependentId = 4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		dto = Factory.createProductDTO();
		
	
		//find all - paged
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		
		//findbyid
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		
		//save
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		
		//update
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		
		
		//delete
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	
	
	@Test
	public void findByIdShouldReturnProductDtoWhenIdExists() {
		
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}
	
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}
	
	

	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 12);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
		
	}
	
	
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExists() {
		
		ProductDTO result = service.update(existingId, dto);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);
		
	}
	
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			ProductDTO result = service.update(nonExistingId, dto);
		});
		
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(nonExistingId);
	}
	
	
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.delete(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
}



















