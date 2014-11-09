package com.linguar.dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoryDictionary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static CategoryDictionary instance = new CategoryDictionary();
	
	private static HashMap<Category, List<String> > category_dictionary;
	
	// initializer 
		{
			category_dictionary = new HashMap<Category, List<String> >();
			
		}
	//get instance
	/* Static 'instance' method */
	public static CategoryDictionary getInstance( ) {
		return instance;
	}
	
	public void setCategoryDic( HashMap<Category, List<String> > c)
	{
		category_dictionary = c;
	}
	
	public HashMap<Category, List<String> > getCatDictionary(){
		return category_dictionary;
	} 
	
	//Function will increase count to category
	public void increaseCount(Category cat)
	{
		
	}
	//Function returns list of words in THAT category
	public List<String> getWordsFromCategory(Category cat)
	{
		if(category_dictionary.containsKey(cat)){
			return category_dictionary.get(cat);
		}		
		return null;
	}
	
	// Function that returns top 5 categories
	public List<Category> getTopCategories()
	{
		List<Category> top = new ArrayList<Category>()
				{{ add(new Category()); add(new Category()); add(new Category()); 
				add(new Category()); add(new Category()); }} ;
				
		for (Category key : category_dictionary.keySet()) {
			int c = 0;
			
			while(c < top.size() )
			{
				if(key.counter > top.get(c).counter){
					//getWordsFromCategory(key);
					top.remove(c);
					top.add(c,key);
					//getWordsFromCategory(top.get(c));
					break;
				}
				else c++;
			}  
		}
		
		return top;
		
	}
	
}
