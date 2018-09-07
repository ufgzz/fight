package com.oofgz.fight;


import com.oofgz.fight.domain.secondary.DsUser;
import com.oofgz.fight.domain.thirdly.DsMessage;
import com.oofgz.fight.repository.secondary.DsUserSecondaryRepository;
import com.oofgz.fight.repository.thirdly.DsMessageThirdlyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MultiplyDatasourceCaseTests {

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate jdbcTemplateSecondary;

    @Autowired
    @Qualifier("thirdlyJdbcTemplate")
    private JdbcTemplate jdbcTemplateThirdly;

    @Autowired
    private DsUserSecondaryRepository dsUserSecondaryRepository;

    @Autowired
    private DsMessageThirdlyRepository dsMessageThirdlyRepository;


    @Before
    public void setUp() {
        log.info("测试多数据源(JdbcTemplate)(JpaRepository)的功能");
        //删除第二个数据源库的数据
        jdbcTemplateSecondary.update("DELETE FROM DS_USER");
        jdbcTemplateSecondary.update("DELETE FROM DS_MESSAGE");
        //删除第三个数据源库的数据
        jdbcTemplateThirdly.update("DELETE FROM DS_USER");
        jdbcTemplateThirdly.update("DELETE FROM DS_MESSAGE");
    }



    @Test
    public void multiplyDatasourceTest() {
        // 往第二个数据源中插入两条数据
        jdbcTemplateSecondary.update("insert into ds_user(name,age) values(?, ?)", "aaa", 20);
        jdbcTemplateSecondary.update("insert into ds_user(name,age) values(?, ?)", "bbb", 30);

        // 往第三个数据源中插入一条数据，若插入的是第一个数据源，则会主键冲突报错
        jdbcTemplateThirdly.update("insert into ds_user(name,age) values(?, ?)", "aaa", 20);

        // 查一下第二个数据源中是否有两条数据，验证插入是否成功
        Assert.assertEquals("2", jdbcTemplateSecondary.queryForObject("select count(1) from ds_user", String.class));

        // 查一下第三个数据源中是否有两条数据，验证插入是否成功
        Assert.assertEquals("1", jdbcTemplateThirdly.queryForObject("select count(1) from ds_user", String.class));

    }

    @Test
    public void multiplyJpaDatasourceTest() {
        dsUserSecondaryRepository.save(new DsUser("aaa", 10));
        dsUserSecondaryRepository.save(new DsUser("bbb", 20));
        dsUserSecondaryRepository.save(new DsUser("ccc", 30));
        dsUserSecondaryRepository.save(new DsUser("ddd", 40));
        dsUserSecondaryRepository.save(new DsUser("eee", 50));
        Assert.assertEquals(5, dsUserSecondaryRepository.findAll().size());

        dsMessageThirdlyRepository.save(new DsMessage("o1", "aaa_aaa_aaa"));
        dsMessageThirdlyRepository.save(new DsMessage("o2", "bbb_bbb_bbb"));
        dsMessageThirdlyRepository.save(new DsMessage("o3", "ccc_ccc_ccc"));
        Assert.assertEquals(3, dsMessageThirdlyRepository.findAll().size());
    }


}
