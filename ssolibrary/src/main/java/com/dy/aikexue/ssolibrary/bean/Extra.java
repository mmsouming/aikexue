package com.dy.aikexue.ssolibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 用户额外信息
 * Created by:Pxy
 * Date: 2016-04-12
 * Time: 14:22
 */
public class Extra implements Serializable {
    private int age;

    private long birthday;

    private String city;

    private String country;

    private List<String> hobby;

    private String job;

    private String province;

    private List<String> specialty;
    //专业
    private List<String> profession;

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }

    public void setHobby(List<String> hobby) {
        this.hobby = hobby;
    }

    public List<String> getHobby() {
        return this.hobby;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJob() {
        return this.job;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return this.province;
    }

    public void setSpecialty(List<String> specialty) {
        this.specialty = specialty;
    }

    public List<String> getSpecialty() {
        return this.specialty;
    }

    public List<String> getProfession() {
        return profession;
    }

    public void setProfession(List<String> profession) {
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "Extra{" +
                "age=" + age +
                ", birthday=" + birthday +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", hobby=" + hobby +
                ", job='" + job + '\'' +
                ", province='" + province + '\'' +
                ", specialty=" + specialty +
                '}';
    }
}