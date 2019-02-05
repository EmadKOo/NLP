package com.emad.textfilter.Model;

/**
 * Created by emad on 2/5/19.
 */

public class Answer {
    String answer;
    String imageLink;

    public Answer(String answer, String imageLink) {
        this.answer = answer;
        this.imageLink = imageLink;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
