package com.linguar.dictionary;


import com.linguar.lessonplan.LessonPlan;

import java.io.Serializable;

public class LessonPlanStats {
	//private static LessonPlanStats instance = new LessonPlanStats();
  //  public static LessonPlanStats getInstance()
    //{
     //   return instance;
  //  }
	public String word;
	public int numCorrect;
	public int numIncorrect;
	public double percentCorrect;
	public int timesShownSinceBeginnning;
	public String lastShown;

	// Date Format that works
	//private Date date = new Date();
	//private DateFormat sdf =  new SimpleDateFormat("yyyyMMddkkmmss",Locale.US);
	
	public LessonPlanStats(){
		word = "";
		numCorrect = 0;
		numIncorrect = 0;
		percentCorrect = 0;
		timesShownSinceBeginnning = 0;
		lastShown = "";
	}
	
	public LessonPlanStats(String w){
		word = w;
		numCorrect = 0;
		numIncorrect = 0;
		percentCorrect = 0;
		timesShownSinceBeginnning = 0;
		lastShown =  "";
	}
	
	public LessonPlanStats(String w, int c, int i){
		word = w;
		numCorrect = c;
		numIncorrect = i;
		this.updateHelper();
	}

	public int Update(int change){
		if (change > 0){
			numCorrect += change;
			updateHelper();
		}
		else{
			numIncorrect += Math.abs(change);
			updateHelper();
		}
		return numCorrect;
	}
	
	private void updateHelper(){
		if (numCorrect!= 0){
			percentCorrect = numCorrect/(numCorrect+numIncorrect);
		}
		else{
			percentCorrect = 0;
		}
		//timesShownSinceBeginnning = 0;
		
	}

}
