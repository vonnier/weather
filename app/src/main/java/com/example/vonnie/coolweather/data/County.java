package com.example.vonnie.coolweather.data;

import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport{
    private int id;
    private String countyName;
    private String weatherId;
    private int cityid;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getCountyName(){
        return  countyName;
    }
    public void setCountyName(String countyName){
        this.countyName=countyName;
    }
    public void setweatherId(String weatherId){
        this.weatherId=weatherId;
    }
    public String getweatherId(){
        return weatherId;
    }
    public int getCityId(){
        return cityid;
    }
    public void setCityId(int cityId){
        this.cityid=cityId;
    }
}
