package com.linguar.lessonplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dictionary.Category;
import dictionary.CategoryDictionary;
import dictionary.Word;

public class WordGetter{
	

	public final int MAX_WORDS_IN_24_HOURS = 7;
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
		
		//If the total number of words in a category are less than twice the number of words to be shown in 24 hours get more categories
		while(allCategoryWords.size() < MAX_WORDS_IN_24_HOURS*2)
		{
			CategoryGetter _cGetter = new CategoryGetter();
			Category newCategory =_cGetter.getNewCategory(categoryList);
			categoryList.add(newCategory);
			allCategoryWords.addAll(_cDictionary.getWordsFromCategory(newCategory));
		}
		
		//Shuffle the obtained list of words
		Collections.shuffle(allCategoryWords);
		
		List<String> _14Words = new ArrayList<String>();
		for(int i=0; i<14; i++)
		{
		_14Words.add(allCategoryWords.get(i));
		}
		 System.out.println("Words in these categories are : "+ _14Words);

		return _14Words;
	}

}
