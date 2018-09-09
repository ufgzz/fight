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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     * Mysql数据库有两种引擎，注意要使用支持事务的引擎，比如innodb，如果是MyIsAm，事务是不起作用的
     * 集成测试.类中@Transactional 注解，会导致测试中 Entity 数据的操作都是在内存中完成，最终并不会进行 commit 操作，
     *          也就是不会将 Entity 数据进行持久化操作，从而导致测试的行为和真实应用的行为不一致。
     * 注：@TransactionConfiguration过时与替代写法
     * (1).原来的defaultRollback属性现在由专门的注解@Rollback（新增注解）代替，其中只有一个属性就是boolean型的value，作用没变，
     *     值为true表示测试时如果涉及了数据库的操作，那么测试完成后，该操作会回滚，也就是不会改变数据库内容；
     *     值为false则与此相反，表示你测试的内容中对数据库的操作会真实的执行到数据库中，不会回滚。
     *     官方文档中还给出了一个新注解@Commit，该注解与@Rollback只能使用一个，同时用貌似可能出现问题，
     *     .@Commit注解中无属性需要设置，不像@Rollback中还有一个value属性，用了@Commit，你的测试操作会改变数据库，不会回滚，等同于@Rollback(value=false)。
     *     这里建议使用@Rollback，不要用@Commit，这样起码你有两种选择可以选。
     * (2).原来放在@TransactionConfiguration注解中的transactionManager属性现在放在了@Transactional注解中。
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void jpaDeptTest() {

        //提供一个300字符串
        String valeSuper300 = "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890";

        String valueNormal = "HHH";

        String valeTransactional = valueNormal;

        //创建10条记录
        jpaDeptRepository.save(new JpaDept("AAA", "Des_AAA", 10));
        jpaDeptRepository.save(new JpaDept("BBB", "Des_BBB", 20));
        jpaDeptRepository.save(new JpaDept("CCC", "Des_CCC", 30));
        jpaDeptRepository.save(new JpaDept("DDD", "Des_DDD", 40));
        jpaDeptRepository.save(new JpaDept("EEE", "Des_EEE", 50));
        jpaDeptRepository.save(new JpaDept("FFF", "Des_FFF", 60));
        jpaDeptRepository.save(new JpaDept("GGG", "Des_GGG", 70));
        //JpaDept jpaDept2 = null;
        //jpaDept2.setName("QQ");
        jpaDeptRepository.save(new JpaDept(valeTransactional, "Des_HHH", 80));
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
