package com.oofgz.fight;


import com.oofgz.fight.properties.BlogProperties;
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
public class PropertiesCaseTests {

    @Autowired
    private BlogProperties blogProperties;


    @Before
    public void setUp() {
        log.info("测试读取配置文件Properties的功能");
    }

    @Test
    public void testBlogProperties() {
        Assert.assertEquals("zfgcian@gmail.com", blogProperties.getName());
        Assert.assertEquals("Spring boot教程", blogProperties.getTitle());
        Assert.assertEquals("zfgcian@gmail.com正在努力学习《Spring boot教程》", blogProperties.getDesc());

        log.info("随机数测试输出：");
        log.info("随机字符串 : " + blogProperties.getValue());
        log.info("随机int : " + blogProperties.getNumber());
        log.info("随机long : " + blogProperties.getBigNumber());
        log.info("随机10以下 : " + blogProperties.getRandomNumIn10());
        log.info("随机10-20 : " + blogProperties.getRandomNumBetween1020());
    }

}
