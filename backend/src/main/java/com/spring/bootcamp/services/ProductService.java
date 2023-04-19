package com.spring.bootcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.bootcamp.dto.CategoryDTO;
import com.spring.bootcamp.dto.ProductDTO;
import com.spring.bootcamp.entities.Category;
import com.spring.bootcamp.entities.Product;
import com.spring.bootcamp.repositories.CategoryRepository;
import com.spring.bootcamp.repositories.ProductRepository;
import com.spring.bootcamp.services.exceptions.DatabaseException;
import com.spring.bootcamp.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		
		Page<Product> list = repository.findAll(pageable);

		return list.map(x -> new ProductDTO(x, x.getCategories()));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {

		Product entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {

		Product entity = new Product();
		copyDtoToEntity(dto, entity);

		entity = repository.save(entity);

		return new ProductDTO(entity);
	}

	

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {

		try {
			Product entity = repository.getReferenceById(id);
			
			copyDtoToEntity(dto, entity);

			entity = repository.save(entity);

			return new ProductDTO(entity);
		}

		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {

		try {
			repository.deleteById(id);
		}

		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation");
		}

	}
	
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setDate(dto.getDate());
		entity.setDate(dto.getDate());
		
		entity.getCategories().clear();
		
		for(CategoryDTO catDto: dto.getCategories()) {
			Category category = categoryRepository.getReferenceById(catDto.getId());
			
			entity.getCategories().add(category);
		}
		
	}

}
