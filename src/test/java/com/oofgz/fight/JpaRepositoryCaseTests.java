package com.oofgz.fight;

import com.oofgz.fight.domain.primary.JpaDept;
import com.oofgz.fight.repository.primary.JpaDeptRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JpaRepositoryCaseTests {

    @Autowired
    private JpaDeptRepository jpaDeptRepository;

    @Before
    public void setUp() {
        log.info("测试JPA的功能");
        jpaDeptRepository.deleteAll();
    }


    @Test
    public void jpaDeptTest() {
        //创建10条记录
        jpaDeptRepository.save(new JpaDept("AAA", "Des_AAA", 10));
        jpaDeptRepository.save(new JpaDept("BBB", "Des_BBB", 20));
        jpaDeptRepository.save(new JpaDept("CCC", "Des_CCC", 30));
        jpaDeptRepository.save(new JpaDept("DDD", "Des_DDD", 40));
        jpaDeptRepository.save(new JpaDept("EEE", "Des_EEE", 50));
        jpaDeptRepository.save(new JpaDept("FFF", "Des_FFF", 60));
        jpaDeptRepository.save(new JpaDept("GGG", "Des_GGG", 70));
        jpaDeptRepository.save(new JpaDept("HHH", "Des_HHH", 80));
        jpaDeptRepository.save(new JpaDept("III", "Des_III", 90));
        jpaDeptRepository.save(new JpaDept("JJJ", "Des_JJJ", 100));

        // 测试findAll, 查询所有记录
        List<JpaDept> jpaDeptList = jpaDeptRepository.findAll();
        Assert.assertEquals(10, jpaDeptList.size());

        // 测试findByName, 查询名称为FFF的JpaDept
        JpaDept jpaDept = jpaDeptRepository.findByName("FFF");
        Assert.assertEquals(60, jpaDept.getNum().longValue());

        // 测试findUser, 查询名称为GGG的JpaDept
        jpaDept = jpaDeptRepository.findJpaDept("GGG");
        Assert.assertEquals(70, jpaDept.getNum().longValue());

        // 测试findByNameAndAge, 查询名称为FFF并且人数为60的JpaDept
        jpaDept = jpaDeptRepository.findByNameAndNum("FFF", 60);
        Assert.assertEquals("FFF", jpaDept.getName());

        // 测试删除名称为AAA的JpaDept
        jpaDeptRepository.delete(jpaDeptRepository.findByName("AAA"));

        // 测试findAll, 查询所有记录, 验证上面的删除是否成功
        jpaDeptList = jpaDeptRepository.findAll();
        Assert.assertEquals(9, jpaDeptList.size());

    }

}
