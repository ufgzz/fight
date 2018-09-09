package com.oofgz.fight.dto.restful;

import java.io.Serializable;

public class RestfulUser implements Serializable {

    //姓名
    private String name;
    //姓名简拼
    private String nameSpell;
    //年龄
    private Integer age;
    //联系方式
    private String phone;
    //职业
    private String profession;

    public RestfulUser() {

    }

    public RestfulUser(String name, Integer age, String phone, String profession) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.profession = profession;
    }

    public String getName() {
        return name;
    }

    public String getNameSpell() {
        return nameSpell;
    }

    public Integer getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfession() {
        return profession;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameSpell(String nameSpell) {
        this.nameSpell = nameSpell;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
