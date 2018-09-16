package com.oofgz.fight;

import com.oofgz.fight.domain.secondary.DsUser;
import com.oofgz.fight.repository.secondary.DsUserSecondaryRepository;
import lombok.extern.log4j.Log4j2;
import net.sf.ehcache.CacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class EhcacheCaseTests {


    @Autowired
    private DsUserSecondaryRepository dsUserSecondaryRepository;

    @Autowired
    private CacheManager cacheManager;


    @Before
    public void setUp() {
        log.info("测试Ehcache的缓存功能");
        dsUserSecondaryRepository.deleteAll();
        dsUserSecondaryRepository.save(new DsUser("AAA", 23));
    }

    @Test
    public void ehcacheTest() {

        DsUser u1 = dsUserSecondaryRepository.findByName("AAA");
        System.out.println("第一次查询：" + u1.getAge());

        DsUser u2 = dsUserSecondaryRepository.findByName("AAA");
        System.out.println("第二次查询：" + u2.getAge());

        u1.setAge(20);
        dsUserSecondaryRepository.save(u1);
        DsUser u3 = dsUserSecondaryRepository.findByName("AAA");
        System.out.println("第三次查询：" + u3.getAge());

    }


}
