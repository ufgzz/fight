package com.oofgz.fight.repository;

import com.oofgz.fight.dto.mongodb.MongoDbUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbUserRepository extends MongoRepository<MongoDbUser, Long> {

    MongoDbUser findByUsername(String username);

}
