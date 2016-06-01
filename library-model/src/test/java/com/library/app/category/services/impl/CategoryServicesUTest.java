package com.library.app.category.services.impl;

import static com.library.app.commomtests.category.CategoryForTestsRepository.categoryWithId;
import static com.library.app.commomtests.category.CategoryForTestsRepository.java;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesUTest {
	private CategoryServices categoryService;
	private CategoryRepository categoryRepository;
	private Validator validator;
	
	@Before
	public void initTestCase(){
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		categoryRepository = mock(CategoryRepository.class); 
		
		categoryService = new CategoryServicesImpl();
		((CategoryServicesImpl) categoryService).validator = validator;
		((CategoryServicesImpl) categoryService).categoryRepository = categoryRepository;
	}
	
	@Test
	public void addCategoryWithNullName(){
		this.addCategoryWithInvalidName(null);
	}
	@Test
	public void addCategoryWithShortName(){
		this.addCategoryWithInvalidName("A");
	}
	
	@Test
	public void addValidCategory(){
		when(categoryRepository.alreadyExists(java())).thenReturn(false);
		when(categoryRepository.add(java())).thenReturn(categoryWithId(java(), 1L));
		Category categoryAdded = categoryService.add(java());
		assertThat(categoryAdded.getId(),is(equalTo(1L)));
		
	}
	@Test
	public void addCategoryWithLongName(){
		this.addCategoryWithInvalidName("This is a long name that will cause a exception to be thrown");
	}
	
	@Test(expected = CategoryExistentException.class)
	public void addCategoryWithExistentName(){
		when(categoryRepository.alreadyExists(java())).thenReturn(true);
		categoryService.add(java());
	}
	
	private void addCategoryWithInvalidName(String name){
		try {
			categoryService.add(new Category(name));
			fail("a error should have been thrown");
		} catch (FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}
	
	@Test
	public void updateWithNullName(){
		updateCategoryWithInvalidName(null);
	}
	
	@Test
	public void updateCategoryWithShortName(){
		updateCategoryWithInvalidName("A");
	}
	
	@Test
	public void updateCategoryWithLongName(){
		updateCategoryWithInvalidName("This is a long name that will cause a exception to be thrown");
	}
	
	@Test(expected = CategoryExistentException.class)
	public void updateCategoryWithExistentName(){
		when(categoryRepository
				.alreadyExists(categoryWithId(java(), 1L)))
				.thenReturn(true);
		
		categoryService.update(categoryWithId(java(), 1L));
	}
	
	private void updateCategoryWithInvalidName(String name){
		try{
			categoryService.update(new Category(name));
			fail("An error should have been thrown");
		}catch(FieldNotValidException e){
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}
}
