package com.example.mlj.mylocaljourney2;
import java.io.Serializable;
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

    public HotSpotInfo() {
        super();
    }

    public HotSpotInfo(String name,String description,String picture, Location location, long viewCount, long planCount ) {
        super();
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.location = location;
        this.planCount = planCount;
        this.viewCount = viewCount;
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
