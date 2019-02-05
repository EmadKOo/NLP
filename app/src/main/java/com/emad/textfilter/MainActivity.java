package com.emad.textfilter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;

import com.emad.textfilter.LocalData.Data;
import com.emad.textfilter.Model.Answer;
import com.emad.textfilter.Model.App;
import com.emad.textfilter.Model.Question;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {


    //Views
    WebView webView;
    ImageView imageView;

    // Lists
    ArrayList<Question> presavedQuestions; // get Data from class PresavedQuestions
    ArrayList<Question> preAskedQuestion; // get its Data from sqlite (Questions asked before && data saved while registration)
    ArrayList<App> allApps;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        imageView= findViewById(R.id.img);

        initLists();

         webView.getSettings().setJavaScriptEnabled(true);
         webView.getSettings().setLoadsImagesAutomatically(true);
         webView.getSettings().setDefaultTextEncodingName("UTF-8");
         webView.getSettings().setLoadsImagesAutomatically(true);


         String question = "who is Trump";
         String answer = getNlpAnswer(filterQuestion(question),allApps);
        Log.d(TAG, "onCreate: "  + answer);
      // new TestParse().execute();
    }


    class TestParse extends AsyncTask<String,Void,String[]> {
        Answer answer;
        @Override
        protected String[] doInBackground(String... request) {
            try {

                String[] result = new String[2];
                // get Text
                String link = "https://www.google.com/search?q=how are you";
                Document document =  Jsoup.connect(link).get();
                Elements elementText = document.getElementsByClass("LGOjhe");
                Log.d(TAG, " element 1   " + elementText.toString());

                result[0] = elementText.toString();

                Elements elementImage =  document.getElementsByClass("tHqoQ rg_meta notranslate");

                result[1] = elementImage.toString();
                return result;

            } catch (Exception e) {
                Log.d(TAG, "Exception : "+ e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result[]) {
            super.onPostExecute(result);

            Log.d(TAG, "onPostExecute: RESULT 0     " + result[0]);
            Log.d(TAG, "onPostExecute: RESULT 1     " + result[1]);

            // extract Text
            int dirIndex = result[0].indexOf("ltr");
            int endDivIndex = result[0].indexOf("/div");


            String text = result[0].substring(dirIndex+5, endDivIndex-16);
            Log.d(TAG, "onPostExecute: " + text);

            // extract image link
            int ouIndex = result[1].indexOf("ou");
            int owIndex = result[1].indexOf("ow");
            String imgLink = result[1].substring(ouIndex+5, owIndex-3);
            Log.d(TAG, "onPostExecute: " + imgLink);


            answer = new Answer(text,imgLink);

            /**
             * here you can change ui by the answer object Data
             */


        }
    }



    public void initLists(){
        presavedQuestions = PresavedQuestions.presavedQuestions();
        preAskedQuestion = getPreAskedQuestions();
        allApps = new ArrayList();
    }

    public String filterQuestion(String question){

        StringTokenizer tokenizer = new StringTokenizer(question);
        ArrayList<String> words = new ArrayList();
        String filteredQuestion="";

        while (tokenizer.hasMoreElements()){
            words.add(tokenizer.nextToken().toLowerCase());
            Log.d(TAG, "filterQuestion: " + tokenizer.nextToken());
        }

        for (int x = 0; x < words.size(); x++) {
            int flag=0;
            for (int y = 0; y < Data.stopWordsList().size(); y++) {
                if (words.get(x).toString().equals(Data.stopWordsList().get(y).toString().toLowerCase())){
                 words.remove(x);
                 flag=1;
                }
                if (flag==1)
                    break;
            }
        }
        for (int x = 0; x <words.size() ; x++) {
            filteredQuestion+=words.get(x).toLowerCase();
        }
        return filteredQuestion;
    }

    public String getNlpAnswer(String filterdQuestion,ArrayList<App> allApplicationNames){
        String answer = "";
        int flag=0; //when it becomes 1 , we got answer

            // first check if question in preserved questions like how are you
            for (int x = 0; x < presavedQuestions.size(); x++) {
                if (presavedQuestions.get(x).getQuestion().toLowerCase().contains(filterdQuestion.toLowerCase())
                    ||presavedQuestions.get(x).getQuestion().toLowerCase().equals(filterdQuestion.toLowerCase())) {
                        answer = presavedQuestions.get(x).getAnswer();
                        return answer;
                }
            }

            // then check if question in sqlite
            /**
             * like what is my name , age, ....... (These info tooked when user open app in first installation)
             * like questions asked before and saved in our sqlite
             */

                // check sqlite
                for (int x = 0; x < preAskedQuestion.size(); x++) {
                    if (preAskedQuestion.get(x).getQuestion().toLowerCase().contains(filterdQuestion.toLowerCase())
                            || preAskedQuestion.get(x).getQuestion().toLowerCase().equals(filterdQuestion.toLowerCase())) {

                        answer = preAskedQuestion.get(x).getAnswer();
                        return answer;
                    }
                }

                // check
                if ((filterdQuestion.startsWith("play"))||(filterdQuestion.startsWith("listen"))||(filterdQuestion.startsWith("hear"))){
                    answer = "Okay, we are working";
                    // intent to youtube or anghami
                }else if (filterdQuestion.startsWith("open")){
                   String appName = filterdQuestion.split(" ")[1];
                   App app;
                   int flagIntent =0;
                    for (int x = 0; x < allApplicationNames.size(); x++) {
                        if (allApplicationNames.get(x).getAppName().equals(appName)){
                            app = allApplicationNames.get(x);
                            flag=1;
                        }
                        if (flagIntent==1)
                            break;
                    }
                    answer = "Okay, we are working";
                    // intent to open app object (you can get package name from app object) line 190

                }else if ((filterdQuestion.startsWith("search"))||(filterdQuestion.startsWith("find"))||(filterdQuestion.startsWith("show"))||(filterdQuestion.startsWith("what is"))){
                    String query = "https://www.google.com/search?q"+filterdQuestion;
                    answer = "Okay, we are working";
                    // intent to open browser and search query is the above variable

                }else {
                    // then get Data from Google
                    answer = getDataFromInternet(filterdQuestion);
                }

                return answer;
    }

    public String getDataFromInternet(String question){
        // check gendy
        String answer = "";
        saveToSQLite(question, answer);
        return "";
    }

    private void saveToSQLite(String question, String answer) {
        // add new questions to sqlite
    }

    public static ArrayList<Question> getPreAskedQuestions(){
        ArrayList<Question> preAskedSqlite = new ArrayList<>();
        return preAskedSqlite;
    }

}
