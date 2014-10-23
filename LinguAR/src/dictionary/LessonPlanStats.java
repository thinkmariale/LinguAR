package dictionary;


public class LessonPlanStats {
	
	public String word;
	public int numCorrect;
	public int numIncorrect;
	public double percentCorrect;
	public int timesShown; // This is for the active mode where the system would be showing the word to the user a few number of times
	public String lastShown;
	
	public LessonPlanStats(){
		word = "";
		numCorrect = 0;
		numIncorrect = 0;
		percentCorrect = 0;
		timesShown = 0;
		lastShown = "";
	}
	
	public LessonPlanStats(String w){
		word = w;
		numCorrect = 0;
		numIncorrect = 0;
		percentCorrect = 0;
		timesShown = 0;

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
		timesShown = numCorrect + numIncorrect;
		
	}

}
