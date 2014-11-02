package dictionary;

public class WordStat {
	
	public String word;
	public int numCorrect;
	public int numIncorrect;
	public double percentCorrect;
	public int timesSeen;
	
	public WordStat(){
		word = "";
		numCorrect = 0;
		numIncorrect = 0;
		percentCorrect = 0;
		timesSeen = 0;
	}
	
	public WordStat(String w){
		word = w;
		numCorrect = 0;
		numIncorrect = 0;
		percentCorrect = 0;
		timesSeen = 0;
	}
	
	public WordStat(String w, int c, int i){
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
		timesSeen = numCorrect + numIncorrect;
		
	}

    public void incTimesSeen() {
        this.timesSeen++;
    }

}
