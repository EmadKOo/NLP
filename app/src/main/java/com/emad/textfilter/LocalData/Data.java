package com.emad.textfilter.LocalData;

import java.util.ArrayList;

/**
 * Created by emad on 2/5/19.
 */

public class Data {


    public static ArrayList<String > stopWordsList(){
     ArrayList<String> stopList = new ArrayList();
     stopList.add("who");
     stopList.add("about");
     stopList.add("hey");
     stopList.add("is");

     return stopList;
    }
}
