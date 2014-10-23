package dictionary;

import java.util.HashSet;


public class Word {
	public String word;
	public String translation;
	public String passiveModeTimestamp;
	public LessonPlanStats stats;
	public HashSet<Category> categoryList;
	
	public Word(){
		word = "";
		translation = "";
		stats = new LessonPlanStats();
		passiveModeTimestamp = "";
		categoryList = new HashSet<Category>(5);
	}
	
	public Word(String w){
		word = w;
		translation = "";
		stats = new LessonPlanStats();
		passiveModeTimestamp = "";
		categoryList = new HashSet<Category>(5);
	}
	
	public Word(String w, String t, int c, int i, HashSet<Category> cats){
		word = w;
		translation = t;
		stats = new LessonPlanStats(w,c,i);
		passiveModeTimestamp = "";
		categoryList = cats;		
	}
	
	public Word(String w, String t){
		word = w;
		translation = t;
		stats = new LessonPlanStats(w);
		passiveModeTimestamp = ""; 
		categoryList = new HashSet<Category>(5);
	}
	
	public String toString(){
		
		
		return word + " : " + translation;
		
		
	}
	
	
	// Date Format that works
//	Date date = new Date();
//	DateFormat sdf =  new SimpleDateFormat("yyyyMMddkkmmss",Locale.US);
//	sdf.format(date);
}
