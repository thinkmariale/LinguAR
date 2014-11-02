package com.linguar.lessonplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dictionary.Category;
import dictionary.CategoryDictionary;
import dictionary.Word;

public class WordGetter{
	

	private CategoryDictionary _cDictionary;	
	
	/**
	 * Returns the list of all words from the list of Categories. The list is shuffled
	 * @param categoryList List of Categories
	 * @return List of all the words from the supplied list of categories
	 * @throws Exception In case of the method is supplied with less than 5 categories, this exception is called
	 */
	public List<String> getWordsFromCategoryList(List<Category> categoryList) throws Exception
	{
		 _cDictionary =  CategoryDictionary.getInstance();
		List<String> allCategoryWords =  new ArrayList<String>();
		
		if(categoryList.size() < 5)
		{
			throw new Exception("Insufficient number of top categories : The number of categories supplied are : "+ categoryList.size());
		}
		
		//Get all the words from the supplied list of categories
		for (Category category : categoryList) {
			System.out.println("Category : "+ category.category);
			 allCategoryWords.addAll(_cDictionary.getWordsFromCategory(category));
		}
		
		//Shuffle the obtained list of words
		Collections.shuffle(allCategoryWords);
		 System.out.println("Words in these categories are : "+ allCategoryWords);

		return allCategoryWords;
	}

}
