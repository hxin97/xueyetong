package org.xyt.entity;

import java.io.Serializable;

public class User_info implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uId;
    private String uPassword;
    private String uName;
    private int uSex;
    private String uUniversity;
    private String uCollege;
    private String uClass;
    private String uPhoneNum;
    private String uPhoto;

    public String getUId(){
        return uId;
    }

    public void setUId(String uId){
        this.uId = uId;
    }

    public String getUPassword(){
        return uPassword;
    }

    public void setUPassword(String uPassword){
        this.uPassword = uPassword;
    }

    public String getUName(){
        return uName;
    }

    public void setUName(String uName){
        this.uName = uName;
    }

    public int getUSex(){
        return uSex;
    }

    public void setUSex(int uSex){
        this.uSex = uSex;
    }

    public String getUUniversity(){
        return uUniversity;
    }

    public void setUUniversity(String uUniversity){
        this.uUniversity = uUniversity;
    }

    public String getUCollege(){
        return uCollege;
    }

    public void setUCollege(String uCollege){
        this.uCollege = uCollege;
    }

    public String getUClass(){
        return uClass;
    }

    public void setUClass(String uClass){
        this.uClass = uClass;
    }

    public String getUPhoneNum(){
        return uPhoneNum;
    }

    public void setUPhoneNum(String uPhoneNum){
        this.uPhoneNum = uPhoneNum;
    }

    public String getUPhoto(){
        return uPhoto;
    }

    public void setUPhoto(String uPhoto){
        this.uPhoto = uPhoto;
    }
}
