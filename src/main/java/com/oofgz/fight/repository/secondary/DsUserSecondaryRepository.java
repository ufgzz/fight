package com.oofgz.fight.repository.secondary;

import com.oofgz.fight.domain.secondary.DsUser;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

@CacheConfig(cacheNames = "dsUserCache")
public interface DsUserSecondaryRepository extends JpaRepository<DsUser, Long> {

    @Cacheable(key = "#p0", condition = "#p0.length() < 10")
    DsUser findByName(String name);

}
