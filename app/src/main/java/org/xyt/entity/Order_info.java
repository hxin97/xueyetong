package org.xyt.entity;

import java.io.Serializable;

public class Order_info implements Serializable {

    private String orderId;
    private String uId;
    private int activityId;
    private String orderTime;
    private int state;

    public String getOrderId(){
        return orderId;
    }
    public void setOrderId(String orderId){
        this.orderId = orderId;
    }

    public String getUId(){
        return uId;
    }
    public void setUId(String uId){
        this.uId = uId;
    }

    public int getActivityId(){
        return activityId;
    }
    public void setActivityId(int activityId){
        this.activityId = activityId;
    }

    public String getOrderTime(){
        return orderTime;
    }
    public void setOrderTime(String orderTime){
        this.orderTime = orderTime;
    }

    public int getState(){
        return state;
    }
    public void setState(int state){
        this.state = state;
    }
}
