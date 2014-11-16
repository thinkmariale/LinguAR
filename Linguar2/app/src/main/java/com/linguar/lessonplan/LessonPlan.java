package com.linguar.lessonplan;


import java.io.Serializable;

/**
 * Created by Adarshkumar Pavani on 09/11/2014.
 */
public class LessonPlan implements Serializable {

    private static final long serialVersionUID = 13L;
    public static LessonPlan getInstance()
    {
        return instance;
    }
    private static LessonPlan instance = new LessonPlan();

    private BonusTestQuota _BTQuota = BonusTestQuota.getInstance();
    private CumulativeWordsLearnt _wLearnt = CumulativeWordsLearnt.getInstance();
    private DailyLessonQuota _dailyQuota = DailyLessonQuota.getInstance();
    private ScoreKeeper _sKeeper = ScoreKeeper.getInstance();
}
