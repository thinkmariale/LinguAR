package com.linguar.lessonplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import com.linguar.dictionary.Category;
import com.linguar.dictionary.CategoryDictionary;

/**
 * @author Adarshkumar Pavani
 *
 */

public class CategoryGetter {

	private CategoryDictionary _catDictionary;
	private DefaultCategories _default;
	private List<Category> defaultCategories =  new ArrayList<Category>();
	private List<Category> topCategories = new ArrayList<Category>();

	/**
	 * This method is used to return the top 5 categories from either the saved list, or the list of default categories, or a mix of both
	 * @return 
	 * @throws Exception If the list of categories requested is  more than the default number of categories available, an exception is thrown
	 */
	public List<Category> getTopFiveCategories() throws Exception{
		_catDictionary= CategoryDictionary.getInstance();
        List<Category> newCategoryList =  new ArrayList<Category>();
        newCategoryList =_catDictionary.getTopCategories();

        return  newCategoryList;
        /*
        for(Category cat : newCategoryList) {
            if(!(cat.category == null || cat.category=="" || cat.category.isEmpty()) )
                topCategories.add(cat);
        }
        //TODO DONT NEED THIS ANYMORE
        System.out.println("Category size list after removal is:" + topCategories.size());

        if(topCategories == null || topCategories.size()==0)
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
		}*/
	}

	/**
	 * This method is used to get a required number of default categories
	 * @param n = number of required default categories
	 * @return
	 * @throws Exception In case the number of reqested categories is less than the available list of default categories, an exception is thrown
	 */
	private List<Category> getDefaultCategories(int n) throws Exception
	{
		_default = new DefaultCategories();
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
	
		
	/**
	 * This method is used to get a new default category that doesn't exist in the list of categories that the method is supplied with
	 * @param categories The category list that needs more categories to be added
	 * @return The new category
	 * @throws Exception If there are no new categories left from the list of default categories or if the default category list is null, the system would find 
	 */
	public Category getNewCategory(List<Category> categories) throws Exception
	{
		if(defaultCategories==null || defaultCategories.size()==0)
		defaultCategories = _default.getAllDefaultCategories();
		
		if(defaultCategories == null || defaultCategories.size()==0)
		{
			throw new Exception("The default category list is null");
		}
		
		int count = 0;
		//Check if there are any more categories that can be loaded into the list to keep the lesson plan running, if not throw an exception
		for (Category defaultCategory : defaultCategories) {
			if(!categories.contains(defaultCategory))
				break;
			else
				count++;
		}

		Category cat = new Category();
		Random rand = new Random();
		//Return a new category
		while(!categories.contains(cat))
		{
		    cat = defaultCategories.get(rand.nextInt(defaultCategories.size()));
		}
		
		return cat;
	}
}
