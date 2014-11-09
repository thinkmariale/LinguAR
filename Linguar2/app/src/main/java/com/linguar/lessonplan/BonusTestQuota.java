package com.linguar.lessonplan;

/**
 * Created by idealboy on 09/11/2014.
 */
public class BonusTestQuota {

    private static BonusTestQuota instance = new BonusTestQuota();
    public static BonusTestQuota getInstance()
    {
        return instance;
    }
    public String lastTakenBonusTest;

    private void serializeSave()
    {

    }

    private void deserializeLoad()
    {

    }
}
