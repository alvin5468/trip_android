package com.example.mlj.mylocaljourney2;

/**
 * Created by alvin on 2016/6/22.
 */
public class ServerInfo {
    private static String baseURL;
    static {
        baseURL = "http://52.197.58.253";
    }
    public String getBaseURL(){
        return this.baseURL;
    }
    public void setBaseURL(String URL){
        this.baseURL = URL;
    }
}
