package com.oofgz.fight.entity;

import java.io.Serializable;

public class User implements Serializable {

    private Long id;
    //姓名
    private String name;
    //年龄
    private String age;
    //联系方式
    private String phone;
    //职业
    private String profession;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfession() {
        return profession;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
