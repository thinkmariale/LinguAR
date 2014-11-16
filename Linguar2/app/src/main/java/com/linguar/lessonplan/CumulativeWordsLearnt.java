package com.linguar.lessonplan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adarshkumar Pavani on 08/11/2014.
 */
public class CumulativeWordsLearnt {

    private static CumulativeWordsLearnt wLearnt = new CumulativeWordsLearnt();

    public  static  CumulativeWordsLearnt getInstance()
    {
        return  wLearnt;
    }

    public List<String> wordsLearnt = new ArrayList<String>();
}
