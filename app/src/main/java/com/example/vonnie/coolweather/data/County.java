package com.example.vonnie.coolweather.data;

import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport{
    private int id;
    private String countyName;
    private int countyCode;
    private int cityId;
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
    public void setCountyCode(int countyCode){
        this.countyCode=countyCode;
    }
    public int getCountyCode(){
        return countyCode;
    }
    public int getCityId(){
        return cityId;
    }
    public void setCityId(int cityId){
        this.cityId=cityId;
    }
}
