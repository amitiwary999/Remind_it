package com.example.amit.remind_it.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by amit on 29/12/16.
 */

public class Items extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private String location;
    private String latx;
    private String laty;
    // private ArrayList<String> tags;
    private String imgPath;
    // private RealmList<Datum> datumRealmList;
    public Items(){

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public String getLatx() {
        return latx;
    }

    public String getLaty() {
        return laty;
    }

    public String getImgPath() {
        return imgPath;
    }

  /*  public ArrayList<String> getTags() {
        return tags;
    }*/

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

  /*  public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }*/

    public void setLaty(String laty) {
        this.laty = laty;
    }

    public void setLatx(String latx) {
        this.latx = latx;
    }

    public void setLocation(String location) {
        this.location = location;
    }




}
