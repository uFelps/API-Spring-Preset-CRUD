package com.spring.bootcamp.dto;

import java.io.Serializable;
import java.time.Instant;

import com.spring.bootcamp.entities.Category;

public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Instant createdAt;
	private Instant updatedAt;

	public CategoryDTO() {

	}

	
	public CategoryDTO(Long id, String name, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}


	public CategoryDTO(Category category) {
		id = category.getId();
		name = category.getName();
		createdAt = category.getCreatedAt();
		updatedAt = category.getUpdatedAt();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

}
