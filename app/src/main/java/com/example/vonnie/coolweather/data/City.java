package com.example.vonnie.coolweather.data;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport{
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceid;
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
        return provinceid;
    }
    public void setProvinceId(int provinceId){
        this.provinceid=provinceId;
    }
}
