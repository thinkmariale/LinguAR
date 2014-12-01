package com.linguar.lessonplan;

import java.io.Serializable;

/**
 * Created by Adarshkumar Pavani on 08/11/2014.
 */
public class ScoreKeeper implements Serializable{

    public int score;
    private static ScoreKeeper skeeper = new ScoreKeeper();
    public static ScoreKeeper getInstance()
    {
        return  skeeper;
    }
}
