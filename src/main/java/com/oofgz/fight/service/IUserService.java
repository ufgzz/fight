package com.oofgz.fight.service;

import com.oofgz.fight.bean.User;

import java.util.List;

public interface IUserService {

    /**
     * 新增一个用户
     * @param name
     * @param age
     * @param phone
     * @param profession
     */
    void create(String name, String age, String phone, String profession);

    /**
     * 获取所有用户
     * @return
     */
    List<User> getAllUsers();

    /**
     * 更新用户信息
     * @param name
     * @param age
     * @param phone
     * @param profession
     */
    void updateUserByName(String name, String age, String phone, String profession);

    /**
     * 根据name删除一个用户
     * @param name
     */
    void deleteByName(String name);

}