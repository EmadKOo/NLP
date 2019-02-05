package com.emad.textfilter.Model;

/**
 * Created by emad on 2/5/19.
 */

public class App {
    int index;
    String AppName;
    String packageName;

    public App(int index, String appName, String packageName) {
        this.index = index;
        AppName = appName;
        this.packageName = packageName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
