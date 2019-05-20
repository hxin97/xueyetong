package org.xyt.entity;

import java.io.Serializable;
import java.sql.Date;

public class Activity_info implements Serializable {
    private int activityId;
    private String activityType;
    private String title;
    private String site;
    private int num;
    private int remainingNum;
    private Date startTime;
    private Date endTime;
    private String speaker;
    private String university;
    private String college;
    private String intro;
    private float lectureHours;
    private float evaluationPoint;

    public int getActivityId() {
        return activityId;
    }
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
    public String getActivityType() {
        return activityType;
    }
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getRemainingNum() {
        return remainingNum;
    }
    public void setRemainingNum(int remainingNum) {
        this.remainingNum = remainingNum;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public String getSpeaker() {
        return speaker;
    }
    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
    public String getUniversity() {
        return university;
    }
    public void setUniversity(String university) {
        this.university = university;
    }
    public String getCollege() {
        return college;
    }
    public void setCollege(String college) {
        this.college = college;
    }
    public String getIntro() {
        return intro;
    }
    public void setIntro(String intro) {
        this.intro = intro;
    }
    public float getLectureHours() {
        return lectureHours;
    }
    public void setLectureHours(float lectureHours) {
        this.lectureHours = lectureHours;
    }
    public float getEvaluationPoint() {
        return evaluationPoint;
    }
    public void setEvaluationPoint(float evaluationPoint) {
        this.evaluationPoint = evaluationPoint;
    }
}
