package com.linguar.lessonplan;

import java.util.HashMap;

public class DailyLessonQuota {

    private static DailyLessonQuota _instance;

    public static DailyLessonQuota getInstance() {
        return _instance;
    }

    private DailyLessonQuota() {
        _instance = new DailyLessonQuota();
    }

    public String lastUsedReviewMode;

    //Serialized 14 Words
    public HashMap<String, Integer> wordsShown;

    //Code for serialize and save
    void saveAndSerialize() {

    }

    //Code for deserialize and load into wordsShown
    void deserializeLoad()
    {


        System.out.println(wordsShown);
    }
	
}
