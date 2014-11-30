package com.linguar.lessonplan;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.app.Activity;



/**
 * Created by idealboy on 09/11/2014.
 */
public class BonusTestQuota implements Serializable {

    private static BonusTestQuota instance = new BonusTestQuota();
    public static BonusTestQuota getInstance()
    {
        return instance;
    }
    public String lastTakenBonusTest;
}
