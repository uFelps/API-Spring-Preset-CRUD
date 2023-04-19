package com.spring.bootcamp.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.bootcamp.dto.ProductDTO;
import com.spring.bootcamp.services.ProductService;
import com.spring.bootcamp.services.exceptions.ResourceNotFoundException;
import com.spring.bootcamp.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourcerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ProductDTO dto;
	private PageImpl<ProductDTO> page;
	private long existingId;
	private long nonExistingId;
	
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 500L;
		
		dto = Factory.createProductDTO();
		page = new PageImpl<>(List.of(dto));
		
		
		//find all paged
		Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
		
		//findbyid
		Mockito.when(service.findById(existingId)).thenReturn(dto);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		//update	//eq - argumento especifico	//any - argumento qualquer
		when(service.update(eq(existingId), any())).thenReturn(dto);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		//delete
		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		
		//create
		Mockito.when(service.insert(any())).thenReturn(dto);
		
	}
	
	
	@Test
	public void insertShouldReturnCreated() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/products")
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	
	@Test
	public void findAllPagedShouldReturnPage() throws Exception {
		
		ResultActions result =  mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON)); //accept - vai aceitar como resposta - json
		
		result.andExpect(status().isOk());
	}
	
	
	@Test
	public void findByIdShouldReturnDTOWhenIdExists() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(put("/products{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpectAll(status().isNoContent());
	}

	
	@Test
	public void deleteShouldReturnNotFoundWhenIdNotExists() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	

}










