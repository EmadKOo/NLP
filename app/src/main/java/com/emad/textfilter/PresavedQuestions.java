package com.emad.textfilter;

import com.emad.textfilter.Model.Question;

import java.util.ArrayList;

/**
 * Created by emad on 2/4/19.
 */

public class PresavedQuestions {
    /***
     *
     * Don`t use Question mark ?
     *
     ***/


    public static ArrayList<Question> presavedQuestions(){
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question("Developed You","Some Google Developers"));

        return questions;
    }
}
