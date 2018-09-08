package com.oofgz.fight.service;

import com.oofgz.fight.dto.restful.RestfulUser;

import java.util.List;

public interface IUserService {

    /**
     * 新增一个用户
     * @param restfulUser
     */
    void create(RestfulUser restfulUser);

    /**
     * 获取所有用户
     * @return
     */
    List<RestfulUser> getAllUsers();

    /**
     * 更新用户信息
     * @param nameSpell
     */
    void updateUserByNameSpell(String nameSpell, RestfulUser restfulUser);

    /**
     * 根据name删除一个用户
     * @param nameSpell
     */
    void deleteByNameSpell(String nameSpell);


    /**
     * 删除所有用户
     */
    void deleteAllUsers();

}
