package dictionary;

import java.text.SimpleDateFormat;
import java.util.Date;
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
		passiveModeTimestamp = (new SimpleDateFormat("yyyyMMddkkmmss")).format(new Date());
		categoryList = cats;		
	}
	
	public Word(String w, String t){
		word = w;
		translation = t;
		stats = new LessonPlanStats(w);
		passiveModeTimestamp = (new SimpleDateFormat("yyyyMMddkkmmss")).format(new Date());
		categoryList = new HashSet<Category>(5);
	}
	
	public String toString(){
		
		
		return word + " : " + translation;
		
		
	}
	
}
