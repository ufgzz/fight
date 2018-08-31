package com.oofgz.fight.bean;

import java.io.Serializable;

public class User implements Serializable {
    //姓名
    private String name;
    //年龄
    private String age;
    //联系方式
    private String phone;
    //职业
    private String profession;

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
