package com.linguar.dictionary;

import java.util.HashSet;


public class Word {
	public String englishWord;
	public String spanishTranslation;
	public String passiveModeTimestamp;
	public LessonPlanStats stats;
	public HashSet<Category> categoryList;
	
	// Date Format that works

	//private Date date = new Date();

//	private DateFormat sdf =  new SimpleDateFormat("yyyyMMddkkmmss",Locale.US);


	public Word(){
		englishWord = "";
		spanishTranslation = "";
		stats = new LessonPlanStats();
		passiveModeTimestamp = "";
		categoryList = new HashSet<Category>(5);
	}
	
	public Word(String w){
		englishWord = w;
		spanishTranslation = "";
		stats = new LessonPlanStats();
		passiveModeTimestamp = "";
		categoryList = new HashSet<Category>(5);
	}
	
	public Word(String w, String t, int c, int i, HashSet<Category> cats){
		englishWord = w;
		spanishTranslation = t;
		stats = new LessonPlanStats(w,c,i);


		passiveModeTimestamp = "";//sdf.format(Calendar.getInstance().getTime());
		categoryList = cats;		
	}
	
	public Word(String w, String t){
		englishWord = w;
		spanishTranslation = t;
		stats = new LessonPlanStats(w);

		passiveModeTimestamp = ""; 
		categoryList = new HashSet<Category>(5);
	}
	
	public String toString(){
		
		return englishWord + " : " + spanishTranslation;
	}

    //--------
	//function to increment count in categories depending the word said
	public boolean incrementCategoryCount()
	{
        // min num of cats to consider showing
       // if(categoryList.size() > 2 )
       // {
	    	for(Category cat:categoryList)
			    cat.counter = cat.counter + 1;
            return true;
		//}

        //return false;
	}
	
}
