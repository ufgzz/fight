package com.oofgz.fight;

import com.oofgz.fight.dto.restful.RestfulUser;
import com.oofgz.fight.service.IUserService;
import com.oofgz.fight.util.Pinyin4jUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FlywayCaseTests {

    @Autowired
    private IUserService userService;

    @Before
    public void setUp() {
        log.info("测试FlyWay数据库的功能");
        // 准备，清空user表
        userService.deleteAllUsers();
    }


    @Test
    public void flywayTest() {

        // 插入5个用户
        RestfulUser user = new RestfulUser("小a", 1, "12345678901", "教授");
        user.setNameSpell(Pinyin4jUtils.converterToFirstSpell(user.getName()));
        userService.create(user);

        user = new RestfulUser("小b", 2, "12345678902", "研究生");
        user.setNameSpell(Pinyin4jUtils.converterToFirstSpell(user.getName()));
        userService.create(user);

        user = new RestfulUser("小c", 3, "12345678903", "本科生");
        user.setNameSpell(Pinyin4jUtils.converterToFirstSpell(user.getName()));
        userService.create(user);

        user = new RestfulUser("小d", 4, "12345678904", "高中生");
        user.setNameSpell(Pinyin4jUtils.converterToFirstSpell(user.getName()));
        userService.create(user);

        user = new RestfulUser("小e", 5, "12345678905", "初中生");
        user.setNameSpell(Pinyin4jUtils.converterToFirstSpell(user.getName()));
        userService.create(user);
        // 查数据库，应该有5个用户
        Assert.assertEquals(5, userService.getAllUsers().size());

        // 删除两个用户
        userService.deleteByNameSpell("xa");
        userService.deleteByNameSpell("xe");

        // 查数据库，应该有5个用户
        Assert.assertEquals(3, userService.getAllUsers().size());

    }


}
