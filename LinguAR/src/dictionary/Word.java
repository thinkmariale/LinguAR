package dictionary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;


public class Word {
	public String englishWord;
	public String spanishTranslation;
	public String passiveModeTimestamp;
	public LessonPlanStats stats;
	public HashSet<Category> categoryList;
	
	// Date Format that works
	//private Date date = new Date();
	private DateFormat sdf =  new SimpleDateFormat("yyyyMMddkkmmss",Locale.US);

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

		passiveModeTimestamp = sdf.format(Calendar.getInstance().getTime());
	
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
	
}
