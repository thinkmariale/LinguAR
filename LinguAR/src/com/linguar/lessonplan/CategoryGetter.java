package com.linguar.lessonplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dictionary.Category;
import dictionary.CategoryDictionary;
/**
 * @author Adarshkumar Pavani
 *
 */

public class CategoryGetter {

	private CategoryDictionary _catDictionary;
	private DefaultCategories _default;
	private List<Category> defaultCategories;
	private List<Category> topCategories;

	/**
	 * This method is used to return the top 5 categories from either the saved list, or the list of default categories, or a mix of both
	 * @return 
	 * @throws Exception If the list of categories requested is  more than the default number of categories available, an exception is thrown
	 */
	public List<Category> getTopFiveCategories() throws Exception{
		topCategories =_catDictionary.getTopCategories();

		if(topCategories == null)
		{
			//Load top 5 categories with the default categories
			return topCategories=getDefaultCategories(5);
		}
		else
		{
			if(topCategories.size()==5)
			{
				return topCategories;
			}
			else if(topCategories.size()>5)
			{
				List<Category> topFiveCategories = new ArrayList<Category>();
				for (int i = 0; i<5; i++)
				{
					topFiveCategories.add(topCategories.get(i));
				}
				return topFiveCategories;
			}
			else
			{
				List<Category> topFiveCategories = new ArrayList<Category>();
				//Adding the list of topCategories retrieved from the list of saved categories
				for (int i = 0; i<topCategories.size(); i++)
				{
					topFiveCategories.add(topCategories.get(i));
				}
				
				//Adding remaining categories to make the count equal to five
				List<Category> tempList = getDefaultCategories(5-topCategories.size());
				for (int i = 0; i<(5-topCategories.size());i++)
				{
					topFiveCategories.add(tempList.get(i));
				}
				return topFiveCategories;
			}
		}

	}

	/**
	 * This method is used to get a required number of default categories
	 * @param n = number of required default categories
	 * @return
	 * @throws Exception In case the number of reqested categories is less than the available list of default categories, an exception is thrown
	 */
	private List<Category> getDefaultCategories(int n) throws Exception
	{
		defaultCategories = _default.getAllDefaultCategories();
		
		//Shuffle the default categories to ensure they are random
		Collections.shuffle(defaultCategories);
		
		if(defaultCategories.size()<n)
		{
			throw new Exception("Number of requested categories are more than the number of default catagories available");
		}
		
		List<Category> reducedList = new ArrayList<Category>(n);
		for(int i = 0; i<n; i++)
		{
			reducedList.add(defaultCategories.get(i));
		}

		return reducedList;
	}
	
	
	//TODO-------------------------------The Exact logic and implementation of this method is still undecided------------------------------------
	
	/**
	 * This method is used to swap out a different default category for the one that the method is supplied with
	 * @param category The default category that needs to be replaced with a new one
	 * @return The new default category
	 * @throws Exception If the default number categories is equal to one (insufficent to swap out a new category), an exception would be thrown 
	 */
	public Category getNewCategory(Category category) throws Exception
	{
		if(defaultCategories==null)
		defaultCategories = _default.getAllDefaultCategories();
		
		if(defaultCategories.size()==1)
		{
			throw new Exception("Can't swap out new category because there is only one default category available");
		}
		Category cat = new Category();
		Random rand = new Random();
		//Return a new category
		while(cat!=category)
		{
		cat = defaultCategories.get(rand.nextInt(defaultCategories.size()));
		}
		
		return cat;
	}
}