package com.example.vonnie.coolweather.data;

import org.litepal.LitePal;

public class City extends LitePal{
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getCityName(){
        return  cityName;
    }
    public void setCityName(String cityName){
        this.cityName=cityName;
    }
    public void setCityCode(int cityCode){
        this.cityCode=cityCode;
    }
    public int getCityCode(){
        return cityCode;
    }
    public int getProvinceId(){
        return provinceId;
    }
    public void setProvinceId(int provinceId){
        this.provinceId=provinceId;
    }
}
