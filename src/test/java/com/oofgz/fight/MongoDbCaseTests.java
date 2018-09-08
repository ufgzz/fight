package com.oofgz.fight;

import com.mongodb.MongoClient;
import com.oofgz.fight.dto.mongodb.MongoDbUser;
import com.oofgz.fight.repository.primary.MongoDbUserRepository;
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
public class MongoDbCaseTests {

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private MongoDbUserRepository mongoDbUserRepository;


    @Before
    public void setUp() {
        log.info("测试MongoDb的功能");
        mongoDbUserRepository.deleteAll();
    }


    @Test
    public void mongoPlusTest() {
        log.info("MinConnectionsPerHost = {}, MaxConnectionsPerHost = {}",
                mongoClient.getMongoClientOptions().getMinConnectionsPerHost(),
                mongoClient.getMongoClientOptions().getConnectionsPerHost()
        );
    }


    @Test
    public void mongoDbUserTest() {

        // 创建三个MongoDbUser，并验证MongoDbUser总数
        mongoDbUserRepository.save(new MongoDbUser(1L, "didiOOO", 30));
        mongoDbUserRepository.save(new MongoDbUser(2L, "mama", 40));
        mongoDbUserRepository.save(new MongoDbUser(3L, "kaka", 50));
        Assert.assertEquals(3, mongoDbUserRepository.findAll().size());

        // 删除一个MongoDbUser，再验证MongoDbUser总数
        MongoDbUser u = mongoDbUserRepository.findById(1L).get();
        mongoDbUserRepository.delete(u);
        Assert.assertEquals(2, mongoDbUserRepository.findAll().size());

        // 删除一个MongoDbUser，再验证MongoDbUser总数
        u = mongoDbUserRepository.findByUsername("mama");
        mongoDbUserRepository.delete(u);
        Assert.assertEquals(1, mongoDbUserRepository.findAll().size());

    }


}
