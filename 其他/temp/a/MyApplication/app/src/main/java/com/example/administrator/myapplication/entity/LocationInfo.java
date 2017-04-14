package com.example.administrator.myapplication.entity;


import com.amap.api.location.AMapLocation;
import com.example.administrator.myapplication.utils.Utils;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by liaoruochen on 2017/3/22.
 * Description:
 */

@Table("location")
public class LocationInfo {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    int id;                 // 指定自增，每个对象需要有一个主键

    String country;         //国    家

    String province;        //省

    String city;            //市

    String cityCode;        //城市编码 020

    String district;        // 区  越秀区

    String adCode;          // 区域 码 440104

    String address;         //地    址 广东省广州市越秀区寺右中街靠近Vicook厨艺坊

    String poiName;         //兴趣点 Vicook厨艺坊

    long time;              //定位时间  具体时间戳   2017-03-22 14:50:54

    String day;             //定位日期  2017-03-22

    double longitude;        //经    度


    double latitude;        //纬    度

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocationInfo(AMapLocation aMapLocation) {
        country = aMapLocation.getCountry();
        province=aMapLocation.getProvince();
        city=aMapLocation.getCity();
        cityCode=aMapLocation.getCityCode();
        district=aMapLocation.getDistrict();
        adCode=aMapLocation.getAdCode();
        address=aMapLocation.getAddress();
        poiName=aMapLocation.getPoiName();
        time=aMapLocation.getTime();
        longitude=aMapLocation.getLongitude();
        latitude=aMapLocation.getLatitude();

        day= Utils.formatUTC(time,"yyyy-MM-dd");
    }
    public LocationInfo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", district='" + district + '\'' +
                ", adCode='" + adCode + '\'' +
                ", address='" + address + '\'' +
                ", poiName='" + poiName + '\'' +
                ", time=" + time +
                ", day='" + day + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
