package com.example.mlj.mylocaljourney2;

/**
 * Created by alvin on 2016/4/4.
 */
public class TripPlanInfo {
    private String uniqueID;
    private String Title;
    private String Day;
    private String ImageURL;

    public TripPlanInfo() {
        super();
    }

    public TripPlanInfo(String uniqueID,String title, String day, String imageURL) {
        super();
        this.uniqueID = uniqueID;
        this.Title = title;
        this.Day = day;
        this.ImageURL = imageURL;
    }


    public String getUniqueID() {
        return uniqueID;
    }

    public void SetUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        this.Day = day;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        this.ImageURL = imageURL;
    }

}
