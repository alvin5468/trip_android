package com.example.mlj.mylocaljourney2;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alvin on 2016/4/19.
 */
public class HotSpotInfo implements Serializable  {
    private String spotId;
    private String placeId;
    private String name;
    private String description;
    private String info;
    private Location location;
    private String picture;
    private long viewCount;
    private long planCount;
    private String remark;
    private int budget;
    private String pathRemark;
    private Date date;
    private String startTime;

    public HotSpotInfo() {
        super();
    }

    public HotSpotInfo(String name,String description,String picture, Location location, long viewCount, long planCount, String remark, int budget, String pathRemark, Date date, String startTime ) {
        super();
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.location = location;
        this.planCount = planCount;
        this.viewCount = viewCount;
        this.remark = remark;
        this.budget = budget;
        this.pathRemark = pathRemark;
        this.date = date;
        this.startTime = startTime;
    }
    public String getSpotId() {
        return spotId;
    }
    public String getPlaceId() {
        return placeId;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getInfo() {
        return info;
    }
    public Location getLocation() {
        return location;
    }
    public String getPicture() {
        return picture;
    }
    public long getViewCount() {
        return viewCount;
    }
    public long getPlanCount() {
        return planCount;
    }


    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
    public void setPlanCount(long planCount) {
        this.planCount = planCount;
    }

    public String getRemark() {
        return remark;
    }

    public int getBudget() {
        return budget;
    }

    public String getPathRemark() {
        return pathRemark;
    }

    public Date getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }
}

class Location {
    Location() {
    }
    Location(String latitude ,String longitude) {
        this.longitude=longitude;
        this.latitude=latitude;
    }
    public String longitude;
    public String latitude;
    public String getLongitude() {
        return this.longitude;
    }
    public String getLatitude() {
        return this.latitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
