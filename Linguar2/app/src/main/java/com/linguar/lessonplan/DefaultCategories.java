package com.linguar.lessonplan;

import java.util.ArrayList;
import java.util.List;

import com.linguar.dictionary.Category;
import com.linguar.dictionary.CategoryDictionary;

public class DefaultCategories {

    private CategoryDictionary _cDictionary;
	//If more default categories are to be added, they can be added in this array.
	private String []defaultCategories =  {"edible_fruit",
			"automotive_vehicle",
			"hold_back",
			"herb",
			"stringed_instrument",
			"chromatic_color"};

    /*
	public List<Category> getAllDefaultCategories()
	{
		List<Category> categoryList= new ArrayList<Category>();
		
		for (String categoryString : defaultCategories) {
			Category category = new Category(categoryString);
			categoryList.add(category);
		}
		
		return categoryList;
		
	}
	*/

    public List<Category> getAllDefaultCategories() {
        _cDictionary = CategoryDictionary.getInstance();
        return _cDictionary.getDefaultCategories();
    }
	
}
