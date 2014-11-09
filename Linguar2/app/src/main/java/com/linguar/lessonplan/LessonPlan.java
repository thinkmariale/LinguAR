package com.linguar.lessonplan;


/**
 * Created by Adarshkumar Pavani on 09/11/2014.
 */
public class LessonPlan {

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
