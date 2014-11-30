package com.linguar.dictionary;

public class Category {
	public String category;
	public int counter;
	
	public Category(){
		category = "";
		counter  = 0;
	} 
	public Category(String w){
		category = w;
		counter  = 0;
	} 
	
	public void set(Category c)
	{
		category = c.category;
		counter  = c.counter;
	}
}


