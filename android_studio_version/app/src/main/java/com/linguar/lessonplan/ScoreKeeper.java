package com.linguar.lessonplan;

/**
 * Created by Adarshkumar Pavani on 08/11/2014.
 */
public class ScoreKeeper {

    public int score;
    private static ScoreKeeper skeeper = new ScoreKeeper();
    public static ScoreKeeper getInstance()
    {
        return  skeeper;
    }

    public void serializeSave()
    {

    }

    private void deserializeLoad()
    {

    }
}
