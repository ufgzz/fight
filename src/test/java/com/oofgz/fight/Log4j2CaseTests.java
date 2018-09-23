package com.oofgz.fight;


import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class Log4j2CaseTests {


    @Before
    public void setUp() {
        log.info("测试Log4j2的功能");
    }


    @Test
    public void log4j2Test() throws Exception {
        log.info("输出info");
        log.debug("输出debug");
        log.error("输出error");
    }


}
