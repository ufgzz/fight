package com.oofgz.fight;

import com.oofgz.fight.dto.mybatis.MybatisUser;
import com.oofgz.fight.repository.primary.MybatisUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MybatisCaseTests {

    @Autowired
    private MybatisUserMapper mybatisUserMapper;


    @Before
    public void setUp() {

        log.info("测试Mybatis的功能");
        List<MybatisUser> mybatisUserList = mybatisUserMapper.findAll();
        for (MybatisUser user : mybatisUserList) {
            mybatisUserMapper.delete(user.getId());
        }
    }

    @Test
    @Rollback
    public void mybatisUserTest() {

        // insert一条数据，并select出来验证
        mybatisUserMapper.insert("AAA", 20);
        MybatisUser mybatisUser = mybatisUserMapper.findByName("AAA");
        Assert.assertEquals(20, mybatisUser.getAge().intValue());

        // update一条数据，并select出来验证
        mybatisUser.setAge(30);
        mybatisUserMapper.update(mybatisUser);
        mybatisUser = mybatisUserMapper.findByName("AAA");
        Assert.assertEquals(30, mybatisUser.getAge().intValue());

        // 删除这条数据，并select验证
        mybatisUserMapper.delete(mybatisUser.getId());
        mybatisUser = mybatisUserMapper.findByName("AAA");
        Assert.assertEquals(null, mybatisUser);

        mybatisUser = new MybatisUser("BBB", 30);
        mybatisUserMapper.insertByUser(mybatisUser);
        Assert.assertEquals(30, mybatisUserMapper.findByName("BBB").getAge().intValue());

        Map<String, Object> map = new HashMap<>();
        map.put("username", "CCC");
        map.put("age", 40);
        mybatisUserMapper.insertByMap(map);
        Assert.assertEquals(40, mybatisUserMapper.findByName("CCC").getAge().intValue());

        List<MybatisUser> mybatisUserList = mybatisUserMapper.findAll();
        for(MybatisUser user : mybatisUserList) {
            //Assert.assertEquals(null, user.getId());
            Assert.assertNotEquals(null, user.getUsername());
        }

    }


}
