package com.linguar.lessonplan;

import java.util.ArrayList;
import java.util.List;

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
}
