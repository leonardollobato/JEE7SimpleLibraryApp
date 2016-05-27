package com.library.app.category.repository;

import static com.library.app.commomtests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
import com.library.app.commomtests.utils.DBCommandTransctionalExecutor;

public class CategoryRepositoryUTest {
	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryRepository categoryRepository;
	private DBCommandTransctionalExecutor dbCommandTransactionalExecutor;

	@Before
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();
		
		categoryRepository = new CategoryRepository();
		categoryRepository.em = em;
		dbCommandTransactionalExecutor = 
				new DBCommandTransctionalExecutor(em);
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Test
	public void addCategoryAndFindIt() {
		Long categoryAddedId = 
				dbCommandTransactionalExecutor
				.executeCommand(() -> {
						return categoryRepository.add(java()).getId();
				});
		
		assertThat(categoryAddedId, is(notNullValue()));
		Category category = categoryRepository.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(),is(java().getName()));
	}
	
	@Test
	public void findCategoryByIdNotFound(){
		final Category category = categoryRepository.findById(999L);
		assertThat(category, is(nullValue()));
	}
	
	@Test
	public void findCategoryByIdWithNullId(){
		final Category category = categoryRepository.findById(null);
		assertThat(category, is(nullValue()));
	}
	
	@Test
	public void updateCategory(){
		final Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(()-> {
			return categoryRepository.add(java()).getId();
		});
		
		Category categoryAfterAdd = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterAdd.getName(), is(equalTo(java().getName())));
		
		categoryAfterAdd.setName(cleanCode().getName());
		dbCommandTransactionalExecutor.executeCommand(()->{
			categoryRepository.update(categoryAfterAdd);
			return null;
		});
		
		Category categoryAfterUpdate = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
		
	}
	
	@Test
	public void findAllCategories(){
		dbCommandTransactionalExecutor.executeCommand(()->{	
			allCategories().forEach(categoryRepository::add);
			allCategories().forEach((item)->System.out.println(item));
			return null;
		});
		
		List<Category> categories = categoryRepository.findAll("name");
		
		assertThat(categories.size(), is(equalTo(4)));
		assertThat(categories.get(0).getName(), is(equalTo(architecture().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(cleanCode().getName())));
		assertThat(categories.get(2).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(3).getName(), is(equalTo(networks().getName())));
	}
	
	@Test
	public void alreadyExistsForAdd(){
		dbCommandTransactionalExecutor.executeCommand(()->{
			categoryRepository.add(java());
			return null;
		});
		
		assertThat(categoryRepository.alreadyExists(java()), is(equalTo(true)));
		assertThat(categoryRepository.alreadyExists(cleanCode()),is(equalTo(false)));
	}
	
	@Test
	public void alreadyExistesCategoryWithId(){
		final Category java = dbCommandTransactionalExecutor.executeCommand(()->{
			categoryRepository.add(cleanCode());
			return categoryRepository.add(java());
		});
		
		assertThat(categoryRepository.alreadyExists(java),is(equalTo(false)));
		
		java.setName(cleanCode().getName());
		assertThat(categoryRepository.alreadyExists(java),is(equalTo(true)));
		
		java.setName(networks().getName());
		assertThat(categoryRepository.alreadyExists(java),is(equalTo(false)));
		
	}
	
	@Test
	public void deleteCategory(){
		final Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(()->{
			return categoryRepository.add(java()).getId();
		});
		
		Category categoryAfterAdd = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterAdd.getName(), is(equalTo(java().getName())));
		
		dbCommandTransactionalExecutor.executeCommand(()->{
			categoryRepository.delete(categoryAfterAdd);
			return null;
		});
		
		Category categoryAfterDelete = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterDelete,is(nullValue()));
		
	}
}
