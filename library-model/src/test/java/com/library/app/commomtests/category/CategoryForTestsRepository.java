package com.library.app.commomtests.category;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;

import com.library.app.category.model.Category;

@Ignore
public class CategoryForTestsRepository {
	
	public static Category cleanCode(){
		return new Category("Clean code");
	}
	public static Category architecture(){
		return new Category("Architecture");
	}
	public static Category networks(){
		return new Category("Networks");
	}
	public static Category java(){
		return new Category("Java");
	}
	
	public static Category categoryWithId(Category category, Long id){
		category.setId(id);
		return category;
	}
	public static List<Category> allCategories(){
		return Arrays.asList(architecture(),cleanCode(),java(),networks());
	}
}
